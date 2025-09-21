package com.senac.votesys.controller;

import com.senac.votesys.dto.OpcaoVotoRequestDTO;
import com.senac.votesys.dto.OpcaoVotoResponseDTO;
import com.senac.votesys.model.Campanhas;
import com.senac.votesys.model.OpcaoVoto;
import com.senac.votesys.repository.CampanhasRepository;
import com.senac.votesys.repository.OpcaoVotoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/opcaoVoto")
@Tag(name = "Opções de Voto", description = "Controlador para gerenciar as opções de voto")
public class OpcaoVotoController {

    @Autowired
    private OpcaoVotoRepository opcaoVotoRepository;

    @Autowired
    private CampanhasRepository campanhasRepository;

    @GetMapping("/{id}")
    @Operation(summary = "Consultar Opção de Voto", description = "Consultar opção de voto por ID")
    public ResponseEntity<OpcaoVotoResponseDTO> consultarPorId(@PathVariable long id) {
        var opcao = opcaoVotoRepository.findById(id).orElse(null);
        if (opcao == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(OpcaoVotoResponseDTO.fromEntity(opcao));
    }

    @GetMapping
    @Operation(summary = "Listar Todas Opções de Voto", description = "Consultar todas as opções de voto")
    public ResponseEntity<List<OpcaoVotoResponseDTO>> listarTodos() {
        var lista = opcaoVotoRepository.findAll().stream()
                .map(OpcaoVotoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/por-campanha/{campanhaId}")
    @Operation(summary = "Listar Opções por Campanha", description = "Consultar todas as opções de voto de uma campanha")
    public ResponseEntity<List<OpcaoVotoResponseDTO>> listarPorCampanha(@PathVariable long campanhaId) {
        var lista = opcaoVotoRepository.findByCampanhaId(campanhaId).stream()
                .map(OpcaoVotoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    @Operation(summary = "Criar Opção de Voto", description = "Cadastrar uma nova opção de voto")
    public ResponseEntity<?> criarOpcaoVoto(@RequestBody OpcaoVotoRequestDTO dto) {
        try {
            var campanha = campanhasRepository.findById(dto.campanhaId()).orElse(null);
            if (campanha == null) {
                return ResponseEntity.notFound().build();
            }

            var novaOpcao = new OpcaoVoto();
            novaOpcao.setNome(dto.nome());
            novaOpcao.setCampanha(campanha);

            var opcaoSalva = opcaoVotoRepository.save(novaOpcao);
            return ResponseEntity.ok(OpcaoVotoResponseDTO.fromEntity(opcaoSalva));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Opção de Voto", description = "Atualizar dados de uma opção de voto existente")
    public ResponseEntity<?> atualizarOpcaoVoto(@PathVariable long id, @RequestBody OpcaoVotoRequestDTO dto) {
        if (!opcaoVotoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            var campanha = campanhasRepository.findById(dto.campanhaId()).orElse(null);
            if (campanha == null) {
                return ResponseEntity.notFound().build();
            }

            var opcao = opcaoVotoRepository.findById(id).orElse(null);
            if (opcao == null) {
                return ResponseEntity.notFound().build();
            }

            opcao.setNome(dto.nome());
            opcao.setCampanha(campanha);

            var opcaoAtualizada = opcaoVotoRepository.save(opcao);
            return ResponseEntity.ok(OpcaoVotoResponseDTO.fromEntity(opcaoAtualizada));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir Opção de Voto", description = "Excluir uma opção de voto")
    public ResponseEntity<?> excluirOpcaoVoto(@PathVariable long id) {
        if (!opcaoVotoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            opcaoVotoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
