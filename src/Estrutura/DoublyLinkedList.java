package Estrutura;

import Modelo.Operacao;

public class DoublyLinkedList {
    private Node cabeca;
    private Node cauda;
    private int tamanho;

    public DoublyLinkedList() {
        this.cabeca = null;
        this.cauda = null;
        this.tamanho = 0;
    }

    public Node getCabeca() {
        return cabeca;
    }

    public Node getCauda() {
        return cauda;
    }

    public int getTamanho() {
        return tamanho;
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }


    public void inserirFim(Operacao operacao) {
        if (operacao == null) {
            return;
        }

        Node novo = new Node(operacao);

        if (isEmpty()) {
            cabeca = novo;
            cauda = novo;
        } else {
            cauda.setSeguinte(novo);
            novo.setAnterior(cauda);
            cauda = novo;
        }
        tamanho++;
    }


    public Operacao removerFim() {
        if (isEmpty()) {
            return null;
        }

        Node removido = cauda;

        if (cabeca == cauda) { // Apenas 1 elemento na lista
            cabeca = null;
            cauda = null;
        } else {
            cauda = cauda.getAnterior();
            cauda.setSeguinte(null);
        }

        tamanho--;

        // Limpa as referências do nó removido por segurança
        removido.setAnterior(null);
        removido.setSeguinte(null);

        return removido.getElemento();
    }
}