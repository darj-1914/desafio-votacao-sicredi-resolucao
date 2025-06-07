package br.com.votacao.service;

import br.com.votacao.model.Pauta;
import br.com.votacao.model.SessaoVotacao;
import br.com.votacao.model.Voto;
import br.com.votacao.repository.VotoRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final SessaoVotacaoService sessaoVotacaoService;

    public VotoService(VotoRepository votoRepository, SessaoVotacaoService sessaoVotacaoService) {
        this.votoRepository = votoRepository;
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    public Voto votar(Long associadoId, Pauta pauta, Voto.OpcaoVoto opcao) {

        Optional<SessaoVotacao> sessaoOpt = sessaoVotacaoService.buscarPorPauta(pauta.getId());

        if (sessaoOpt.isEmpty()) {
            throw new IllegalStateException("Sessão de votação não aberta para esta pauta.");
        }

        SessaoVotacao sessao = sessaoOpt.get();

        Instant agora = Instant.now();
        if (!sessao.isAberta() || agora.isAfter(sessao.getFim())) {
            sessaoVotacaoService.fecharSessao(sessao);
            throw new IllegalStateException("Sessão de votação encerrada.");
        }

        Optional<Voto> votoExistente = votoRepository.findByAssociadoIdAndPautaId(associadoId, pauta.getId());
        if (votoExistente.isPresent()) {
            throw new IllegalStateException("Associado já votou nesta pauta.");
        }

        Voto voto = new Voto();
        voto.setAssociadoId(associadoId);
        voto.setPauta(pauta);
        voto.setOpcao(opcao);

        return votoRepository.save(voto);
    }

    public long contarVotosSim(Long pautaId) {
        return votoRepository.countByPautaIdAndOpcao(pautaId, Voto.OpcaoVoto.SIM);
    }

    public long contarVotosNao(Long pautaId) {
        return votoRepository.countByPautaIdAndOpcao(pautaId, Voto.OpcaoVoto.NAO);
    }
}
