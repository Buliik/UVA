import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class Main {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private static final Integer maxCycle = (100 - 1) / 3 + 1;

    // pro kazdy ostrov (1, 2, 3) uchovava data, kde pri values[a][x][y][z];
    // a je cislo vrcholu
    // x je pocet, kolikrat jeste musime navstivit vrchol 1
    // y je pocet, kolikrat jeste musime navstivit vrchol 2
    // z je pocet, kolikrat jeste musime navstivit vrchol 3
    // Hodnota ulozena na tomto miste je bud NULL nebo pocet cest z tohoto vrcholu s timto nastavenim, kolika se da dostat k cili.
    // tedy vyuzit vsechny vrcholy a skoncit ve vrcholu s cislem 1
    private static BigInteger[][][][] values = new BigInteger[3][maxCycle][maxCycle][maxCycle];

    // pocita kolikaty je toto pripad
    private static Integer caseCounter = 0;

    /**
     * @param for1_2Or3 obsahuje cislo ostrova, ktery je aktualne navstiveny
     * @param ones      obsahuje, kolikrat jeste musime navstivit vrchol cislo 1
     * @param twos      obsahuje, kolikrat jeste musime navstivit vrchol cislo 2
     * @param threes    obsahuje, kolikrat jeste musime navstivit vrchol cislo 3
     * @return          Vrati cislo, kolik je moznych takovych cest, kterymi se dostaneme do cile.
     */
    private static BigInteger countField(Integer for1_2Or3,  Integer ones, Integer twos, Integer threes) {
        if (ones < 0 || twos < 0 || threes < 0) {   // pokud je kterykoli vrchol pod nulou, tak jsme ostrov navstivili vicekrat, nez jsme meli
            return BigInteger.ZERO;
        } else if (ones == 0 && twos == 0 && threes == 0) {
            if (for1_2Or3 == 1) {           // Navstiveny byly vsechny vrcholy, ale cesta neskoncila v 1
                return BigInteger.ONE;      // jinak, tedy pokud je aktualni vrchol 1 byla nalezena cesta.
            } else {
                return BigInteger.ZERO;
            }
        }

        Integer tmp = for1_2Or3 - 1;    // kvůli indexaci od 0...

        if (values[tmp][ones][twos][threes] == null) {
            switch (for1_2Or3) {
                case 1: // Pokud jsme ve vrcholu 1, tak muzeme jit jen do vrcholu 2 a 3 a tedy soucet techto hodnot je
                    // vysledek, tak zavolame toto pocitani pro vrchol 2 s ponizenym poctem nutnosti navstiveni vrcholu
                    // 2 a pripoctem to same pro vrchol 3
                    values[tmp][ones][twos][threes] = countField(2, ones, twos - 1, threes).add(countField(3, ones, twos, threes - 1));
                    break;
                case 2:// totez pro vrchol 2
                    values[tmp][ones][twos][threes] = countField(1, ones - 1, twos, threes).add(countField(3, ones, twos, threes - 1));
                    break;
                case 3:// totez pro vrchol 3
                    values[tmp][ones][twos][threes] = countField(1, ones - 1, twos, threes).add(countField(2, ones, twos - 1, threes));
                    break;
            }
        }

        return values[tmp][ones][twos][threes];
    }

    /**
     * @param index Input from user
     */
    private static void doStuff(Integer index) {
        Integer count = (index - 1) / 3;

        BigInteger result = countField(1, count, count, count); // Vypočteme pro všechny možné cesty (i ty, které jsou inverzní)

        // pokud je index (tzn. vstup od uživatele) sudy, tak vime, ze tam nejsou zadne cesty, ktere se ctou zleva i zprava stejne.

        // pokud je ale index lichy, tedy na vstupu je liche cislo, tak uprostred je jen jedno cislo a tedy
        // je treba pripocitat jeste cesty, ktere jsou symetricke zleva a zprava toho docilime volanim pocitani
        // cest s parametrem (index+1)/2
        if (index % 2 != 0) {
            count = (index + 1) / 2 / 3;

            result = result.add(countField(1, count, count, count));
            // pripocteni cest, ktere jsou symetricke.
        }

        System.out.println("Case " + caseCounter + ": " + result.divide(BigInteger.valueOf(2)));
        //protoze jsme pocitali kazdou cestu i cestu inverzni je potreba sumu vydelit 2.

    public static void main(String[] args) {
        String line;
        Integer rows;
        Integer index;

        try {
            line = br.readLine();
            rows = Integer.parseInt(line);

            for (int i = 1; i <= rows; i++) {
                line = br.readLine();
                index = Integer.parseInt(line);

                caseCounter++;

                doStuff(index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
