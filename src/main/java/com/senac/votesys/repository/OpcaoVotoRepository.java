package com.senac.votesys.repository;

import com.senac.votesys.model.OpcaoVoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcaoVotoRepository extends JpaRepository<OpcaoVoto, Long> {
    List<OpcaoVoto> findByCampanhaId(Long campanhaId);
}
