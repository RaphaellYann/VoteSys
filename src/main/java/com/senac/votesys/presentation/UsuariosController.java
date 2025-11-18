package com.senac.votesys.presentation;

import com.senac.votesys.application.dto.usuario.UsuarioPrincipalDTO;
import com.senac.votesys.application.dto.usuario.UsuarioRequestDTO;
import com.senac.votesys.application.service.UsuariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Controlador de Usuários", description = "Gerencia o CRUD de usuários (sem exclusão)")
public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    @GetMapping("/{id}")
    @Operation(summary = "Consultar Usuário por ID", description = "Busca um usuário específico pelo ID")
    public ResponseEntity<?> consultarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal UsuarioPrincipalDTO usuarioLogado) {

        if (usuarioLogado == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        var usuario = usuariosService.consultarPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna todos os usuários cadastrados")
    public ResponseEntity<?> listarTodos(@AuthenticationPrincipal UsuarioPrincipalDTO usuarioLogado) {
        if (usuarioLogado == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        var usuarios = usuariosService.consultarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    @Operation(summary = "Criar novo usuário", description = "Cadastra um novo usuário no sistema")
    public ResponseEntity<?> criarUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        try {
            var usuarioResponse = usuariosService.criarUsuario(usuarioRequestDTO);
            return ResponseEntity.ok(usuarioResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    public ResponseEntity<?> atualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioRequestDTO usuarioRequestDTO,
            @AuthenticationPrincipal UsuarioPrincipalDTO usuarioLogado) {

        if (usuarioLogado == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        // Usuário só pode editar a si mesmo, a menos que seja Admin Geral.
        if (!usuarioLogado.isAdminGeral() && !usuarioLogado.id().equals(id)) {
            return ResponseEntity.status(403).body("Você não tem permissão para editar este usuário.");
        }

        try {
            // Agora passa todos os argumentos necessários para o service
            var usuarioResponse = usuariosService.salvarUsuario(id, usuarioRequestDTO, usuarioLogado);
            return ResponseEntity.ok(usuarioResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }
}
