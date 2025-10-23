package com.senac.votesys.domain.repository;

import com.senac.votesys.domain.entity.OpcaoVoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcaoVotoRepository extends JpaRepository<OpcaoVoto, Long> {
    List<OpcaoVoto> findByCampanhaId(Long campanhaId);
}
