package br.com.votacao.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long associadoId;

    @ManyToOne
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;

    @Enumerated(EnumType.STRING)
    private OpcaoVoto opcao;

    public enum OpcaoVoto {
        SIM,
        NAO
    }
}