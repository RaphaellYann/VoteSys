package com.senac.votesys.application.service;

import com.senac.votesys.application.dto.login.LoginRequestDTO;
import com.senac.votesys.domain.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository usuarioRepository;

    public boolean validarSenha(LoginRequestDTO loginRequest) {

        return usuarioRepository.existsUsuarioByEmailContainingAndSenha(loginRequest.email(),loginRequest.senha());
    }
}
