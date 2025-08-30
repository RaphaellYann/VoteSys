package com.senac.votesys.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.senac.votesys.model.Token;
import com.senac.votesys.repository.TokenRepository;
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

    public String gerarToken(String usuario, String senha) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JWT.create()
                .withIssuer(emissor)
                .withSubject(usuario)
                .withExpiresAt(this.gerarDataExpiracao())
                .sign(algorithm);

        tokenRepository.save(new Token(null, token, usuario));

        return token;
    }

    public String validarToken(String token) {
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
