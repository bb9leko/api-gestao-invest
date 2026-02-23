package br.com.bb9leko.apigestaoinvest.rest;

import br.com.bb9leko.apigestaoinvest.dto.AtivoConsolidadoDTO;
import br.com.bb9leko.apigestaoinvest.model.Transacao;
import br.com.bb9leko.apigestaoinvest.repository.TransacaoRepository;
import br.com.bb9leko.apigestaoinvest.dto.Evento;
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
    @Produces(MediaType.APPLICATION_JSON)
    public List<AtivoConsolidadoDTO> listarAtivosAgrupados() {
        // Agrupa por classificacao|corretora|ticket para consolidar posições por ativo
        Map<String, List<Transacao>> agrupados = transacaoRepository.listAll().stream()
                .collect(Collectors.groupingBy(this::agrupamentoChave));

        List<AtivoConsolidadoDTO> ativos = agrupados.values().stream()
                .map(this::consolidarAtivo)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Ordena por valor total (decrescente)
        ativos.sort(Comparator.comparing(a -> a.valorMedio.multiply(a.quantidadeTotal), Comparator.reverseOrder()));

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

        BigDecimal saldo = BigDecimal.ZERO;
        BigDecimal totalCompras = BigDecimal.ZERO;
        BigDecimal somaValorUnitario = BigDecimal.ZERO;
        BigDecimal valorTotalCompras = BigDecimal.ZERO;

        for (Transacao t : transacoes) {
            Evento evento = t.getCompraOUVenda();
            BigDecimal qtd = t.getQuantidade() != null ? t.getQuantidade() : BigDecimal.ZERO;
            BigDecimal valorUnit = nullToZero(t.getValorUnitario());
            // Se for COMPRA ou BONIFICACAO somamos ao saldo e contabilizamos custos
            if (evento == Evento.COMPRA || evento == Evento.BONIFICACAO || evento == Evento.DESDOBRAMENTO
            || evento == Evento.CONVERSAO) {
                saldo = saldo.add(qtd);
                somaValorUnitario = somaValorUnitario.add(valorUnit.multiply(qtd));
                totalCompras = totalCompras.add(qtd);
                valorTotalCompras = valorTotalCompras
                        .add(valorUnit.multiply(qtd))
                        .add(nullToZero(t.getValorTaxaLiquidacao()))
                        .add(nullToZero(t.getValorTaxasEmolumentos()))
                        .add(nullToZero(t.getValorImpostos()))
                        .add(nullToZero(t.getValorCorretagem()))
                        .add(nullToZero(t.getOutrosValoresCobrados()));
            } else if (evento == Evento.VENDA) {
                saldo = saldo.subtract(qtd);
            }
        }

        if (saldo.compareTo(BigDecimal.ZERO) <= 0) return null;

        Transacao primeiro = transacoes.get(0);
        AtivoConsolidadoDTO dto = new AtivoConsolidadoDTO();
        dto.corretora = primeiro.getCorretora();
        dto.classificacaoAtivo = primeiro.getClassificacaoAtivo() != null ? primeiro.getClassificacaoAtivo().name() : null;
        dto.ticket = primeiro.getTicket();
        dto.quantidadeTotal = saldo;
        dto.valorMedio = totalCompras.compareTo(BigDecimal.ZERO) > 0
                ? somaValorUnitario.divide(totalCompras, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        dto.valorTotalCompras = valorTotalCompras;
        return dto;
    }

    private BigDecimal nullToZero(BigDecimal b) {
        return b == null ? BigDecimal.ZERO : b;
    }
}
