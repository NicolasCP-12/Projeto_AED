package Modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Encomenda {
    private String id;
    private double peso;
    private double volume;
    private LocalDateTime tempoChegada;
    private int prioridade;

    public Encomenda(String id, double peso, double volume, LocalDateTime tempoChegada, int prioridade) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("O ID da encomenda é obrigatório.");
        }
        if (peso <= 0 || volume <= 0) {
            throw new IllegalArgumentException("O peso e o volume têm de ser positivos.");
        }
        if (tempoChegada == null) {
            throw new IllegalArgumentException("O tempo de chegada é obrigatório.");
        }
        this.id = id;
        this.peso = peso;
        this.volume = volume;
        this.tempoChegada = tempoChegada;
        this.prioridade = prioridade;
    }

    public String getId() {
        return id;
    }

    public double getPeso() {
        return peso;
    }

    public double getVolume() {
        return volume;
    }

    public LocalDateTime getTempoChegada() {
        return tempoChegada;
    }

    public double getPrioridade() {
        return prioridade;
    }

    public void calcularPrioridade(int novaPrioridade) {
        this.prioridade = novaPrioridade;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("Encomenda [ID=%s | Prioridade=%d | Peso=%.2fkg | Volume=%.2fm3 | Chegada=%s]",
                id, prioridade, peso, volume, tempoChegada.format(fmt));
    }
}