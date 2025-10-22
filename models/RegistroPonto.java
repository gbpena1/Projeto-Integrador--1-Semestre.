package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class RegistroPonto {
    public String matricula;
    public LocalDate data;
    public LocalTime horaEntrada;
    public LocalTime horaSaida;
    public TipoRegistro tipo;
    public int horasCompensadas;

    public RegistroPonto(String matricula, LocalDate data, LocalTime entrada, LocalTime saida, TipoRegistro tipo, int horasCompensadas) {
        this.matricula = matricula;
        this.data = data;
        this.horaEntrada = entrada;
        this.horaSaida = saida;
        this.tipo = tipo;
        this.horasCompensadas = horasCompensadas;
    }

    public int calcularHoras() {
        if (tipo == TipoRegistro.NORMAL) {
            return horaSaida.getHour() - horaEntrada.getHour();
        } else {
            return -horasCompensadas;
        }
    }

    public enum TipoRegistro {
        NORMAL,
        COMPENSADA
    }
}