package algo;

import java.util.HashSet;
import java.util.Set;

import object.Board;
import object.Node;

public class IDS {
    public static SolveResult solve(Board start, int mode) {
        int depthLimit = 0;
        int visitedCount = 0;
        while (true) {
            Set<String> visited = new HashSet<>();
            Node result = dls(new Node(start, null, null, 0, 0), depthLimit, visited, mode);
            if (result != null) return new SolveResult(result, visitedCount);
            depthLimit++;
            visitedCount += visited.size();
        }
    }

    private static Node dls(Node current, int limit, Set<String> visited, int mode) {
        String hash = current.board.toString();
        if (visited.contains(hash)) return null;
        visited.add(hash);
        if (Helper.isGoal(current.board)) return current;
        if (limit == 0) return null;
        for (Node child : Helper.expand(current, false, false, mode)) {
            Node result = dls(child, limit - 1, visited, mode);
            if (result != null) return result;
        }
        return null;
    }
}
