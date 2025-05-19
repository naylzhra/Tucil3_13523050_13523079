package algo;

import object.Board;
import object.Piece;;

public final class Heuristic {
    public static int blockingDistance(Board b) {
        Piece p = b.getPrimaryPiece();                // tambahkan getter di Board
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
}