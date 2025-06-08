package br.com.votacao.service;

import br.com.votacao.model.Pauta;
import br.com.votacao.model.SessaoVotacao;
import br.com.votacao.model.Voto;
import br.com.votacao.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    VotoRepository votoRepository;

    @Mock
    SessaoVotacaoService sessaoVotacaoService;

    @InjectMocks
    VotoService votoService;

    Pauta pauta = new Pauta();

    @BeforeEach
    void setup() {
        pauta.setId(1L);
    }

    @Test
    void votar_deveSalvarVotoQuandoSessaoAbertaENaoVotado() {
        SessaoVotacao sessao = new SessaoVotacao();
        sessao.setAberta(true);
        sessao.setFim(Instant.now().plusSeconds(60));
        when(sessaoVotacaoService.buscarPorPauta(1L)).thenReturn(Optional.of(sessao));
        when(votoRepository.findByAssociadoIdAndPautaId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(votoRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        Voto voto = votoService.votar(1L, pauta, Voto.OpcaoVoto.SIM);
        assertEquals(1L, voto.getAssociadoId());
        assertEquals(pauta, voto.getPauta());
        assertEquals(Voto.OpcaoVoto.SIM, voto.getOpcao());
    }

    @Test
    void votar_deveLancarQuandoSessaoNaoAberta() {
        SessaoVotacao sessao = new SessaoVotacao();
        sessao.setAberta(false);
        sessao.setFim(Instant.now().minusSeconds(10));
        when(sessaoVotacaoService.buscarPorPauta(1L)).thenReturn(Optional.of(sessao));
        assertThrows(IllegalStateException.class, () -> votoService.votar(1L, pauta, Voto.OpcaoVoto.SIM));
        verify(sessaoVotacaoService).fecharSessao(sessao);
    }

    @Test
    void votar_deveLancarQuandoSessaoNaoExiste() {
        when(sessaoVotacaoService.buscarPorPauta(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> votoService.votar(1L, pauta, Voto.OpcaoVoto.SIM));
    }

    @Test
    void votar_deveLancarQuandoAssociadoJaVotou() {
        SessaoVotacao sessao = new SessaoVotacao();
        sessao.setAberta(true);
        sessao.setFim(Instant.now().plusSeconds(60));
        when(sessaoVotacaoService.buscarPorPauta(1L)).thenReturn(Optional.of(sessao));
        when(votoRepository.findByAssociadoIdAndPautaId(anyLong(), anyLong())).thenReturn(Optional.of(new Voto()));
        assertThrows(IllegalStateException.class, () -> votoService.votar(1L, pauta, Voto.OpcaoVoto.SIM));
    }

    @Test
    void contarVotosSim_deveRetornarContagem() {
        when(votoRepository.countByPautaIdAndOpcao(1L, Voto.OpcaoVoto.SIM)).thenReturn(5L);
        assertEquals(5L, votoService.contarVotosSim(1L));
    }

    @Test
    void contarVotosNao_deveRetornarContagem() {
        when(votoRepository.countByPautaIdAndOpcao(1L, Voto.OpcaoVoto.NAO)).thenReturn(3L);
        assertEquals(3L, votoService.contarVotosNao(1L));
    }
}

