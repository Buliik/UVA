import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static final Integer MONTHS = 13;                    // pocet mesicu v roce (bez NEWT a OVERFLOW)
    public static final Integer DAYS = 28;                      // pocet dni v mesici

    public static final Integer NEWT_INDEX = 13;                // index N mesice
    public static final Integer OVERFLOW_INDEX = 14;            // index O mesice

    public static final Integer NEWT_DAY_YEAR_ORDER = 365;      // kolikaty je den v roce v mesici N
    public static final Integer OVERFLOW_DAY_YEAR_ORDER = 366;  // kolikaty je den v roce v mesici O

    public static final Integer CYCLE_DAY_LENGTH = 1029983;     // Delka (ve dnech) celeho 2820 denniho cyklu

    public static Integer[][] callendarDays;                    // Staticky ulozene hodnoty vypocitane z kalendare

    private static void fillCallendar() {                       // naplni callendarDays hodnotami
        Integer month = 0;
        Integer day = 0;
        int iterator = 0;

        while (callendarDays[month][day] == null) {
            callendarDays[month++][day++] = ++iterator;
            month %= MONTHS;
            day %= DAYS;
        }
    }

    private static void doStuff(MyDate date1, MyDate date2) {
        // obsahuje pocet celych cyklu mezi temito daty
        Integer allCyclesBetweenDates = (date2.getStartYearOfThatCycle() - date1.getStartYearOfNextCycle()) / MyDate.CYCLE_LENGTH;

        Integer date1DaysToEndOfCycle = date1.getDaysCountFromEndOfThatCycle();             // v obrazku hodnota Z
        Integer date2DaysFromBeginOfCycle = date2.getDaysCountFromBeginningOfThatCycle();   // v obrazku hodnota X

        if (allCyclesBetweenDates < 0) {            // pokud jsou v jednom cyklu
            System.out.println(date1DaysToEndOfCycle + date2DaysFromBeginOfCycle - CYCLE_DAY_LENGTH);
        } else if (allCyclesBetweenDates == 0) {    // pokud jsou v cyklu bezprostredne nasledujicim
            System.out.println(date1DaysToEndOfCycle + date2DaysFromBeginOfCycle);
        } else {                                    // pro ostatni pocet cyklu mezi daty
            System.out.println(date1DaysToEndOfCycle + date2DaysFromBeginOfCycle + allCyclesBetweenDates * CYCLE_DAY_LENGTH);
        }
    }

    public static void main(String[] args) {
        callendarDays = new Integer[MONTHS][DAYS];
        fillCallendar();

        String line;
        String[] splittedLine;
        MyDate date1;
        MyDate date2;

        try {
            while ((line = br.readLine()) != null) {
                splittedLine = line.split("\\s"); // rozdeli vstup na dve casti pomoci mezery

                if (splittedLine.length <= 1) {
                    System.out.println("eh?");
                    continue;
                }

                date1 = new MyDate(splittedLine[0]);
                date2 = new MyDate(splittedLine[splittedLine.length - 1]);

                if (!date1.isValid() || !date2.isValid()) {
                    System.out.println("eh?");
                    continue;
                }

                if (date1.compareTo(date2) < 0) { // Prohodi data tak, aby prvni bylo nizsi
                    doStuff(date2, date1);
                } else {
                    doStuff(date1, date2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class MyDate implements Comparable<MyDate> {
    private Integer day;            // den
    private Integer month;          // mesic
    private Integer year;           // rok

    private Integer yearInCycle;    // pocitadlo roku v cyklu
    private Integer whatOf;         // hodnota whatOf pro tento rok

    public static final Integer CYCLE_LENGTH = 2820;            // DELKA cyklu prestupnych let
    private static final Integer OVERFLOW_YEARS_COUNT = 683;    // pocet overflow let v jednom cyklu
    private static final Integer MAX_YEAR = 2000000;            // maximalni rok pro overovani

    private static final Integer DAY = 0;   // indexy do poli
    private static final Integer MONTH = 1;
    private static final Integer YEAR = 2;

    private static final Integer ERROR = -1;    // error hodnota vyuzivana i pro vypocet zvolena -1
    private static final String[] S_MONTHS = {  // seznam nazvu mesicu
            "alligator",
            "bog",
            "crayfish",
            "damp",
            "eel",
            "fen",
            "gumbo",
            "hurricane",
            "inundation",
            "jaguar",
            "kudzu",
            "lake",
            "marsh",
            "newt",
            "overflow"
    };

    public MyDate(String date) {
        String[] splitted = date.split("-");

        yearInCycle = null;
        whatOf = null;

        if (splitted.length != 3) { // overeni validity zadani data
            this.day = ERROR;
            this.month = ERROR;
            this.year = ERROR;
            return;
        }

        // rozparsovani jednotlivych hodnot
        parseMonth(splitted[MONTH]);
        parseYear(splitted[YEAR]);
        parseDay(splitted[DAY]);
    }

    // po parsovani se da zjistit, jestli je datum validni
    public Boolean isValid() {
        return !(day.equals(ERROR) ||
                month.equals(ERROR) ||
                year.equals(ERROR));
    }

    // rozparsuje rok
    private void parseYear(String year) {
        this.year = Integer.parseInt(year);

        if (this.year < 0 || this.year >= MAX_YEAR) {
            this.year = ERROR;
        }
    }

    // rozparsuje mesic
    private void parseMonth(String month) // returns number of month based on input string
    {
        month = month.toLowerCase();

        for (int i = 0; i < S_MONTHS.length; i++) {
            if (S_MONTHS[i].indexOf(month) == 0) {
                this.month = i;
                return;
            }
        }

        this.month = ERROR;
    }

    // rozparsuje den
    private void parseDay(String day) {
        Integer retVal = Integer.parseInt(day);

        if (this.month.equals(ERROR) || this.year.equals(ERROR)) {
            this.day = ERROR;
        } else if (!this.month.equals(Main.OVERFLOW_INDEX) && !this.month.equals(Main.NEWT_INDEX)) {
            this.day = (retVal >= Main.DAYS || retVal < 0) ? ERROR : retVal;
        } else if (this.month.equals(Main.NEWT_INDEX)) {
            this.day = (retVal == 0) ? retVal : ERROR;
        } else if (retVal >= OVERFLOW_YEARS_COUNT || retVal < 0 || !retVal.equals(this.getWhatOf())) {
            this.day = ERROR;
        } else {
            this.day = 0;
        }
    }

    // pouze vypocita kolikaty je tento rok rokem v danem cyklu. tedy ze rok 2821 je 1 rokem v danem cyklu
    private Integer countYearInCycle(Integer year) {
        return year % CYCLE_LENGTH;
    }

    public Integer countWhatOf(Integer year) { // vrátí číslo dne pro daný overflow měsíc v rámci cyklu nebo -1 pokud rok není přestupný
        Integer x = this.countYearInCycle(year);
        if ((OVERFLOW_YEARS_COUNT * x) % CYCLE_LENGTH < OVERFLOW_YEARS_COUNT) {
            return ((OVERFLOW_YEARS_COUNT * x) / CYCLE_LENGTH) % OVERFLOW_YEARS_COUNT;
        } else {
            return ERROR;
        }
    }

    @Override
    public int compareTo(MyDate myDate) {
        int yearDifference = (myDate.year - this.year);
        Integer dayInYearMyDate = (myDate.month.equals(Main.NEWT_INDEX) ? Main.NEWT_DAY_YEAR_ORDER : (myDate.month.equals(Main.OVERFLOW_INDEX) ? Main.OVERFLOW_DAY_YEAR_ORDER : Main.callendarDays[myDate.month][myDate.day]));
        Integer dayInYearThis = (this.month.equals(Main.NEWT_INDEX) ? Main.NEWT_DAY_YEAR_ORDER : (this.month.equals(Main.OVERFLOW_INDEX) ? Main.OVERFLOW_DAY_YEAR_ORDER : Main.callendarDays[this.month][this.day]));

        if (yearDifference != 0) {
            return yearDifference;
        }

        return dayInYearMyDate - dayInYearThis;
    }

    public Integer getYearInCycle() {
        if (yearInCycle == null) {
            yearInCycle = this.countYearInCycle(this.year);
        }

        return yearInCycle;
    }

    public Integer getWhatOf() {
        if (this.whatOf == null) {
            this.whatOf = countWhatOf(this.year);
        }

        return this.whatOf;
    }

    // zjisti, ktery rok je rokem, kdy zacina nasledujici cyklus.
    public Integer getStartYearOfNextCycle() {
        return this.getStartYearOfThatCycle() + CYCLE_LENGTH;
    }

    // zjisti ktery rok je pocatecnim rokem tohoto cyklu.
    public Integer getStartYearOfThatCycle() {
        return this.year - this.getYearInCycle();
    }

    // zjisti pocet dni, kolik jich zbyva do konce tohoto cyklu
    public Integer getDaysCountFromEndOfThatCycle() {
        return Main.CYCLE_DAY_LENGTH - this.getDaysCountFromBeginningOfThatCycle();
    }

    // vypocte kolikaty je toto den v aktualnim cyklu.
    public Integer getDaysCountFromBeginningOfThatCycle() {
        Integer yearDay;

        if (this.month.equals(Main.NEWT_INDEX)) {
            yearDay = Main.NEWT_DAY_YEAR_ORDER;
        } else if (this.month.equals(Main.OVERFLOW_INDEX)) {
            yearDay = Main.OVERFLOW_DAY_YEAR_ORDER;
        } else {
            yearDay = Main.callendarDays[this.month][this.day];
        }

        Integer numberOfOverflowYears = ERROR;
        Integer actYear = this.getYearInCycle() - 1;
        while (numberOfOverflowYears.equals(ERROR) && this.getYearInCycle() != 0) {
            numberOfOverflowYears = this.countWhatOf(actYear--);
        }

        return this.getYearInCycle() * 365 + numberOfOverflowYears + yearDay;
    }
}












