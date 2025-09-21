package com.senac.votesys.dto;

import com.senac.votesys.model.OpcaoVoto;

// DTO para representar a resposta da API.
public record OpcaoVotoResponseDTO(
        Long id,
        String nome,
        Integer totalVotos,
        Long campanhaId,
        String nomeCampanha // Bônus: útil para o frontend não ter que fazer outra chamada
) {
    /**
     * Método de fábrica estático.
     * Uma forma limpa de converter uma Entidade para um DTO.
     * @param opcaoVoto A entidade a ser convertida.
     * @return O DTO preenchido.
     */
    public static OpcaoVotoResponseDTO fromEntity(OpcaoVoto opcaoVoto) {
        return new OpcaoVotoResponseDTO(
                opcaoVoto.getId(),
                opcaoVoto.getNome(),
                opcaoVoto.getTotalVotos(),
                opcaoVoto.getCampanha().getId(),
                opcaoVoto.getCampanha().getTitulo() // Supondo que a entidade Campanhas tenha um getNome()
        );
    }
}