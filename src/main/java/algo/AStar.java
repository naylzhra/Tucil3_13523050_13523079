package algo;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import object.Board;
import object.Node;

public class AStar {
    public static SolveResult solve(Board start, int mode) {
        //List<Object> result = new ArrayList<Object>();
        PriorityQueue<Node> simpulHidup = new PriorityQueue<>(Comparator.comparingInt(node -> node.f()));
        Set<String> visited = new HashSet<>();
        int h0 = Helper.heuristic(start, mode);
        simpulHidup.add(new Node(start, null, null, 0, h0));
        while (!simpulHidup.isEmpty()) {
            Node current = simpulHidup.poll();
            String hash = current.board.toString();
            if (visited.contains(hash)) {
                continue;
            }
            visited.add(hash);
            if (Helper.isGoal(current.board)) {
                return new SolveResult(current, visited.size());
            }
            for (Node child : Helper.expand(current, true, true, mode)) {
                simpulHidup.add(child);
            }
        }
        return new SolveResult(null, visited.size());
    }
}
