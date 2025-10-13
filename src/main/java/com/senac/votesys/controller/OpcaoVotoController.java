package com.senac.votesys.controller;

import com.senac.votesys.dto.OpcaoVotoRequestDTO;
import com.senac.votesys.service.OpcaoVotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/opcaoVoto")
@Tag(name = "Opções de Voto", description = "Controlador para gerenciar as opções de voto")
public class OpcaoVotoController {

    @Autowired
    private OpcaoVotoService opcaoVotoService;

    @GetMapping("/{id}")
    @Operation(summary = "Consultar Opção de Voto", description = "Consultar opção de voto por ID")
    public ResponseEntity<?> consultarPorId(@PathVariable long id) {
        try {
            var opcao = opcaoVotoService.consultarPorId(id);
            return ResponseEntity.ok(opcao);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar Todas Opções de Voto", description = "Consultar todas as opções de voto")
    public ResponseEntity<?> listarTodos() {
        return ResponseEntity.ok(opcaoVotoService.listarTodos());
    }

    @GetMapping("/por-campanha/{campanhaId}")
    @Operation(summary = "Listar Opções por Campanha", description = "Consultar todas as opções de voto de uma campanha")
    public ResponseEntity<?> listarPorCampanha(@PathVariable long campanhaId) {
        return ResponseEntity.ok(opcaoVotoService.listarPorCampanha(campanhaId));
    }

    @PostMapping
    @Operation(summary = "Criar Opção de Voto", description = "Cadastrar uma nova opção de voto")
    public ResponseEntity<?> criarOpcaoVoto(@RequestBody OpcaoVotoRequestDTO dto) {
        try {
            var opcao = opcaoVotoService.criarOpcaoVoto(dto);
            return ResponseEntity.ok(opcao);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Opção de Voto", description = "Atualizar dados de uma opção de voto existente")
    public ResponseEntity<?> atualizarOpcaoVoto(@PathVariable long id, @RequestBody OpcaoVotoRequestDTO dto) {
        try {
            var opcao = opcaoVotoService.atualizarOpcaoVoto(id, dto);
            return ResponseEntity.ok(opcao);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir Opção de Voto", description = "Excluir uma opção de voto")
    public ResponseEntity<?> excluirOpcaoVoto(@PathVariable long id) {
        try {
            opcaoVotoService.excluirOpcaoVoto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
