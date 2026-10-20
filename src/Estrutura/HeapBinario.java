package Estrutura;

import Modelo.Encomenda;

public class HeapBinario {
    private Encomenda[] elementos;
    private int tamanho;
    private static final int CAPACIDADE_PADRAO = 10;

    public HeapBinario() {
        this(CAPACIDADE_PADRAO);
    }

    public HeapBinario(int capacidadeInicial) {
        if (capacidadeInicial <= 0) {
            capacidadeInicial = CAPACIDADE_PADRAO;
        }
        this.elementos = new Encomenda[capacidadeInicial];
        this.tamanho = 0;
    }

    public void inserir(Encomenda encomenda) {
        if (encomenda == null) {
            throw new IllegalArgumentException("A encomenda não pode ser nula.");
        }

        if (tamanho == elementos.length) {
            redimensionar();
        }

        elementos[tamanho] = encomenda;
        heapifyUp(tamanho);
        tamanho++;
    }

    public Encomenda removerTopo() {
        if (isEmpty()) {
            return null;
        }

        Encomenda topo = elementos[0];
        elementos[0] = elementos[tamanho - 1];
        elementos[tamanho - 1] = null;
        tamanho--;

        if (tamanho > 0) {
            heapifyDown(0);
        }

        return topo;
    }

    public Encomenda obterTopo() {
        if (isEmpty()) {
            return null;
        }
        return elementos[0];
    }

    public Encomenda removerPorId(String id) {
        if (id == null || isEmpty()) {
            return null;
        }

        int indiceEncontrado = -1;
        for (int i = 0; i < tamanho; i++) {
            if (elementos[i].getId().equalsIgnoreCase(id)) {
                indiceEncontrado = i;
                break;
            }
        }

        if (indiceEncontrado == -1) {
            return null;
        }

        Encomenda removida = elementos[indiceEncontrado];
        elementos[indiceEncontrado] = elementos[tamanho - 1];
        elementos[tamanho - 1] = null;
        tamanho--;

        if (indiceEncontrado < tamanho) {
            heapifyDown(indiceEncontrado);
            heapifyUp(indiceEncontrado);
        }

        return removida;
    }

    private void heapifyUp(int indice) {
        while (indice > 0) {
            int indicePai = (indice - 1) / 2;

            if (elementos[indice].getPrioridade() > elementos[indicePai].getPrioridade()) {
                trocar(indice, indicePai);
                indice = indicePai;
            } else {
                break;
            }
        }
    }

    private void heapifyDown(int indice) {
        while (indice < tamanho) {
            int esquerdo = 2 * indice + 1;
            int direito = 2 * indice + 2;
            int maior = indice;

            if (esquerdo < tamanho && elementos[esquerdo].getPrioridade() > elementos[maior].getPrioridade()) {
                maior = esquerdo;
            }

            if (direito < tamanho && elementos[direito].getPrioridade() > elementos[maior].getPrioridade()) {
                maior = direito;
            }

            if (maior != indice) {
                trocar(indice, maior);
                indice = maior;
            } else {
                break;
            }
        }
    }

    private void trocar(int i, int j) {
        Encomenda temp = elementos[i];
        elementos[i] = elementos[j];
        elementos[j] = temp;
    }

    private void redimensionar() {
        int novaCapacidade = elementos.length == 0 ? CAPACIDADE_PADRAO : elementos.length * 2;
        Encomenda[] novoArray = new Encomenda[novaCapacidade];
        System.arraycopy(elementos, 0, novoArray, 0, elementos.length);
        elementos = novoArray;
    }

    public int getTamanho() {
        return tamanho;
    }

    public boolean isEmpty() {
        return tamanho == 0;
    }
}