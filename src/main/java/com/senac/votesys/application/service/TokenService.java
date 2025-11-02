package com.senac.votesys.application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.senac.votesys.application.dto.login.LoginRequestDTO;
import com.senac.votesys.application.dto.usuario.UsuarioPrincipalDTO;
import com.senac.votesys.domain.entity.Token;
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

    @Value("${spring.secretkey}") //para buscar a variavel dentro do properties
    private String secret;

    @Value("${spring.tempo_expiracao}")
    private Long tempo;

    private String emissor = "DEVTEST";

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    public String gerarToken(LoginRequestDTO loginRequestDto) {

        var usuario = usuariosRepository.findByEmail(loginRequestDto.email()).orElse(null);

        Algorithm algorithm = Algorithm.HMAC256(secret); //criando uma chave com a palavra secreta
        String token = JWT.create() //criando o token
                .withIssuer(emissor)   //quem ta emitindo o token
                .withSubject(usuario.getEmail())  //informaçoes carregadas no token
                .withExpiresAt(this.gerarDataExpiracao())
                .sign(algorithm);

        tokenRepository.save(new Token(null, token, usuario));

        return token;

    }


//    public DecodedJWT validarToken(String token) {
//        Algorithm algorithm = Algorithm.HMAC256(secret);
//        JWTVerifier verifier = JWT.require(algorithm)
//                .withIssuer(emissor)
//                .build();
//        return verifier.verify(token);
//    }

    public UsuarioPrincipalDTO validarToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(emissor)
                .build();
        verifier.verify(token);

        var tokenResult = tokenRepository.findByToken(token).orElse(null);

        if (tokenResult == null) {
            throw new IllegalArgumentException("Token invalido");
        }

        return new UsuarioPrincipalDTO(tokenResult.getUsuario());
    }

    private Instant gerarDataExpiracao() {
        var dataAtual = LocalDateTime.now(); //pega o instant atual
        dataAtual = dataAtual.plusMinutes(tempo); //adiciona a variavel tempo ao instant atual
        return dataAtual.toInstant(ZoneOffset.of("-03:00")); //define o fuso horario para contar o instant

    }

}
