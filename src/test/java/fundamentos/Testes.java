package fundamentos;

public class Testes {
    byte b = 123;
    char c = '1';
    char[] c2 = {'a', 'b', 'c'};


    public static void main(String[] args) {
        Testes t = new Testes();

        char[] d = t.c2;
        t.c2 = null;

        try{
            for (char valor : t.c2) {
                System.out.println(valor);
            }
        } catch (NullPointerException e) {
            System.out.println("NullPointerException capturada!");
        }
    }
}