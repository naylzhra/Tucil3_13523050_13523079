package algo;

import java.util.ArrayList;
import java.util.List;
import object.Board;
import object.Node;
import object.Piece;;

public final class Helper {
    public static int blockingDistance(Board b) {
        Piece p = b.getPrimaryPiece();                
        if (p == null) return 0;

        int distance = 0, blockers = 0;
        if (p.isHorizontal) {
            distance += b.getGoalCol() - (p.col + p.length);
            for (int j = p.col + p.length; j < b.width; j++) {
                char c = b.grid[p.row][j];
                if (c != '\0') blockers++;
            }
        } else {
            distance += b.getGoalRow() - (p.row + p.length);
            for (int r = p.row + p.length; r < b.height; r++) {
                char occ = b.grid[r][p.col];
                if (occ != '\0') blockers++;
            }
        }
        return distance + blockers;
    }

    public static boolean isGoal(Board b) {
        Piece p = b.getPrimaryPiece();
        if (p == null) return false;
        switch (b.exitDir) {
            case 'R':
                return p.isHorizontal && p.row == b.goalRow &&
                    p.col + p.length == b.goalCol;
            case 'L':
                return p.isHorizontal && p.row == b.goalRow &&
                    p.col == b.goalCol + 1;
            case 'D':
                return !p.isHorizontal && p.col == b.goalCol &&
                    p.row + p.length == b.goalRow;
            case 'U':
                return !p.isHorizontal && p.col == b.goalCol &&
                    p.row == b.goalRow + 1;
            default:
                return false;
        }
    }

    public static List<Node> expand(Node n, boolean useHeuristic, boolean useG) {
        List<Node> childs = new ArrayList<>();
        Board b = n.board;

        for (Piece p : b.pieces) {
            if (p.isHorizontal) {
                generate(p, "L", n, b, childs, useHeuristic, useG);
                generate(p, "R", n, b, childs, useHeuristic, useG);
            } else {
                generate(p, "U", n, b, childs, useHeuristic, useG);
                generate(p, "D", n, b, childs, useHeuristic, useG);
            }
        }
        return childs;
    }

    private static void generate(Piece p, String dir, Node current, Board board, List<Node> out, boolean heuristic, boolean useG) {
        Board next = board;
        int step = 0;
        Piece currentPiece = p;
        while (next.canMove(currentPiece, dir)) {
            step++;
            next = next.applyMove(currentPiece, dir);
            int h = heuristic ? Helper.blockingDistance(next) : 0;
            String mv = p.id + " " + dir + " " + step;
            if (useG) out.add(new Node(next, current, mv, current.g + 1, h));
            else out.add(new Node(next, current, mv, 0, h));
            currentPiece = next.pieces.stream().filter(pc -> pc.id == p.id).findFirst().get();
        }
    }
}