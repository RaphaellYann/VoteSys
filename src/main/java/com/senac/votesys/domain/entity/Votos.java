package com.senac.votesys.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString; // <--- IMPORTANTE

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "votos")
public class Votos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @ToString.Exclude
    private Usuarios usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campanha_id", nullable = false)
    @ToString.Exclude
    private Campanhas campanha;

    @ManyToMany
    @JoinTable(
            name = "votos_opcoes",
            joinColumns = @JoinColumn(name = "voto_id"),
            inverseJoinColumns = @JoinColumn(name = "opcao_id")
    )
    @ToString.Exclude
    private List<OpcaoVoto> opcoes;

    private LocalDateTime dataVoto = LocalDateTime.now();
}