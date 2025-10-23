package com.senac.votesys.presentation;

import com.senac.votesys.domain.entity.Usuarios;
import com.senac.votesys.domain.repository.UsuariosRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Controlador de Usuários", description = "Camada responsável por controlar os registros de usuários")
public class UsuariosController {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @GetMapping("/{id}")
    @Operation(summary = "Listar Usuário", description = "Método resposavel por listar usuário por ID")
    public ResponseEntity<Usuarios> consultaPorId(@PathVariable Long id) {
        var usuario = usuariosRepository.findById(id)
                .orElse(null);

        SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    @Operation(summary = "Listar todos Usuários", description = "Método resposavel por listar os usuários")
    public ResponseEntity<?> consultarTodos() {

        return ResponseEntity.ok(usuariosRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Criar Usuario", description = "Método responsável em criar um novo usuário")
    public ResponseEntity<?> criarUsuario(@RequestBody Usuarios usuario){

        try {

            var usuarioResponse = usuariosRepository.save(usuario);

            return ResponseEntity.ok(usuarioResponse);

        }catch (Exception e){
            return ResponseEntity.badRequest().build();

        }
    }

}
