package com.senac.votesys.application.service;

import com.senac.votesys.application.dto.usuario.UsuarioPrincipalDTO;
import com.senac.votesys.application.dto.votos.VotosRequestDTO;
import com.senac.votesys.application.dto.votos.VotosResponseDTO;
import com.senac.votesys.domain.entity.OpcaoVoto;
import com.senac.votesys.domain.entity.TipoCampanha;
import com.senac.votesys.domain.entity.Votos;
import com.senac.votesys.domain.repository.CampanhasRepository;
import com.senac.votesys.domain.repository.OpcaoVotoRepository;
import com.senac.votesys.domain.repository.UsuariosRepository;
import com.senac.votesys.domain.repository.VotosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class VotacaoService {

    @Autowired
    private VotosRepository votosRepository;

    @Autowired
    private CampanhasRepository campanhasRepository;

    @Autowired
    private OpcaoVotoRepository opcaoVotoRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Transactional
    public VotosResponseDTO registrarVoto(VotosRequestDTO dto, UsuarioPrincipalDTO usuarioLogado) {

        if (usuarioLogado == null) {
            throw new RuntimeException("Usuário não autenticado.");
        }

        var usuario = usuariosRepository.findById(usuarioLogado.id())
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado no banco."));

        var campanha = campanhasRepository.findById(dto.campanhaId())
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada."));

        var opcoesDoBanco = opcaoVotoRepository.findAllById(dto.opcoesIds());
        if (opcoesDoBanco.isEmpty()) {
            throw new RuntimeException("Nenhuma opção válida selecionada.");
        }

        Map<Long, OpcaoVoto> mapOpcoes = opcoesDoBanco.stream()
                .collect(Collectors.toMap(OpcaoVoto::getId, Function.identity()));

        List<OpcaoVoto> opcoesOrdenadas = dto.opcoesIds().stream()
                .map(mapOpcoes::get)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());

        boolean jaVotou = !votosRepository.findByUsuarioAndCampanha(usuario, campanha).isEmpty();

        switch (campanha.getTipoCampanha()) {
            case VOTO_UNICO -> {
                if (jaVotou) {
                    throw new RuntimeException("Você já votou nesta campanha. Só é permitido um voto.");
                }
                if (opcoesOrdenadas.size() != 1) {
                    throw new RuntimeException("Para Voto Único, selecione exatamente 1 opção.");
                }
            }

            case RANQUEADO -> {
                if (jaVotou) {
                    throw new RuntimeException("Você já enviou seu ranking. Não é possível votar novamente.");
                }

                if (opcoesOrdenadas.size() != campanha.getOpcoesDeVoto().size()) {
                    throw new RuntimeException("Para votação Ranqueada, você deve ordenar todas as opções da campanha.");
                }
            }

            case SELECAO_MULTIPLA -> {
                if (jaVotou) {
                    throw new RuntimeException("Você já realizou sua seleção nesta campanha.");
                }
                if (opcoesOrdenadas.size() < 1) {
                    throw new RuntimeException("Selecione pelo menos uma opção.");
                }
            }

            case VOTOS_MULTIPLOS -> {
                if (opcoesOrdenadas.size() != 1) {
                    throw new RuntimeException("Em votos múltiplos, vote em uma opção por vez.");
                }
            }
        }

        // Salva o Voto
        Votos voto = new Votos();
        voto.setUsuario(usuario);
        voto.setCampanha(campanha);
        voto.setOpcoes(opcoesOrdenadas);

        Votos salvo = votosRepository.save(voto);

        // Contagem de Pontos e Atualização das Opções
        int totalOpcoesNaCampanha = campanha.getOpcoesDeVoto().size();

        for (int i = 0; i < opcoesOrdenadas.size(); i++) {
            OpcaoVoto opcao = opcoesOrdenadas.get(i);
            int pontosParaAdicionar = 0;

            if (campanha.getTipoCampanha() == TipoCampanha.RANQUEADO) {
                // Lógica Método de Borda:
                pontosParaAdicionar = (totalOpcoesNaCampanha - 1) - i;
                if (pontosParaAdicionar < 0) pontosParaAdicionar = 0;

            } else {
                // Para outros tipos, 1 ponto simples por escolha
                pontosParaAdicionar = 1;
            }

            opcao.setTotalVotos(opcao.getTotalVotos() + pontosParaAdicionar);
            opcaoVotoRepository.save(opcao);
        }

        return VotosResponseDTO.fromEntity(salvo);
    }

    public List<VotosResponseDTO> listarVotosPorCampanha(Long campanhaId) {
        var campanha = campanhasRepository.findById(campanhaId)
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada"));

        List<Votos> votos = votosRepository.findByCampanha(campanha);

        return votos.stream()
                .map(VotosResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}