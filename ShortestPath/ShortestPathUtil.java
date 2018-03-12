

package cs6301.g23;
import cs6301.g23.Graph.Vertex;
import cs6301.g23.XGraph;
import cs6301.g23.XGraph.XVertex;
import cs6301.g23.Graph.Edge;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Queue;

public class LP4 {
	Graph g;
	Vertex s;
	ShortestPathVertex[] sv;
	Graph h;
	Vertex[] array ;
	int tp_count=0;
	int max_rewards=0;
	public class ShortestPathVertex extends Vertex{

		Integer distance;
		Vertex parent;
		int count;
		boolean seen;
		int d[];
		int indegree;
		public ShortestPathVertex(Vertex u) {
			super(u);
			parent = null;
			distance = 0;
			count = 0;
			seen = false;
			d = new int[g.size()];
			indegree=u.revAdj.size();
		}
		
	}

	// common constructor for all parts of LP4: g is graph, s is source vertex
	public LP4(Graph g, Vertex s) {
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
	
//	public LP4(Graph g){
//		this.g = g;
//		
//	}

	// Part a. Return number of topological orders of g
		public long countTopologicalOrders() {
			//dagChecker()
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

		void printArr(Vertex []vArr){
			for(Vertex v:vArr){
				System.out.print(v+" ");
			}
			System.out.println();
		}


		// Part b. Print all topological orders of g, one per line, and 
		//	return number of topological orders of g
		public long enumerateTopologicalOrders() {
			// To do
			//dagChecker()
			this.tp_count=0;
			XGraph xg=new XGraph(g);
			List<XVertex> vl=new LinkedList<XVertex>();
			for(Vertex v:xg){
				if(sv[v.name].indegree==0){
					vl.add((XVertex)v);
				}
			}
			Vertex[] arr=new Vertex[g.size()];
			for(XVertex v: vl){
			topologicalOrderHelper(xg,v,1,true,arr);
			}
			return this.tp_count;
		}


public void initialize(Graph g){
		for(Vertex u:g){
			ShortestPathVertex su = sv[u.name];
			su.seen = false;
			su.parent=null;
			su.distance = Integer.MAX_VALUE;			
		}
	}


	// Part c. Return the number of shortest paths from s to t
	// 	Return -1 if the graph has a negative or zero cycle
	public long countShortestPaths(Vertex t) {
		long numberOfPaths = 0;
		boolean done = bellmanFordAlgo();
		long shortestPath = sv[t.name].distance;
//		System.out.println("shortest path of t "+sv[t.name].distance);
//		System.out.println();
		if(done){
			Graph h = new Graph(g.size());
			h.directed = true;
			getTightEdgeGraph(h);
			HashMap<Vertex, Long> map = new HashMap<Vertex, Long>();
			numberOfPaths = countShortestPath(h.getVertex(s.name+1),h.getVertex(t.name+1), map);
		}
		else{
			numberOfPaths = -1;
		}
		return numberOfPaths;
	}
	
	long countShortestPath(Vertex s, Vertex v, HashMap<Vertex, Long> resultMap){
		Long count = 0l, temp = 0l;
		if(v == s){
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

	boolean bellmanFordAlgo(){
		Queue<Vertex> q = new LinkedList<Vertex>();
		for(Vertex u:g){
			ShortestPathVertex su = sv[u.name];
			su.distance = Integer.MAX_VALUE;
			su.count = 0;
			su.seen = false;
		}
		ShortestPathVertex source = sv[s.name];
		source.distance = 0;
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
	public long enumerateShortestPaths(Vertex t) {
		// To do
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
	
	private void enumerateShortestPath(Vertex s, Vertex t, boolean[] visited, Vertex[] path, int index){
		visited[s.name] = true;
		path[index++] = s;
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
			shortestDistance = (int) st.distance;
			System.out.println("vertext "+t+" distance "+st.distance+" parent "+st.parent);
		}
		
		return shortestDistance;
	}
	
	boolean bellmanFordAlgoTake1(int limit){
		boolean noChange = false;
		for(Vertex u:g){
			ShortestPathVertex su = sv[u.name];
			su.d[0] = Integer.MAX_VALUE;
			su.parent = null;
		}
		ShortestPathVertex source = sv[s.name];
		source.d[0] = 0;
		for(int k=1;k<=g.size(); k++){
			noChange = true;
			for(Vertex u:g){
				ShortestPathVertex su = sv[u.name];
				su.d[k] = su.d[k-1];
				for(Edge e:u.revAdj){
					Vertex v = e.otherEnd(u);
					ShortestPathVertex otherEnd = sv[v.name];
					if(su.d[k] > otherEnd.d[k-1] + e.weight){
						su.d[k] = otherEnd.d[k-1] + e.weight;
						su.parent = otherEnd;
						System.out.println("vertex "+u+" distance "+su.d[k]+" parent "+su.parent);
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
	
	


	
	public void dijkstra(Graph gh,Vertex source){
		   initialize(gh);
		   ShortestPathVertex ss = sv[source.name];
		   ss.distance=0;
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
		public int reward(HashMap<Vertex,Integer> vertexRewardMap, List<Vertex> tour) {
			// To do
			this.bellmanFordAlgo();
			Graph gImage = new Graph(g.size());
			gImage.setDirected(true);
			getTightEdgeGraph(gImage);
//			for(Vertex v:gImage){
//				for(Edge e:v){
//					System.out.println("Image"+" "+e);
//				}
//			}
			findMaxRewards(vertexRewardMap,tour,gImage.getVertex(s.name+1),0,new LinkedList<Vertex>());
			return this.max_rewards;
		}
		
		
		void findMaxRewards(HashMap<Vertex, Integer> vertexRewardMap, List<Vertex> tour,
			Vertex currentNode,int currentReward,LinkedList<Vertex> pathToNode) {
			//System.out.println("find rewards called for "+currentNode+" "+vertexRewardMap.get(currentNode)+" "+currentReward);
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
			//	System.out.println("Calling max rewards "+currentReward);
				findMaxRewards(vertexRewardMap,tour,v,currentReward,pathToNode);
				spv.seen=false;
			}
			if(currentReward>this.max_rewards){
				List<Vertex> pathBackToSource=new LinkedList<Vertex>();
				//Check path in original graph
				Vertex endVertex=g.getVertex(currentNode.name+1);
				boolean foundPathToSource=findPathToSource(endVertex,pathBackToSource);
				//System.out.println("Find path called for"+endVertex+" "+foundPathToSource);
				
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
		
		void clearSeenStatus(List<Vertex> pathList){
			for(Vertex v:pathList){
				ShortestPathVertex spv=sv[v.name];
			//	System.out.println("Setting seen false to "+v);
				spv.seen=false;
			}
		}

		public boolean findPathToSource(Vertex currentNode,List<Vertex> pathList) {
			//System.out.println("F called "+currentNode);
			if ( currentNode.name == s.name){
				return true;
			}
			Iterator<Edge> git=currentNode.iterator();
			while(git.hasNext()){
				Edge e=git.next();
				Vertex otherVertex = e.otherEnd(currentNode);
				//System.out.println(" F eEdge "+e+" other vertex "+otherVertex);
				ShortestPathVertex svOtherVertex=sv[otherVertex.name];
				if (!svOtherVertex.seen) {
				//	System.out.println("node not seen "+otherVertex);
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
