package com.senac.votesys.controller;

import com.senac.votesys.dto.LoginRequestDTO;
import com.senac.votesys.dto.LoginResponseDTO;
import com.senac.votesys.services.TokenService;
import com.senac.votesys.services.UsuariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação Controller", description = "Controller responsavel pela autenticação da aplicação" )
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuariosService usuariosService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Método responsavel por efetuar o login do usuário")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request){

        if (!usuariosService.validarSenha(request)) {
            return ResponseEntity.badRequest().body("Usuario ou senha inválido");
        }

        var token = tokenService.gerarToken(request);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

}
