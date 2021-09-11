package com.yudiz.beacondemo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.yudiz.beacondemo.MainActivity.positionDB;
import static com.yudiz.beacondemo.NavigationView.navigation_destinationNode;


public  class Dijkstra  {

    private Node dijkstra_node;

    private static final String TAG = "DIJKSTRA";

    /*
    private static Node nodeA = new Node("1F轉1",35,74,1);
    private static Node nodeB = new Node("61117",5,76,1);
    private static Node nodeC = new Node("61112",35,79,1);
    private static Node nodeD = new Node("1F樓1",38,74,1);
    private static Node nodeE = new Node("1F樓2",8,82,1);
    private static Node nodeF = new Node("1F轉2",11,79,1);
    private static Node nodeG = new Node("1F轉3",11,82,1);
    private static Node nodeH = new Node("1F轉4",5,79,1);
    private static Node nodeI = new Node("小圓桌",29,74,1);
    private static Node nodeJ = new Node("61108",35,69,1);
    private static Node nodeK = new Node("61211",11,70,2);
    private static Node nodeL = new Node("2F樓1",38,64,2);
    private static Node nodeM = new Node("2F樓2",8,74,2);
    private static Node nodeN = new Node("2F轉1",11,73,2);
    private static Node nodeO = new Node("2F轉2",35,65,2);
    private static Node nodeP = new Node("61214",35,70,2);

*/


    public static Node node[];
    private static float x_s=0;
    private static float x_d=0;
    private static float y_s=0;
    private static float y_d=0;
    protected static int point_num;
    protected static int connect_num;


    public  Graph calculateShortestPathFromSource(Graph graph, Node source,Node destination) {

        source.setDistance(0);
        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);
        while (unsettledNodes.size() != 0) {

            //取unsettledNodes中最近的點出來作為currentNode
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);

            Iterator<java.util.Map.Entry<Node, Integer>> iterator = currentNode.getAdjacentNodes().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Node, Integer> adjacencyPair = (Map.Entry<Node, Integer>) iterator.next();
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();

                if(settledNodes.contains(destination)){
                    break;
                }

                if ((!settledNodes.contains(adjacentNode))) {
                    Log.d(TAG, "testPath1: "+currentNode.getName()+" testPath2: "+adjacentNode.getName());
                    unsettledNodes.remove(adjacentNode);
                    CalculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }


            }
            settledNodes.add(currentNode);
            Log.d(TAG, "calculateShortestPathFromSource:~~~~ "+printPath(source,currentNode));
            if(currentNode.getName().equals(destination.getName())){
                dijkstra_node=currentNode;
            }
        }
        Log.d(TAG, "calculateShortestPathFromSource:~~~~ "+printPath(source,dijkstra_node));
        return graph;
    }

    private static Node getLowestDistanceNode(Set < Node > unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }


    private static void CalculateMinimumDistance(Node evaluationNode,int edgeWeigh, Node sourceNode) {
        int sourceDistance = sourceNode.getDistance();
        if ((sourceDistance + edgeWeigh) <= evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<Node>(sourceNode.getShortestPath());
            Iterator<Node> iterator=shortestPath.iterator();
            //Log.d(TAG, "printPath: first :  "+iterator.next().getName());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    protected String printPath(Node sourceNode, Node destinationNode) {

        String path="shortest path : ";
        //Log.d(TAG, "printPath: ~~"+destinationNode.getName());
        LinkedList<Node> list=new LinkedList<Node>(destinationNode.getShortestPath());
        Iterator<Node> iterator=list.iterator();


        while(iterator.hasNext()){
            Node n=iterator.next();
            path=path+n.getName()+" >> ";
            Log.d(TAG, "printPath: ~~"+path);

        }

        path = path + destinationNode.getName();



        return path;

    }

    private void setRelation(){
        //set the relationship between nodes
        //nodeB is key. 10 is value.

        LinkedList<Node> nulllist=new LinkedList<Node>();

        Cursor cursor = positionDB.connectQueryAll();
        connect_num=cursor.getCount();
        cursor.moveToFirst();

        //Log.d(TAG, "setRelation: ~~"+connect_num);


        for (int i=0;i<point_num;i++){
            if(!(cursor.getString(5).equals("-1"))) {
                for(int j=0;j<point_num;j++){
                    if(node[j].getName().equals(cursor.getString(5))){
                        node[i].addDestination(node[j]);
                    }
                    if(node[j].getName().equals(cursor.getString(4))){
                        node[i].addDestination(node[j]);
                    }
                    if(node[j].getName().equals(cursor.getString(3))){
                        node[i].addDestination(node[j]);
                    }
                    if(node[j].getName().equals(cursor.getString(2))){
                        node[i].addDestination(node[j]);
                    }

                }

            }else if(!(cursor.getString(4).equals("-1"))){
                for(int j=0;j<point_num;j++){
                    if(node[j].getName().equals(cursor.getString(4))){
                        node[i].addDestination(node[j]);
                    }
                    if(node[j].getName().equals(cursor.getString(3))){
                        node[i].addDestination(node[j]);
                    }
                    if(node[j].getName().equals(cursor.getString(2))){
                        node[i].addDestination(node[j]);
                    }

                }

            }else if(!(cursor.getString(3).equals("-1"))){
                for(int j=0;j<point_num;j++){
                    if(node[j].getName().equals(cursor.getString(3))){
                        node[i].addDestination(node[j]);
                    }
                    if(node[j].getName().equals(cursor.getString(2))){
                        node[i].addDestination(node[j]);
                    }

                }

            }else if(!(cursor.getString(2).equals("-1"))){
                for(int j=0;j<point_num;j++){

                    if(node[j].getName().equals(cursor.getString(2))){
                        node[i].addDestination(node[j]);
                    }

                }

            }
            cursor.moveToNext();
            node[i].setDistance(Integer.MAX_VALUE);
            node[i].setShortestPath(nulllist);
        }




    }

    private Graph setGraph(){
        //construct the graph
        Graph graph = new Graph();
        for(int i=0;i<point_num;i++){

            graph.addNode(node[i]);
        }

        return graph;

    }

    protected String setPath(Node sourceNode, Node destinationNode){

        Log.d(TAG, "yydijkstraPath"+destinationNode.getX()+","+destinationNode.getY());
        setRelation();



        //Dijkstra計算
        calculateShortestPathFromSource(setGraph(), sourceNode,destinationNode);

        navigation_destinationNode=dijkstra_node;
        return printPath(sourceNode,dijkstra_node);
    }

    protected LinkedList<Node> setPath_list(Node sourceNode, Node destinationNode){

        setRelation();

        calculateShortestPathFromSource(setGraph(), sourceNode,destinationNode);


        return destinationNode.getShortestPath();
    }


    public Dijkstra(){


        Cursor cursor = positionDB.pointQueryAll();
        //創建資料集
        cursor.moveToFirst(); //移至第一筆
        point_num=cursor.getCount();
        if(point_num!=0) {
            node = new Node[point_num];

            for (int i = 0; i < point_num; i++) {
                node[i] = new Node(
                        cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5))

                );
                cursor.moveToNext();//移下一筆資料
            }
        }

        setRelation();
    }





}
