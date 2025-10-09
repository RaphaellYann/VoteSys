package com.senac.votesys.dto;

import com.senac.votesys.model.Campanhas;
import java.time.LocalDateTime;

public record CampanhasResponseDTO(
        Long id,
        String titulo,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        boolean ativo
) {
    public CampanhasResponseDTO(Campanhas c) {
        this(c.getId(), c.getTitulo(), c.getDescricao(), c.getDataInicio(), c.getDataFim(), c.isAtivo());
    }
}
