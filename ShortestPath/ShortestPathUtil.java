/**
 * Program for implementing various algorithms in ShortestPath
 * @author Radhika Kalaiselvan
 * @version 1.1 - 11/11/17
 */
package cs6301.g23;
import cs6301.g23.Graph.Vertex;
import cs6301.g23.XGraph;
import cs6301.g23.Graph.Edge;
import cs6301.g23.XGraph.XVertex;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;

public class ShortestPathUtil {
	Graph g;
	Vertex s;
	ShortestPathVertex[] sv;
	Graph h;
	Vertex[] array ;
	int tp_count=0;
	int max_rewards=0;
	public class ShortestPathVertex extends Vertex{

		Long distance;
		Vertex parent;
		int count;
		boolean seen;
		int indegree;
		Long d[];
		public ShortestPathVertex(Vertex u) {
			super(u);
			parent = null;
			distance = 0l;
			count = 0;
			seen = false;
			d = new Long[g.size()];
			indegree=u.revAdj.size();
		}

	}

	// common constructor for all parts of ShortestPathUtil: g is graph, s is source vertex
	/**
	 * Commom constructor for all parts of ShortestPathUtil
	 * Shortest Path Vertex is initialized here
	 * @param g
	 * @param s
	 */
	public ShortestPathUtil(Graph g, Vertex s) {
		this.g = g;
		this.s = s;
		this.sv = new ShortestPathVertex[g.size()];
		for(Vertex v:g){
			sv[v.name] = new ShortestPathVertex(v);
		}
		h = new Graph(g.size());
		h.directed = true;
		array = new Vertex[h.size()];
	}


	// Part a. Return number of topological orders of g
	/**
	 * 
	 * @return
	 */
	public long countTopologicalOrders() {
		this.tp_count=0;
		XGraph xg=new XGraph(g);
		List<XVertex> vl=new LinkedList<XVertex>();
		for(Vertex v:xg){
			if(sv[v.name].indegree==0){
				vl.add((XVertex)v);
			}
		}
		for(XVertex v: vl){
			topologicalOrderHelper(xg,v,1,false,null);
		}
		return this.tp_count;
	}
	
	/**
	 * Given a graph and a current node the method keeps adding the vertices in the topological order
	 * in the arr[]. The method is called recursively for the vertices which have an in-degree 0
	 * @param x
	 * @param node
	 * @param nodeNum
	 * @param print
	 * @param arr
	 */

	void topologicalOrderHelper(XGraph x, XVertex node, int nodeNum,boolean print,Vertex[] arr){		

		if(print){
			arr[nodeNum-1]=node;
		}

		if(nodeNum==x.size()){
			this.tp_count++;
			if(print){
				printArr(arr);
			}
			return;
		}
		for(Edge v:node.adj){
			sv[v.to.name].indegree--;
		}
		node.disabled=true;
		for(Vertex v:x){
			XVertex xv=(XVertex)v;
			if(sv[xv.name].indegree==0){
				topologicalOrderHelper(x,xv,nodeNum+1,print,arr);
			}
		}
		for(Edge v:node.adj){
			sv[v.to.name].indegree++;
		}
		node.disabled=false;
	}

	/**
	 * Prints the topological order in the array
	 * @param vArr
	 */
	void printArr(Vertex []vArr){
		for(Vertex v:vArr){
			System.out.print(v+" ");
		}
		System.out.println();
	}


	// Part b. Print all topological orders of g, one per line, and 
	//	return number of topological orders of g
	/**
	 * Calls the functions topologicalOrderHelper with the print variable set to true
	 * This enables the print function - which prints the topological order
	 * @return
	 */
	public long enumerateTopologicalOrders() {
		this.tp_count=0;
		XGraph xg=new XGraph(g);
		List<XVertex> vl=new LinkedList<XVertex>();
		for(Vertex v:xg){
			if(sv[v.name].indegree==0){
				vl.add((XVertex)v);
			}
		}
		Vertex[] arr=new Vertex[g.size()];
		//if multiple nodes have zero indegree
		for(XVertex v: vl){
			topologicalOrderHelper(xg,v,1,true,arr);
		}
		return this.tp_count;
	}

	/**
	 * Used by Dijkstra's algorithm to initialise the graph
	 * @param g
	 */
	public void initialize(Graph g){
		for(Vertex u:g){
			ShortestPathVertex su = sv[u.name];
			su.seen = false;
			su.parent=null;
			su.distance = Long.MAX_VALUE;			
		}
	}


