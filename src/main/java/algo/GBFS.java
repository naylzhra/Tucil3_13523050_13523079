package algo;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import object.Board;
import object.Node;

public class GBFS {
    public static SolveResult solve(Board start) {
        PriorityQueue<Node> simpulHidup = new PriorityQueue<>(Comparator.comparingInt(node -> node.h));
        Set<String> visited = new HashSet<>();
        int h0 = Helper.blockingDistance(start);
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
            for (Node child : Helper.expand(current, true, false)) {
                simpulHidup.add(child);
            }
        }
        return new SolveResult(null, visited.size());
    }

}
