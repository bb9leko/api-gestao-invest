package br.com.bb9leko.apigestaoinvest.rest;

import br.com.bb9leko.apigestaoinvest.dto.Evento;
import br.com.bb9leko.apigestaoinvest.dto.ClassificacaoAtivo;
import br.com.bb9leko.apigestaoinvest.dto.TransacaoDTO;
import br.com.bb9leko.apigestaoinvest.model.Transacao;
import br.com.bb9leko.apigestaoinvest.repository.TransacaoRepository;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoResourceUnitTest {

    @Mock
    TransacaoRepository mockRepository;

    @InjectMocks
    TransacaoResource resource;

    @SuppressWarnings("unchecked")
    @Test
    void listarTransacoes_deveRetornarListaDTO() {
        Transacao t = new Transacao();
        t.setTicket("TICK1");
        t.setClassificacaoAtivo(ClassificacaoAtivo.values()[0]);
        t.setCompraOUVenda(Evento.values()[0]);
        t.setValorUnitario(new BigDecimal("10"));
        t.setQuantidade(BigDecimal.valueOf(2));

        doReturn(Collections.singletonList(t)).when(mockRepository).listAll();

        var result = resource.listarTransacoes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TICK1", result.get(0).getTicket());
        verify(mockRepository, times(1)).listAll();
    }

    @SuppressWarnings("unchecked")
    @Test
    void buscarPorTicket_deveRetornarListaFiltrada() {
        String ticket = "ABC";
        Transacao t = new Transacao();
        t.setTicket(ticket);
        t.setClassificacaoAtivo(ClassificacaoAtivo.values()[0]);
        t.setCompraOUVenda(Evento.values()[0]);
        t.setValorUnitario(new BigDecimal("5"));
        t.setQuantidade(BigDecimal.valueOf(3));

        // stub exato da assinatura usada pela implementação: list("ticket", ticket)
        when(mockRepository.list("ticket", ticket)).thenReturn(Collections.singletonList(t));

        var result = resource.buscarPorTicket(ticket);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ticket, result.get(0).getTicket());

        // garantir que a lista foi consultada
        verify(mockRepository, times(1)).list("ticket", ticket);
    }

    @Test
    void buscarTransacaoPorId_quandoExistir_deveRetornar200ComDTO() {
        Long id = 10L;
        Transacao t = new Transacao();
        t.setTicket("XYZ");
        t.setClassificacaoAtivo(ClassificacaoAtivo.values()[0]);
        t.setCompraOUVenda(Evento.values()[0]);
        t.setValorUnitario(new BigDecimal("7.5"));
        t.setQuantidade(BigDecimal.valueOf(4));
        when(mockRepository.findById(id)).thenReturn(t);

        Response resp = resource.buscarTransacaoPorId(id);

        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        Object entity = resp.getEntity();
        assertTrue(entity instanceof TransacaoDTO);
        TransacaoDTO dto = (TransacaoDTO) entity;
        assertEquals("XYZ", dto.getTicket());
        verify(mockRepository, times(1)).findById(id);
    }

    @Test
    void buscarTransacaoPorId_quandoNaoExistir_deveRetornar404() {
        Long id = 99L;
        when(mockRepository.findById(id)).thenReturn(null);

        Response resp = resource.buscarTransacaoPorId(id);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        verify(mockRepository, times(1)).findById(id);
    }

    @Test
    void excluirTransacao_quandoExistir_deveDeletarERetornar204() {
        Long id = 5L;
        Transacao t = new Transacao();
        t.setClassificacaoAtivo(ClassificacaoAtivo.values()[0]);
        t.setCompraOUVenda(Evento.values()[0]);
        t.setValorUnitario(new BigDecimal("1"));
        t.setQuantidade(BigDecimal.valueOf(1));
        when(mockRepository.findById(id)).thenReturn(t);

        Response resp = resource.excluirTransacao(id);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
        verify(mockRepository, times(1)).findById(id);
        verify(mockRepository, times(1)).delete(t);
    }

    @Test
    void excluirTransacao_quandoNaoExistir_deveRetornar404() {
        Long id = 6L;
        when(mockRepository.findById(id)).thenReturn(null);

        Response resp = resource.excluirTransacao(id);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        verify(mockRepository, times(1)).findById(id);
        verify(mockRepository, never()).delete(any());
    }
}
