package com.senac.votesys.repository;

import com.senac.votesys.model.Campanhas;
import com.senac.votesys.model.Usuarios;
import com.senac.votesys.model.Votos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VotosRepository extends JpaRepository<Votos, Long> {

    List<Votos> findByCampanha(Campanhas campanha);

    List<Votos> findByUsuarioAndCampanha(Usuarios usuario, Campanhas campanha);
}
