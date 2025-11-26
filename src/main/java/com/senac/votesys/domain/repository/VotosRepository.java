package com.senac.votesys.domain.repository;

import com.senac.votesys.domain.entity.Campanhas;
import com.senac.votesys.domain.entity.Usuarios;
import com.senac.votesys.domain.entity.Votos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VotosRepository extends JpaRepository<Votos, Long> {

    List<Votos> findByCampanha(Campanhas campanha);

    List<Votos> findByUsuarioAndCampanha(Usuarios usuario, Campanhas campanha);

    boolean existsByUsuarioIdAndCampanhaId(Long usuarioId, Long campanhaId);
}
