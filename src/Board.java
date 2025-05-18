import java.util.List;

public class Board {
    public char[][] grid;
    public List<Piece> pieces;
    public int width, height;
    public int goalRow, goalCol;
    public char exitDir;

    public Board(char[][] grid, List<Piece> pieces, int goalRow, int goalCol, char exitDir) {
        this.height = grid.length;
        this.width = grid[0].length;
        this.grid = grid;
        this.pieces = pieces;
        this.goalRow = goalRow;
        this.goalCol = goalCol;
        this.exitDir = exitDir;
    }

}
