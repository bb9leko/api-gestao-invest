package br.com.bb9leko.investmentmanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.bb9leko.investmentmanagement.dto.ClassificacaoAtivo;
import br.com.bb9leko.investmentmanagement.dto.Evento;
import br.com.bb9leko.investmentmanagement.dto.TransacaoDTO;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "Transacoes")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonbDateFormat("dd/MM/yyyy")
    private LocalDate dataEvento;

    private String corretora;

    @Enumerated(EnumType.STRING)
    @Column(name = "classificacaoativo")
    private ClassificacaoAtivo classificacaoAtivo;

    private String ticket;

    @Enumerated(EnumType.STRING)
    private Evento compraOUVenda;

    @Column(precision = 38, scale = 2)
    private BigDecimal quantidade;

    private BigDecimal valorUnitario;

    @Column(precision = 38, scale = 2)
    private BigDecimal valorTotal;

    @Column(precision = 38, scale = 2)
    private BigDecimal valorTaxaLiquidacao;

    private BigDecimal valorTaxasEmolumentos;

    private BigDecimal valorImpostos;

    private BigDecimal outrosValoresCobrados;

    private BigDecimal valorCorretagem;

    private BigDecimal valorTotalComCustosEDespesas;

    public Transacao(TransacaoDTO dto) {
        aplicar(dto);
    }

    public void aplicar(TransacaoDTO dto) {
        this.dataEvento = dto.getDataEvento();
        this.corretora = dto.getCorretora();
        this.classificacaoAtivo = ClassificacaoAtivo.valueOf(dto.getClassificacaoAtivo());
        this.ticket = dto.getTicket();
        this.compraOUVenda = Evento.valueOf(dto.getCompraOUVenda());
        this.quantidade = dto.getQuantidade();
        this.valorUnitario = dto.getValorUnitario();
        this.valorTaxaLiquidacao = dto.getValorTaxaLiquidacao();
        this.valorTaxasEmolumentos = dto.getValorTaxasEmolumentos();
        this.valorImpostos = dto.getValorImpostos();
        this.outrosValoresCobrados = dto.getOutrosValoresCobrados();
        this.valorCorretagem = dto.getValorCorretagem();
    }

    public Transacao() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public ClassificacaoAtivo getClassificacaoAtivo() {
        return classificacaoAtivo;
    }

    public void setClassificacaoAtivo(ClassificacaoAtivo classificacaoAtivo) {
        this.classificacaoAtivo = classificacaoAtivo;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Evento getCompraOUVenda() {
        return compraOUVenda;
    }

    public void setCompraOUVenda(Evento compraOUVenda) {
        this.compraOUVenda = compraOUVenda;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
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

    @PrePersist
    @PreUpdate
    private void calcularValorTotalComCustosEDespesas() {
        BigDecimal valorUnitarioCalculado = this.valorUnitario != null ? this.valorUnitario : BigDecimal.ZERO;
        BigDecimal quantidadeCalculada = this.quantidade != null ? this.quantidade : BigDecimal.ZERO;
        this.valorTotal = valorUnitarioCalculado.multiply(quantidadeCalculada);

        if (this.compraOUVenda == Evento.VENDA) {
            this.valorTotalComCustosEDespesas = this.valorTotal
                .subtract(this.valorTaxaLiquidacao != null ? this.valorTaxaLiquidacao : BigDecimal.ZERO)
                .subtract(this.valorTaxasEmolumentos != null ? this.valorTaxasEmolumentos : BigDecimal.ZERO)
                .subtract(this.valorImpostos != null ? this.valorImpostos : BigDecimal.ZERO)
                .subtract(this.outrosValoresCobrados != null ? this.outrosValoresCobrados : BigDecimal.ZERO)
                .subtract(this.valorCorretagem != null ? this.valorCorretagem : BigDecimal.ZERO);
        } else {
            this.valorTotalComCustosEDespesas = this.valorTotal
                .add(this.valorTaxaLiquidacao != null ? this.valorTaxaLiquidacao : BigDecimal.ZERO)
                .add(this.valorTaxasEmolumentos != null ? this.valorTaxasEmolumentos : BigDecimal.ZERO)
                .add(this.valorImpostos != null ? this.valorImpostos : BigDecimal.ZERO)
                .add(this.outrosValoresCobrados != null ? this.outrosValoresCobrados : BigDecimal.ZERO)
                .add(this.valorCorretagem != null ? this.valorCorretagem : BigDecimal.ZERO);
        }        
    }

    @Override
    public String toString() {
        return "Transacao{" +
                "id=" + id +
                ", dataEvento=" + dataEvento +
                ", corretora='" + corretora + '\'' +
                ", classificacaoAtivo=" + classificacaoAtivo +
                ", ticket='" + ticket + '\'' +
                ", compraOUVenda=" + compraOUVenda +
                ", quantidade=" + quantidade +
                ", valorUnitario=" + valorUnitario +
                ", valorTotal=" + valorTotal +
                ", valorTaxaLiquidacao=" + valorTaxaLiquidacao +
                ", valorTaxasEmolumentos=" + valorTaxasEmolumentos +
                ", valorImpostos=" + valorImpostos +
                ", outrosValoresCobrados=" + outrosValoresCobrados +
                ", valorCorretagem=" + valorCorretagem +
                ", valorTotalComCustosEDespesas=" + valorTotalComCustosEDespesas +
                '}';
    }
}
