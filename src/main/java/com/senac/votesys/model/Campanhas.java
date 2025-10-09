package com.senac.votesys.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "campanhas")
public class Campanhas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descricao;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    private boolean ativo;

    @OneToMany(mappedBy = "campanha", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpcaoVoto> opcoesDeVoto = new ArrayList<>();
}

