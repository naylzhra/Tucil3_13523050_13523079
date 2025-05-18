package object;
public class Node implements Comparable<Node> {
    public Board board;
    public Node parent;
    public String move;    
    public int g;          // g(n)
    public int h;          // h(n)
    public int f;          // g(n) + h(n)

    public Node(Board board, Node parent, String move, int g, int h) {
        this.board = board;
        this.parent = parent;
        this.move = move;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.f, other.f);
    }

    public void print() {
        System.out.println("=== NODE INFO ===");
        System.out.println("g(n): " + g + ", h(n): " + h + ", f(n): " + f);
        System.out.println("Move: " + (move != null ? move : "START"));
        System.out.println("Board:");
        board.print();
        System.out.println("=================");
    }


}
