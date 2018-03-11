/**
 * 
 */
package cs6301.g23;

import java.util.LinkedList;
import java.util.Queue;
/**
 * @author radhikakalaiselvan
 *
 */
public class BreadthFirstSearch {
	Graph g;
	int graphSize;
	int sourceArray[],distance[];
	boolean marked[];
	boolean bfsCompleted;
/**
 * 
 * @param g Generate BreadthFirstSearch Class  object by calling this constructor 
 * which requires the graph object g. Refer Graph.java
 * The constructor also initializes the arrays required for Breadth first search.
 * marked[] is used to maintain the status of vertex if it has been visited or not
 * distance[] is used to store the distance from the source on which BFS is called.
 * sourceArray[] is used to maintain the source of each vertex, used to find the path in the graph.
 */
	BreadthFirstSearch(Graph g){
		this.g=g;
		graphSize=g.n;
		sourceArray=new int[graphSize];
		marked=new boolean[graphSize];
		distance=new int[graphSize];
		resetArraysToDefaults();
		bfsCompleted=false;
	}
	/**
	 * This method resets all the arrays used by BFS. Call this method before re-running
	 * bfs on a different source vertex.
	 */
	public void resetArraysToDefaults(){
		for(int i=0;i<marked.length;i++)
		{
			marked[i]=false;
		}
		for(int j=0;j<distance.length;j++)
		{
			distance[j]=0;
		}
		for(int i=0;i<sourceArray.length;i++){
			sourceArray[i]=0;
		}
		bfsCompleted=false;
	}
	
	/**
	 * Given a vertex returns the status if was already marked or not
	 * @return
	 */
	private boolean isMarked(Graph.Vertex v){
		return marked[v.name];
	}
	/**
	 * mark the given vertex as true.
	 * @param v
	 */
	private void setMarked(Graph.Vertex v){
	  marked[v.name]=true;
	}
	/**
	 * Sets the source of the current node in the sourceArray, which is used to back track the path.
	 * @param node
	 * @param source
	 */
	private void updateSourceArray(Graph.Vertex node, Graph.Vertex source){
		sourceArray[node.name]=source.name;
	}
	
	/**
	 * Update the distance for the given vertex to the given distance.
	 * @param node
	 * @param dist
	 */
	private void updateDistance(Graph.Vertex node, int dist){
		distance[node.name]=dist+1;
	}
		
	/**
	 * Perform BFS on the graph object initialized in the constructor for the given source node.
	 * used bfsCompleted flag to check maintain the status if bfs was performed already or not.
	 */
	public void bfs(Graph.Vertex source){
		Queue<Graph.Vertex> q=new LinkedList<Graph.Vertex>();
		q.add(source);
		updateSourceArray(source,source);
		setMarked(source);
		distance[source.name]=0;
		while(!q.isEmpty()){
		Graph.Vertex v=q.poll();
		for(Graph.Edge e: v) {
			Graph.Vertex adjNode=e.otherEnd(v);
				if(!isMarked(adjNode)){
		         q.add(adjNode);
		         setMarked(adjNode);
		         updateSourceArray(adjNode,v);
		         updateDistance(adjNode,distance[v.name]);
			  }
			}
		}
		bfsCompleted=true;
	}
	/*
	 * If the bfs was completed already, the distance array will be updated.
	 *  Returns the node the greatest distance from the previous source vertex on which 
	 *  bfs was called.
	 */
	public  Graph.Vertex getFarthestVertex(){
		assert bfsCompleted;
		int maxDist=0;
		int index=0;
		for(int i=0;i<distance.length;i++){
			if(distance[i]>maxDist){
				maxDist=distance[i];
				index=i;
			}
		}
		return g.getVertex(index+1);
	}
	
	/**
	 * If the bfs was completed and sourceArray would be updated. This method finds the 
	 * path from the given source to the destination using sourceArray and also prints the 
	 * path.
	 * @param source
	 * @param destination
	 * @return
	 */
public LinkedList<Graph.Vertex> getPath(Graph.Vertex source,Graph.Vertex destination){
	assert bfsCompleted;
	LinkedList<Graph.Vertex> path=new LinkedList<Graph.Vertex>();
	path.add(destination);
	int length=distance[destination.name];
	System.out.println("Diameter "+length);
		while(destination.name!=source.name){
		destination=g.getVertex(sourceArray[destination.name]+1);
		path.add(destination);
	}
	return path;
}
/**
 * Helper method to print all the status arrays.
 */
public void printArrays(){
	for(int i=0;i<marked.length;i++)
	{
		System.out.println(" marked ["+i+"]=> "+marked[i]);
	}
	for(int j=0;j<distance.length;j++)
	{
		System.out.println(" distance ["+j+"]=> "+distance[j]);
	}
	for(int i=0;i<sourceArray.length;i++){
		System.out.println(" sourceArray ["+i+"]=> "+sourceArray[i]);
	}
}
/**
 * Given a list of vertex object prints the path.
 * @param path
 */

}
