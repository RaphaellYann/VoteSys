package com.senac.votesys.dto;

import java.time.LocalDateTime;

public record CampanhasRequestDTO(
        String titulo,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        boolean ativo
) {}
