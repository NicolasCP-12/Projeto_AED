package Modelo;

import java.time.LocalDateTime;

public class Intervalo {
    private LocalDateTime inicio;
    private LocalDateTime fim;

    public Intervalo(LocalDateTime inicio, LocalDateTime fim) {
        this.inicio = inicio;
        this.fim = fim;
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
}
