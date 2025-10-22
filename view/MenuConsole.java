package view;

import Controller.FuncionarioController;
import Controller.RegistroController;
import models.RegistroPonto.TipoRegistro;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class MenuConsole {
    static Scanner scanner = new Scanner(System.in);

    public static void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Cadastrar Funcionário");
            System.out.println("2. Registrar Ponto");
            System.out.println("3. Consultar Saldo");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> cadastrarFuncionario();
                case 2 -> registrarPonto();
                case 3 -> consultarSaldo();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void cadastrarFuncionario() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();
        FuncionarioController.cadastrarFuncionario(nome, matricula);
    }

    private static void registrarPonto() {
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Data (AAAA-MM-DD): ");
        LocalDate data = LocalDate.parse(scanner.nextLine());
        System.out.print("Tipo (NORMAL ou COMPENSADA): ");
        TipoRegistro tipo = TipoRegistro.valueOf(scanner.nextLine().toUpperCase());

        if (tipo == TipoRegistro.NORMAL) {
            System.out.print("Hora Entrada (HH:mm): ");
            LocalTime entrada = LocalTime.parse(scanner.nextLine());
            System.out.print("Hora Saída (HH:mm): ");
            LocalTime saida = LocalTime.parse(scanner.nextLine());
            RegistroController.registrarPonto(matricula, data, entrada, saida, tipo, 0);
        } else {
            System.out.print("Horas a abater: ");
            int horas = scanner.nextInt();
            scanner.nextLine();
            RegistroController.registrarPonto(matricula, data, null, null, tipo, horas);
        }
    }

    private static void consultarSaldo() {
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();
        int saldo = RegistroController.consultarSaldo(matricula);
        System.out.println("Saldo de horas: " + saldo);
    }
}