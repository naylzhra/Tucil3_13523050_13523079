package object;

import java.util.LinkedList;
import java.util.List;

public class Node implements Comparable<Node> {
    public Board board;
    public Node parent;
    public String move;    
    public int g;          // g(n)
    public int h;          // h(n)

    public Node(Board board, Node parent, String move, int g, int h) {
        this.board = board;
        this.parent = parent;
        this.move = move;
        this.g = g;
        this.h = h;
    }

    public int f() {
        return g + h;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.f(), other.f());
    }

    public List<Node> getPath(){
        LinkedList<Node> list = new LinkedList<>();
        for(Node n = this; n != null; n = n.parent) list.addFirst(n);
        return list;
    }
   
    public void print() {
        System.out.println("=== NODE INFO ===");
        System.out.println("g(n): " + g + ", h(n): " + h + ", f(n): " + f());
        System.out.println("Move: " + (move != null ? move : "START"));
        System.out.println("Board:");
        board.print();
        System.out.println("=================");
    }


}
