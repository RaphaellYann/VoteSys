package com.senac.votesys.presentation;

import com.senac.votesys.application.dto.login.LoginRequestDTO;
import com.senac.votesys.application.dto.login.LoginResponseDTO;
import com.senac.votesys.application.dto.usuario.*;
import com.senac.votesys.application.service.TokenService;
import com.senac.votesys.application.service.UsuariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Controller de Autenticação", description = "Controla o login e a recuperação de senha dos usuários")
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuariosService usuariosService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Realiza autenticação do usuário e gera token JWT")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {

        if (!usuariosService.validarSenha(request)) {
            return ResponseEntity.badRequest().body("Usuário ou senha inválido.");
        }

        var token = tokenService.gerarToken(request);

        var usuario = usuariosService.consultarPorEmail(request.email());

        return ResponseEntity.ok(new LoginResponseDTO(token, usuario));
    }

    @PostMapping("/recuperarsenha")
    @Operation(summary = "Esqueci Minha Senha", description = "Envia e-mail com link de recuperação de senha")
    public ResponseEntity<?> gerarLinkRecuperacao(@RequestBody RecuperarSenhaDTO recuperarSenhaDTO) {
        try {

            usuariosService.gerarLinkRecuperacao(recuperarSenhaDTO);

            return ResponseEntity.ok("E-mail de recuperação enviado com sucesso!");

        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping("/resetarsenha")
    @Operation(summary = "Redefinir Senha", description = "Define uma nova senha usando o token recebido por e-mail")
    public ResponseEntity<?> resetarSenha(@RequestBody ResetarSenhaDTO resetarSenhaDTO) {
        try {

            usuariosService.resetarSenha(resetarSenhaDTO);

            return ResponseEntity.ok("Senha alterada com sucesso!");
        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PostMapping("/alterarsenha")
    @Operation(summary = "Alterar Senha", description = "Permite que um usuário logado altere sua senha atual")
    public ResponseEntity<?> alterarSenha(@RequestBody AlterarSenhaDTO alterarSenhaDTO) {

        try {

            usuariosService.alterarSenha(alterarSenhaDTO);

            return ResponseEntity.ok("Senha alterada com sucesso!");
        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}
