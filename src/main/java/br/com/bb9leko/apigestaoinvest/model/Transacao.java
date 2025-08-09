package br.com.bb9leko.apigestaoinvest.model;

import br.com.bb9leko.apigestaoinvest.dto.Evento;
import br.com.bb9leko.apigestaoinvest.dto.ClassificacaoAtivo;
import br.com.bb9leko.apigestaoinvest.dto.TransacaoDTO;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    private ClassificacaoAtivo classificacaoAtivo;

    private String ticket;

    @Enumerated(EnumType.STRING)
    private Evento compraOUVenda;

    private int quantidade;

    private BigDecimal valorUnitario;

    private BigDecimal valorTotal;

    private BigDecimal valorTaxaLiquidacao;

    private BigDecimal valorTaxasEmolumentos;

    private BigDecimal valorImpostos;

    private BigDecimal outrosValoresCobrados;

    //Ou Taxa Operacional
    private BigDecimal valorCorretagem;

    private BigDecimal valorTotalComCustosEDespesas;

    public Transacao(TransacaoDTO dto) {
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
        // Os valores calculados (valorTotal, etc.) serão tratados pelo @PrePersist
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
        return this.valorUnitario.multiply(BigDecimal.valueOf(this.quantidade));
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
        return this.valorTotalComCustosEDespesas = this.valorUnitario.multiply(BigDecimal.valueOf(this.quantidade))
                .add(this.valorTaxaLiquidacao != null ? this.valorTaxaLiquidacao : BigDecimal.ZERO)
                .add(this.valorTaxasEmolumentos != null ? this.valorTaxasEmolumentos : BigDecimal.ZERO)
                .add(this.valorImpostos != null ? this.valorImpostos : BigDecimal.ZERO)
                .add(this.outrosValoresCobrados != null ? this.outrosValoresCobrados : BigDecimal.ZERO)
                .add(this.valorCorretagem != null ? this.valorCorretagem : BigDecimal.ZERO);
    }

    public void setValorTotalComCustosEDespesas(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    @PrePersist
    @PreUpdate
    private void calcularValorTotalComCustosEDespesas() {
        this.valorTotal = this.valorUnitario.multiply(BigDecimal.valueOf(this.quantidade));

        BigDecimal total = this.valorTotal
                .add(this.valorTaxaLiquidacao != null ? this.valorTaxaLiquidacao : BigDecimal.ZERO)
                .add(this.valorTaxasEmolumentos != null ? this.valorTaxasEmolumentos : BigDecimal.ZERO)
                .add(this.valorImpostos != null ? this.valorImpostos : BigDecimal.ZERO)
                .add(this.outrosValoresCobrados != null ? this.outrosValoresCobrados : BigDecimal.ZERO)
                .add(this.valorCorretagem != null ? this.valorCorretagem : BigDecimal.ZERO);

        this.valorTotalComCustosEDespesas = total;
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
