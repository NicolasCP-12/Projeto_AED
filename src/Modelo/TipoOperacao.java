package Modelo;


//definicao tipo de operacao
public class TipoOperacao {
    private int codigo;
    private String designacao;

    public TipoOperacao(int codigo, String designacao) {
        this.codigo = codigo;
        this.designacao = designacao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDesignacao() {
        return designacao;
    }
}