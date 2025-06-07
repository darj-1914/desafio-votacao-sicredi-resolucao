package br.com.votacao.repository;

import br.com.votacao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    Optional<Voto> findByAssociadoIdAndPautaId(Long associadoId, Long pautaId);
    long countByPautaIdAndOpcao(Long pautaId, Voto.OpcaoVoto opcao);
}
