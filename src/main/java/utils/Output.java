package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import algo.SolveResult;
import object.Board;
import object.Node;

public class Output {
    public static void write(String filepath, SolveResult result) throws IOException {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(filepath))) {
            Node goal = result.solution;
            int visited = result.nodesVisited;

            w.write("===== RUSH HOUR SOLUTION =====");
            w.newLine();
            if (goal == null) {
                w.write("No solution found.");
                w.newLine();
                w.write("Nodes visited: " + visited);
                w.newLine();
                return;
            }

            List<Node> path = goal.getPath();
            int moves = path.size() - 1;

            w.write("Total moves   : " + moves);
            w.newLine();
            w.write("Nodes visited : " + visited);
            w.newLine();
            w.write("------------------------------");
            w.newLine();

            for (int i = 0; i < path.size(); i++) {
                Node n = path.get(i);
                if (n.move == null) {
                    w.write("Papan awal: ");
                } else {
                    String moveDesc = (n.move);
                    w.write("Gerakan " + i + " : " + moveDesc);
                }
                
                w.newLine();

                Board b = n.board;
                for (int r = 0; r < b.height; r++) {
                    char[] rowArr = b.grid[r].clone(); // buat kalau mau timpa pakai 'K'
                    if (r == b.goalRow && b.goalCol >= 0 && b.goalCol < b.width) {
                        rowArr[b.goalCol] = 'K';
                    }
                    w.write(new String(b.grid[r]));
                    w.newLine();
                }
                w.write("------------------------------");
                w.newLine();
            }
        }
    }
}
