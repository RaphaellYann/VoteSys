package com.senac.votesys.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "opcaoVoto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpcaoVoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Integer totalVotos = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campanhas_id")
    private Campanhas campanha;
}