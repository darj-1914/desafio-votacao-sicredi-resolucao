package br.com.votacao.controller;

import br.com.votacao.client.CpfClient;
import br.com.votacao.controller.v1.VotoControllerV1;
import br.com.votacao.model.Pauta;
import br.com.votacao.model.Voto;
import br.com.votacao.service.PautaService;
import br.com.votacao.service.VotoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VotoControllerV1.class)
class VotoControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotoService votoService;

    @MockBean
    private PautaService pautaService;

    @MockBean
    private CpfClient cpfClient;

    @Test
    void deveRegistrarVoto() throws Exception {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        Voto voto = new Voto();
        voto.setAssociadoId(123L);
        voto.setOpcao(Voto.OpcaoVoto.SIM);

        when(pautaService.buscarPorId(1L)).thenReturn(pauta);
        when(votoService.votar(123L, pauta, Voto.OpcaoVoto.SIM)).thenReturn(voto);
        when(cpfClient.verificarPermissaoVoto(anyString())).thenReturn("ABLE_TO_VOTE");

        String json = """
        {
            "associadoId": "123",
            "pautaId": "1",
            "opcao": "SIM",
            "cpf": "00000000000"
        }
    """;

        mockMvc.perform(post("/api/v1/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarBadRequestParaParametrosInvalidos() throws Exception {
        mockMvc.perform(post("/api/v1/votos")
                        .param("associadoId", "")
                        .param("pautaId", "1")
                        .param("opcao", "SIM"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarNotFoundSePautaNaoExistir() throws Exception {
        when(pautaService.buscarPorId(1L)).thenThrow(new RuntimeException("Pauta não encontrada"));

        String json = """
                    {
                        "associadoId": "123",
                        "pautaId": "1",
                        "opcao": "SIM",
                        "cpf": "00000000000"
                    }
                """;

        mockMvc.perform(post("/api/v1/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }
}