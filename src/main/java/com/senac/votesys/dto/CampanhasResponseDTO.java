package com.senac.votesys.dto;

import com.senac.votesys.model.Campanhas;
import com.senac.votesys.model.OpcaoVoto;
import com.senac.votesys.model.TipoCampanha;

import java.time.LocalDateTime;

public record CampanhasResponseDTO(
        Long id,
        String titulo,
        String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        boolean ativo,
        boolean votacaoAnonima,
        TipoCampanha tipoCampanha,
        int totalVotos
) {
    public static CampanhasResponseDTO fromEntity(Campanhas campanha) {
        int total = 0;

        if (campanha.getOpcoesDeVoto() != null) {
            total = campanha.getOpcoesDeVoto()
                    .stream()
                    .mapToInt(OpcaoVoto::getTotalVotos)
                    .sum();
        }
        return new CampanhasResponseDTO( // remover e fazer igual professor usando this
                campanha.getId(),
                campanha.getTitulo(),
                campanha.getDescricao(),
                campanha.getDataInicio(),
                campanha.getDataFim(),
                campanha.isAtivo(),
                campanha.isVotacaoAnonima(),
                campanha.getTipoCampanha(),
                total
        );
    }
}
