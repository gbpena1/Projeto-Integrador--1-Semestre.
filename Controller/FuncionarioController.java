package Controller;

import models.Funcionario;
import java.util.ArrayList;

public class FuncionarioController {
    public static ArrayList<Funcionario> funcionarios = new ArrayList<>();

    public static void cadastrarFuncionario(String nome, String matricula) {
        funcionarios.add(new Funcionario(nome, matricula));
        System.out.println("Funcionário cadastrado.");
    }

    public static Funcionario buscarPorMatricula(String matricula) {
        for (Funcionario f : funcionarios) {
            if (f.matricula.equals(matricula)) {
                return f;
            }
        }
        return null;
    }
}