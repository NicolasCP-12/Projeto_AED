package Algoritmo;

import Modelo.Intervalo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class SelecaoIntervalos {

    /**
     * Otimiza as janelas temporais de entrega selecionando o conjunto máximo de intervalos sem sobreposição.
     */

    public List<Intervalo> otimizarJanelas(List<Intervalo> intervalos) {
        if (intervalos == null || intervalos.isEmpty()) {
            return new ArrayList<>();
        }

        // Cópia para preservar a lista original de intervalos intacta
        List<Intervalo> candidatos = new ArrayList<>(intervalos);

        // 1. Ordenação pelo tempo de fim crescente via MergeSort -> O(N log N)
        mergeSort(candidatos, 0, candidatos.size() - 1);

        // 2. Seleção gananciosa dos intervalos sem sobreposição -> O(N)
        List<Intervalo> selecionados = new ArrayList<>();
        LocalDateTime fimUltimoSelecionado = null;

        for (Intervalo atual : candidatos) {
            // Seleciona se for o primeiro ou se o início for maior/igual ao fim do último aceite
            if (fimUltimoSelecionado == null || !atual.getInicio().isBefore(fimUltimoSelecionado)) {
                selecionados.add(atual);
                fimUltimoSelecionado = atual.getFim();
            }
        }

        return selecionados;
    }

    /**
     * Algoritmo de ordenação MergeSort baseado no paradigma de Divisão e Conquista
     */
    private void mergeSort(List<Intervalo> lista, int esquerda, int direita) {
        if (esquerda < direita) {
            int meio = esquerda + (direita - esquerda) / 2;

            // Passo de Divisão
            mergeSort(lista, esquerda, meio);
            mergeSort(lista, meio + 1, direita);


            merge(lista, esquerda, meio, direita);
        }
    }


    private void merge(List<Intervalo> lista, int esquerda, int meio, int direita) {
        int n1 = meio - esquerda + 1;
        int n2 = direita - meio;

        List<Intervalo> subEsquerda = new ArrayList<>(n1);
        List<Intervalo> subDireita = new ArrayList<>(n2);

        for (int i = 0; i < n1; i++) {
            subEsquerda.add(lista.get(esquerda + i));
        }
        for (int j = 0; j < n2; j++) {
            subDireita.add(lista.get(meio + 1 + j));
        }

        int i = 0, j = 0;
        int k = esquerda;

        while (i < n1 && j < n2) {
            // Compara os tempos de fim para garantir a estabilidade e ordenação crescente
            if (!subDireita.get(j).getFim().isBefore(subEsquerda.get(i).getFim())) {
                lista.set(k, subEsquerda.get(i));
                i++;
            } else {
                lista.set(k, subDireita.get(j));
                j++;
            }
            k++;
        }

        // Copia os elementos restantes da sublista esquerda, se existirem
        while (i < n1) {
            lista.set(k, subEsquerda.get(i));
            i++;
            k++;
        }

        // Copia os elementos restantes da sublista direita, se existirem
        while (j < n2) {
            lista.set(k, subDireita.get(j));
            j++;
            k++;
        }
    }
}