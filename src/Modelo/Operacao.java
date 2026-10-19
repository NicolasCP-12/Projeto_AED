package Modelo;


// Ainda para decidir como será a gestao de de frotas e encomendas
public class Operacao implements Comando {
    private TipoOperacao tipo;
    private Encomenda encomenda;
    private Veiculo veiculo; // Opcional, usado quando é uma atribuição

    public Operacao(TipoOperacao tipo, Encomenda encomenda, Veiculo veiculo) {
        this.tipo = tipo;
        this.encomenda = encomenda;
        this.veiculo = veiculo;
    }

    public Operacao(TipoOperacao tipo, Encomenda encomenda) {
        this.tipo = tipo;
        this.encomenda = encomenda;
        this.veiculo = null;
    }

    @Override
    public void executar() {
        // Exemplo genérico de execução:
        System.out.println("A executar operação: " + tipo.getDesignacao() + " para a encomenda " + encomenda.getId());
        if (tipo.getDesignacao().equalsIgnoreCase("ATRIBUICAO") && veiculo != null) {
            veiculo.adicionarEncomenda(encomenda);
        }

    }

    @Override
    public void desfazer() {
        // Lógica inversa da execução
        System.out.println("A desfazer operação: " + tipo.getDesignacao() + " para a encomenda " + encomenda.getId());
        if (tipo.getDesignacao().equalsIgnoreCase("ATRIBUICAO") && veiculo != null) {
            veiculo.removerEncomenda(encomenda);
        }
    }

    public TipoOperacao getTipo() { return tipo; }
    public Encomenda getEncomenda() { return encomenda; }
    public Veiculo getVeiculo() { return veiculo; }
}