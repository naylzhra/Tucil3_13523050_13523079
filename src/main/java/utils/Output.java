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

            for (int step = 0; step < path.size(); step++) {
                Node n = path.get(step);
                if (n.move == null) {
                    w.write("Papan awal:");
                } else {
                    w.write("Gerakan " + step + " : " + n.move);
                }
                w.newLine();

                Board b = n.board;

                if (b.exitDir == 'U') {
                    for (int c = 0; c < b.width; c++) {
                        w.write(c == b.goalCol ? 'K' : ' ');
                    }
                    w.newLine();
                }

                for (int r = 0; r < b.height; r++) {
                    if (b.exitDir == 'L') {
                        w.write(r == b.goalRow ? 'K' : ' ');
                    }
                    w.write(new String(b.grid[r]));
                    if (b.exitDir == 'R') {
                        w.write(r == b.goalRow ? 'K' : ' ');
                    }
                    w.newLine();
                }

                if (b.exitDir == 'D') {
                    for (int c = 0; c < b.width; c++) {
                        w.write(c == b.goalCol ? 'K' : ' ');
                    }
                    w.newLine();
                }

                w.write("------------------------------");
                w.newLine();
            }
        }
    }
}
