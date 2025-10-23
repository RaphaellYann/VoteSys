package com.senac.votesys.application.dto.opcaoVoto;


import com.senac.votesys.domain.entity.OpcaoVoto;

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
