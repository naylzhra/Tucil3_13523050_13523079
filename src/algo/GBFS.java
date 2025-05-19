package algo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import object.Board;
import object.Node;
import object.Piece;

public class GBFS {
    public static Node solve(Board start) {
        PriorityQueue<Node> simpulHidup = new PriorityQueue<>(Comparator.comparingInt(node -> node.h));
        Set<String> visited = new HashSet<>();
        int h0 = Heuristic.blockingDistance(start);
        simpulHidup.add(new Node(start, null, null, 0, h0));
        while (!simpulHidup.isEmpty()) {
            Node current = simpulHidup.poll();
            String hash = current.board.toString();
            if (visited.contains(hash)) {
                continue;
            }
            visited.add(hash);
            if (isGoal(current.board)) {
                return current;
            }
            for (Node child : expand(current, true)) {
                simpulHidup.add(child);
            }
        }
        return null;
    }

    static boolean isGoal(Board b) {
        Piece p = b.getPrimaryPiece();
        if (p == null)
            return false;

        return switch (b.exitDir) {
            case 'R' -> p.isHorizontal && p.row == b.goalRow &&
                    p.col + p.length == b.goalCol;
            case 'L' -> p.isHorizontal && p.row == b.goalRow &&
                    p.col == b.goalCol + 1;
            case 'D' -> !p.isHorizontal && p.col == b.goalCol &&
                    p.row + p.length == b.goalRow;
            case 'U' -> !p.isHorizontal && p.col == b.goalCol &&
                    p.row == b.goalRow + 1;
            default -> false;
        };
    }

    static List<Node> expand(Node n, boolean useHeuristic) {
        List<Node> childs = new ArrayList<>();
        Board b = n.board;

        for (Piece p : b.pieces) {
            if (p.isHorizontal) {
                generate(p, "L", n, b, childs, useHeuristic);
                generate(p, "R", n, b, childs, useHeuristic);
            } else {
                generate(p, "U", n, b, childs, useHeuristic);
                generate(p, "D", n, b, childs, useHeuristic);
            }
        }
        return childs;
    }

    private static void generate(Piece p, String dir, Node current, Board board, List<Node> out, boolean heuristic) {
        Board next = board;
        int step = 0;
        Piece currentPiece = p;
        while (next.canMove(currentPiece, dir)) {
            step++;
            next = next.applyMove(currentPiece, dir);
            int h = heuristic ? Heuristic.blockingDistance(next) : 0;
            String mv = p.id + " " + dir + " " + step;
            out.add(new Node(next, current, mv, current.g + step, h));
            currentPiece = next.pieces.stream().filter(pc -> pc.id == p.id).findFirst().get();
        }
    }
}
