import algo.AStar;
import algo.GBFS;
import java.io.*;
import java.util.*;
import object.Board;
import object.Node;
import object.Piece;
import utils.Input;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("=========================================================================");
        System.out.println("                            RUSH HOUR SOLVER                             ");
        System.out.println("=========================================================================");
        System.out.println("        Mayla Yaffa Ludmilla (13523050) & Nayla Zahira (13523079)        ");
        System.out.println("-------------------------------------------------------------------------\n");
        System.err.println("Algoritma yang tersedia: \n1. Greedy Best First Search (GBFS)\n2. Uniform Cost Search (UCS)\n3. A*\n");


        Scanner sc = new Scanner(System.in);
        Board board = null;

        while (board == null) {
            System.out.print("Masukkan nama file input (misal: input1): ");
            String filename = sc.nextLine().trim();
            String path = "test/input/" + filename + ".txt";

            try {
                board = Input.readBoardFromFile(path);
            } catch (FileNotFoundException e) {
                System.out.println("File tidak ditemukan di: " + path);
                System.out.println("Silakan masukkan nama file yang benar.\n");
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
                System.out.println("Coba lagi.\n");
            }
        }

        String algorithm;
        while (true) {
            System.out.print("Masukkan algoritma yang ingin dipakai (GBFS/UCS/A*): ");
            algorithm = sc.nextLine().trim().toUpperCase();
            if (algorithm.equals("GBFS") || algorithm.equals("UCS") || algorithm.equals("A*")) {
                break;
            } else {
                System.out.println("Tidak ada algoritma " + algorithm + " yang tersedia. Pilih antara GBFS/UCS/A*.");
            }
        }

        System.out.println("\n-------------------------------------------------------------------------\n");
        System.out.println("Papan Awal:");
        for (int i = 0; i < board.height; i++) {
            for (int j = 0; j < board.width; j++) {
                System.out.print(board.grid[i][j]);
            }
            System.out.println();
        }
        //debugging
        cekValidasiBoardDanPiece(board);

        long t0, t1;
        Node goal;
        List<Node> path;
        switch(algorithm){
            case "GBFS":
                System.out.println("\n\nMenggunakan algoritma Greedy Best First Search (GBFS)");
                t0 = System.currentTimeMillis();
                goal = GBFS.solve(board);
                t1 = System.currentTimeMillis();
                if (goal == null) {
                    System.out.println("❌  Tidak ada solusi.");
                    break;
                }
                path = goal.getPath();
                System.out.println("\nSolved in " + (path.size()-1) + " moves, " + (t1 - t0) + " ms\n");
                printSteps(path);
                break;
            case "UCS":
                System.out.println("Menggunakan algoritma Uniform Cost Search (UCS)");
                break;
            case "A*":
                System.out.println("Menggunakan algoritma A*");System.out.println("\n\nMenggunakan algoritma Greedy Best First Search (GBFS)");
                t0 = System.currentTimeMillis();
                goal = AStar.solve(board);
                t1 = System.currentTimeMillis();
                if (goal == null) {
                    System.out.println("❌  Tidak ada solusi.");
                    break;
                }
                path = goal.getPath();
                System.out.println("\nSolved in " + (path.size()-1) + " moves, " + (t1 - t0) + " ms\n");
                printSteps(path);
                break;
        }
    }

    public static void printSteps(List<Node> path){
        for (int i = 0; i < path.size(); i++) {
            Node n = path.get(i);
            System.out.println("Step " + i + (n.move == null ? " (START)" : " : " + n.move));
            n.board.print();
            System.out.println();
        }
    } 

    // hasil gpt untuk debug
    public static void cekValidasiBoardDanPiece(Board board) {
    System.out.println("\n===== CEK ATRIBUT BOARD =====");
    System.out.println("Ukuran board (height x width): " + board.height + " x " + board.width);
    System.out.println("Posisi goal: (" + board.goalRow + ", " + board.goalCol + ")");
    System.out.println("Jumlah piece: " + board.pieces.size());

    // Print grid
    System.out.println("Isi grid:");
    for (int i = 0; i < board.height; i++) {
        for (int j = 0; j < board.width; j++) {
            System.out.print(board.grid[i][j]);
        }
        System.out.println();
    }

    System.out.println("\n===== CEK ATRIBUT PIECE =====");
    for (Piece p : board.pieces) {
        System.out.println("ID: " + p.id + ", Start: (" + p.row + "," + p.col + "), Panjang: " + p.length +
                           ", " + (p.isHorizontal ? "Horizontal" : "Vertikal") +
                           ", Primary: " + p.isPrimary);

        // Cek validasi posisi
        boolean outOfBounds = false;
        if (p.isHorizontal) {
            if (p.col < 0 || p.col + p.length > board.width || p.row < 0 || p.row >= board.height) {
                outOfBounds = true;
            }
        } else {
            if (p.row < 0 || p.row + p.length > board.height || p.col < 0 || p.col >= board.width) {
                outOfBounds = true;
            }
        }
        if (outOfBounds) {
            System.out.println("⚠️  WARNING: Piece " + p.id + " berada di luar batas board!");
        }
    }

    // Cek minimal satu primary block
    boolean adaPrimary = false;
    for (Piece p : board.pieces) {
        if (p.isPrimary) adaPrimary = true;
    }
    if (!adaPrimary) {
        System.out.println("⚠️  WARNING: Tidak ada blok utama (primary block) di board!");
    }

    // Cek grid kosong
    if (board.grid == null || board.grid.length == 0) {
        System.out.println("⚠️  WARNING: Grid board kosong!");
    }
}

}
