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
            @RequestParam(required = false) String filtro) {

        return campanhasService.listarCampanhasParaVotacao(filtro);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar campanha por ID")
    public ResponseEntity<?> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal UsuarioPrincipalDTO autenticacao) {

        return campanhasService.listarPorId(id, autenticacao);
    }

    @GetMapping
    @Operation(summary = "Listar todas as campanhas")
    public ResponseEntity<List<CampanhasResponseDTO>> listarTodos(
            @RequestParam(required = false) String filtro,
            @AuthenticationPrincipal UsuarioPrincipalDTO autenticacao) {

        return campanhasService.listarTodos(filtro, autenticacao);
    }


    @PostMapping
    @Operation(summary = "Criar nova campanha")
    public ResponseEntity<?> criar(
            @RequestBody CampanhasRequestDTO dto,
            @AuthenticationPrincipal UsuarioPrincipalDTO autenticacao) {

        return campanhasService.criarCampanha(dto, autenticacao);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar campanha existente")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @RequestBody CampanhasRequestDTO dto,
            @AuthenticationPrincipal UsuarioPrincipalDTO autenticacao) {

        return campanhasService.atualizarCampanha(id, dto, autenticacao);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir campanha")
    public ResponseEntity<?> excluir(
            @PathVariable Long id) {

        return campanhasService.excluirCampanha(id);
    }
}
