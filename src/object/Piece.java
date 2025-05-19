package object;
public class Piece {
    public char id;
    public int row;
    public int col;
    public int length;
    public boolean isHorizontal;
    public boolean isPrimary;

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";

    public Piece(char id, int row, int col, boolean isHorizontal, boolean isPrimary) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.length = 1;
        this.isHorizontal = isHorizontal;
        this.isPrimary = isPrimary;
    }

    public Piece(char id, int row, int col, int length, boolean isHorizontal, boolean isPrimary) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.length = length;
        this.isHorizontal = isHorizontal;
        this.isPrimary = isPrimary;
    }

    public char getId(){
        return this.id;
    }

    public void addLength(){
        this.length += 1; // aku mikirnya untuk input piece yg vertikal, setiap nemu lg char dgn id yang sudah ada, lengthnya ditambah
    }

    public Piece move(String direction) { // return piece karena untuk setiap state akan dibuat board baru, tp blm diskusi
        int newRow = this.row;
        int newCol = this.col;
        if (isHorizontal) {
            if (direction.equals("L")) newCol--;
            else if (direction.equals("R")) newCol++;
        } else {
            if (direction.equals("U")) newRow--;
            else if (direction.equals("D")) newRow++;
        }
        return new Piece(id, newRow, newCol, length, isHorizontal, isPrimary);
    }

    public void print() {
        String info = "Piece " + id +
                    " at (" + row + "," + col + "), len=" + length +
                    ", " + (isHorizontal ? "Horizontal" : "Vertical") +
                    (isPrimary ? ", Primary" : "");
        if (isPrimary) {
            System.out.println(RED + info + RESET);
        } else {
            System.out.println(info);
        }
    }
}
