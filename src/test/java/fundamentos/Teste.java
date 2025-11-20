package fundamentos;

import java.io.Console;

public class Teste {
    public static void main(String[] args) {

        // Linha 1
        int resultado;

        // Linha 2
        int a = 5;

        // Linha 3 (Chamada de Função)
        resultado = Soma(a, 10); // A função Soma(x, y) apenas retorna x + y

        // Linha 4
        WriteLine(resultado);


    }

    private static int Soma(int a, int i) {
        return a + i;
    }

    private static void WriteLine(Object obj) {
        System.out.println(obj);
    }

}