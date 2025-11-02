package com.senac.votesys.domain.repository;

import com.senac.votesys.domain.entity.Campanhas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CampanhasRepository extends JpaRepository<Campanhas,Long> {

    List<Campanhas> findByUsuarioId(Long usuarioId);

    Optional<Campanhas> findByIdAndUsuarioId(Long id, Long usuarioId);

    List<Campanhas> findByAtivoAndDataInicioBeforeAndDataFimAfter(
            boolean ativo,
            LocalDateTime dataInicio,
            LocalDateTime dataFim
    );
}
