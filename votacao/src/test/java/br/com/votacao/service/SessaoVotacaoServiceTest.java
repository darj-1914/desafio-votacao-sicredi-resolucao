package br.com.votacao.service;

import br.com.votacao.model.Pauta;
import br.com.votacao.model.SessaoVotacao;
import br.com.votacao.repository.SessaoVotacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessaoVotacaoServiceTest {

    @Mock
    SessaoVotacaoRepository sessaoRepository;

    @InjectMocks
    SessaoVotacaoService sessaoService;

    @Test
    void abrirSessao_deveSalvarSessaoComDuracaoInformada() {
        Pauta pauta = new Pauta();
        when(sessaoRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        SessaoVotacao sessao = sessaoService.abrirSessao(pauta, 10L);
        assertEquals(pauta, sessao.getPauta());
        assertTrue(sessao.isAberta());
        assertNotNull(sessao.getInicio());
        assertNotNull(sessao.getFim());
        assertTrue(sessao.getFim().isAfter(sessao.getInicio()));
    }

    @Test
    void abrirSessao_deveSalvarSessaoComDuracaoPadrao() {
        Pauta pauta = new Pauta();
        when(sessaoRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        SessaoVotacao sessao = sessaoService.abrirSessao(pauta, null);
        assertEquals(pauta, sessao.getPauta());
        assertTrue(sessao.isAberta());
        assertNotNull(sessao.getInicio());
        assertNotNull(sessao.getFim());
        assertTrue(sessao.getFim().isAfter(sessao.getInicio()));
    }

    @Test
    void buscarPorPauta_deveRetornarSessaoQuandoExistir() {
        SessaoVotacao sessao = new SessaoVotacao();
        when(sessaoRepository.findByPautaId(1L)).thenReturn(Optional.of(sessao));
        assertEquals(Optional.of(sessao), sessaoService.buscarPorPauta(1L));
    }

    @Test
    void buscarPorPauta_deveRetornarVazioQuandoNaoExistir() {
        when(sessaoRepository.findByPautaId(1L)).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), sessaoService.buscarPorPauta(1L));
    }

    @Test
    void fecharSessao_deveAlterarStatusEAguardar() {
        SessaoVotacao sessao = new SessaoVotacao();
        sessaoService.fecharSessao(sessao);
        assertFalse(sessao.isAberta());
        verify(sessaoRepository).save(sessao);
    }
}

