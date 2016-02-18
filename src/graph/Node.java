package graph;

import java.util.ArrayList;

public class Node {

    private ArrayList<Edge> adjacent;
    private int x,y;

    public Node(int x, int y){
        this.x = x;
        this.y = y;
    }

    private ArrayList<Edge> getAdjacent(){
        return adjacent;
    }
}
