package br.com.votacao.controller;

import br.com.votacao.controller.v1.PautaControllerV1;
import br.com.votacao.model.Pauta;
import br.com.votacao.service.PautaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PautaControllerV1.class)
class PautaControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PautaService pautaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarPauta() throws Exception {
        Pauta pauta = new Pauta();
        pauta.setTitulo("Teste");

        when(pautaService.salvar(pauta)).thenReturn(pauta);

        mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pauta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Teste"));
    }

    @Test
    void deveListarPautas() throws Exception {
        Pauta pauta = new Pauta();
        pauta.setTitulo("Teste");

        when(pautaService.listar()).thenReturn(List.of(pauta));

        mockMvc.perform(get("/api/v1/pautas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Teste"));
    }
}