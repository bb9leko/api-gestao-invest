package br.com.bb9leko.apigestaoinvest.dto;

public enum ClassificacaoAtivo {
    FIIS("FIIs"),
    ACOES("ACOES"),
    CRIPTOMOEDAS("CRIPTO"),
    RENDA_FIXA("Renda Fixa"),
    TESOURO_NACIONAL("Tesouro Direto"),
    ETFS("ETFs"),;

    private final String descricao;

    ClassificacaoAtivo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
