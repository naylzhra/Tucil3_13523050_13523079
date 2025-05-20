package algo;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import object.Board;
import object.Node;

public class UCS {
    public static SolveResult solve(Board start, int mode) {
        PriorityQueue<Node> simpulHidup = new PriorityQueue<>(Comparator.comparingInt(node -> node.g));
        Set<String> visited = new HashSet<>();
        simpulHidup.add(new Node(start, null, null, 0, 0));
        while (!simpulHidup.isEmpty()) {
            Node current = simpulHidup.poll();
            String hash = current.board.toString();
            if (!visited.add(hash)) continue; 
            if (Helper.isGoal(current.board)) {
                return new SolveResult(current, visited.size());
            }
            for (Node child : Helper.expand(current, false, true, mode)) {
                simpulHidup.add(child);
            }
        }
        return new SolveResult(null, visited.size());
    }
}
