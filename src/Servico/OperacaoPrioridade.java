package Servico;

import Estrutura.HeapBinario;
import Modelo.CatalogoOperacoes;
import Modelo.Encomenda;
import Modelo.Operacao;

public class OperacaoPrioridade extends Operacao {
    private HeapBinario fila;
    private int prioridadeAnterior;
    private int novaPrioridade;

    public OperacaoPrioridade(Encomenda encomenda, HeapBinario fila, int prioridadeAnterior, int novaPrioridade) {
        super(CatalogoOperacoes.ALTERACAO_PRIORIDADE, encomenda);
        this.fila = fila;
        this.prioridadeAnterior = prioridadeAnterior;
        this.novaPrioridade = novaPrioridade;
    }

    @Override
    public void executar() {
        Encomenda removida = fila.removerPorId(getEncomenda().getId());
        if (removida != null) {
            removida.calcularPrioridade(novaPrioridade);
            fila.inserir(removida);
        }
    }

    @Override
    public void desfazer() {
        Encomenda removida = fila.removerPorId(getEncomenda().getId());
        if (removida != null) {
            removida.calcularPrioridade(prioridadeAnterior);
            fila.inserir(removida);
        }
    }
}