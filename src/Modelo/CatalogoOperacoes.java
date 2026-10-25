package Modelo;

public class CatalogoOperacoes {
    public static final TipoOperacao ENTRADA = new TipoOperacao(1, "ENTRADA");
    public static final TipoOperacao ATRIBUICAO = new TipoOperacao(2, "ATRIBUICAO");
    public static final TipoOperacao CANCELAMENTO = new TipoOperacao(3, "CANCELAMENTO");
    public static final TipoOperacao ALTERACAO_PRIORIDADE = new TipoOperacao(4, "ALTERACAO_PRIORIDADE");

    private CatalogoOperacoes() {
    }
}