package com.senac.votesys.domain.entity;

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

    private boolean votacaoAnonima;

    public String getTituloUpper(){
        return this.titulo.toUpperCase();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_votacao", nullable = false)
    private TipoCampanha tipoCampanha;

    @OneToMany(mappedBy = "campanha", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpcaoVoto> opcoesDeVoto = new ArrayList<>();
}
