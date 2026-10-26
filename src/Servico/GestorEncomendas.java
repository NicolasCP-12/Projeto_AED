package Servico;

import Estrutura.HeapBinario;
import Modelo.CatalogoOperacoes;
import Modelo.Encomenda;

import java.util.ArrayList;
import java.util.List;

public class GestorEncomendas {
    private HeapBinario filaPrioridade;
    private GestorHistorico gestorHistorico;

    public GestorEncomendas(GestorHistorico gestorHistorico) {
        this.filaPrioridade = new HeapBinario();
        this.gestorHistorico = gestorHistorico;
    }

    /**
     * Regista uma encomenda validando a unicidade com procurarPorId -> O(N)
     */
    public boolean registarEncomenda(Encomenda encomenda) {
        if (encomenda == null || filaPrioridade.procurarPorId(encomenda.getId()) != null) {
            return false;
        }
        filaPrioridade.inserir(encomenda);
        if (gestorHistorico != null) {
            gestorHistorico.registarOperacao(
                    new OperacaoFila(CatalogoOperacoes.ENTRADA, encomenda, filaPrioridade)
            );
        }
        return true;
    }

    /**
     * Insere uma encomenda lida de ficheiro sem a registar no histórico -> O(N)
     * (o estado carregado não deve poder ser desfeito)
     */
    public boolean carregarEncomenda(Encomenda encomenda) {
        if (encomenda == null || filaPrioridade.procurarPorId(encomenda.getId()) != null) {
            return false;
        }
        filaPrioridade.inserir(encomenda);
        return true;
    }

    public Encomenda consultarMaisPrioritaria() {
        return filaPrioridade.obterTopo();
    }

    public Encomenda extrairMaisPrioritaria() {
        return filaPrioridade.removerTopo();
    }

    /**
     * Cancela uma encomenda por ID -> O(N)
     */
    public Encomenda cancelarEncomenda(String id) {
        Encomenda removida = filaPrioridade.removerPorId(id);
        if (removida != null && gestorHistorico != null) {
            gestorHistorico.registarOperacao(
                    new OperacaoFila(CatalogoOperacoes.CANCELAMENTO, removida, filaPrioridade)
            );
        }
        return removida;
    }

    /**
     * Altera a prioridade removendo e reinserindo no Heap -> O(N)
     */
    public boolean alterarPrioridade(String id, int novaPrioridade) {
        Encomenda existente = filaPrioridade.procurarPorId(id);
        if (existente == null) {
            return false;
        }
        int prioridadeAntiga = (int) existente.getPrioridade();
        filaPrioridade.removerPorId(id);
        existente.calcularPrioridade(novaPrioridade);
        filaPrioridade.inserir(existente);

        if (gestorHistorico != null) {
            gestorHistorico.registarOperacao(
                    new OperacaoPrioridade(existente, filaPrioridade, prioridadeAntiga, novaPrioridade)
            );
        }
        return true;
    }

    public List<Encomenda> listarEncomendasPorPrioridade() {
        List<Encomenda> lista = new ArrayList<>();
        HeapBinario auxiliar = new HeapBinario(Math.max(10, filaPrioridade.getTamanho()));

        while (!filaPrioridade.isEmpty()) {
            Encomenda enc = filaPrioridade.removerTopo();
            lista.add(enc);
            auxiliar.inserir(enc);
        }

        while (!auxiliar.isEmpty()) {
            filaPrioridade.inserir(auxiliar.removerTopo());
        }

        return lista;
    }

    public HeapBinario getFilaPrioridade() {
        return filaPrioridade;
    }
}