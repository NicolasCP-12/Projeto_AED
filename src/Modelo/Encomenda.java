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