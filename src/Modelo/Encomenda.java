package Modelo;

import java.time.LocalDateTime;

public class Encomenda {
    private String id;
    private double peso;
    private double volume;
    private LocalDateTime tempoChegada;
    private int prioridade;
    private Intervalo janelaEntrega;

    public Encomenda(String id, double peso, double volume, LocalDateTime tempoChegada, int prioridade) {
        this(id, peso, volume, tempoChegada, prioridade, null);
    }

    public Encomenda(String id, double peso, double volume, LocalDateTime tempoChegada, int prioridade, Intervalo janelaEntrega) {
        this.id = id;
        this.peso = peso;
        this.volume = volume;
        this.tempoChegada = tempoChegada;
        this.prioridade = prioridade;
        this.janelaEntrega = janelaEntrega;
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

    public Intervalo getJanelaEntrega() {
        return janelaEntrega;
    }

    public void setJanelaEntrega(Intervalo janelaEntrega) {
        this.janelaEntrega = janelaEntrega;
    }

    public double getDensidade() {
        double custoTotal = this.peso + this.volume;
        if (custoTotal <= 0) {
            return this.prioridade;
        }
        return this.prioridade / custoTotal;
    }

    @Override
    public String toString() {
        return "Encomenda[" + id + " | Peso: " + peso + "kg | Vol: " + volume + "m³ | Prio: " + prioridade + "]";
    }
}
