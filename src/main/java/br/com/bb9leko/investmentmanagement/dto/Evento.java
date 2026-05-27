package br.com.bb9leko.investmentmanagement.dto;

public enum Evento {
    COMPRA("compra"),
    VENDA("venda"),
    BONIFICACAO("bonificacao"),
    DESDOBRAMENTO("desdobramento"),
    CONVERSAO("conversao"),;

    private final String descricao;

    Evento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
