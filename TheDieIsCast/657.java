import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

public class Main {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static char[][] maze;
    private static Integer width;
    private static Integer heigth;

    private static Integer throwOrder;

    private static final char D = '.';
    private static final char X = 'X';
    private static final char A = '*';

    private static Boolean isInBounds(Integer x, Integer y) {
        return (x >= 0 && x < width && y >= 0 && y < heigth);
    }

    private static void removeX(Integer x, Integer y) {
        if (!isInBounds(x, y) || maze[y][x] != X) {
            return;
        }

        maze[y][x] = A;

        removeX(x + 1, y);  // Vpravo
        removeX(x, y + 1);  // Dolu
        removeX(x - 1, y);  // Vlevo
        removeX(x, y - 1);  // Nahoru
    }

    private static Integer goAsterisks(Integer x, Integer y, Integer amount) {
        if (!isInBounds(x, y) || maze[y][x] == D) {
            return amount;
        }

        if (maze[y][x] == X) {
            amount++;
            removeX(x, y);
        }

        maze[y][x] = D;

        amount = goAsterisks(x + 1, y, amount);
        amount = goAsterisks(x, y + 1, amount);
        amount = goAsterisks(x - 1, y, amount);
        amount = goAsterisks(x, y - 1, amount);

        return amount;
    }

    private static void printNumbers(ArrayList<Integer> diceNumbers) { // vypíše výsledky ve stoupajícím pořadí
        System.out.println("Throw "+throwOrder);
        Boolean isFirst = true;
        for (Integer i:diceNumbers) {
            if (!isFirst) {
                System.out.print(" ");
            }
            isFirst = false;
            System.out.print(i);
        }
        System.out.println();
    }

    private static void doStuff() {
        ArrayList<Integer> diceNumbers = new ArrayList<>();

        for (int y = 0; y < heigth; y++) {
            for (int x = 0; x < width; x++) {
                if (maze[y][x] == D) {
                    continue;
                }

                diceNumbers.add(goAsterisks(x, y, 0)); // zjisti kolik je na kostce
            }
        }

        diceNumbers.sort(Integer::compareTo);

        printNumbers(diceNumbers);
    }

    public static void main(String[] args) {
        String line;
        String[] splittedLine;

        throwOrder = 0;

        try {
            while (!(line = br.readLine()).equals("0 0")) {
                splittedLine = line.split("\\s");

                throwOrder++;

                width = Integer.parseInt(splittedLine[0]);
                heigth = Integer.parseInt(splittedLine[1]);

                maze = new char[heigth][width];

                for (int Y = 0; Y < heigth; Y++) {
                    maze[Y] = br.readLine().toCharArray();
                }

                doStuff();

                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
