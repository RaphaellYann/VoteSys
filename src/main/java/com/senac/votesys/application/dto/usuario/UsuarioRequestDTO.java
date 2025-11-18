package com.senac.votesys.application.dto.usuario;

public record UsuarioRequestDTO (Long id,
                                 String nome,
                                 String email,
                                 String cpf,
                                 String role,
                                 String senha) {
}