	// Part c. Return the number of shortest paths from s to t
	// 	Return -1 if the graph has a negative or zero cycle
	/**
	 * This method calls the Bell man Ford algorithm, if it is done, it creates a tight edged graph 
	 * h. The method then calls the countShortestPath method with the vertices in the h graph. It returns -1
	 * if the graph contains a negative cycle
	 * @param t
	 * @return
	 */
	public long countShortestPaths(Vertex t) {
		long numberOfPaths = 0;
		boolean done = bellmanFordAlgo();
		if(done)//if bellman ford is done and does not contain a -ve cycle
		{
			Graph h = new Graph(g.size());
			h.setDirected(true);
			getTightEdgeGraph(h);
			HashMap<Vertex, Long> map = new HashMap<Vertex, Long>();
			numberOfPaths = countShortestPath(h.getVertex(s.name+1),h.getVertex(t.name+1), map);
		}
		else{
			numberOfPaths = -1;
		}
		return numberOfPaths;
	}

	/**
	 * This is a recursive function to calculate the number of shortest path from the 
	 * given source to the destination. The number of paths are stored in a hashmap to
	 * avoid recomputing them. 
	 * @param s
	 * @param v
	 * @param resultMap
	 * @return
	 */
	long countShortestPath(Vertex s, Vertex v, HashMap<Vertex, Long> resultMap){
		Long count = 0l, temp = 0l;
		if(s == v){
			count = 1l;
		}
		else{
			Vertex otherEnd; 
			for(Edge e:v.revAdj){
				otherEnd = e.otherEnd(v);
				temp = resultMap.get(otherEnd);
				if(temp!=null){
					count+=temp;
				}
				else{
					count+=countShortestPath(s, otherEnd, resultMap);
				}
			}
		}
		resultMap.put(v,count);
		return count;

	}

	/**
	 * This method implements the Bellman Ford Take 3 algorithm which is used for
	 * shortest path problem.
	 * @return
	 */
	boolean bellmanFordAlgo(){
		Queue<Vertex> q = new LinkedList<Vertex>();
		for(Vertex u:g){
			ShortestPathVertex su = sv[u.name];
			su.distance = Long.MAX_VALUE;
			su.count = 0;
			su.seen = false;
		}
		ShortestPathVertex source = sv[s.name];
		source.distance = 0l;
		source.seen = true;
		q.add(source);
		while(!q.isEmpty()){
			Vertex u = q.remove();
			ShortestPathVertex su = sv[u.name];
			su.seen = false;
			su.count = su.count + 1;
			if(su.count >= g.size())
				return false;
			Iterator<Edge> it = u.adj.iterator();
			while(it.hasNext()){
				Edge e = it.next();
				Vertex otherEnd = e.otherEnd(u);
				ShortestPathVertex sOtherEnd = sv[otherEnd.name];
				if(sOtherEnd.distance > su.distance + e.weight){
					sOtherEnd.distance = su.distance + e.weight;
					sOtherEnd.parent = u;
					if(!sOtherEnd.seen){
						q.add(otherEnd);
						sOtherEnd.seen = true;
					}
				}

			}
		}
		return true;
	}

	/**
	 * This method returns a tight edged graph. The tight edges alone are added to 
	 * the new graph h passed as a parameter.
	 * @param h
	 */
	void getTightEdgeGraph(Graph h){
		for(Vertex v:g){
			for(Edge e:v){
				Vertex u = e.otherEnd(v);
				if(sv[u.name].distance == sv[v.name].distance+e.weight){
					h.addEdge(h.getVertex(v.name+1),h.getVertex(u.name+1), e.weight);
				}	
			}
		}
	}

	// Part d. Print all shortest paths from s to t, one per line, and 
	//	return number of shortest paths from s to t.
	//	Return -1 if the graph has a negative or zero cycle.
	/**
	 * This method calls the bellmanFordAlgo, and if it done creates a
	 * tight edged graph h. It then calculates the number of paths from 
	 * source to destination and calls the function to enumerateShortestPath. 
	 * The method returns -1 if there is a negative cycle in the graph.
	 * @param t
	 * @return
	 */
	public long enumerateShortestPaths(Vertex t) {
		boolean[] visited = new boolean[g.size()];
		Vertex[] path = new Vertex[g.size()];
		int index =0;
		long numberOfPaths =0l;
		boolean done = bellmanFordAlgo();
		if(done){
			Graph h = new Graph(g.size());
			h.directed = true;
			getTightEdgeGraph(h);
			HashMap<Vertex, Long> map = new HashMap<Vertex, Long>();
			numberOfPaths = countShortestPath(h.getVertex(s.name+1),h.getVertex(t.name+1), map);
			for(int i=0;i<visited.length;i++){
				visited[i] = false;
			}
			enumerateShortestPath(h.getVertex(s.name+1), h.getVertex(t.name+1), visited, path, index);
		}
		else
			numberOfPaths = -1;

		return numberOfPaths;
	}

