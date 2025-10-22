package Controller;

import models.RegistroPonto;
import models.RegistroPonto.TipoRegistro;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class RegistroController {
    public static ArrayList<RegistroPonto> registros = new ArrayList<>();

    public static void registrarPonto(String matricula, LocalDate data, LocalTime entrada, LocalTime saida, TipoRegistro tipo, int horasCompensadas) {
        registros.add(new RegistroPonto(matricula, data, entrada, saida, tipo, horasCompensadas));
        System.out.println("Registro salvo.");
    }

    public static int consultarSaldo(String matricula) {
        int saldo = 0;
        for (RegistroPonto r : registros) {
            if (r.matricula.equals(matricula)) {
                saldo += r.calcularHoras();
            }
        }
        return saldo;
    }
}
