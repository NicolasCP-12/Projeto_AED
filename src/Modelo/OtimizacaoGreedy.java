package Modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OtimizacaoGreedy {

    public static List<Encomenda> selecionarEncomendas(List<Encomenda> lista, double maxPeso, double maxVolume) {
        if (lista == null || lista.isEmpty()) {
            return new ArrayList<>();
        }

        // 1. Criamos uma cópia para não alterar a lista original
        List<Encomenda> candidatos = new ArrayList<>(lista);

        // 2. Ordenação simples (Bubble Sort) por densidade decrescente
        for (int i = 0; i < candidatos.size() - 1; i++) {
            for (int j = 0; j < candidatos.size() - 1 - i; j++) {
                // Se a encomenda atual tiver menor densidade que a próxima, trocamos de posição
                if (candidatos.get(j).getDensidade() < candidatos.get(j + 1).getDensidade()) {
                    Encomenda temp = candidatos.get(j);
                    candidatos.set(j, candidatos.get(j + 1));
                    candidatos.set(j + 1, temp);
                }
            }
        }

        // 3. Seleção gulosa: percorrer por ordem e carregar o que couber
        List<Encomenda> selecionadas = new ArrayList<>();
        double pesoAtual = 0.0;
        double volumeAtual = 0.0;

        for (int i = 0; i < candidatos.size(); i++) {
            Encomenda enc = candidatos.get(i);

            // Verifica se a encomenda cabe tanto no limite de peso como no de volume
            if (pesoAtual + enc.getPeso() <= maxPeso && volumeAtual + enc.getVolume() <= maxVolume) {
                selecionadas.add(enc);
                pesoAtual += enc.getPeso();
                volumeAtual += enc.getVolume();
            }
        }

        System.out.println("Empacotamento Guloso");
        System.out.println("Carregadas: " + selecionadas.size() + " de " + lista.size() + " encomendas.");
        System.out.println("Peso acumulado: " + pesoAtual + " / " + maxPeso + " kg");
        System.out.println("Volume acumulado: " + volumeAtual + " / " + maxVolume + " m³");

        return selecionadas;
    }

    // Interval Scheduling

    public static List<Encomenda> otimizarJanelasEntrega(List<Encomenda> lista) {
        if (lista == null || lista.isEmpty()) {
            return new ArrayList<>();
        }

        // 1. Filtrar apenas encomendas que tenham janela de entrega definida
        List<Encomenda> candidatas = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            Encomenda e = lista.get(i);
            if (e.getJanelaEntrega() != null) {
                candidatas.add(e);
            }
        }

        // 2. Ordenação simples (Bubble Sort) por tempo de fim crescente
        for (int i = 0; i < candidatas.size() - 1; i++) {
            for (int j = 0; j < candidatas.size() - 1 - i; j++) {
                LocalDateTime fimJ = candidatas.get(j).getJanelaEntrega().getFim();
                LocalDateTime fimJMais1 = candidatas.get(j + 1).getJanelaEntrega().getFim();

                // Se o fim da entrega j for depois do fim da j+1, trocamos
                if (fimJ.isAfter(fimJMais1)) {
                    Encomenda temp = candidatas.get(j);
                    candidatas.set(j, candidatas.get(j + 1));
                    candidatas.set(j + 1, temp);
                }
            }
        }

        // 3. Seleção gulosa de intervalos compatíveis
        List<Encomenda> compativeis = new ArrayList<>();
        LocalDateTime fimUltima = null;

        for (int i = 0; i < candidatas.size(); i++) {
            Encomenda enc = candidatas.get(i);
            LocalDateTime inicioEnc = enc.getJanelaEntrega().getInicio();

            // Se for a primeira entrega, ou se não começar antes do fim da última selecionada (sem conflito)
            if (fimUltima == null || !inicioEnc.isBefore(fimUltima)) {
                compativeis.add(enc);
                fimUltima = enc.getJanelaEntrega().getFim(); // Atualiza a hora em que o veículo fica livre
            }
        }

        System.out.println("--- Otimização de Janelas Temporais ---");
        System.out.println("Entregas sem conflito selecionadas: " + compativeis.size() + " de " + candidatas.size());

        return compativeis;
    }

}
