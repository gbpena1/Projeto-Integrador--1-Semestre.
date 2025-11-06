package view;

import Controller.FuncionarioController;
import Controller.RegistroController;
import models.RegistroPonto.TipoRegistro;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class MenuConsole {
    static Scanner scanner = new Scanner(System.in);

    public void exibirMenu() {
        String opcao;
        String menu;

        do {
            menu = " SISTEMA BANCO DE HORAS         ║";
            menu += "\n1. Cadastrar Funcionário";
            menu += "\n2. Listar Funcionários";
            menu += "\n3. Registrar Ponto";
            menu += "\n4. Consultar Saldo";
            menu += "\n5. Relatório Detalhado";
            menu += "\n0. Sair";
            menu += "\nEscolha uma opção: ";

            opcao = JOptionPane.showInputDialog(null, menu, "*** Menu Principal ***", JOptionPane.QUESTION_MESSAGE);

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
                    JOptionPane.showMessageDialog(null, "O sistema será encerrado!", "Sair do sistema", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.");
                    break;
            }

        } while (!"0".equals(opcao));
    }

    
    private static void cadastrarFuncionario() {
        String nome = JOptionPane.showInputDialog(null, "Digite o nome:");
        String matricula = JOptionPane.showInputDialog(null, "Digite a matrícula:");

        FuncionarioController.cadastrarFuncionario(nome, matricula);
    }

    
    private static void registrarPonto() {
        String matricula = JOptionPane.showInputDialog(null, "Digite a matrícula:");
        String dataStr = JOptionPane.showInputDialog(null, "Data (AAAA-MM-DD) ou deixe em branco para hoje:");

        LocalDate data;
        try {
            data = (dataStr == null || dataStr.trim().isEmpty()) ? LocalDate.now() : LocalDate.parse(dataStr.trim());
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Data inválida. Use o formato AAAA-MM-DD");
            return;
        }

        String tipoStr = JOptionPane.showInputDialog(null, "Tipo de registro:\n1 - NORMAL\n2 - COMPENSADA");
        int tipoOpcao;

        try {
            tipoOpcao = Integer.parseInt(tipoStr.trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Opção inválida. Digite apenas 1 ou 2.");
            return;
        }

        TipoRegistro tipo;
        if (tipoOpcao == 1) {
            tipo = TipoRegistro.NORMAL;
        } else if (tipoOpcao == 2) {
            tipo = TipoRegistro.COMPENSADA;
        } else {
            JOptionPane.showMessageDialog(null, "Opção inválida. Digite 1 ou 2.");
            return;
        }

        if (tipo == TipoRegistro.NORMAL) {
            try {
                String entradaStr = JOptionPane.showInputDialog(null, "Hora Entrada (HH:mm):");
                LocalTime entrada = LocalTime.parse(entradaStr.trim());

                String saidaStr = JOptionPane.showInputDialog(null, "Hora Saída (HH:mm):");
                LocalTime saida = LocalTime.parse(saidaStr.trim());

                RegistroController.registrarPonto(matricula, data, entrada, saida, tipo, 0);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(null, "Horário inválido. Use o formato HH:mm (ex: 08:00)");
            }
        } else {
            try {
                String horasStr = JOptionPane.showInputDialog(null, "Horas a compensar:");
                int horas = Integer.parseInt(horasStr.trim());

                RegistroController.registrarPonto(matricula, data, null, null, tipo, horas);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Valor inválido.");
            }
        }
    }


    private static void consultarSaldo() {
        String matricula = JOptionPane.showInputDialog(null, "Digite a matrícula:");

        if (FuncionarioController.buscarPorMatricula(matricula) == null) {
            JOptionPane.showMessageDialog(null, "Funcionário não encontrado.");
            return;
        }

        int saldoMinutos = RegistroController.consultarSaldoMinutos(matricula);
        String saldoFormatado = RegistroController.formatarSaldo(saldoMinutos);

        JOptionPane.showMessageDialog(null, "Saldo de horas: " + saldoFormatado);
    }

    
    private static void exibirRelatorioDetalhado() {
        String matricula = JOptionPane.showInputDialog(null, "Digite a matrícula:");
        RegistroController.exibirRelatorio(matricula);
    }
}