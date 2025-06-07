package br.com.votacao.controller;

import br.com.votacao.model.Pauta;
import br.com.votacao.model.SessaoVotacao;
import br.com.votacao.service.PautaService;
import br.com.votacao.service.SessaoVotacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessoes")
public class SessaoVotacaoController {

    private final SessaoVotacaoService sessaoService;
    private final PautaService pautaService;

    public SessaoVotacaoController(SessaoVotacaoService sessaoService, PautaService pautaService) {
        this.sessaoService = sessaoService;
        this.pautaService = pautaService;
    }

    @PostMapping("/abrir/{pautaId}")
    public ResponseEntity<SessaoVotacao> abrirSessao(
            @PathVariable Long pautaId,
            @RequestParam(required = false) Long duracaoMinutos) {

        Pauta pauta = pautaService.buscarPorId(pautaId);
        if (pauta == null) return ResponseEntity.notFound().build();

        SessaoVotacao sessao = sessaoService.abrirSessao(pauta, duracaoMinutos);
        return ResponseEntity.ok(sessao);
    }
}
