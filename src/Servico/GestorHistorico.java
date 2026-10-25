package Servico;

import Estrutura.DoublyLinkedList;
import Estrutura.Node;
import Modelo.Operacao;

import java.util.ArrayList;
import java.util.List;

public class GestorHistorico {
    private DoublyLinkedList listaDesfazer;
    private DoublyLinkedList listaRefazer;
    private Node cursorNavegacao;

    public GestorHistorico() {
        this.listaDesfazer = new DoublyLinkedList();
        this.listaRefazer = new DoublyLinkedList();
        this.cursorNavegacao = null;
    }

    /**
     * Regista uma nova operação em O(1) na cauda da lista de desfazer
     * e limpa a lista de refazer.
     */
    public void registarOperacao(Operacao operacao) {
        if (operacao == null) {
            return;
        }
        listaDesfazer.inserirFim(operacao);
        listaRefazer = new DoublyLinkedList();
        cursorNavegacao = listaDesfazer.getCauda();
    }

    /**
     * Desfaz a operação mais recente (cauda da listaDesfazer) em O(1)
     * e move-a para a listaRefazer.
     */
    public boolean desfazer() {
        if (listaDesfazer.isEmpty()) {
            return false;
        }
        Operacao op = listaDesfazer.removerFim();
        op.desfazer();
        listaRefazer.inserirFim(op);
        cursorNavegacao = listaDesfazer.getCauda();
        return true;
    }

    /**
     * Refaz a última operação desfeita (cauda da listaRefazer) em O(1)
     * e devolve-a à listaDesfazer.
     */
    public boolean refazer() {
        if (listaRefazer.isEmpty()) {
            return false;
        }
        Operacao op = listaRefazer.removerFim();
        op.executar();
        listaDesfazer.inserirFim(op);
        cursorNavegacao = listaDesfazer.getCauda();
        return true;
    }

    public Operacao obterOperacaoAtual() {
        return cursorNavegacao != null ? cursorNavegacao.getElemento() : null;
    }

    public Operacao navegarAnterior() {
        if (cursorNavegacao != null && cursorNavegacao.getAnterior() != null) {
            cursorNavegacao = cursorNavegacao.getAnterior();
        }
        return obterOperacaoAtual();
    }

    public Operacao navegarSeguinte() {
        if (cursorNavegacao != null && cursorNavegacao.getSeguinte() != null) {
            cursorNavegacao = cursorNavegacao.getSeguinte();
        }
        return obterOperacaoAtual();
    }

    public void irParaInicio() {
        cursorNavegacao = listaDesfazer.getCabeca();
    }

    public void irParaFim() {
        cursorNavegacao = listaDesfazer.getCauda();
    }

    public List<String> listarHistoricoFormatado() {
        List<String> lista = new ArrayList<>();
        Node atual = listaDesfazer.getCabeca();
        int indice = 1;

        while (atual != null) {
            Operacao op = atual.getElemento();
            String marcador = (atual == cursorNavegacao) ? " <-- [CURSOR ATUAL]" : "";
            String infoVeiculo = (op.getVeiculo() != null) ? " | Veículo: " + op.getVeiculo().getId() : "";
            lista.add(String.format("%d. [%s] Encomenda: %s (Prio: %d)%s%s",
                    indice++,
                    op.getTipo().getDesignacao(),
                    op.getEncomenda().getId(),
                    (int) op.getEncomenda().getPrioridade(),
                    infoVeiculo,
                    marcador));
            atual = atual.getSeguinte();
        }
        return lista;
    }

    public DoublyLinkedList getListaDesfazer() {
        return listaDesfazer;
    }

    public DoublyLinkedList getListaRefazer() {
        return listaRefazer;
    }
}