package com.senac.votesys.application.dto.opcaoVoto;

public record OpcaoVotoRequestDTO(
        String nome,
        Long campanhaId
) {}
