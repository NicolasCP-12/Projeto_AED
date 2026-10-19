package Estrutura;

import Modelo.Operacao;

public class DoublyLinkedList {
    private Node cabeca;
    private Node Cauda;
    private int tamanho;

    public DoublyLinkedList() {
        cabeca = null;
        Cauda = null;
        tamanho = 0;
    }

    public Node getCabeca() {
        return cabeca;
    }

    public Node getCauda() {
        return Cauda;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setCabeca(Node cabeca) {
        this.cabeca = cabeca;
    }

    public void setCauda(Node cauda) {
        Cauda = cauda;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public Boolean isEmpty() {
        if (cabeca == null && Cauda == null && tamanho == 0) {
            return true;
        }
        return false;
    }

    public void inserirFim(Node novo) {
        if (isEmpty()) {
            cabeca = novo;
            Cauda = novo;
            tamanho++;
        }
    }
    public Node removerFim() {
        if (isEmpty()) {
            return null;
        }
        Node aux = Cauda;
        Cauda = aux.anterior;
        Cauda.seguinte = null;
        tamanho--;
        return aux;
    }

    // public Operacao remover(){
      //  if (isEmpty()) {
       //     return null;
      //  }

   // }
}
