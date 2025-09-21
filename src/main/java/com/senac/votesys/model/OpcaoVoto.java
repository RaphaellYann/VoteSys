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

    // Inicializado com 0 para garantir que toda nova opção comece sem votos.
    private Integer totalVotos = 0;

    // FetchType.LAZY é a melhor prática. Evita carregar dados desnecessários.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campanhas_id")
    private Campanhas campanha;
}