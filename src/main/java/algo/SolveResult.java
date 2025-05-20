package algo;

import object.Node;

public class SolveResult {
    public final Node solution;
    public final int nodesVisited;

    public SolveResult(Node solution, int nodesVisited) {
        this.solution     = solution;
        this.nodesVisited = nodesVisited;
    }
}
