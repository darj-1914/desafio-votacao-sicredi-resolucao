package br.com.votacao.performance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VotacaoPerformanceTest {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void testCargaAltaDeVotos() throws InterruptedException {
        String baseUrl = "http://localhost:" + port;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> pauta = Map.of("descricao", "Teste de Performance");
        HttpEntity<Map<String, Object>> pautaEntity = new HttpEntity<>(pauta, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(baseUrl + "/api/v1/pautas", pautaEntity, Map.class);

        if (response.getBody() == null || response.getBody().get("id") == null) {
            throw new IllegalStateException("Falha ao criar pauta para o teste.");
        }

        Long pautaId = Long.valueOf(response.getBody().get("id").toString());

        long start = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 100_000; i++) {
            final int associadoId = i;
            executor.submit(() -> {
                String votoJson = String.format("""
                        {
                          "associadoId": %d,
                          "pautaId": %d,
                          "opcao": "%s"
                        }
                        """, associadoId, pautaId, associadoId % 2 == 0 ? "SIM" : "NAO");

                HttpEntity<String> votoEntity = new HttpEntity<>(votoJson, headers);
                try {
                    restTemplate.postForEntity(baseUrl + "/votos", votoEntity, String.class);
                } catch (Exception e) {
                    System.err.println("Erro ao enviar voto para associadoId " + associadoId + ": " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        boolean terminou = executor.awaitTermination(30, TimeUnit.MINUTES);

        long fim = System.currentTimeMillis();
        long duracao = fim - start;

        if (terminou) {
            System.out.println("Teste de carga finalizado com sucesso.");
            System.out.println("Tempo total: " + duracao + " ms");
        } else {
            throw new IllegalStateException("Threads não finalizaram no tempo esperado.");
        }
    }
}