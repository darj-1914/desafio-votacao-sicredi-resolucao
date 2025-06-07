package br.com.votacao.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class SessaoVotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pauta_id", unique = true)
    private Pauta pauta;

    private Instant inicio;

    private Instant fim;

    private boolean aberta;
}