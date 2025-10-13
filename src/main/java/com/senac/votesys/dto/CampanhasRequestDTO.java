package com.senac.votesys.dto;

import com.senac.votesys.model.TipoCampanha;
import java.time.LocalDateTime;

public record CampanhasRequestDTO(
        String titulo,
        String descricao,
        LocalDateTime dataInicio,//ver se pode apgar
        LocalDateTime dataFim,// ver se poder apagar
        boolean ativo,
        boolean votacaoAnonima,
        TipoCampanha tipoCampanha
) {}
