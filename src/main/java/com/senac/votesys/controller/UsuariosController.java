package com.senac.votesys.controller;

import com.senac.votesys.model.Usuarios;
import com.senac.votesys.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Controlador de Usuários", description = "Camada responsavel por controlar os registros de usuários")
public class UsuariosController {

    @Autowired
    private UsuarioRepository usuariosRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Usuarios> consultaPorId(@PathVariable Long id) {
        var usuario = usuariosRepository.findById(id)
                .orElse(null);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    @Operation(summary = "usuarios", description = "Método resposavel por calcular os custos da folha de pagamento e apos faz os lançamentos contabeis na tabela...")
    public ResponseEntity<?> consultarTodos() {
        return ResponseEntity.ok(usuariosRepository.findAll());
    }

    @PostMapping
    @Operation(summary = "Salvar Usuario", description = "Método responsável em criar usuários")
    public ResponseEntity<?> salvarUsuario(@RequestBody Usuarios usuario){

        try {

            var usuarioResponse = usuariosRepository.save(usuario);

            return ResponseEntity.ok(usuarioResponse);

        }catch (Exception e){
            return ResponseEntity.badRequest().build();

        }
    }

}
