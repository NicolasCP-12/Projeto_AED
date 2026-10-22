package Modelo;

import java.time.LocalDateTime;

public class Intervalo {
    private LocalDateTime inicio;
    private LocalDateTime fim;

    public Intervalo(LocalDateTime inicio, LocalDateTime fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    public Intervalo(int horaInicio, int horaFim) {
        this(LocalDateTime.now().withHour(horaInicio).withMinute(0).withSecond(0).withNano(0),
             LocalDateTime.now().withHour(horaFim).withMinute(0).withSecond(0).withNano(0));
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public Boolean intersecao(Intervalo intervalo) {
        return this.inicio.isBefore(intervalo.getFim()) && this.fim.isAfter(intervalo.getInicio());
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d até %02d:%02d",
                inicio.getHour(), inicio.getMinute(),
                fim.getHour(), fim.getMinute());
    }
}
