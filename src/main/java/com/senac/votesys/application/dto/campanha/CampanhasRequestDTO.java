package com.senac.votesys.application.dto.campanha;

import com.senac.votesys.domain.entity.TipoCampanha;

import java.time.LocalDateTime;

public record CampanhasRequestDTO(
        String titulo,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        boolean ativo,
        TipoCampanha tipoCampanha
) {}
