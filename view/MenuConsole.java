package view;

import Controller.FuncionarioController;
import Controller.RegistroController;
import models.RegistroPonto.TipoRegistro;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JOptionPane;

public class MenuConsole {

    public void exibirMenu() {
        String opcao;

        do {
            opcao = JOptionPane.showInputDialog(
                "=== Banco de Horas ===\n\n" +
                "1 - Cadastrar Funcionário\n" +
                "2 - Listar Funcionários\n" +
                "3 - Registrar Ponto\n" +
                "4 - Consultar Saldo\n" +
                "5 - Relatório Detalhado\n" +
                "0 - Sair\n\n" +
                "Escolha uma opção:"
            );

            if (opcao == null) {
                opcao = "0";
            }

            switch (opcao) {
                case "1":
                    cadastrarFuncionario();
                    break;
                case "2":
                    FuncionarioController.listarFuncionarios();
                    break;
                case "3":
                    registrarPonto();
                    break;
                case "4":
                    consultarSaldo();
                    break;
                case "5":
                    exibirRelatorioDetalhado();
                    break;
                case "0":
                    JOptionPane.showMessageDialog(null, "Sistema encerrado!");
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    break;
            }

        } while (true);
    }

    private static void cadastrarFuncionario() {
        String nome = JOptionPane.showInputDialog("Digite o nome do funcionário:");
        if (nome == null) return;

        String matricula = JOptionPane.showInputDialog("Digite a matrícula:");
        if (matricula == null) return;

        FuncionarioController.cadastrarFuncionario(nome, matricula);
    }

    private static void registrarPonto() {
        String matricula = JOptionPane.showInputDialog("Digite a matrícula do funcionário:");
        if (matricula == null) return;

        String dataStr = JOptionPane.showInputDialog("Digite a data (AAAA-MM-DD) ou deixe em branco para hoje:");
        if (dataStr == null) return;

        LocalDate data;
        if (dataStr.trim().isEmpty()) {
            data = LocalDate.now();
        } else {
            try {
                data = LocalDate.parse(dataStr);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Data inválida!");
                return;
            }
        }

        String[] opcoesTipo = {"NORMAL (Trabalhou)", "COMPENSADA (Tirar folga)"};
        int tipoOpcao = JOptionPane.showOptionDialog(
            null,
            "Selecione o tipo de registro:",
            "Tipo de Registro",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opcoesTipo,
            opcoesTipo[0]
        );

        if (tipoOpcao == -1) return;

        TipoRegistro tipo = (tipoOpcao == 0) ? TipoRegistro.NORMAL : TipoRegistro.COMPENSADA;

        if (tipo == TipoRegistro.NORMAL) {
            registrarPontoNormal(matricula, data);
        } else {
            registrarPontoCompensado(matricula, data);
        }
    }

    private static void registrarPontoNormal(String matricula, LocalDate data) {
        try {
            String entradaStr = JOptionPane.showInputDialog("Digite a hora de ENTRADA (HH:mm):");
            if (entradaStr == null) return;
            LocalTime entrada = LocalTime.parse(entradaStr);

            String saidaStr = JOptionPane.showInputDialog("Digite a hora de SAÍDA (HH:mm):");
            if (saidaStr == null) return;
            LocalTime saida = LocalTime.parse(saidaStr);

            RegistroController.registrarPonto(matricula, data, entrada, saida, TipoRegistro.NORMAL, 0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Horário inválido! Use o formato HH:mm");
        }
    }

    private static void registrarPontoCompensado(String matricula, LocalDate data) {
        try {
            String horasStr = JOptionPane.showInputDialog("Digite a quantidade de HORAS a compensar:");
            if (horasStr == null) return;
            int horas = Integer.parseInt(horasStr);

            RegistroController.registrarPonto(matricula, data, null, null, TipoRegistro.COMPENSADA, horas);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Valor inválido!");
        }
    }

    private static void consultarSaldo() {
        String matricula = JOptionPane.showInputDialog("Digite a matrícula do funcionário:");
        if (matricula == null) return;

        if (FuncionarioController.buscarPorMatricula(matricula) == null) {
            JOptionPane.showMessageDialog(null, "Funcionário não encontrado!");
            return;
        }

        int saldoMinutos = RegistroController.consultarSaldoMinutos(matricula);
        String saldoFormatado = RegistroController.formatarSaldo(saldoMinutos);

        JOptionPane.showMessageDialog(null, 
            "Matrícula: " + matricula + "\n" +
            "Saldo: " + saldoFormatado);
    }

    private static void exibirRelatorioDetalhado() {
        String matricula = JOptionPane.showInputDialog("Digite a matrícula do funcionário:");
        if (matricula == null) return;

        RegistroController.exibirRelatorio(matricula);
    }
}