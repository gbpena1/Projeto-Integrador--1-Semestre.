package models;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

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
    /**
     * Calcula as horas trabalhadas em minutos
     * minutos trabalhados (positivo para NORMAL, negativo para COMPENSADA)
     */
	public int calcularMinutos() {
		if (tipo == TipoRegistro.NORMAL) {
			// Calcula a diferença entre saída e entrada em minutos
			Duration duracao = Duration.between(horaEntrada, horaSaida);
			return (int) duracao.toMinutes();
		} else {
			// Horas compensadas são abatidas do saldo (valor negativo)
			return -(horasCompensadas * 60); // Converte horas para minutos
		}
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
/*
Novo Método: calcularMinutos()
O que ele faz:
Para ponto NORMAL (trabalhou):

	Duration.between(): Calcula a diferença entre dois horários

	Exemplo: De 08:30 até 17:45

	.toMinutes(): Converte para minutos

	08:30 até 17:45 = 555 minutos (9h15min)


Para ponto COMPENSADO (tirou folga):

	-(horasCompensadas * 60): Converte horas em minutos e deixa negativo

	Exemplo: 2 horas compensadas = -120 minutos
	O sinal negativo significa que está "gastando" horas do banco
 */
