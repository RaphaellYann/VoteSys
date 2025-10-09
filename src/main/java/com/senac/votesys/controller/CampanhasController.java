package com.senac.votesys.controller;

import com.senac.votesys.dto.CampanhasRequestDTO;
import com.senac.votesys.services.CampanhasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campanhas")
@Tag(name = "Controle de Campanhas", description = "Camada responsável por controlar os registros de campanhas")
public class CampanhasController {

    @Autowired
    private CampanhasService campanhasService;

    @GetMapping("/{id}")
    @Operation(summary = "Listar Campanha", description = "Consulta campanha por ID")
    public ResponseEntity<?> listarPorId(@PathVariable long id) {
        try {
            var campanha = campanhasService.listarPorId(id);
            return ResponseEntity.ok(campanha);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar Todas Campanhas", description = "Consulta todas as campanhas")
    public ResponseEntity<?> listarTodos() {
        return ResponseEntity.ok(campanhasService.listarTodos());
    }

    @PostMapping
    @Operation(summary = "Criar Campanha", description = "Cria uma nova campanha")
    public ResponseEntity<?> criarCampanha(@RequestBody CampanhasRequestDTO dto) {
        try {
            var campanha = campanhasService.criarCampanha(dto);
            return ResponseEntity.ok(campanha);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Campanha", description = "Atualiza dados de uma campanha")
    public ResponseEntity<?> atualizarCampanha(@PathVariable long id, @RequestBody CampanhasRequestDTO dto) {
        try {
            var campanha = campanhasService.atualizarCampanha(id, dto);
            return ResponseEntity.ok(campanha);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir Campanha", description = "Exclui uma campanha")
    public ResponseEntity<?> excluirCampanha(@PathVariable long id) {
        try {
            campanhasService.excluirCampanha(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
