package com.senac.votesys.application.dto.votos;


import com.senac.votesys.domain.entity.OpcaoVoto;
import com.senac.votesys.domain.entity.Votos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record VotosResponseDTO(
        Long id,
        Long usuarioId,
        Long campanhaId,
        List<Long> opcoesIds,
        LocalDateTime dataVoto
) {
    public static VotosResponseDTO fromEntity(Votos voto) {
        return new VotosResponseDTO(
                voto.getId(),
                voto.getUsuario() != null ? voto.getUsuario().getId() : null,
                voto.getCampanha() != null ? voto.getCampanha().getId() : null,
                voto.getOpcoes() != null
                        ? voto.getOpcoes().stream().map(OpcaoVoto::getId).collect(Collectors.toList())
                        : null,
                voto.getDataVoto()
        );
    }
}
