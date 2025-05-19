package algo;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import object.Board;
import object.Node;

public class UCS {
    public static Node solve(Board start) {
        PriorityQueue<Node> simpulHidup = new PriorityQueue<>(Comparator.comparingInt(node -> node.g));
        Set<String> visited = new HashSet<>();
        simpulHidup.add(new Node(start, null, null, 0, 0));
        while (!simpulHidup.isEmpty()) {
            Node current = simpulHidup.poll();
            String hash = current.board.toString();
            if (!visited.add(current.board)) continue; 
            if (Helper.isGoal(current.board)) {
                return current;
            }
            for (Node child : Helper.expand(current, false, true)) {
                simpulHidup.add(child);
            }
        }
        return null;
    }
}
