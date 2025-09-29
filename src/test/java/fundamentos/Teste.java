package fundamentos;

public class Teste {
    public static void main(String[] args) {
        int[] values = {10, 30, 50};
        for (var val : values) {
            var x = 0;
            while (x < values.length) {
                System.out.println(x + " " + val);
                x++;
            }
        }
    }
}