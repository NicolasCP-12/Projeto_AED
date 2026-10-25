package Servico;

import Estrutura.HeapBinario;
import Modelo.CatalogoOperacoes;
import Modelo.Encomenda;
import Modelo.Operacao;
import Modelo.Veiculo;

public class OperacaoAtribuicao extends Operacao {
    private HeapBinario fila;

    public OperacaoAtribuicao(Encomenda encomenda, Veiculo veiculo, HeapBinario fila) {
        super(CatalogoOperacoes.ATRIBUICAO, encomenda, veiculo);
        this.fila = fila;
    }

    @Override
    public void executar() {
        if (getVeiculo() != null && getEncomenda() != null) {
            if (getVeiculo().adicionarEncomenda(getEncomenda())) {
                if (fila != null) {
                    fila.removerPorId(getEncomenda().getId());
                }
            }
        }
    }

    @Override
    public void desfazer() {
        if (getVeiculo() != null && getEncomenda() != null) {
            if (getVeiculo().removerEncomenda(getEncomenda())) {
                if (fila != null && fila.procurarPorId(getEncomenda().getId()) == null) {
                    fila.inserir(getEncomenda());
                }
            }
        }
    }
}