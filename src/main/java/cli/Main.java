package cli;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import algo.AStar;
import algo.GBFS;
import algo.IDS;
import algo.SolveResult;
import algo.UCS;
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
        System.err.println("Algoritma yang tersedia: \n1. Greedy Best First Search (GBFS)\n2. Uniform Cost Search (UCS)\n3. A*\n4. IDS\n");


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
            System.out.print("Masukkan algoritma yang ingin dipakai (GBFS/UCS/A*/IDS): ");
            algorithm = sc.nextLine().trim().toUpperCase();
            if (algorithm.equals("GBFS") || algorithm.equals("UCS") || algorithm.equals("A*") || algorithm.equals("IDS")) {
                break;
            } else {
                System.out.println("Tidak ada algoritma " + algorithm + " yang tersedia. Pilih antara GBFS/UCS/A*.");
            }
        }

        int mode;
        while (true) {
            System.out.print("Masukkan heuristic yang ingin dipakai: \n1. Distance + Blocker\n2. Blocker + 2nd Blocker\n3. Distance Only\n");
            mode = Integer.parseInt(sc.nextLine());
            if (mode >= 1 && mode <= 3) {
                break;
            } else {
                System.out.println("Input tidak sesuai opsi pilihan yang tersedia (1-3)");
            }
        }
        

        System.out.println("\n-------------------------------------------------------------------------\n");
        System.out.println("Papan Awal:");
        for (int i = 0; i < board.height; i++) {
            for (int j = 0; j < board.width; j++) {
                if (i == board.goalRow && j == board.goalCol) {
                System.out.print('K'); 
            } else {
                System.out.print(board.grid[i][j]);
            }
            }
            System.out.println();
        }

        long t0, t1;
        SolveResult goal = null;
        List<Node> path;
        t0 = System.currentTimeMillis();
        switch(algorithm){
            case "GBFS":
                System.out.println("\n\nMenggunakan algoritma Greedy Best First Search (GBFS)");
                goal = GBFS.solve(board, mode -1);
                break;
            case "UCS":
                System.out.println("Menggunakan algoritma Uniform Cost Search (UCS)");
                goal = UCS.solve(board, mode -1);
                break;
            case "A*":
                System.out.println("Menggunakan algoritma A*");
                goal = AStar.solve(board, mode - 1);
                break;
            case "IDS":
                System.err.println("Menggunakan algoritma Iterative Deepening Search (IDS)");
                goal = IDS.solve(board, mode - 1);
                break;
        }
        t1 = System.currentTimeMillis();
        if (goal == null) {
            System.out.println("Tidak ada solusi.");
            return;
        }
        path = goal.solution.getPath();
        System.out.println("\nSolved in " + (path.size()-1) + " moves, " + (t1 - t0) + " ms, " + goal.nodesVisited + "nodes visites\n");
        printSteps(path);
    }

    public static void printSteps(List<Node> path) {
        Node prev = null;
        for (int i = 0; i < path.size(); i++) {
            Node cur = path.get(i);
            char id = (cur.move != null && !cur.move.isEmpty()) ? cur.move.charAt(0) : '\0';
            boolean[][] oldMask = null;
            if (prev != null && id != '\0') {
                Board prevBoard = prev.board;
                oldMask = new boolean[prevBoard.height][prevBoard.width];
                for (int r = 0; r < prevBoard.height; r++)
                    for (int c = 0; c < prevBoard.width; c++)
                        if (prevBoard.grid[r][c] == id) oldMask[r][c] = true;
            }

            System.out.println("Step " + i + (cur.move == null ? " (START)" : " : " + cur.move));
            boolean showExit = (i == path.size() - 1);
            cur.board.print(showExit, id, oldMask);  
            System.out.println();
            prev = cur;   
        }
    }
}
