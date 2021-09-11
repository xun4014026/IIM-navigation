package com.yudiz.beacondemo;

import java.util.HashSet;
import java.util.Set;

public class Graph {

    private Set<Node> nodes = new HashSet<>();

    public void addNode(Node node0) {
        nodes.add(node0);
    }

}