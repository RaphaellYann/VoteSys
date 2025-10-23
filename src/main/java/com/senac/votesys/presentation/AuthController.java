package com.senac.votesys.presentation;

import com.senac.votesys.application.dto.login.LoginRequestDTO;
import com.senac.votesys.application.dto.login.LoginResponseDTO;
import com.senac.votesys.application.service.TokenService;
import com.senac.votesys.application.service.UsuariosService;
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
