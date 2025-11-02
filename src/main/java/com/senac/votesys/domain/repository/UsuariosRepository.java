package com.senac.votesys.domain.repository;


import com.senac.votesys.domain.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios,Long> {

    boolean existsUsuarioByEmailContainingAndSenha(String email, String senha);

    Optional<Usuarios> findByEmail(String email);

    Optional<Usuarios> findByTokenSenha(String tokenSenha);
}
