package com.senac.votesys.dto;

import com.senac.votesys.model.OpcaoVoto;

public record OpcaoVotoResponseDTO(
        Long id,
        String nome,
        Integer totalVotos,
        Long campanhaId,
        String nomeCampanha
) {
    public static OpcaoVotoResponseDTO fromEntity(OpcaoVoto opcaoVoto) {
        return new OpcaoVotoResponseDTO(
                opcaoVoto.getId(),
                opcaoVoto.getNome(),
                opcaoVoto.getTotalVotos(),
                opcaoVoto.getCampanha().getId(),
                opcaoVoto.getCampanha().getTitulo()
        );
    }
}
