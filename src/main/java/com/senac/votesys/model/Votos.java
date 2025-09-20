package com.senac.votesys.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="votos")
public class Votos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "votos_id")
    private Campanhas campanha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opcaoVoto_id")
    private OpcaoVoto opcaoVoto;

    private LocalDateTime dataHora;

}
