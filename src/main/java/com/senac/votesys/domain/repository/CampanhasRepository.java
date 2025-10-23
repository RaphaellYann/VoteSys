package com.senac.votesys.domain.repository;

import com.senac.votesys.domain.entity.Campanhas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampanhasRepository extends JpaRepository<Campanhas,Long> {
}
