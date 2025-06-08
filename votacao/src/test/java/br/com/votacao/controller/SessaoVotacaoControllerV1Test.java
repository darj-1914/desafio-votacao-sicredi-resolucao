package br.com.votacao.controller;

import br.com.votacao.controller.v1.SessaoVotacaoControllerV1;
import br.com.votacao.model.Pauta;
import br.com.votacao.model.SessaoVotacao;
import br.com.votacao.service.PautaService;
import br.com.votacao.service.SessaoVotacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessaoVotacaoControllerV1.class)
class SessaoVotacaoControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessaoVotacaoService sessaoVotacaoService;

    @MockBean
    private PautaService pautaService;

    @Test
    void deveAbrirSessao() throws Exception {
        Pauta pauta = new Pauta();
        pauta.setId(1L);
        SessaoVotacao sessao = new SessaoVotacao();
        when(pautaService.buscarPorId(1L)).thenReturn(pauta);
        when(sessaoVotacaoService.abrirSessao(pauta, 10L)).thenReturn(sessao);

        mockMvc.perform(post("/api/v1/sessoes/abrir/1?duracaoMinutos=10"))
                .andExpect(status().isOk());
    }
}

