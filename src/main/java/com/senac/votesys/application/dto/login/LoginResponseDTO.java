package com.senac.votesys.application.dto.login;

import com.senac.votesys.application.dto.usuario.UsuarioResponseDTO;

public record LoginResponseDTO(String token, UsuarioResponseDTO usuario) {
}
