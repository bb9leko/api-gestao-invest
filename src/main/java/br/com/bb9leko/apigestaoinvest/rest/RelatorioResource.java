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
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Path("/ativos")
@Produces(MediaType.APPLICATION_JSON)
public class RelatorioResource {

    @Inject
    TransacaoRepository transacaoRepository;

    /**
     * Retorna uma lista de ativos consolidados onde cada DTO já contém
     * o campo `classificacaoAtivo` (por exemplo: ACOES, ETFS, etc.).
     */
    @GET
    @Path("/consolidados")
    public List<AtivoConsolidadoDTO> listarAtivosAgrupados() {
        // Agrupa por classificacao|corretora|ticket para consolidar posições por ativo
        Map<String, List<Transacao>> agrupados = transacaoRepository.listAll().stream()
                .collect(Collectors.groupingBy(this::agrupamentoChave));

        List<AtivoConsolidadoDTO> ativos = agrupados.values().stream()
                .map(this::consolidarAtivo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Ordena por valor total (decrescente)
        ativos.sort(Comparator.comparing(a -> a.valorMedio.multiply(BigDecimal.valueOf(a.quantidadeTotal)), Comparator.reverseOrder()));

        return ativos;
    }

    private String agrupamentoChave(Transacao t) {
        // chave: classificacao|corretora|ticket
        String classificacao = t.getClassificacaoAtivo() != null ? t.getClassificacaoAtivo().name() : "";
        return classificacao + "|" + Objects.toString(t.getCorretora(), "") + "|" + Objects.toString(t.getTicket(), "");
    }

    private AtivoConsolidadoDTO consolidarAtivo(List<Transacao> transacoes) {
        // Ordena por data para consistência (data antiga primeiro)
        transacoes.sort(Comparator.comparing(Transacao::getDataEvento));

        int saldo = 0;
        int totalCompras = 0;
        BigDecimal somaValorUnitario = BigDecimal.ZERO;
        BigDecimal valorTotalCompras = BigDecimal.ZERO;

        for (Transacao t : transacoes) {
            String tipo = t.getCompraOUVenda() != null ? t.getCompraOUVenda().name() : "";
            if ("COMPRA".equals(tipo)) {
                saldo += t.getQuantidade();
                somaValorUnitario = somaValorUnitario.add(t.getValorUnitario().multiply(BigDecimal.valueOf(t.getQuantidade())));
                totalCompras += t.getQuantidade();
                valorTotalCompras = valorTotalCompras
                        .add(t.getValorUnitario().multiply(BigDecimal.valueOf(t.getQuantidade())))
                        .add(nullToZero(t.getValorTaxaLiquidacao()))
                        .add(nullToZero(t.getValorTaxasEmolumentos()))
                        .add(nullToZero(t.getValorImpostos()))
                        .add(nullToZero(t.getValorCorretagem()))
                        .add(nullToZero(t.getOutrosValoresCobrados()));
            } else if ("VENDA".equals(tipo)) {
                saldo -= t.getQuantidade();
            }
        }

        if (saldo <= 0) return null;

        Transacao primeiro = transacoes.get(0);
        AtivoConsolidadoDTO dto = new AtivoConsolidadoDTO();
        dto.corretora = primeiro.getCorretora();
        dto.classificacaoAtivo = primeiro.getClassificacaoAtivo() != null ? primeiro.getClassificacaoAtivo().name() : null;
        dto.ticket = primeiro.getTicket();
        dto.quantidadeTotal = saldo;
        dto.valorMedio = totalCompras > 0
                ? somaValorUnitario.divide(BigDecimal.valueOf(totalCompras), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        dto.valorTotalCompras = valorTotalCompras;
        return dto;
    }

    private BigDecimal nullToZero(BigDecimal b) {
        return b == null ? BigDecimal.ZERO : b;
    }
}
