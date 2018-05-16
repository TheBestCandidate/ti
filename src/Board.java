public class Board {
    private int boardSize;
    private FieldType[][] fieldTypes;

    public Board(int boardSize) {
        createBoard(boardSize);
    }

    private void createBoard(int boardSize) {
        this.boardSize=boardSize;
        fieldTypes = new FieldType[boardSize][boardSize];
        for (int i = 0; i < fieldTypes[0].length; i++) {
            for (int j = 0; j < fieldTypes.length; j++){
                fieldTypes[i][j]=FieldType.EMPTY;
            }
        }
    }

    public int getBoardSize() {
        return boardSize;
    }

    public FieldType[][] getFieldTypes() {
        return fieldTypes;
    }

    public void markField(int x, int y, Player player) throws MarkedFieldException{
        if(fieldTypes[y][x]==FieldType.EMPTY){
            fieldTypes[y][x]=player.getPlayerSymbol();
        }
        else{
            throw new MarkedFieldException("Wybierz nie zaznaczone pole!");
        }
    }

    public void draw(){
        System.out.println();
        for (FieldType[] row : fieldTypes) {
            for (FieldType cell : row) {
                System.out.print("|" + cell.getSymbol());
            }
            System.out.print("|\n");
        }
    }

}
