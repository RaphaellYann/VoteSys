package com.senac.votesys.application.dto.usuario;

import com.senac.votesys.domain.entity.Usuarios;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public record UsuarioPrincipalDTO(
        Long id,
        String email,
        Collection<? extends GrantedAuthority> autorizacao,
        String role
) {

    public UsuarioPrincipalDTO(Usuarios usuario) {
        this(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getAuthorities(),
                usuario.getRole()
        );
    }

    public boolean isAdminGeral() {
        return "ROLE_ADMIN_GERAL".equalsIgnoreCase(this.role);
    }

    public boolean isAdminNormal() {
        return "ROLE_ADMIN_NORMAL".equalsIgnoreCase(this.role);
    }

    public boolean isUser() {
        return "ROLE_USER".equalsIgnoreCase(this.role);
    }
}
