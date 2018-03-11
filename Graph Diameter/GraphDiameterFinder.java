package cs6301.g23;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author radhikakalaiselvan
 *
 */
public class GraphDiameterFinder {
	public static void printPath(LinkedList<Graph.Vertex> path){
		String pathS="";
		for(Graph.Vertex node : path){
			pathS+=node+" ";
		}
		pathS=pathS.trim().replace(" ","->");
		System.out.println(pathS);
	}
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Scanner in;
		 if (args.length > 0) {
		 File inputFile = new File(args[0]);
		 in = new Scanner(inputFile);
		 } 
		 else {
			 in = new Scanner(System.in); 
		 }
		Graph g=Graph.readGraph(in);
		printPath(getDiameter(g));
	}
	/**
	 * Following method return the diameter for the given graph using the steps given below,
	 * (graph is expected to be a tree)
	 * 1.Find the farthest node for any node in the graph using BFS.
	 * 2. With the farthest node as the source call BFS and get the new farthest node.
	 * 3. Find the distance between these two node and print the path.
	 * @param g
	 * @return
	 */
	public static LinkedList<Graph.Vertex> getDiameter(Graph g){
		BreadthFirstSearch bfsObj=new BreadthFirstSearch(g);
		bfsObj.bfs(g.getVertex(1));
		Graph.Vertex newSourceNode=bfsObj.getFarthestVertex();
		bfsObj.resetArraysToDefaults();
		bfsObj.bfs(newSourceNode);
		Graph.Vertex newDestinationNode=bfsObj.getFarthestVertex();	
		LinkedList<Graph.Vertex> path=bfsObj.getPath(newSourceNode,newDestinationNode);	
	    return path;
	}

}
