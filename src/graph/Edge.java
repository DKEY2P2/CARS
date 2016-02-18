package graph;

import java.util.ArrayList;

public class Edge {

    private Node n1,n2;
    private ArrayList<Node> an = new ArrayList<Node>();

    public Edge(Node n1, Node n2){
        this.n1 = n1;
        this.n2 = n2;
        an.add(n1);
        an.add(n2);
    }

    private Node getNode1(){
        return n1;
    }

    private Node getNode2(){
        return n2;
    }

    private ArrayList<Node> getIncident(){
        return an;
    }
}
