package com.senac.votesys.application.service;

import com.senac.votesys.application.dto.usuario.UsuarioPrincipalDTO;
import com.senac.votesys.application.dto.votos.VotosRequestDTO;
import com.senac.votesys.application.dto.votos.VotosResponseDTO;
import com.senac.votesys.domain.entity.Votos;
import com.senac.votesys.domain.repository.CampanhasRepository;
import com.senac.votesys.domain.repository.OpcaoVotoRepository;
import com.senac.votesys.domain.repository.UsuariosRepository;
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

    @Autowired
    private UsuariosRepository  usuariosRepository;

    public ResponseEntity<?> registrarVoto(VotosRequestDTO dto, UsuarioPrincipalDTO usuarioLogado) {

        if (usuarioLogado == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado. Você deve estar logado para votar.");
        }

        var usuario = usuariosRepository.findById(usuarioLogado.id())
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado no banco de dados. ID: " + usuarioLogado.id()));


        var campanha = campanhasRepository.findById(dto.campanhaId()).orElse(null);
        if (campanha == null) {
            return ResponseEntity.notFound().build();
        }

        var opcoes = opcaoVotoRepository.findAllById(dto.opcoesIds());
        if (opcoes.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhuma opção válida selecionada.");
        }

        switch (campanha.getTipoCampanha()) {
            case VOTO_UNICO -> {
                if (!votosRepository.findByUsuarioAndCampanha(usuario, campanha).isEmpty()) {
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

        Votos voto = new Votos();
        voto.setUsuario(usuario);
        voto.setCampanha(campanha);
        voto.setOpcoes(opcoes);

        Votos salvo = votosRepository.save(voto);

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
