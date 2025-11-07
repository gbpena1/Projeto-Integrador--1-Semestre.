package Controller;
import models.RegistroPonto;
import models.RegistroPonto.TipoRegistro;
import models.Funcionario;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class RegistroController {
	public static ArrayList<RegistroPonto> registros = new ArrayList<>();

	public static boolean registrarPonto(String matricula, LocalDate data, LocalTime entrada, LocalTime saida, TipoRegistro tipo, int horasCompensadas) {
		// Verifica se o funcionário existe
		Funcionario funcionario = FuncionarioController.buscarPorMatricula(matricula);
		if (funcionario == null) {
			JOptionPane.showMessageDialog(null,"ERRO: Funcionário com matrícula " + matricula + " não encontrado.");
			JOptionPane.showMessageDialog(null,"Cadastre o funcionário antes de registrar ponto.");
			return false;
		}

		// Validações para registro NORMAL
		if (tipo == TipoRegistro.NORMAL) { 
			if (entrada == null || saida == null) {
				JOptionPane.showMessageDialog(null, ("ERRO: Horário de entrada e saída são obrigatórios para ponto normal."));
				return false;
			}

			if (saida.isBefore(entrada) || saida.equals(entrada)) {
				JOptionPane.showMessageDialog(null, "\"ERRO: Horário de saída deve ser posterior ao horário de entrada.\"");
				return false;
			}
		}

		// Validações para registro COMPENSADO
		if (tipo == TipoRegistro.COMPENSADA) {
			if (horasCompensadas <= 0) {
				JOptionPane.showMessageDialog(null,"ERRO: Horas compensadas deve ser maior que zero.");
				return false;
			}
		}

		registros.add(new RegistroPonto(matricula, data, entrada, saida, tipo, horasCompensadas));
		JOptionPane.showMessageDialog(null, "Registro de Ponto salvo com sucesso!");
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
				saldo += r.calcularMinutos();
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
			JOptionPane.showMessageDialog(null, "Funcionário não encontrado.");
			return;
		}
		// 1. Inicializa o construtor de strings (StringBuilder)
		StringBuilder relatorio = new StringBuilder();

		// 2. Adiciona o cabeçalho do relatório com quebras de linha (\n)
    	relatorio.append(" Relatório Banco De Horas\n\n");
    	relatorio.append(" Nome: ").append(funcionario.nome).append("\n");
    	relatorio.append(" Matrícula: ").append(funcionario.matricula).append("\n\n");
    	relatorio.append("--------------------------------------------------\n");
    	relatorio.append(" Registros Detalhados:\n");
    	relatorio.append("--------------------------------------------------\n");

		boolean temRegistros = false;

		// 3. Itera sobre os registros e adiciona cada linha ao StringBuilder
		for (RegistroPonto r : registros) {
			if (r.matricula.equals(matricula)) {
				temRegistros = true;
		
		// Inicia a linha com a data
        relatorio.append("Data: ").append(r.data).append(" | ");

if (r.tipo == TipoRegistro.NORMAL) {
                int minutos = r.calcularMinutos();
                relatorio.append("Entrada: ").append(r.horaEntrada)
                         .append(" | Saída: ").append(r.horaSaida)
                         .append(" | Trabalhadas: ").append(formatarSaldo(minutos));
            } else { // TipoRegistro.COMPENSADA
                relatorio.append("COMPENSAÇÃO: ")
                         .append(r.horasCompensadas).append("h abatidas");
            }
            // Adiciona uma quebra de linha após cada registro completo
            relatorio.append("\n");
        }
    }

    if (!temRegistros) {
        relatorio.append("Nenhum registro de ponto encontrado.\n");
    }
// 4. Adiciona o saldo total (Rodapé)
    int saldoMinutos = consultarSaldoMinutos(matricula);
    relatorio.append("--------------------------------------------------\n");
    relatorio.append("--- SALDO TOTAL: ").append(formatarSaldo(saldoMinutos)).append(" ---\n");
    relatorio.append("--------------------------------------------------");

    // 5. Exibe a ÚNICA janela de mensagem com o relatório completo!
    JOptionPane.showMessageDialog(null, relatorio.toString());
}
}
