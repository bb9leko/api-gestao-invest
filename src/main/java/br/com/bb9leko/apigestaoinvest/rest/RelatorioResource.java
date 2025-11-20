package br.com.bb9leko.apigestaoinvest.rest;

import br.com.bb9leko.apigestaoinvest.dto.AtivoConsolidadoDTO;
import br.com.bb9leko.apigestaoinvest.model.Transacao;
import br.com.bb9leko.apigestaoinvest.repository.TransacaoRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Path("/ativos/consolidados")
@Produces(MediaType.APPLICATION_JSON)
public class RelatorioResource {

    @Inject
    TransacaoRepository transacaoRepository;

    @GET
    @Path("/agrupados")
    public Map<String, List<AtivoConsolidadoDTO>> listarAtivosAgrupados() {
        Map<String, List<Transacao>> agrupados = transacaoRepository.listAll().stream()
                .collect(Collectors.groupingBy(this::agrupamentoChave));

        List<AtivoConsolidadoComClassificacao> ativos = agrupados.values().stream()
                .map(this::consolidarAtivoComClassificacao)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<String, List<AtivoConsolidadoDTO>> resultado = ativos.stream()
                .collect(Collectors.groupingBy(
                        ativo -> ativo.classificacaoAtivo,
                        Collectors.mapping(ativo -> ativo.dto, Collectors.toList())
                ));

        resultado.values().forEach(lista ->
                lista.sort(Comparator.comparing(
                        dto -> dto.valorMedio.multiply(BigDecimal.valueOf(dto.quantidadeTotal)),
                        Comparator.reverseOrder()
                ))
        );

        return resultado;
    }

    private String agrupamentoChave(Transacao t) {
        return t.getCorretora() + "|" + t.getClassificacaoAtivo() + "|" + t.getTicket();
    }

    private static class AtivoConsolidadoComClassificacao {
        String classificacaoAtivo;
        AtivoConsolidadoDTO dto;
        AtivoConsolidadoComClassificacao(String classificacaoAtivo, AtivoConsolidadoDTO dto) {
            this.classificacaoAtivo = classificacaoAtivo;
            this.dto = dto;
        }
    }

    private AtivoConsolidadoComClassificacao consolidarAtivoComClassificacao(List<Transacao> transacoes) {
        transacoes.sort(Comparator.comparing(Transacao::getDataEvento));
        int saldo = 0, totalCompras = 0;
        BigDecimal somaValorUnitario = BigDecimal.ZERO;
        BigDecimal valorTotalCompras = BigDecimal.ZERO;

        for (Transacao t : transacoes) {
            if ("COMPRA".equals(t.getCompraOUVenda().name())) {
                saldo += t.getQuantidade();
                somaValorUnitario = somaValorUnitario.add(t.getValorUnitario().multiply(BigDecimal.valueOf(t.getQuantidade())));
                totalCompras += t.getQuantidade();
                valorTotalCompras = valorTotalCompras
                        .add(t.getValorUnitario().multiply(BigDecimal.valueOf(t.getQuantidade())))
                        .add(t.getValorTaxaLiquidacao())
                        .add(t.getValorTaxasEmolumentos())
                        .add(t.getValorImpostos())
                        .add(t.getValorCorretagem())
                        .add(t.getOutrosValoresCobrados());
            } else if ("VENDA".equals(t.getCompraOUVenda().name())) {
                saldo -= t.getQuantidade();
            }
        }

        if (saldo <= 0) return null;

        Transacao t = transacoes.get(0);
        AtivoConsolidadoDTO dto = new AtivoConsolidadoDTO();
        dto.corretora = t.getCorretora();
        // dto.classificacaoAtivo removido da resposta
        dto.ticket = t.getTicket();
        dto.quantidadeTotal = saldo;
        dto.valorMedio = totalCompras > 0 ? somaValorUnitario.divide(BigDecimal.valueOf(totalCompras), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
        dto.valorTotalCompras = valorTotalCompras;
        return new AtivoConsolidadoComClassificacao(t.getClassificacaoAtivo().name(), dto);
    }
}
