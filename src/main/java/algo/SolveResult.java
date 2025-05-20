package algo;

import object.Node;

public class SolveResult {
    public final Node solution;
    public final int nodesVisited;
    public final long timeMs;

    public SolveResult(Node solution, int nodesVisited) {
        this.solution     = solution;
        this.nodesVisited = nodesVisited;
        this.timeMs       = 0;
    }

    public SolveResult(Node solution, int nodesVisited, long timeMs) {
        this.solution     = solution;
        this.nodesVisited = nodesVisited;
        this.timeMs       = timeMs;
    }
}
