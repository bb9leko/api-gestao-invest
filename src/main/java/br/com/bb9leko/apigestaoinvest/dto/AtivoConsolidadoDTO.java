package br.com.bb9leko.apigestaoinvest.dto;

import java.math.BigDecimal;

public class AtivoConsolidadoDTO {
    public String corretora;
    public String classificacaoAtivo;
    public String ticket;
    public int quantidadeTotal;
    public BigDecimal valorMedio;
    public BigDecimal valorTotalCompras;
    public BigDecimal valorTotalTaxaLiquidacao;
    public BigDecimal valorTotalTaxasEmolumentos;
    public BigDecimal valorTotalImpostos;
    public BigDecimal valorTotalCorretagem;
    public BigDecimal valorTotalOutros;
}