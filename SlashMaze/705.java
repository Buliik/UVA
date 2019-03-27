import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static Integer width;
    private static Integer heigth;

    private static Field[][] maze;

    private static Integer cyclesCount;
    private static Integer longestCycle;

    private static Integer MazeCounter;

    private static Boolean checkCoordinates(Integer X, Integer Y) { // zjisti, jestli jsme uvnitr pole
        return !(X < 0 || Y < 0 || X >= width || Y >= heigth);
    }

    private static void disablePathNotRecursive(Integer X, Integer Y, Boolean up) { // zakaze celou cestu vedouci z pole.
        while (true) {
            maze[Y][X].disablePosition(up); // zakazeme dane pole

            // pokud z tohoto pole muzeme jit vpravo tzn je to / a jsme dole, nebo \ a jsme nahore, zaroven policko vpravo je uvnitr pole a
            // zaroven na pole vpravo muzeme prijit zleva tedy / a nahore je volno, nebo \ a dole je volno
            // tak provedeme presun na pole vpravo, takze zvysime X a to, jestli jsme nahore nebo dole zjistime takto:
            // pokud jsou lomitka stejna, tak se pozice zmeni. priklad:  // z leve dole jdeme do prave nahoru \\ z leve zhora jdeme do prave dolu
            // pokud jsou lomitka ruzna, zustavame na stejnem miste priklad: \/ z leve zhora jdeme do prave nahoru /\ obdobne...
            // v pripade, ze se presouvame vertikalne, vzdy se meni pozice, protoze jdeme zespoda nahoru, nebo zhora dolu
            if (maze[Y][X].canGoRight(up) && checkCoordinates(X + 1, Y) && maze[Y][X].canGoTOField(maze[Y][X + 1], 0)) {
                up = (maze[Y][X].type == maze[Y][X + 1].type ? !up : up);
                X++;
            } else if (maze[Y][X].canGoDown(up) && checkCoordinates(X, Y + 1) && maze[Y][X].canGoTOField(maze[Y + 1][X], 1)) {
                up = !up;
                Y++;
            } else if (maze[Y][X].canGoLeft(up) && checkCoordinates(X - 1, Y) && maze[Y][X].canGoTOField(maze[Y][X - 1], 2)) {
                up = (maze[Y][X].type == maze[Y][X - 1].type ? !up : up);
                X--;
            } else if (maze[Y][X].canGoUp(up) && checkCoordinates(X, Y - 1) && maze[Y][X].canGoTOField(maze[Y - 1][X], 3)) {
                up = !up;
                Y--;
            } else {
                // pokud nemam uz kam jit, tak jsem dosel nakonec a muzu vyskocit z cyklu
                break;
            }
        }
    }

    private static void doStuff() {
        // Pro kazde lomeno v horni rade zakazeme cestu, ktera se da dosahnout z horni pozice (ta konci na kraji pole, takze netvori cyklus)
        for (int i = 0; i < width; i++) {
            if (maze[0][i].isFree(true)) {
                disablePathNotRecursive(i, 0, true);
            }
        }

        // obdobne pro spodni radu a dolni pozici
        for (int i = 0; i < width; i++) {
            if (maze[heigth - 1][i].isFree(false)) {
                disablePathNotRecursive(i, heigth - 1, false);
            }
        }

        // obdobne pro levy sloupec
        for (int i = 0; i < heigth; i++) {
            if (maze[i][0].isFree(maze[i][0].type)) {
                disablePathNotRecursive(0, i, maze[i][0].type);
            }
        }

        // a pro pravy sloupec
        for (int i = 0; i < heigth; i++) {
            if (maze[i][width - 1].isFree(!maze[i][width - 1].type)) {
                disablePathNotRecursive(width - 1, i, !maze[i][width - 1].type);
            }
        }

        // Ted kdyz jsou zakazany vsechny cesty netvorici smycky
        for (int Y = 0; Y < heigth; Y++) {
            for (int X = 0; X < width; X++) {
                // pro kazde pole tedy overime horni i spodni cast a pokud tato cast je volna, to znamena, ze zasahuje
                // do cyklicke cesty a provedu zakazani teto cesty, abych ji nepocital dvakrat a zaroven spocitam, kolik
                // policek jsem prosel. (delka cyklu)
                if (maze[Y][X].isFree(true)) {
                    cyclesCount++;              // inkrementace poctu cyklu v bludisti
                    findPathNotRecursive(X, Y, true);
                }

                if (maze[Y][X].isFree(false)) {
                    cyclesCount++;
                    findPathNotRecursive(X, Y, false);
                }
            }
        }

        System.out.println("Maze #"+MazeCounter+":");

        if (cyclesCount == 0) {
            System.out.println("There are no cycles.");
        } else {
            System.out.println(cyclesCount+" Cycles; the longest has length "+longestCycle+".");
        }

        System.out.println();
    }

    private static void findPathNotRecursive(Integer X, Integer Y, Boolean up) {
        // funguje stejne jako disablePath funkce, jen navic pocita delku cyklu a neoveruje, jeslti jsme v poli,
        // protoze vime, ze kdyz jsme v cyklu, tak to je vzdy v poli.
        Integer actLength = 0;

        while (true) {
            maze[Y][X].disablePosition(up);
            actLength++;

            if (maze[Y][X].canGoRight(up) && maze[Y][X].canGoTOField(maze[Y][X + 1], 0)) {
                up = (maze[Y][X].type == maze[Y][X + 1].type ? !up : up);
                X++;
            } else if (maze[Y][X].canGoDown(up) && maze[Y][X].canGoTOField(maze[Y + 1][X], 1)) {
                up = !up;
                Y++;
            } else if (maze[Y][X].canGoLeft(up) && maze[Y][X].canGoTOField(maze[Y][X - 1], 2)) {
                up = (maze[Y][X].type == maze[Y][X - 1].type ? !up : up);
                X--;
            } else if (maze[Y][X].canGoUp(up) && maze[Y][X].canGoTOField(maze[Y - 1][X], 3)) {
                up = !up;
                Y--;
            } else {
                if (longestCycle < actLength) {
                    longestCycle = actLength;
                }

                break;
            }
        }
    }

    public static void main(String[] args) {
        String line;
        String[] splittedLine;
        char[] lineChars;
        MazeCounter = 0;

        try {
            while (!(line = br.readLine()).equals("0 0")) {
                splittedLine = line.split("\\s"); // rozdeli vstup na dve casti pomoci mezery

                MazeCounter++;

                width = Integer.parseInt(splittedLine[0]);
                heigth = Integer.parseInt(splittedLine[1]);

                maze = new Field[heigth][width];

                longestCycle = 0;
                cyclesCount = 0;

                for (int i = 0; i < heigth; i++) {
                    lineChars = br.readLine().toCharArray();
                    for (int j = 0; j < width; j++) {
                        maze[i][j] = new Field(lineChars[j] == '/');
                    }
                }

                doStuff();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Field {
    Boolean type;           //  0 is \ and 1 is /
    Boolean upValue;        //  false znamena, ze se neda stoupnout nahodu tedy do casti nad lomitkem, true znamena, ze se zde stoupnout da
    Boolean downValue;      // obdobne pro spodni cast pod lomitkem

    public Field(Boolean type) { // pouze nastavime typ a povolime obe cesty
        this.type = type;
        this.upValue = true;
        this.downValue = true;
    }

    public void disablePosition(Boolean up) { // zakazeme policko, na kterem stojime. takze vrch, nebo spodek up = true, znamena zakaz vrsek a obdobne pro false
        if (up) {
            upValue = false;
        } else {
            downValue = false;
        }
    }

    /**
     * @param direction Direction of income
     * @return TRUE if UP, FALSE if DOWN
     */
    public Boolean getIncomePosition(Integer direction) { // zjisti, podle smeru odkud jsme prisli, zdali jsme nad, nebo pod lomitkem TRUE = nad, FALSE = pod
        switch (direction) {    // Income direction
            case 0:             // From RIGHT
                return (!this.type);
            case 1:             // From DOWN
                return false;
            case 2:             // From LEFT
                return this.type;
            case 3:             // From UP
                return true;
        }

        return false;
    }

    public Boolean isFree(Boolean up) {
        return (up ? this.upValue : this.downValue);
    } // zjisti jestli je toto misto volne

    // nasledujici 4 funkce zjistuji, jestli muzeme opustit toto policko danym smerem.
    public Boolean canGoRight(Boolean up) {
        return  (!type && up) || (type && !up);
    }

    public Boolean canGoDown(Boolean up) {
        return  !up;
    }

    public Boolean canGoLeft(Boolean up) {
        return  (!type && !up) || (type && up);
    }

    public Boolean canGoUp(Boolean up) {
        return  up;
    }

    /**
     * @param field Field to go
     * @param direction Direction of leaving actual field: (0 = RIGHT, 1 = DOWN, 2 = LEFT, 3 = UP)
     */
    public Boolean canGoTOField(Field field, Integer direction) { // zjisti, jestli na policko "field" muzeme prijit z daneho smeru.
        Boolean incomePosition;

        switch (direction) {    // Leaving direction
            case 0:             // RIGHT
                incomePosition = field.getIncomePosition(2);
                return field.canGoLeft(incomePosition) && field.isFree(incomePosition);
            case 1:             // DOWN
                incomePosition = field.getIncomePosition(3);
                return field.canGoUp(incomePosition) && field.isFree(incomePosition);
            case 2:             // LEFT
                incomePosition = field.getIncomePosition(0);
                return field.canGoRight(incomePosition) && field.isFree(incomePosition);
            case 3:             // UP
                incomePosition = field.getIncomePosition(1);
                return field.canGoDown(incomePosition) && field.isFree(incomePosition);
        }

        return false;
    }
}












