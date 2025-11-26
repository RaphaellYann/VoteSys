package com.senac.votesys.application.dto.campanha;

import com.senac.votesys.domain.entity.Campanhas;
import com.senac.votesys.domain.entity.OpcaoVoto;
import com.senac.votesys.domain.entity.TipoCampanha;

import java.time.LocalDateTime;

public record CampanhasResponseDTO(
        Long id,
        String titulo,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        boolean ativo,
        TipoCampanha tipoCampanha,
        int totalVotos,
        boolean usuarioJaVotou
) {
    public static CampanhasResponseDTO fromEntity(Campanhas campanha, boolean usuarioJaVotou) {
        int total = 0;

        if (campanha.getOpcoesDeVoto() != null) {
            total = campanha.getOpcoesDeVoto()
                    .stream()
                    .mapToInt(OpcaoVoto::getTotalVotos)
                    .sum();
        }

        return new CampanhasResponseDTO(
                campanha.getId(),
                campanha.getTitulo(),
                campanha.getDescricao(),
                campanha.getDataInicio(),
                campanha.getDataFim(),
                campanha.isAtivo(),
                campanha.getTipoCampanha(),
                total,
                usuarioJaVotou
        );
    }
}