	/**
	 * This is a recursive method to print all the shortest paths from the given
	 * source to the destination. The method call the printPath if the destination 
	 * vertex is called in the recursive method. The path is stored in the path array,
	 * and the visited array is to keep track if a particular vertex is visited in the
	 * particular path.
	 * @param s
	 * @param t
	 * @param visited
	 * @param path
	 * @param index
	 */
	private void enumerateShortestPath(Vertex s, Vertex t, boolean[] visited, Vertex[] path, int index){
		path[index++] = s;
		visited[s.name] = true;
		if(s == t){
			printPath(path, index);
		}
		else{
			for(Edge e:s.adj){
				Vertex v = e.otherEnd(s);
				if(!visited[v.name]){
					enumerateShortestPath(v, t, visited, path, index);
				}
			}
		}
		index--;
		visited[s.name]=false;

	}

	/**
	 * Method to print the path in the given array
	 * @param array
	 * @param c
	 */
	void printPath(Vertex[] array, int c){
		for(int i=0;i< c;i++){
			{
				System.out.print(array[i]);
				System.out.print(" ");
			}
		}
		System.out.println();
	}


	// Part e. Return weight of shortest path from s to t using at most k edges
	/**
	 * The method returns the shortest path from s to t. It calls the Bellman ford
	 * Take 1 algorithm to calculate the distance using specific number of edge. Tight edge 
	 * graph is created and then the destination vertex's kth distance is printed.   
	 * @param t
	 * @param k
	 * @return
	 */
	public int constrainedShortestPath(Vertex t, int k) {
		// To do
		int shortestDistance = 0;
		boolean done = bellmanFordAlgoTake1(k);
		System.out.println("done "+done);
		if(done){
			Graph h = new Graph(g.size());
			h.directed = true;
			getTightEdgeGraph(h);
			ShortestPathVertex st = sv[t.name];
			System.out.println("before "+st.d[k]);
			shortestDistance = st.d[k].intValue();
			System.out.println("after "+shortestDistance);
		}
		
		return shortestDistance;
	}

