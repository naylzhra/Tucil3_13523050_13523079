import java.io.*;
import java.util.*;

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


    }
}
