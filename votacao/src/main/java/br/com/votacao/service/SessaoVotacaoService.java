package br.com.votacao.service;

import br.com.votacao.model.Pauta;
import br.com.votacao.model.SessaoVotacao;
import br.com.votacao.repository.SessaoVotacaoRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class SessaoVotacaoService {

    private final SessaoVotacaoRepository sessaoRepository;

    public SessaoVotacaoService(SessaoVotacaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    public SessaoVotacao abrirSessao(Pauta pauta, Long duracaoMinutos) {
        Instant agora = Instant.now();
        Instant fim = (duracaoMinutos != null) ? agora.plus(duracaoMinutos, ChronoUnit.MINUTES) : agora.plus(1, ChronoUnit.MINUTES);

        SessaoVotacao sessao = new SessaoVotacao();
        sessao.setPauta(pauta);
        sessao.setInicio(agora);
        sessao.setFim(fim);
        sessao.setAberta(true);

        return sessaoRepository.save(sessao);
    }

    public Optional<SessaoVotacao> buscarPorPauta(Long pautaId) {
        return sessaoRepository.findByPautaId(pautaId);
    }

    public void fecharSessao(SessaoVotacao sessao) {
        sessao.setAberta(false);
        sessaoRepository.save(sessao);
    }
}
