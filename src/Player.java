public class Player {
    private String name;
    private final FieldType playerSymbol;

    public Player(String name, FieldType playerSymbol) {
        this.name = name;
        this.playerSymbol = playerSymbol;
    }

    public FieldType getPlayerSymbol() {
        return playerSymbol;
    }
    public String getName(){
        return name;
    }
}
