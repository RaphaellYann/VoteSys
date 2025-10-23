package com.senac.votesys.application.dto.campanhas;

import com.senac.votesys.domain.entity.TipoCampanha;

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
