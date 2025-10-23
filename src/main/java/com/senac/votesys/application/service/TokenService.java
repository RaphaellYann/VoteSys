package com.senac.votesys.application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.senac.votesys.application.dto.login.LoginRequestDTO;
import com.senac.votesys.domain.entity.Token;
import com.senac.votesys.domain.entity.Usuarios;
import com.senac.votesys.domain.repository.TokenRepository;
import com.senac.votesys.domain.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${spring.secretkey}")
    private String secret;

    @Value("${spring.tempo_expiracao}")
    private Long tempoExpiracao;

    private final String emissor = "VoteSys";

    @Autowired
    TokenRepository tokenRepository;


    @Autowired
    private UsuariosRepository usuarioRepository;

    public String gerarToken(LoginRequestDTO loginRequest) {

        var usuario = usuarioRepository.findByEmail(loginRequest.email()).orElse(null);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JWT.create()
                .withIssuer(emissor)
                .withSubject(usuario.getEmail())
                .withExpiresAt(this.gerarDataExpiracao())
                .sign(algorithm);

        tokenRepository.save(new Token(null, token, usuario));

        return token;
    }

    public Usuarios validarToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(emissor)
                .build();
             verifier.verify(token);

             var tokenResult = tokenRepository.findByToken(token).orElse(null);

             if(tokenResult == null){
                 throw new IllegalArgumentException("Token Inválido!");
             }

             return tokenResult.getUsuario();
    }

    private Instant gerarDataExpiracao() {
        LocalDateTime dataAtual = LocalDateTime.now().plusMinutes(tempoExpiracao);
        dataAtual = dataAtual.plusMinutes(tempoExpiracao);
        return dataAtual.toInstant(ZoneOffset.of("-03:00"));
    }
}