	/**
	 * This method implements the Bellman ford Take 1 algorithm. It uses a
	 * distance array to store the distance of the vertex from the source using 
	 * k edge in that array.
	 * @param limit
	 * @return
	 */
	boolean bellmanFordAlgoTake1(int limit){
		boolean noChange = false;
		for(Vertex u:g){
			ShortestPathVertex su = sv[u.name];
			su.d[0] = Long.MAX_VALUE;
			su.parent = null;
		}
		ShortestPathVertex source = sv[s.name];
		source.d[0] = 0l;
		for(int k=1;k<=g.size(); k++){
			noChange = true;
			for(Vertex u:g){
				ShortestPathVertex su = sv[u.name];
				su.d[k] = su.d[k-1];
				for(Edge e:u.revAdj){
					Vertex v = e.otherEnd(u);
					ShortestPathVertex otherEnd = sv[v.name];
					if(otherEnd.d[k - 1] != Long.MAX_VALUE && su.d[k] > otherEnd.d[k-1] + e.weight){
						su.d[k] = otherEnd.d[k-1] + e.weight;
						su.parent = otherEnd;
						noChange = false;
					}
				}
			}
			if(noChange){
				for(Vertex u:g){
					ShortestPathVertex su = sv[u.name];
					su.distance = su.d[k];
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param gh
	 * @param source
	 */
	public void dijkstra(Graph gh,Vertex source){
		initialize(gh);
		ShortestPathVertex ss = sv[source.name];
		ss.distance=0l;
		PriorityQueue<Vertex> pq=new PriorityQueue<Vertex>(11,new Comparator<Vertex>() {  
			public int compare(Vertex v1, Vertex v2) {  
				return sv[v1.name].distance.compareTo(sv[v2.name].distance);  
			}      
		});
		for(Vertex v:gh){
			pq.add(v);
		}
		while(!pq.isEmpty()){
			Vertex u=pq.remove();
			ShortestPathVertex su=sv[u.name];
			su.seen=true;
			for(Edge e:u){
				relax(e);
			}
		}

		for(Vertex v:g){
			ShortestPathVertex su=sv[v.name];
			su.seen=false;
		}
	}

	boolean relax(Edge e){
		ShortestPathVertex u=sv[e.from.name];
		ShortestPathVertex v=sv[e.to.name];
		if(v.distance>(u.distance+e.weight)){
			v.distance=u.distance+e.weight;
			v.parent=u;
			return true;
		}
		return false;
	}

	// Part f. Reward collection problem
	// Reward for vertices is passed as a parameter in a hash map
	// tour is empty list passed as a parameter, for output tour
	// Return total reward for tour
	/**
	 * This method prints the reward collected while traveling on the shortest path. 
	 * It calls the method findMaxRewards
	 * @param vertexRewardMap
	 * @param tour
	 * @return
	 */
	public int reward(HashMap<Vertex,Integer> vertexRewardMap, List<Vertex> tour) {
		dijkstra(g,s);
		Graph gImage = new Graph(g.size());
		gImage.setDirected(true);
		getTightEdgeGraph(gImage);
		findMaxRewards(vertexRewardMap,tour,gImage.getVertex(s.name+1),0,new LinkedList<Vertex>());
		return this.max_rewards;
	}


	/**
	 * The list tour stores the vertices in the tour. This method is a recursive method
	 * called for each vertex in the tight edge graph. After the recursive call, it traces whether 
	 * there is a path back to the source from the current node. These rewards are not added in 
	 * the final reward of the problem.
	 * @param vertexRewardMap
	 * @param tour
	 * @param currentNode
	 * @param currentReward
	 * @param pathToNode
	 */
	void findMaxRewards(HashMap<Vertex, Integer> vertexRewardMap, List<Vertex> tour,
		Vertex currentNode,int currentReward,LinkedList<Vertex> pathToNode) {
		currentReward+=vertexRewardMap.get(currentNode);
		pathToNode.add(currentNode);
		Iterator<Edge> it=currentNode.iterator();
		while(it.hasNext())
		{
			Edge e= it.next();
			System.out.println(currentNode+" "+e);
			Vertex v=e.otherEnd(currentNode);
			ShortestPathVertex spv=sv[v.name];
			spv.seen=true;
			findMaxRewards(vertexRewardMap,tour,v,currentReward,pathToNode);
			spv.seen=false;
		}
		if(currentReward>this.max_rewards){
			List<Vertex> pathBackToSource=new LinkedList<Vertex>();
			Vertex endVertex=g.getVertex(currentNode.name+1);
			boolean foundPathToSource=findPathToSource(endVertex,pathBackToSource);
			
			if(foundPathToSource){
				this.max_rewards=currentReward;
				tour.clear();
				for(Vertex v:pathToNode){
					tour.add(v);
				}
				for(Vertex rv:pathBackToSource){
					tour.add(rv);
				}
			}
			pathToNode.removeLast();
			clearSeenStatus(pathBackToSource);
		}

	}

	/**
	 * Resets the seen status of the vertices.
	 * @param pathList
	 */
	void clearSeenStatus(List<Vertex> pathList){
		for(Vertex v:g){
			ShortestPathVertex spv=sv[v.name];
			System.out.println("Before seen "+v+" "+spv.seen);
			spv.seen=false;
		}
		for(Vertex v:pathList){
			ShortestPathVertex spv=sv[v.name];
			System.out.println("Setting seen false to "+v);
			spv.seen=false;
		}
	}

	/**
	 * This method checks if a path exists from the given node
	 * to the source vertex.
	 * @param currentNode
	 * @param pathList
	 * @return
	 */
	public boolean findPathToSource(Vertex currentNode,List<Vertex> pathList) {
		System.out.println("F called "+currentNode);
		if ( currentNode.name == s.name){
			return true;
		}
		Iterator<Edge> git=currentNode.iterator();
		while(git.hasNext()){
			Edge e=git.next();
			Vertex otherVertex = e.otherEnd(currentNode);
			System.out.println(" F eEdge "+e+" other vertex "+otherVertex);
			ShortestPathVertex svOtherVertex=sv[otherVertex.name];
			if (!svOtherVertex.seen) {
				System.out.println("node not seen "+otherVertex);
				pathList.add(otherVertex);
				svOtherVertex.seen=true;
				if (findPathToSource(otherVertex,pathList))
					return true;
			}
		}
		return false;
	}

	// Do not modify this function
	static void printGraph(Graph g, HashMap<Vertex,Integer> map, Vertex s, Vertex t, int limit) {
		System.out.println("Input graph:");
		for(Vertex u: g) {
			if(map != null) { 
				System.out.print(u + "($" + map.get(u) + ")\t: ");
			} else {
				System.out.print(u + "\t: ");
			}
			for(Edge e: u) {
				System.out.print(e + "[" + e.weight + "] ");
			}
			System.out.println();
		}
		if(s != null) { System.out.println("Source: " + s); }
		if(t != null) { System.out.println("Target: " + t); }
		if(limit > 0) { System.out.println("Limit: " + limit + " edges"); }
		System.out.println("_____________");
	}
}
