package com.senac.votesys.application.dto.usuario;

import com.senac.votesys.domain.entity.Usuarios;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        String role
) {

    public UsuarioResponseDTO(Usuarios usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getRole());
    }
}
