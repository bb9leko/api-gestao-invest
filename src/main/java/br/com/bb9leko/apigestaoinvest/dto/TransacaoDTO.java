package br.com.bb9leko.apigestaoinvest.dto;

import br.com.bb9leko.apigestaoinvest.model.Transacao;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransacaoDTO {

    @JsonProperty("dataEvento")
    @JsonbDateFormat("dd/MM/yyyy")
    private LocalDate dataEvento;

    @JsonProperty("corretora")
    private String corretora;

    @JsonProperty("classificacaoAtivo")
    private String classificacaoAtivo;

    @JsonProperty("ticket")
    private String ticket;

    @JsonProperty("compraOUVenda")
    private String compraOUVenda;

    @JsonProperty("quantidade")
    private int quantidade;

    @JsonProperty("valorUnitario")
    private BigDecimal valorUnitario;

    @JsonProperty("valorTotal")
    private BigDecimal valorTotal;

    @JsonProperty("valorTaxaLiquidacao")
    private BigDecimal valorTaxaLiquidacao;

    @JsonProperty("valorTaxasEmolumentos")
    private BigDecimal valorTaxasEmolumentos;

    @JsonProperty("valorImpostos")
    private BigDecimal valorImpostos;

    @JsonProperty("outrosValoreesCobrados")
    private BigDecimal outrosValoresCobrados;

    @JsonProperty("valorCorretagem")
    private BigDecimal valorCorretagem;

    @JsonProperty("valorTotalComCustosEDespesas")
    private BigDecimal valorTotalComCustosEDespesas;

    public TransacaoDTO(Transacao transacao) {
        this.dataEvento = transacao.getDataEvento();
        this.corretora = transacao.getCorretora();
        this.classificacaoAtivo = transacao.getClassificacaoAtivo().name();
        this.ticket = transacao.getTicket();
        this.compraOUVenda = transacao.getCompraOUVenda().name();
        this.quantidade = transacao.getQuantidade();
        this.valorUnitario = transacao.getValorUnitario();
        this.valorTotal = transacao.getValorTotal();
        this.valorTaxaLiquidacao = transacao.getValorTaxaLiquidacao();
        this.valorTaxasEmolumentos = transacao.getValorTaxasEmolumentos();
        this.valorImpostos = transacao.getValorImpostos();
        this.outrosValoresCobrados = transacao.getOutrosValoresCobrados();
        this.valorCorretagem = transacao.getValorCorretagem();
        this.valorTotalComCustosEDespesas = transacao.getValorTotalComCustosEDespesas();

    }

    public TransacaoDTO() {
        // Default constructor
    }
    public LocalDate getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(LocalDate dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getCorretora() {
        return corretora;
    }

    public void setCorretora(String corretora) {
        this.corretora = corretora;
    }

    public String getClassificacaoAtivo() {
        return classificacaoAtivo;
    }

    public void setClassificacaoAtivo(String classificacaoAtivo) {
        this.classificacaoAtivo = classificacaoAtivo;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getCompraOUVenda() {
        return compraOUVenda;
    }

    public void setCompraOUVenda(String compraOUVenda) {
        this.compraOUVenda = compraOUVenda;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorTaxaLiquidacao() {
        return valorTaxaLiquidacao;
    }

    public void setValorTaxaLiquidacao(BigDecimal valorTaxaLiquidacao) {
        this.valorTaxaLiquidacao = valorTaxaLiquidacao;
    }

    public BigDecimal getValorTaxasEmolumentos() {
        return valorTaxasEmolumentos;
    }

    public void setValorTaxasEmolumentos(BigDecimal valorTaxasEmolumentos) {
        this.valorTaxasEmolumentos = valorTaxasEmolumentos;
    }

    public BigDecimal getValorImpostos() {
        return valorImpostos;
    }

    public void setValorImpostos(BigDecimal valorImpostos) {
        this.valorImpostos = valorImpostos;
    }

    public BigDecimal getOutrosValoresCobrados() {
        return outrosValoresCobrados;
    }

    public void setOutrosValoresCobrados(BigDecimal outrosValoresCobrados) {
        this.outrosValoresCobrados = outrosValoresCobrados;
    }

    public BigDecimal getValorCorretagem() {
        return valorCorretagem;
    }

    public void setValorCorretagem(BigDecimal valorCorretagem) {
        this.valorCorretagem = valorCorretagem;
    }

    public BigDecimal getValorTotalComCustosEDespesas() {
        return valorTotalComCustosEDespesas;
    }

    public void setValorTotalComCustosEDespesas(BigDecimal valorTotalComCustosEDespesas) {
        this.valorTotalComCustosEDespesas = valorTotalComCustosEDespesas;
    }
}
