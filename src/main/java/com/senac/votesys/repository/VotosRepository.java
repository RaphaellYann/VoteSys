package com.senac.votesys.repository;

import com.senac.votesys.model.Votos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotosRepository extends JpaRepository<Votos,Long> {
}
