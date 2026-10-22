package Algoritmo;

import Estrutura.HeapBinario;
import Modelo.Encomenda;
import Modelo.Veiculo;

import java.util.ArrayList;
import java.util.List;


public class Empacotador {

    /**
     * Carrega um veículo com as encomendas mais prioritárias disponíveis na fila.
     * Encomendas que não couberem neste veículo são devolvidas ao HeapBinario.
     */
    public List<Encomenda> carregarVeiculo(HeapBinario heap, Veiculo veiculo) {
        List<Encomenda> encomendasCarregadas = new ArrayList<>();

        if (heap == null || heap.isEmpty() || veiculo == null) {
            return encomendasCarregadas;
        }

        // Heap auxiliar para reter temporariamente encomendas que não couberam
        HeapBinario naoCouberam = new HeapBinario(heap.getTamanho());

        while (!heap.isEmpty()) {
            // Extrai a encomenda com maior prioridade no topo do Max-Heap -> O(log N)
            Encomenda candidata = heap.removerTopo();

            // O método adicionarEncomenda do Veiculo valida o pesoMaximo e volumeMaximo
            if (veiculo.adicionarEncomenda(candidata)) {
                encomendasCarregadas.add(candidata);
            } else {
                // Não coube: guarda temporariamente no heap auxiliar
                naoCouberam.inserir(candidata);
            }
        }

        // Devolve ao HeapBinario principal as encomendas que ultrapassavam a capacidade do veículo
        while (!naoCouberam.isEmpty()) {
            heap.inserir(naoCouberam.removerTopo());
        }

        return encomendasCarregadas;
    }
}