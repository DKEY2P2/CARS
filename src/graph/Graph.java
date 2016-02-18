package graph;

import java.util.ArrayList;

public class Graph {

    private ArrayList<Edge> ae = new ArrayList<Edge>();
    private ArrayList<Node> an = new ArrayList<Node>();

    public Graph(ArrayList<Edge> ae, ArrayList<Node> an){
        this.ae = ae;
        this.an = an;
    }

    private boolean adjacent(Node n1, Node n2){

    }

    private ArrayList<Node> neighbours(Node n){

    }

    private Node addNode(Node n){
        an.add(n);
        return n;
    }

    private Node removeNode(Node n){
        for(Node node:an)
            if(node == n)

    }

    private Edge addEdge(){

    }

    private Edge removeEdge(){

    }

}
