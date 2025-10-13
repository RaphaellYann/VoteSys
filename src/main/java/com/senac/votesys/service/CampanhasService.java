package com.senac.votesys.service;

import com.senac.votesys.dto.CampanhasRequestDTO;
import com.senac.votesys.dto.CampanhasResponseDTO;
import com.senac.votesys.model.Campanhas;
import com.senac.votesys.repository.CampanhasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampanhasService {

    @Autowired
    private CampanhasRepository campanhasRepository;

    public ResponseEntity<CampanhasResponseDTO> listarPorId(Long id) {
        var campanha = campanhasRepository.findById(id).orElse(null);
        if (campanha == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(CampanhasResponseDTO.fromEntity(campanha));
    }

    public ResponseEntity<List<CampanhasResponseDTO>>   listarTodos(String filtro) {
        var lista = campanhasRepository.findAll().stream()
                .sorted(Comparator.comparing(Campanhas::getTituloUpper))
                .filter( a -> filtro == null || a.getTitulo().contains(filtro))
                .map(CampanhasResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }


    public ResponseEntity<?> criarCampanha(CampanhasRequestDTO dto) {
        try {
            var campanha = new Campanhas();
            campanha.setTitulo(dto.titulo());
            campanha.setDescricao(dto.descricao());
            campanha.setDataInicio(dto.dataInicio());
            campanha.setDataFim(dto.dataFim());
            campanha.setAtivo(dto.ativo());
            campanha.setVotacaoAnonima(dto.votacaoAnonima());
            campanha.setTipoCampanha(dto.tipoCampanha());

            var salvo = campanhasRepository.save(campanha);
            return ResponseEntity.ok(CampanhasResponseDTO.fromEntity(salvo));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar campanha: " + e.getMessage());
        }
    }

    public ResponseEntity<?> atualizarCampanha(Long id, CampanhasRequestDTO dto) {
        var campanha = campanhasRepository.findById(id).orElse(null);
        if (campanha == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            campanha.setTitulo(dto.titulo());
            campanha.setDescricao(dto.descricao());
            campanha.setDataInicio(dto.dataInicio());
            campanha.setDataFim(dto.dataFim());
            campanha.setAtivo(dto.ativo());
            campanha.setVotacaoAnonima(dto.votacaoAnonima());
            campanha.setTipoCampanha(dto.tipoCampanha());

            var atualizado = campanhasRepository.save(campanha);
            return ResponseEntity.ok(CampanhasResponseDTO.fromEntity(atualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar campanha: " + e.getMessage());
        }
    }

    public ResponseEntity<?> excluirCampanha(Long id) {
        if (!campanhasRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            campanhasRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir campanha: " + e.getMessage());
        }
    }
}
