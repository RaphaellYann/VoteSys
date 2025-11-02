package com.senac.votesys.presentation;

import com.senac.votesys.application.dto.usuario.UsuarioRequestDTO;
import com.senac.votesys.application.dto.usuario.UsuarioResponseDTO;
import com.senac.votesys.application.service.UsuariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN_GERAL')")
    public ResponseEntity<UsuarioResponseDTO> consultarPorId(@PathVariable Long id) {
        var usuario = usuariosService.consultarPorId(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Retorna todos os usuários cadastrados")
    @PreAuthorize("hasRole('ADMIN_GERAL')")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {

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
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        try {
            var usuarioResponse = usuariosService.salvarUsuario(usuarioRequestDTO);

            return ResponseEntity.ok(usuarioResponse);

        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }
}
