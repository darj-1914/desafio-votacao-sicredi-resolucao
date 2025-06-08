package br.com.votacao.service;

import br.com.votacao.model.Pauta;
import br.com.votacao.repository.PautaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    PautaRepository pautaRepository;

    @InjectMocks
    PautaService pautaService;

    @Test
    void salvar_deveRetornarPautaSalva() {
        Pauta pauta = new Pauta();
        when(pautaRepository.save(pauta)).thenReturn(pauta);
        assertEquals(pauta, pautaService.salvar(pauta));
    }

    @Test
    void listar_deveRetornarListaDePautas() {
        List<Pauta> pautas = List.of(new Pauta());
        when(pautaRepository.findAll()).thenReturn(pautas);
        assertEquals(pautas, pautaService.listar());
    }

    @Test
    void buscarPorId_deveRetornarPautaQuandoExistir() {
        Pauta pauta = new Pauta();
        when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
        assertEquals(pauta, pautaService.buscarPorId(1L));
    }

    @Test
    void buscarPorId_deveRetornarNullQuandoNaoExistir() {
        when(pautaRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(pautaService.buscarPorId(1L));
    }
}
