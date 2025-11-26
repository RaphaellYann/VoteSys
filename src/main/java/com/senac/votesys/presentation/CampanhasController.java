package com.senac.votesys.presentation;

import com.senac.votesys.application.dto.campanha.CampanhasRequestDTO;
import com.senac.votesys.application.dto.campanha.CampanhasResponseDTO;
import com.senac.votesys.application.dto.usuario.UsuarioPrincipalDTO;
import com.senac.votesys.application.service.CampanhasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campanhas")
@Tag(name = "Controle de Campanhas", description = "Gerencia as campanhas de votação com diferentes tipos de regras")
public class CampanhasController {

    @Autowired
    private CampanhasService campanhasService;

    @GetMapping("/publicas")
    @Operation(summary = "Listar campanhas públicas para votação", description = "endpoint para os usuários normais votarem")
    public ResponseEntity<List<CampanhasResponseDTO>> listarPublicas(
            @RequestParam(required = false) String filtro,
            @AuthenticationPrincipal UsuarioPrincipalDTO autenticacao) {

        return ResponseEntity.ok(campanhasService.listarCampanhasParaVotacao(filtro, autenticacao));
    }

    @GetMapping("/resultados")
    @Operation(summary = "Listar campanhas para tela de Resultados",
            description = "Retorna todas as campanhas para que qualquer usuário veja os parciais/finais.")
    public ResponseEntity<List<CampanhasResponseDTO>> listarParaResultados(
            @RequestParam(required = false) String filtro,
            @AuthenticationPrincipal UsuarioPrincipalDTO autenticacao) {

        return ResponseEntity.ok(campanhasService.listarCampanhasParaResultados(filtro, autenticacao));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar campanha por ID")
    public ResponseEntity<CampanhasResponseDTO> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal UsuarioPrincipalDTO autenticacao) {

        var campanha = campanhasService.listarPorId(id, autenticacao);

        if (campanha == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(campanha);
    }

    @GetMapping
    @Operation(summary = "Listar todas as campanhas")
    public ResponseEntity<List<CampanhasResponseDTO>> listarTodos(
            @RequestParam(required = false) String filtro,
            @AuthenticationPrincipal UsuarioPrincipalDTO autenticacao) {

        return ResponseEntity.ok(campanhasService.listarTodos(filtro, autenticacao));
    }

    @PostMapping
    @Operation(summary = "Criar nova campanha")
    public ResponseEntity<CampanhasResponseDTO> criar(
            @RequestBody CampanhasRequestDTO dto,
            @AuthenticationPrincipal UsuarioPrincipalDTO autenticacao) {

        try {
            var campanhaResponse = campanhasService.criarCampanha(dto, autenticacao);
            return ResponseEntity.ok(campanhaResponse);
        } catch (Exception e) {

            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar campanha existente")
    public ResponseEntity<CampanhasResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody CampanhasRequestDTO dto,
            @AuthenticationPrincipal UsuarioPrincipalDTO autenticacao) {

        try {
            var campanhaResponse = campanhasService.atualizarCampanha(id, dto, autenticacao);
            return ResponseEntity.ok(campanhaResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir campanha")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        try {
            campanhasService.excluirCampanha(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}