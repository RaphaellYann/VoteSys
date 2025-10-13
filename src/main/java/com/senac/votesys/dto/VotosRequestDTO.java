package com.senac.votesys.dto;

import java.util.List;

public record VotosRequestDTO(
        Long campanhaId,
        List<Long> opcoesIds
) {}
