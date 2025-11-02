    package com.senac.votesys.domain.entity;

    import com.senac.votesys.application.dto.usuario.UsuarioRequestDTO;
    import com.senac.votesys.application.dto.usuario.UsuarioResponseDTO;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.time.LocalDateTime;
    import java.util.Collection;
    import java.util.List;

    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name="usuarios")
    public class Usuarios implements UserDetails {

        public Usuarios (UsuarioRequestDTO usuarioRequestDTO) {
            this.setNome(usuarioRequestDTO.nome());
            this.setSenha(usuarioRequestDTO.senha());
            this.setEmail(usuarioRequestDTO.email());
            this.setCpf(usuarioRequestDTO.cpf());
            this.setRole(usuarioRequestDTO.role());

            if(this.getDataCadastro()==null){
                this.setDataCadastro(LocalDateTime.now()
                ); // caso nao tenha data cadastro iera implentar, caso tenha ira continuar igual
            }
        }

        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        private Long id;

        private String nome;

        private String cpf;

        private String senha;

        private String email;

        private String role;

        private String tokenSenha;

        private LocalDateTime dataCadastro;

        private LocalDateTime tokenSenhaExpiracao;


        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            if ("ROLE_ADMIN_GERAL".equals(this.role)) {
                return List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN_GERAL"),
                        new SimpleGrantedAuthority("ROLE_ADMIN_NORMAL"),
                        new SimpleGrantedAuthority("ROLE_USER")
                );

            }else if ("ROLE_ADMIN_NORMAL".equals(this.role)) {
                return List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN_NORMAL"),
                        new SimpleGrantedAuthority("ROLE_USER")
                );

            }else {
                return List.of(new SimpleGrantedAuthority("ROLE_USER")
                );
            }


        }

        @Override
        public String getPassword() {
            return this.senha;
        }

        @Override
        public String getUsername() {
            return this.email;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        public UsuarioResponseDTO toDtoResponse() { //convertendo um usuario
            return new UsuarioResponseDTO(this);
        }

    }