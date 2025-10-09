package com.senac.votesys.services;

import com.senac.votesys.dto.CampanhasRequestDTO;
import com.senac.votesys.dto.CampanhasResponseDTO;
import com.senac.votesys.model.Campanhas;
import com.senac.votesys.repository.CampanhasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampanhasService {

    @Autowired
    private CampanhasRepository campanhasRepository;

    public CampanhasResponseDTO listarPorId(long id) {
        var campanha = campanhasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada"));
        return new CampanhasResponseDTO(campanha);
    }

    public List<CampanhasResponseDTO> listarTodos() {
        return campanhasRepository.findAll().stream()
                .map(CampanhasResponseDTO::new)
                .toList();
    }

    public CampanhasResponseDTO criarCampanha(CampanhasRequestDTO dto) {
        Campanhas campanha = new Campanhas();
        campanha.setTitulo(dto.titulo());
        campanha.setDescricao(dto.descricao());
        campanha.setDataInicio(dto.dataInicio());
        campanha.setDataFim(dto.dataFim());
        campanha.setAtivo(dto.ativo());

        var campanhaSalva = campanhasRepository.save(campanha);
        return new CampanhasResponseDTO(campanhaSalva);
    }

    public CampanhasResponseDTO atualizarCampanha(long id, CampanhasRequestDTO dto) {
        if (!campanhasRepository.existsById(id)) {
            throw new RuntimeException("Campanha não encontrada");
        }

        Campanhas campanha = new Campanhas();
        campanha.setId(id);
        campanha.setTitulo(dto.titulo());
        campanha.setDescricao(dto.descricao());
        campanha.setDataInicio(dto.dataInicio());
        campanha.setDataFim(dto.dataFim());
        campanha.setAtivo(dto.ativo());

        var campanhaAtualizada = campanhasRepository.save(campanha);
        return new CampanhasResponseDTO(campanhaAtualizada);
    }

    public void excluirCampanha(long id) {
        if (!campanhasRepository.existsById(id)) {
            throw new RuntimeException("Campanha não encontrada");
        }
        campanhasRepository.deleteById(id);
    }
}
