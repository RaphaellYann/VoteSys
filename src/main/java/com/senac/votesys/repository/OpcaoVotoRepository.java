package com.senac.votesys.repository;

import com.senac.votesys.model.OpcaoVoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpcaoVotoRepository extends JpaRepository<OpcaoVoto, Long> {
}
