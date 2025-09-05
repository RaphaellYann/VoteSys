package com.senac.votesys.repository;


import com.senac.votesys.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuarios, Long> {

    Optional<Usuarios> findByEmailAndNome(String email, String Nome);

    boolean existsUsuarioByEmailContainingAndSenha(String email, String senha);

    Optional<Usuarios> findByEmail(String email);
}
