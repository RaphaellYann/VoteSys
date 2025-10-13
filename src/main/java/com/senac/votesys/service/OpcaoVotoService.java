package com.senac.votesys.service;

import com.senac.votesys.dto.OpcaoVotoRequestDTO;
import com.senac.votesys.dto.OpcaoVotoResponseDTO;
import com.senac.votesys.model.OpcaoVoto;
import com.senac.votesys.repository.CampanhasRepository;
import com.senac.votesys.repository.OpcaoVotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpcaoVotoService {

    @Autowired
    private OpcaoVotoRepository opcaoVotoRepository;

    @Autowired
    private CampanhasRepository campanhasRepository;

    public OpcaoVotoResponseDTO consultarPorId(long id) {
        var opcao = opcaoVotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opção de voto não encontrada"));
        return OpcaoVotoResponseDTO.fromEntity(opcao);
    }

    public List<OpcaoVotoResponseDTO> listarTodos() {
        return opcaoVotoRepository.findAll().stream()
                .map(OpcaoVotoResponseDTO::fromEntity)
                .toList();
    }

    public List<OpcaoVotoResponseDTO> listarPorCampanha(long campanhaId) {
        return opcaoVotoRepository.findByCampanhaId(campanhaId).stream()
                .map(OpcaoVotoResponseDTO::fromEntity)
                .toList();
    }

    public OpcaoVotoResponseDTO criarOpcaoVoto(OpcaoVotoRequestDTO dto) {
        var campanha = campanhasRepository.findById(dto.campanhaId())
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada"));

        OpcaoVoto novaOpcao = new OpcaoVoto();
        novaOpcao.setNome(dto.nome());
        novaOpcao.setCampanha(campanha);
        novaOpcao.setTotalVotos(0);

        var opcaoSalva = opcaoVotoRepository.save(novaOpcao);
        return OpcaoVotoResponseDTO.fromEntity(opcaoSalva);
    }

    public OpcaoVotoResponseDTO atualizarOpcaoVoto(long id, OpcaoVotoRequestDTO dto) {
        var opcao = opcaoVotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opção de voto não encontrada"));

        var campanha = campanhasRepository.findById(dto.campanhaId())
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada"));

        opcao.setNome(dto.nome());
        opcao.setCampanha(campanha);

        var opcaoAtualizada = opcaoVotoRepository.save(opcao);
        return OpcaoVotoResponseDTO.fromEntity(opcaoAtualizada);
    }

    public void excluirOpcaoVoto(long id) {
        if (!opcaoVotoRepository.existsById(id)) {
            throw new RuntimeException("Opção de voto não encontrada");
        }
        opcaoVotoRepository.deleteById(id);
    }
}
