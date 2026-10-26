package Servico;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Calcula o nível de prioridade de uma encomenda a partir de três critérios:
 *   P = 10 * urgência + taxa paga / 5 + horas de espera (limitadas a 48)
 * A urgência (1 a 5) domina, a taxa paga distingue encomendas igualmente urgentes
 * e o tempo de espera evita que encomendas pouco urgentes fiquem indefinidamente na fila.
 */
public class CalculadoraPrioridade {
    public static final int URGENCIA_MINIMA = 1;
    public static final int URGENCIA_MAXIMA = 5;
    private static final int PESO_URGENCIA = 10;
    private static final int DIVISOR_TAXA = 5;
    private static final int MAX_HORAS_ESPERA = 48;

    private CalculadoraPrioridade() {
    }

    public static int calcular(int urgencia, double taxaPaga, LocalDateTime tempoChegada, LocalDateTime agora) {
        if (urgencia < URGENCIA_MINIMA || urgencia > URGENCIA_MAXIMA) {
            throw new IllegalArgumentException("A urgência tem de estar entre " + URGENCIA_MINIMA + " e " + URGENCIA_MAXIMA + ".");
        }
        if (taxaPaga < 0) {
            throw new IllegalArgumentException("A taxa paga não pode ser negativa.");
        }
        long horasEspera = Duration.between(tempoChegada, agora).toHours();
        horasEspera = Math.max(0, Math.min(horasEspera, MAX_HORAS_ESPERA));
        return PESO_URGENCIA * urgencia + (int) (taxaPaga / DIVISOR_TAXA) + (int) horasEspera;
    }
}
