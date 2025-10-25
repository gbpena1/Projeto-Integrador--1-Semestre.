package view;
import Controller.FuncionarioController;
import Controller.RegistroController;
import models.RegistroPonto.TipoRegistro;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class MenuConsole {
	static Scanner scanner = new Scanner(System.in);

	public static void exibirMenu() {
		int opcao;
		do {
			System.out.println("\n╔═══════════════════════════════════╗");
			System.out.println("║    SISTEMA BANCO DE HORAS         ║");
			System.out.println("╚═══════════════════════════════════╝");
			System.out.println("1. Cadastrar Funcionário");
			System.out.println("2. Listar Funcionários");
			System.out.println("3. Registrar Ponto");
			System.out.println("4. Consultar Saldo");
			System.out.println("5. Relatório Detalhado");
			System.out.println("0. Sair");
			System.out.print("\nEscolha uma opção: ");

			try {
				opcao = scanner.nextInt();
				scanner.nextLine();

				switch (opcao) {
					case 1 -> cadastrarFuncionario();
					case 2 -> FuncionarioController.listarFuncionarios();
					case 3 -> registrarPonto();
					case 4 -> consultarSaldo();
					case 5 -> exibirRelatorioDetalhado();
					case 0 -> System.out.println("\nEncerrando sistema... Até logo!");
					default -> System.out.println("\n❌ Opção inválida. Tente novamente.");
				}
			} catch (Exception e) {
				System.out.println("\n❌ Entrada inválida. Digite apenas números.");
				scanner.nextLine(); // Limpa o buffer
				opcao = -1; // Continua o loop
			}
		} while (opcao != 0);

		scanner.close();
    }

	private static void cadastrarFuncionario() {
		System.out.println("\n--- CADASTRAR FUNCIONÁRIO ---");
		System.out.print("Nome: ");
		String nome = scanner.nextLine().trim();

		if (nome.isEmpty()) {
			System.out.println("❌ Nome não pode estar vazio.");
			return;
		}

		System.out.print("Matrícula: ");
		String matricula = scanner.nextLine().trim();

		if (matricula.isEmpty()) {
			System.out.println("❌ Matrícula não pode estar vazia.");
			return;
		}

		FuncionarioController.cadastrarFuncionario(nome, matricula);
	}

	private static void registrarPonto() {
		System.out.println("\n--- REGISTRAR PONTO ---");
		System.out.print("Matrícula: ");
		String matricula = scanner.nextLine().trim();
		// Colocando data automatica no padrão (AAAA-MM-DD)
		System.out.print("Data (AAAA-MM-DD) ou ENTER para hoje: ");
		String dataStr = scanner.nextLine().trim();
		LocalDate data;
		// Validação de data/hora
		try {
			data = dataStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dataStr);
		} catch (DateTimeParseException e) {
			System.out.println("❌ Data inválida. Use o formato AAAA-MM-DD");
			return;
		}

		System.out.print("Tipo (1-NORMAL ou 2-COMPENSADA): ");
		int tipoOpcao;
		try {
			tipoOpcao = scanner.nextInt();
			scanner.nextLine();
		} catch (Exception e) {
			System.out.println("❌ Opção inválida.");
			scanner.nextLine();
			return;
		}

		TipoRegistro tipo;
		if (tipoOpcao == 1) {
			tipo = TipoRegistro.NORMAL;
		} else if (tipoOpcao == 2) {
			tipo = TipoRegistro.COMPENSADA;
		} else {
			System.out.println("❌ Opção inválida. Digite 1 ou 2.");
			return;
		}

		if (tipo == TipoRegistro.NORMAL) {
			try {
				System.out.print("Hora Entrada (HH:mm): ");
				LocalTime entrada = LocalTime.parse(scanner.nextLine().trim());

				System.out.print("Hora Saída (HH:mm): ");
				LocalTime saida = LocalTime.parse(scanner.nextLine().trim());

				RegistroController.registrarPonto(matricula, data, entrada, saida, tipo, 0);
			} catch (DateTimeParseException e) {
				System.out.println("❌ Horário inválido. Use o formato HH:mm (ex: 08:00)");
			}
		} else {
			try {
				System.out.print("Horas a compensar: ");
				int horas = scanner.nextInt();
				scanner.nextLine();

				RegistroController.registrarPonto(matricula, data, null, null, tipo, horas);
			} catch (Exception e) {
				System.out.println("❌ Valor inválido.");
				scanner.nextLine();
			}
		}
	}

	private static void consultarSaldo() {
		System.out.println("\n--- CONSULTAR SALDO ---");
		System.out.print("Matrícula: ");
		String matricula = scanner.nextLine().trim();

		if (FuncionarioController.buscarPorMatricula(matricula) == null) {
			System.out.println("❌ Funcionário não encontrado.");
			return;
		}

		int saldoMinutos = RegistroController.consultarSaldoMinutos(matricula);
		String saldoFormatado = RegistroController.formatarSaldo(saldoMinutos);

		System.out.println("\n💰 Saldo de horas: " + saldoFormatado);
	}

	private static void exibirRelatorioDetalhado() {
		System.out.println("\n--- RELATÓRIO DETALHADO ---");
		System.out.print("Matrícula: ");
		String matricula = scanner.nextLine().trim();

		RegistroController.exibirRelatorio(matricula);
	}
}
/*
Tratamento de erros:
	try {
    opcao = scanner.nextInt();
    scanner.nextLine();
    // ... código ...
} catch (Exception e) {
    System.out.println("\n❌ Entrada inválida. Digite apenas números.");
    scanner.nextLine();
    opcao = -1;
}
	O que ele faz:
		try: Tenta executar o código
		catch: Se der erro, captura e não trava o programa
		scanner.nextLine(): Limpa o que foi digitado errado

		Antes: Se digitasse "abc" → programa TRAVAVA ❌
		Agora: Se digitar "abc" → mostra erro e continua ✅

Validações nos Cadastros:
	if (nome.isEmpty()) {
    System.out.println("❌ Nome não pode estar vazio.");
    return;
}
	O que ele faz:
		.trim(): Remove espaços extras
		.isEmpty(): Verifica se está vazio
		return: Sai do método sem fazer nada
		Impede: o cadastro de funcionário sem nome ou matrícula

 Data Automatica:
	Operador ternário: condição ? seVerdadeiro : seFalso
		Com ele consigo atribuir uma condição de uma variavel sem usar if/else

		Se digitou vazio (ENTER) → usa data de hoje (LocalDate.now())
		Se digitou algo → converte o texto pra data (LocalDate.parse())

		Antes: Tinha que digitar a data toda vez
		Agora: Aperta ENTER = usa hoje

Validação de Data/Hora
	O que faz:
		Se digitar data errada (ex: "99/99/9999") ele mostra erro ao invés de travar

Tipo de Registro
	if (tipoOpcao == 1) {
    tipo = TipoRegistro.NORMAL;
	} else if (tipoOpcao == 2) {
    tipo = TipoRegistro.COMPENSADA;
	}
	O que faz
		digita só o numero

 */
