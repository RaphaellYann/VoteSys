package com.senac.votesys.application.dto.votos;

import java.util.List;

public record VotosRequestDTO(
        Long campanhaId,
        List<Long> opcoesIds
) {}
