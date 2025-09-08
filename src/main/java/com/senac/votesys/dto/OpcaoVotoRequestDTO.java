package com.senac.votesys.dto;

import com.senac.votesys.model.Campanhas;

public record OpcaoVotoRequestDTO(String descricao,
                                  String totalVotos) {
}
