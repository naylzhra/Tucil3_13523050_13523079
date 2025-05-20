package algo;

import java.util.HashSet;
import java.util.Set;

import object.Board;
import object.Node;

public class IDS {
    public static Node solve(Board start) {
        int depthLimit = 0;
        while (true) {
            Set<String> visited = new HashSet<>();
            Node result = dls(new Node(start, null, null, 0, 0), depthLimit, visited);
            if (result != null) return result;
            depthLimit++;
        }
    }

    private static Node dls(Node current, int limit, Set<String> visited) {
        String hash = current.board.toString();
        if (visited.contains(hash)) return null;
        visited.add(hash);
        if (Helper.isGoal(current.board)) return current;
        if (limit == 0) return null;
        for (Node child : Helper.expand(current, false, false)) {
            Node result = dls(child, limit - 1, visited);
            if (result != null) return result;
        }
        return null;
    }
}
