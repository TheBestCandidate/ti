public enum FieldType {
    EMPTY(0, " "), X(1, "X"), O(2, "O");

    private final int number;
    private final String symbol;

    public int getNumber() {
        return number;
    }
    public String getSymbol() {
        return symbol;
    }

    FieldType(int number, String symbol) {
        this.number = number;
        this.symbol = symbol;
    }
    public static FieldType fieldTypeForSymbol(String symbol) throws WrongSymbolException {
        for(FieldType field : values()){
            if(field.symbol.equals(symbol.toUpperCase())){
                return field;
            }
        }
        throw new WrongSymbolException("Wybierz poprawny symbol (X/O), lub zostaw puste pole");
    }
}
