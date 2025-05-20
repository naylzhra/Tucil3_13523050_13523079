package algo;

import java.util.ArrayList;
import java.util.List;
import object.Board;
import object.Node;
import object.Piece;;

public final class Helper {

    public static int heuristic(Board b, int mode){
        switch(mode){
            case 0:
                return blockingDistance(b);
            case 1:
                return blockingPlusPlus(b);
            case 2:
                return distanceOnly(b);
            default:
                return distanceOnly(b);
        }
    }
    private static int blockingDistance(Board b) {
        Piece p = b.getPrimaryPiece();
        if (p == null) return 0;

        int dist = distanceOnly(b);
        int blockers = countDirectBlockers(b, p);
        return dist + blockers;
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

    public static List<Node> expand(Node n, boolean useHeuristic, boolean useG, int mode) {
        List<Node> childs = new ArrayList<>();
        Board b = n.board;

        for (Piece p : b.pieces) {
            if (p.isHorizontal) {
                generate(p, "L", n, b, childs, useHeuristic, useG, mode);
                generate(p, "R", n, b, childs, useHeuristic, useG, mode);
            } else {
                generate(p, "U", n, b, childs, useHeuristic, useG, mode);
                generate(p, "D", n, b, childs, useHeuristic, useG, mode);
            }
        }
        return childs;
    }

    private static void generate(Piece p, String dir, Node current, Board board, List<Node> out, boolean heuristic, boolean useG, int mode) {
        Board next = board;
        int step = 0;
        Piece currentPiece = p;
        while (next.canMove(currentPiece, dir)) {
            step++;
            next = next.applyMove(currentPiece, dir);
            int h = heuristic ? Helper.heuristic(next, mode) : 0;
            String mv = p.id + " " + dir + " " + step;
            if (useG) out.add(new Node(next, current, mv, current.g + 1, h));
            else out.add(new Node(next, current, mv, 0, h));
            currentPiece = next.pieces.stream().filter(pc -> pc.id == p.id).findFirst().get();
        }
    }

    private static int countDirectBlockers(Board b, Piece p) {
        return directBlockerPieces(b, p).size();
    }

    private static int distanceOnly(Board b) {
        Piece p = b.getPrimaryPiece();
        if (p == null) return 0;

        if (p.isHorizontal) {
            return Math.abs(b.getGoalCol() - (b.exitDir == 'L' ? p.col : p.col + p.length - 1));
        } else {
            return Math.abs(b.getGoalRow() - (b.exitDir == 'U' ? p.row : p.row + p.length - 1));
        }
    }

    private static List<Piece> directBlockerPieces(Board b, Piece p) {
        List<Piece> list = new ArrayList<>();
        if (p.isHorizontal) {
            int r = p.row;
            if (b.exitDir == 'L') {
                for (int c = p.col - 1; c >= 0; c--) {
                    char id = b.grid[r][c];
                    if (id != '.') {
                        list.add(findPieceById(b, id));
                    }
                }
            } else {
                for (int c = p.col + p.length; c < b.width; c++) {
                    char id = b.grid[r][c];
                    if (id != '.') {
                        list.add(findPieceById(b, id));
                    }
                }
            }
        } else {
            int c = p.col;
            if (b.exitDir == 'U') {
                for (int r = p.row - 1; r >= 0; r--) {
                    char id = b.grid[r][c];
                    if (id != '.') {
                        list.add(findPieceById(b, id));
                    }
                }
            } else {
                for (int r = p.row + p.length; r < b.height; r++) {
                    char id = b.grid[r][c];
                    if (id != '.') {
                        list.add(findPieceById(b, id));
                    }
                }
            }
        }
        return list;
    }

    private static Piece findPieceById(Board b, char id) {
        for (Piece pc : b.pieces) {
            if (pc.id == id) return pc;
        }
        return null;
    }

    private static int blockingPlusPlus(Board b) {
        Piece p = b.getPrimaryPiece();
        if (p == null) return 0;

        int dist      = distanceOnly(b);
        int blockers  = countDirectBlockers(b, p);
        int secBlocks = 0;

        for (Piece q : directBlockerPieces(b, p)) {
            if (q.isHorizontal) {
                boolean leftFree  = q.col > 0 && b.grid[q.row][q.col-1] == '.';
                boolean rightFree = q.col + q.length < b.width && b.grid[q.row][q.col+q.length] == '.';
                if (!leftFree && !rightFree) secBlocks++;
            } else {
                boolean upFree   = q.row > 0 && b.grid[q.row-1][q.col] == '.';
                boolean downFree = q.row + q.length < b.height && b.grid[q.row+q.length][q.col] == '.';
                if (!upFree && !downFree) secBlocks++;
            }
        }
        return dist + blockers + secBlocks;
    }
}