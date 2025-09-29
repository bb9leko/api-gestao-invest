package br.com.bb9leko.apigestaoinvest.dto;

public enum ClassificacaoAtivo {
    FIIS("Fundos Imobiliários"),
    ACOES("Ações"),
    CRIPTOMOEDAS("Criptomoedas"),
    RENDA_FIXA("Renda Fixa"),
    TESOURO_NACIONAL("Tesouro Direto"),
    ETFS("ETFs"),
    ETFS_INTERNACIONAIS("ETFs Internacionais"),
    STOCKS("Stocks"),;

    private final String descricao;

    ClassificacaoAtivo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
