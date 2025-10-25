package Controller;
import models.Funcionario;
import java.util.ArrayList;

public class FuncionarioController {
	public static ArrayList<Funcionario> funcionarios = new ArrayList<>();

	public static boolean cadastrarFuncionario(String nome, String matricula) {
		// Verifica se já existe funcionário com essa matrícula
		if (buscarPorMatricula(matricula) != null) {
			System.out.println("ERRO: Já existe um funcionário cadastrado com a matrícula " + matricula);
			return false;
		}

		funcionarios.add(new Funcionario(nome, matricula));
		System.out.println("Funcionário cadastrado com sucesso!");
		return true;
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
		if (funcionarios.isEmpty()) {
			System.out.println("Nenhum funcionário cadastrado.");
			return;
		}

		System.out.println("\n=== FUNCIONÁRIOS CADASTRADOS ===");
		for (Funcionario f : funcionarios) {
			System.out.println("Nome: " + f.nome + " | Matrícula: " + f.matricula);
		}
	}
}
/*boolean no retorno: Agora o método diz se deu certo (true) ou errado (false)
Verificação de matrícula duplicada:

Antes de cadastrar ele busca se já existe alguém com essa matrícula
buscarPorMatricula(matricula) != null significa "encontrou alguém?"
Se encontrou ele mostra erro e não cadastra
Se não encontrou ele cadastra normalmente

Mensagens mais claras: "com sucesso" vs "ERRO"


Novo método listarFuncionarios()

isEmpty(): Verifica se a lista está vazia
for (Funcionario f : funcionarios): Percorre todos os funcionários (um por um)
Mostra cada um: Imprime nome e matrícula de cada funcionário
*/
