package com.senac.votesys.repository;

import com.senac.votesys.model.OpcaoVoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpcaoVotoRepository extends JpaRepository<OpcaoVoto, Long> {

    List<OpcaoVoto> findByCampanhaId(long campanhaId);
}
