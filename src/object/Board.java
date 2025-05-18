package object;
package object;
import java.util.List;

public class Board {
    public char[][] grid;
    public List<Piece> pieces;
    public int width, height;
    public int goalRow, goalCol;
    public char exitDir;

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";

    public Board(char[][] grid, List<Piece> pieces, int goalRow, int goalCol, char exitDir) {
        this.height = grid.length;
        this.width = grid[0].length;
        this.grid = grid;
        this.pieces = pieces;
        this.goalRow = goalRow;
        this.goalCol = goalCol;
        this.exitDir = exitDir;
        this.exitDir = exitDir;
    }

    public void print() {
        System.out.println("Board (" + height + " x " + width + "), Goal at (" + goalRow + ", " + goalCol + ")");
        System.out.println("Grid:");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char c = grid[i][j];
                boolean isPrimary = false;
                // Find if c is a primary piece (by id)
                for (Piece p : pieces) {
                    if (p.id == c && p.isPrimary) {
                        isPrimary = true;
                        break;
                    }
                }
                if (isPrimary) {
                    System.out.print(RED + c + RESET);
                } else {
                    System.out.print(c);
                } 
            }
            System.out.println();
        }
        System.out.println("Pieces:");
        for (Piece p : pieces) {
            p.print();
        }
    }


}
