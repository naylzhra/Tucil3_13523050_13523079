package object;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    public char[][] grid;
    public List<Piece> pieces;
    public int width, height;
    public int goalRow, goalCol;
    public char exitDir;

    public static final char EMPTY = '.';

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
        // System.out.println("Pieces:");
        // for (Piece p : pieces) {
        //     p.print();
        // }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(height * (width + 1));
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                char ch = grid[r][c];
                sb.append(ch == EMPTY ? EMPTY : ch);
            }
            sb.append('/');                   // pemisah baris
        }
        sb.append(exitDir);                   // agar beda sisi exit beda hash
        return sb.toString();
    }

    @Override public int hashCode() { return toString().hashCode(); }
    @Override public boolean equals(Object o){
        return (o instanceof Board b) && this.toString().equals(b.toString());
    }

    public Board applyMove(Piece p, String dir) {
        char[][] newGrid = new char[height][width];
        for (int i = 0; i < height; i++) {
            newGrid[i] = Arrays.copyOf(grid[i], width);
        }

        List<Piece> newPieces = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece.id == p.id) {
                Piece moved = piece.move(dir);  
                newPieces.add(moved);

                for (int i = 0; i < piece.length; i++) {
                    if (piece.isHorizontal) {
                        newGrid[piece.row][piece.col + i] = EMPTY;
                    } else {
                        newGrid[piece.row + i][piece.col] = EMPTY;
                    }
                }

                for (int i = 0; i < moved.length; i++) {
                    if (moved.isHorizontal) {
                        newGrid[moved.row][moved.col + i] = moved.id;
                    } else {
                        newGrid[moved.row + i][moved.col] = moved.id;
                    }
                }
            } else {
                newPieces.add(new Piece(piece.id, piece.row, piece.col, piece.length, piece.isHorizontal, piece.isPrimary));
            }
        }

        return new Board(newGrid, newPieces, goalRow, goalCol, exitDir);
    }

    public boolean canMove(Piece p, String dir) {
        if (p.isHorizontal) {
            if (dir.equals("L")) {
                if (p.col - 1 >= 0 && grid[p.row][p.col - 1] == EMPTY) return true;
            } else if (dir.equals("R")) {
                if (p.col + p.length < width && grid[p.row][ p.col + p.length] == EMPTY) return true;
            }
        } else { 
            if (dir.equals("U")) {
                if (p.row - 1 >= 0 && grid[p.row - 1][p.col] == EMPTY) return true;
            } else if (dir.equals("D")) {
                if (p.row + p.length < height && grid[p.row + p.length][p.col] == EMPTY) return true;
            }
        }
        return false;
    }

    public Piece getPrimaryPiece() {
        for (Piece p : pieces) {
            if (p.isPrimary) {
                return p;
            }
        }
        return null;
    }
    public int getGoalRow() {
        return goalRow;
    }

    public int getGoalCol() {
        return goalCol;
    }
}
