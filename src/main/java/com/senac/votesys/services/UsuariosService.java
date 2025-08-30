package com.senac.votesys.services;

import com.senac.votesys.dto.LoginRequest;
import com.senac.votesys.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuariosService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean validarSenha(LoginRequest loginRequest) {

        return usuarioRepository.existsUsuarioByEmailContainingAndSenha(loginRequest.email(),loginRequest.senha());
    }
}
