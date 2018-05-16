import java.util.Scanner;

public class GameLogics {
    private static int moveCount = 0;
    private Player[] players = new Player[2];
    private Player playing;
    private Player waiting;
    private Board board;
    private Scanner input = new Scanner(System.in);
    private int boardSize;
    private int numberOfSymbolsInARow;

    public void setGame() {
        while (true) {
            try {
                System.out.println("Podaj rozmiar planszy: ");
                boardSize = Integer.parseInt(input.nextLine());
                if (boardSize <= 1) {
                    throw new NumberFormatException();
                }
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("Podana warość musi być liczbą dodatnią, większą od 1");
            }
        }

        board = new Board(boardSize);

        while (true) {
            try {
                System.out.println("Podaj minimalną ilość znaków w rzędzie potrzebną do wygrania: ");
                numberOfSymbolsInARow = Integer.parseInt(input.nextLine());
                if (numberOfSymbolsInARow < 1 || numberOfSymbolsInARow > boardSize) {
                    throw new NumberFormatException();
                }
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("Podana warość musi być liczbą dodatnią, z przediału 1-" + boardSize);
            }
        }

        System.out.println("Gracz 1 - podaj swoje imię: ");
        String name = input.nextLine();

        String symbol;
        FieldType fieldType;
        while (true) {
            try {
                System.out.println("Okay, a teraz wybierz kółko czy krzyżyk (X/O). Jeśli chcesz wybrać domyślny symbol naciśnij ENTER");
                symbol = input.nextLine();
                if (symbol.equals("")) {
                    symbol = "X";
                }
                fieldType = FieldType.fieldTypeForSymbol(symbol);
                break;
            } catch (WrongSymbolException wse) {
                System.out.println(wse.getMessage());
            }
        }

        players[0] = new Player(name, fieldType);
        String name2;
        while (true) {
            System.out.println("Gracz 2 - podaj swoje imię: ");
            name2 = input.nextLine();
            if (name2.equals(name)) {
                System.out.println("Imie dla Gracza 2, musi się różnić od imienia dla Gracza 1");
            } else {
                break;
            }
        }
        players[1] = fieldType.getSymbol().equals("X") ? new Player(name2, FieldType.O) : new Player(name2, FieldType.X);
    }

    public void playGame() {
        while (true) {
            if (moveCount % 2 == 0) {
                playing = players[0];
                waiting = players[1];
            } else {
                playing = players[1];
                waiting = players[0];
            }

            board.draw();
            System.out.println(playing.getName() + " podaj współrzędne pola, które chcesz oznaczyć (x/y): ");
            String coordinates = input.nextLine();
            String[] parameters = coordinates.split(" ");
            if (parameters.length != 2) {
                System.out.println("Niepoprawne parametry. Poadaj jeszcze raz w formacie (x/y)");
                continue;
            }
            int x;
            int y;
            try {
                x = Integer.parseInt(parameters[0]) - 1;
                y = Integer.parseInt(parameters[1]) - 1;
            } catch (NumberFormatException nfe) {
                System.out.println("Współrzędne muszą być liczbami!");
                continue;
            }
            if (x < 0 || x >= board.getFieldTypes()[0].length) {
                System.out.println("Współrzędna x, musi być z przedziału:1 - " + board.getFieldTypes()[0].length);
                continue;
            }
            if (y < 0 || y >= board.getFieldTypes().length) {
                System.out.println("Współrzędna y, musi być z przedziału:1 - " + board.getFieldTypes().length);
                continue;
            }

            try {
                board.markField(x, y, playing);
            } catch (MarkedFieldException e) {
                System.out.println(e.getMessage());
                continue;
            }
            moveCount++;
            if (checkWinningCondition()) {
                board.draw();
                System.out.println(playing.getName() + " wygrywa!");
                break;
            }
            if(checkTieCondition()){
                board.draw();
                System.out.println("Remis.");
                break;
            }
        }

    }
    private boolean checkTieCondition(){
        for (int x = 0; x < board.getFieldTypes()[0].length; x++) {
            for (int y = 0; y < board.getFieldTypes().length; y++) {
                if (board.getFieldTypes()[y][x].equals(FieldType.EMPTY)){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWinningCondition() {
        for (int x = 0; x < board.getFieldTypes()[0].length; x++) {
            for (int y = 0; y < board.getFieldTypes().length; y++) {
                if (board.getFieldTypes()[y][x].equals(playing.getPlayerSymbol())) {
                    try {
                        if (checkVerticalWinningCondition(x, y)) {
                            return true;
                        }
                    } catch (ArrayIndexOutOfBoundsException a) {}
                    try {
                        if (checkHorizontalWinningCondition(x, y)) {
                            return true;
                        }
                    } catch (ArrayIndexOutOfBoundsException a) {}
                    try {
                        if (checkFirstDiagonalWinningCondition(x, y)) {
                            return true;
                        }
                    } catch (ArrayIndexOutOfBoundsException a) {}
                    try {
                        if (checkSecondDiagonalWinningCondition(x, y)) {
                            return true;
                        }
                    } catch (ArrayIndexOutOfBoundsException a) {}
                }

            }

        }
        return false;
    }

    private boolean checkVerticalWinningCondition(int x, int y) {
        int counter = 0;
        for (int i = 0; i < numberOfSymbolsInARow; i++) {
            if (board.getFieldTypes()[y + i][x].equals(playing.getPlayerSymbol())) {
                counter++;
            }
        }
        return (counter==numberOfSymbolsInARow);
    }

    private boolean checkHorizontalWinningCondition(int x, int y) {
        int counter = 0;
        for (int i = 0; i < numberOfSymbolsInARow; i++) {
            if (board.getFieldTypes()[y][x + i].equals(playing.getPlayerSymbol())) {
                counter++;
            }
        }
        return (counter==numberOfSymbolsInARow);
    }

    private boolean checkFirstDiagonalWinningCondition(int x, int y) {
        int counter = 0;
        for (int i = 0; i < numberOfSymbolsInARow; i++) {
            if (board.getFieldTypes()[y + i][x + i].equals(playing.getPlayerSymbol())) {
                counter++;
            }
        }
        return (counter==numberOfSymbolsInARow);
    }
    private boolean checkSecondDiagonalWinningCondition(int x, int y) {
        int counter = 0;
        for (int i = 0; i < numberOfSymbolsInARow; i++) {
            if (board.getFieldTypes()[y - i][x + i].equals(playing.getPlayerSymbol())) {
                counter++;
            }
        }
        return (counter==numberOfSymbolsInARow);
    }
}

