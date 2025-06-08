package br.com.votacao.controller.v2;

import br.com.votacao.client.CpfClient;
import br.com.votacao.exception.CpfNotFoundException;
import br.com.votacao.model.Pauta;
import br.com.votacao.model.Voto;
import br.com.votacao.service.PautaService;
import br.com.votacao.service.VotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v2/votos")
public class VotoControllerV2 {

    private final VotoService votoService;
    private final PautaService pautaService;
    private final CpfClient cpfClient;

    public VotoControllerV2(VotoService votoService, PautaService pautaService, CpfClient cpfClient) {
        this.votoService = votoService;
        this.pautaService = pautaService;
        this.cpfClient = cpfClient;
    }

    @PostMapping
    public ResponseEntity<?> votar(@RequestBody Map<String, String> payload) {
        try {
            String cpf = payload.get("cpf");
            String status = cpfClient.verificarPermissaoVoto(cpf);
            if ("UNABLE_TO_VOTE".equals(status)) {
                return ResponseEntity.status(404).body(Map.of("status", status));
            }

            Long associadoId = Long.parseLong(payload.get("associadoId"));
            Long pautaId = Long.parseLong(payload.get("pautaId"));
            String opcaoStr = payload.get("opcao");

            Pauta pauta = pautaService.buscarPorId(pautaId);
            if (pauta == null) return ResponseEntity.notFound().build();

            Voto.OpcaoVoto opcao = Voto.OpcaoVoto.valueOf(opcaoStr.toUpperCase());

            Voto voto = votoService.votar(associadoId, pauta, opcao);

            return ResponseEntity.ok(voto);
        } catch (CpfNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("erro", "CPF inválido"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Requisição inválida"));
        }
    }

    @GetMapping("/resultado/{pautaId}")
    public ResponseEntity<?> resultado(@PathVariable Long pautaId) {
        long sim = votoService.contarVotosSim(pautaId);
        long nao = votoService.contarVotosNao(pautaId);

        return ResponseEntity.ok(Map.of(
                "pautaId", pautaId,
                "sim", sim,
                "nao", nao,
                "resultado", sim > nao ? "APROVADO" : sim < nao ? "REJEITADO" : "EMPATE"
        ));
    }
}
