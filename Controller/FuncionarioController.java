package Controller;
import models.Funcionario;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class FuncionarioController {
	public static ArrayList<Funcionario> funcionarios = new ArrayList<>();

	// public static boolean cadastrarFuncionario(String nome, String matricula) {
	// 	// Verifica se já existe funcionário com essa matrícula
	// 	if (buscarPorMatricula(matricula) != null) {
	// 		System.out.println("ERRO: Já existe um funcionário cadastrado com a matrícula " + matricula);
	// 		return false;
	// 	}

	// 	funcionarios.add(new Funcionario(nome, matricula));
	// 	System.out.println("Funcionário cadastrado com sucesso!");
	// 	return true;
	// }
	public static void cadastrarFuncionario(String nome, String matricula) {
    if (nome == null || nome.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Nome não pode ser vazio!");
        return;
    }
    if (matricula == null || matricula.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Matrícula não pode ser vazia!");
        return;
    }
    if (buscarPorMatricula(matricula) != null) {
    	JOptionPane.showMessageDialog(null, "Matrícula já existe!");
   		return;
}
funcionarios.add(new Funcionario(nome, matricula));
}

	public static Funcionario buscarPorMatricula(String matricula) {
		for (Funcionario f : funcionarios) {
			if (f.matricula.equals(matricula)) {
				return f;
			}
		}
		return null;
	}

public static void listarFuncionarios() {
    // 1. Verifica se a lista está vazia
    if (funcionarios.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Nenhum funcionário cadastrado.");
        return;
    }

    // 2. Cria um construtor de String (StringBuilder)
    StringBuilder listaFuncionarios = new StringBuilder();

    // 3. Adiciona o cabeçalho
    listaFuncionarios.append("=== FUNCIONÁRIOS CADASTRADOS ===\n\n");
    listaFuncionarios.append("--------------------------------------------------\n");

    // 4. Itera sobre a lista e adiciona cada funcionário com quebra de linha (\n)
    for (Funcionario f : funcionarios) {
        listaFuncionarios.append("Nome: ")
                         .append(f.nome)
                         .append(" | Matrícula: ")
                         .append(f.matricula)
                         .append("\n"); // Crucial: Quebra de linha após cada funcionário
    }
    
    listaFuncionarios.append("--------------------------------------------------");

    // 5. Exibe a ÚNICA janela de mensagem com a lista completa
    JOptionPane.showMessageDialog(null, listaFuncionarios.toString());
	}
}
