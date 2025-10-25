package Controller;
import models.RegistroPonto;
import models.RegistroPonto.TipoRegistro;
import models.Funcionario;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class RegistroController {
	public static ArrayList<RegistroPonto> registros = new ArrayList<>();

	public static boolean registrarPonto(String matricula, LocalDate data, LocalTime entrada, LocalTime saida, TipoRegistro tipo, int horasCompensadas) {
		// Verifica se o funcionário existe
		Funcionario funcionario = FuncionarioController.buscarPorMatricula(matricula);
		if (funcionario == null) {
			System.out.println("ERRO: Funcionário com matrícula " + matricula + " não encontrado.");
			System.out.println("Cadastre o funcionário antes de registrar ponto.");
			return false;
		}

		// Validações para registro NORMAL
		if (tipo == TipoRegistro.NORMAL) {
			if (entrada == null || saida == null) {
				System.out.println("ERRO: Horário de entrada e saída são obrigatórios para ponto normal.");
				return false;
			}

			if (saida.isBefore(entrada) || saida.equals(entrada)) {
				System.out.println("ERRO: Horário de saída deve ser posterior ao horário de entrada.");
				return false;
			}
		}

		// Validações para registro COMPENSADO
		if (tipo == TipoRegistro.COMPENSADA) {
			if (horasCompensadas <= 0) {
				System.out.println("ERRO: Horas compensadas deve ser maior que zero.");
				return false;
			}
		}

		registros.add(new RegistroPonto(matricula, data, entrada, saida, tipo, horasCompensadas));
		System.out.println("Registro de ponto salvo com sucesso!");
		return true;
	}

	/**
	 * Consulta o saldo em minutos do funcionário
	 */
	public static int consultarSaldoMinutos(String matricula) {
		int saldoMinutos = 0;
		for (RegistroPonto r : registros) {
			if (r.matricula.equals(matricula)) {
				saldoMinutos += r.calcularMinutos();
			}
		}
		return saldoMinutos;
	}

	/**
	 * Consulta o saldo em horas (versão antiga mantida)
	 */
	public static int consultarSaldo(String matricula) {
		int saldo = 0;
		for (RegistroPonto r : registros) {
			if (r.matricula.equals(matricula)) {
				saldo += r.calcularHoras();
			}
		}
		return saldo;
	}

	/**
	 * Formata minutos para o formato HH:mm
	 */
	public static String formatarSaldo(int minutos) {
		boolean negativo = minutos < 0;
		minutos = Math.abs(minutos);

		int horas = minutos / 60;
		int mins = minutos % 60;

		String sinal = negativo ? "-" : "+";
		return String.format("%s%02d:%02d", sinal, horas, mins);
	}

	/**
	 * Exibe relatório detalhado de um funcionário
	 */
	public static void exibirRelatorio(String matricula) {
		Funcionario funcionario = FuncionarioController.buscarPorMatricula(matricula);
		if (funcionario == null) {
			System.out.println("Funcionário não encontrado.");
			return;
		}

		System.out.println("\n=== RELATÓRIO DE BANCO DE HORAS ===");
		System.out.println("Nome: " + funcionario.nome);
		System.out.println("Matrícula: " + funcionario.matricula);
		System.out.println("\nRegistros:");

		boolean temRegistros = false;
		for (RegistroPonto r : registros) {
			if (r.matricula.equals(matricula)) {
				temRegistros = true;
				System.out.print("Data: " + r.data + " | ");

				if (r.tipo == TipoRegistro.NORMAL) {
					int minutos = r.calcularMinutos();
					System.out.println("Entrada: " + r.horaEntrada + " | Saída: " + r.horaSaida + 
										" | Trabalhadas: " + formatarSaldo(minutos));
				} else {
					System.out.println("COMPENSAÇÃO: " + r.horasCompensadas + "h abatidas");
				}
			}
		}

		if (!temRegistros) {
			System.out.println("Nenhum registro encontrado.");
		}

		int saldoMinutos = consultarSaldoMinutos(matricula);
		System.out.println("\n--- SALDO TOTAL: " + formatarSaldo(saldoMinutos) + " ---");
	}
}
/*
 Validações no registrarPonto():
	O que ele faz:
		Busca o funcionário pela matrícula
		Se não encontrou (null = nada) ele mostra erro
		Impede o registro de ponto de alguém que não existe

if (saida.isBefore(entrada) || saida.equals(entrada)) {
System.out.println("ERRO: Horário de saída deve ser posterior ao horário de entrada.");
return false;
}
	O que ele faz:
		isBefore(): Verifica se saída é antes da entrada
		equals(): Verifica se são iguais
I		Empede o registro de saída antes da entrada Impede

Novo método: consultarSaldoMinutos()
	O que ele faz:

		Começa com saldo zero
		Percorre TODOS os registros de ponto
		Se o registro é dessa pessoa → soma os minutos
		Retorna o total em minutos
Exemplo:

	Segunda: +555 minutos (9h15min trabalhadas)
	Terça: +480 minutos (8h trabalhadas)
	Quarta: -120 minutos (2h compensadas)
	TOTAL: 915 minutos = 15h15min no banco

Novo método: formatarSaldo()
	O que ele faz:
		Transforma minutos em formato bonito HH:mm
			Passo a Passo:

				Math.abs(): Remove o sinal negativo (trabalha com valor absoluto)
				-135 minutos passa a ser 135 minutos
				minutos / 60: Divide por 60 = horas inteiras
				135 / 60 = 2 horas
				minutos % 60: Resto da divisão = minutos restantes
				135 % 60 = 15 minutos
				String.format("%s%02d:%02d"): Formata com zeros à esquerda
				%s = sinal (+ ou -)
				%02d = número com 2 dígitos (01, 02, 10, etc)

Novo Método: exibirRelatorio()
	O que ele faz:
		Deixa mais bonito
 */
