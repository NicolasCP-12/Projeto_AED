package Servico;

import Estrutura.HeapBinario;
import Modelo.CatalogoOperacoes;
import Modelo.Encomenda;
import Modelo.Operacao;
import Modelo.TipoOperacao;

public class OperacaoFila extends Operacao {
    private HeapBinario fila;

    public OperacaoFila(TipoOperacao tipo, Encomenda encomenda, HeapBinario fila) {
        super(tipo, encomenda);
        this.fila = fila;
    }

    @Override
    public void executar() {
        if (getTipo().getDesignacao().equalsIgnoreCase(CatalogoOperacoes.ENTRADA.getDesignacao())) {
            if (fila.procurarPorId(getEncomenda().getId()) == null) {
                fila.inserir(getEncomenda());
            }
        } else if (getTipo().getDesignacao().equalsIgnoreCase(CatalogoOperacoes.CANCELAMENTO.getDesignacao())) {
            fila.removerPorId(getEncomenda().getId());
        }
    }

    @Override
    public void desfazer() {
        if (getTipo().getDesignacao().equalsIgnoreCase(CatalogoOperacoes.ENTRADA.getDesignacao())) {
            fila.removerPorId(getEncomenda().getId());
        } else if (getTipo().getDesignacao().equalsIgnoreCase(CatalogoOperacoes.CANCELAMENTO.getDesignacao())) {
            if (fila.procurarPorId(getEncomenda().getId()) == null) {
                fila.inserir(getEncomenda());
            }
        }
    }
}