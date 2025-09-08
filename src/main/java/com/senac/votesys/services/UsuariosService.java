package com.senac.votesys.services;

import com.senac.votesys.dto.LoginRequestDTO;
import com.senac.votesys.repository.UsuariosRepository;
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
