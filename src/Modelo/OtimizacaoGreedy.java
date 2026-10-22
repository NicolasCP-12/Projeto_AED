package Modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OtimizacaoGreedy {

    // 1. Algoritmo Guloso para Empacotamento de Encomendas num Veículo
    public static List<Encomenda> selecionarEncomendas(List<Encomenda> lista, double maxPeso, double maxVolume) {
        List<Encomenda> candidatos = new ArrayList<>(lista);

        // Ordenar por densidade decrescente: prioridade / (peso + volume)
        for (int i = 0; i < candidatos.size() - 1; i++) {
            for (int j = 0; j < candidatos.size() - 1 - i; j++) {
                double densidade1 = candidatos.get(j).getPrioridade() / (candidatos.get(j).getPeso() + candidatos.get(j).getVolume());
                double densidade2 = candidatos.get(j + 1).getPrioridade() / (candidatos.get(j + 1).getPeso() + candidatos.get(j + 1).getVolume());

                if (densidade1 < densidade2) {
                    Encomenda temp = candidatos.get(j);
                    candidatos.set(j, candidatos.get(j + 1));
                    candidatos.set(j + 1, temp);
                }
            }
        }

        // Selecionar as que cabem no limite de peso e volume
        List<Encomenda> selecionadas = new ArrayList<>();
        double pesoAtual = 0;
        double volumeAtual = 0;

        for (int i = 0; i < candidatos.size(); i++) {
            Encomenda enc = candidatos.get(i);
            if (pesoAtual + enc.getPeso() <= maxPeso && volumeAtual + enc.getVolume() <= maxVolume) {
                selecionadas.add(enc);
                pesoAtual += enc.getPeso();
                volumeAtual += enc.getVolume();
            }
        }

        return selecionadas;
    }

    // 2. Algoritmo Guloso para Seleção de Intervalos (Janelas Temporais)
    public static List<Intervalo> otimizarJanelasEntrega(List<Intervalo> lista) {
        List<Intervalo> candidatos = new ArrayList<>(lista);

        // Ordenar por tempo de fim crescente
        for (int i = 0; i < candidatos.size() - 1; i++) {
            for (int j = 0; j < candidatos.size() - 1 - i; j++) {
                if (candidatos.get(j).getFim().isAfter(candidatos.get(j + 1).getFim())) {
                    Intervalo temp = candidatos.get(j);
                    candidatos.set(j, candidatos.get(j + 1));
                    candidatos.set(j + 1, temp);
                }
            }
        }

        // Selecionar os intervalos sem sobreposição
        List<Intervalo> selecionados = new ArrayList<>();
        LocalDateTime fimAtual = null;

        for (int i = 0; i < candidatos.size(); i++) {
            Intervalo atual = candidatos.get(i);
            if (fimAtual == null || !atual.getInicio().isBefore(fimAtual)) {
                selecionados.add(atual);
                fimAtual = atual.getFim();
            }
        }

        return selecionados;
    }
}
