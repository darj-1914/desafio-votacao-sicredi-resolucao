package br.com.votacao.controller;

import br.com.votacao.model.Pauta;
import br.com.votacao.service.PautaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pautas")
public class PautaController {

    private final PautaService pautaService;

    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @PostMapping
    public ResponseEntity<Pauta> criar(@RequestBody Pauta pauta) {
        return ResponseEntity.ok(pautaService.salvar(pauta));
    }

    @GetMapping
    public ResponseEntity<List<Pauta>> listar() {
        return ResponseEntity.ok(pautaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pauta> buscar(@PathVariable Long id) {
        Pauta pauta = pautaService.buscarPorId(id);
        if (pauta == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pauta);
    }
}
