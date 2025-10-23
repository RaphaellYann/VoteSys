package com.senac.votesys.application.service;

import com.senac.votesys.application.dto.votos.VotosRequestDTO;
import com.senac.votesys.application.dto.votos.VotosResponseDTO;
import com.senac.votesys.domain.entity.Usuarios;
import com.senac.votesys.domain.entity.Votos;
import com.senac.votesys.domain.repository.CampanhasRepository;
import com.senac.votesys.domain.repository.OpcaoVotoRepository;
import com.senac.votesys.domain.repository.VotosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotacaoService {

    @Autowired
    private VotosRepository votosRepository;

    @Autowired
    private CampanhasRepository campanhasRepository;

    @Autowired
    private OpcaoVotoRepository opcaoVotoRepository;

    public ResponseEntity<?> registrarVoto(VotosRequestDTO dto, Usuarios usuario) {

        var campanha = campanhasRepository.findById(dto.campanhaId()).orElse(null);
        if (campanha == null) {
            return ResponseEntity.notFound().build();
        }

        // Campanha privada requer usuário logado
        if (!campanha.isVotacaoAnonima() && usuario == null) {
            return ResponseEntity.badRequest().body("Usuário obrigatório para campanha privada.");
        }

        var opcoes = opcaoVotoRepository.findAllById(dto.opcoesIds());
        if (opcoes.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhuma opção válida selecionada.");
        }

        // Regras de tipo de campanha
        switch (campanha.getTipoCampanha()) {
            case VOTO_UNICO -> {
                if (usuario != null && !votosRepository.findByUsuarioAndCampanha(usuario, campanha).isEmpty()) {
                    return ResponseEntity.badRequest().body("Usuário já votou nesta campanha.");
                }
            }
            case SELECAO_MULTIPLA -> {}
            case RANQUEADO -> {
                if (opcoes.size() != campanha.getOpcoesDeVoto().size()) {
                    return ResponseEntity.badRequest().body("Você deve ordenar todas as opções da campanha.");
                }
            }
            case VOTOS_MULTIPLOS -> {}
        }

        // Cria e salva o voto
        Votos voto = new Votos();
        voto.setUsuario(usuario);
        voto.setCampanha(campanha);
        voto.setOpcoes(opcoes);

        Votos salvo = votosRepository.save(voto);

        // Incrementa contagem de votos em cada opção
        opcoes.forEach(op -> {
            op.setTotalVotos(op.getTotalVotos() + 1);
            opcaoVotoRepository.save(op);
        });

        return ResponseEntity.ok(VotosResponseDTO.fromEntity(salvo));
    }

    public ResponseEntity<List<VotosResponseDTO>> listarVotosPorCampanha(Long campanhaId) {
        var campanha = campanhasRepository.findById(campanhaId).orElse(null);
        if (campanha == null) return ResponseEntity.notFound().build();

        List<Votos> votos = votosRepository.findByCampanha(campanha);
        List<VotosResponseDTO> dtoList = votos.stream()
                .map(VotosResponseDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(dtoList);
    }
}
