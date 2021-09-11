package com.yudiz.beacondemo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Node {

    private String name;
    private double x;
    private double y;
    private int floor;
    private int vote;

    private LinkedList<Node> shortestPath = new LinkedList<Node>();

    private Integer distance;

    Map<Node, Integer> adjacentNodes = new HashMap<>();

    public Node(String name,int x,int y,int floor,int vote) {
        this.name = name;
        this.x=x;
        this.y=y;
        this.distance= Integer.MAX_VALUE;
        this.floor=floor;
        this.vote=vote;

    }

    public Node() {

    }


    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public void addDestination(Node destination) {
        adjacentNodes.put(destination, 1);
    }

    public double getX(){
        return this.x;
    }

    public void setX(double x){ this.x=x; }

    public double getY(){
        return this.y;
    }

    public void setY(double y){this.y=y;}

    public int getFloor(){return this.floor;}

    public void setFloor(int f){
        floor=f;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getVote() {
        return vote;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setDistance(int i) {
        distance=i;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public int getDistance() {
        return distance;
    }

    public void setShortestPath(LinkedList<Node> shortestPath) {
        this.shortestPath=shortestPath;
    }

    public LinkedList<Node> getShortestPath() {
        return shortestPath;
    }

}
