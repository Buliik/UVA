import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

public class Main {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static Integer N;
    private static String result;

    private static void doStuff() {
        Integer r = 0; // vzdálenost v počtu hexagonů od středu
        Integer k = 1;

        while(N > k) { // rtý obvod má r*6 políček
            r++;
            k += r*6; // poslední bunka v obvodu obsahujícím hledanou bunku
        }

        Integer x = r;
        Integer y = 0;

        if (k.equals(N)) {
            result = x + " " + y;
            return;
        }

        // následně se proti směru hodinových ručiček prochází po obvodu dokud se nenalezne hledaná bunka

        while (x != -y) {   // sever
            y--;
            k--;
            if (k.equals(N)) {
                result = x + " " + y;
                return;
            }
        }

        while (x != 0) {    // severozápad
            x--;
            k--;
            if (k.equals(N)) {
                result = x + " " + y;
                return;
            }
        }

        while (y != 0) {    // jihozápad
            x--;
            y++;
            k--;
            if (k.equals(N)) {
                result = x + " " + y;
                return;
            }
        }

        while (-x != y) {   // jih
            y++;
            k--;
            if (k.equals(N)) {
                result = x + " " + y;
                return;
            }
        }

        while (x != 0) {    // jihovýchod
            x++;
            k--;
            if (k.equals(N)) {
                result = x + " " + y;
                return;
            }
        }

        while (y != 1) {    // severovýchod
            x++;
            y--;
            k--;
            if (k.equals(N)) {
                result = x + " " + y;
                return;
            }
        }
    }

    public static void main(String[] args) {
        String line;
        String[] splittedLine;

        try {
            while ((line = br.readLine()) != null) {
                N = Integer.parseInt(line);

                if (N == 1) { //střed je speciální případ, není třeba počítat.
                    System.out.println("0 0");
                    continue;
                }

                doStuff();

                System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
