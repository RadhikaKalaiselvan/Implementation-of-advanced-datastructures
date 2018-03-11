package cs6301.g23;
/**
* @author Radhika Kalaiselvan
*Version 1.0 - 9/17/17
*/
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import cs6301.g23.Graph.Edge;
import cs6301.g23.Graph.Vertex;

public class GraphProblems extends GraphAlgorithm<GraphProblems.Node>{
 
	public static int time;
    public static int cno;
    public static int topNum;
    int graphSize;

    LinkedList<Graph.Vertex> decFinList=new LinkedList<Graph.Vertex>();
	static boolean cycle=false;
	
	/*
	 * Node is used as a wrapper over Graph, instead of creating arrays to maintain
	 * information like distance,parent,indegree, etc.
	 * 
	 */
   static class Node{
	boolean seen;
	int dis=0;
	int cno;
	int fin=0,top=0;
	Node parent=null;
	int inDegree=0;
	boolean isOnStack=false;

	Node(){
		this.seen=false;
	}
	Node(Node parent){
		this.seen=false;
		this.parent=parent;
	}
	public boolean isSeen() {
		return seen;
	}
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	public int getDis() {
		return dis;
	}
	public void setDis(int dis) {
		this.dis = dis;
	}
	public int getCno() {
		return cno;
	}
	public void setCno(int cno) {
		this.cno = cno;
	}
	public int getFin() {
		return fin;
	}
	public void setFin(int fin) {
		this.fin = fin;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public int getInDegree() {
		return inDegree;
	}
	public void setInDegree(int inDegree) {
		this.inDegree = inDegree;
	}
	public boolean getIsOnStack() {
		return isOnStack;
	}
	public void setIsOnStack(boolean isOnStack) {
		this.isOnStack = isOnStack;
	}
}

   /*
    * Constructor of this class, we create Node objects for each vertex nd store
    * it in node[]
    */
public GraphProblems(Graph g) {
	super(g);
	graphSize=g.n;
	this.node=new Node[graphSize];
	for(int i=0;i<graphSize;i++)
    node[i]=new Node();
}
/*
 * Prints the decFinList 
 */
public void printList(){
	   for(Vertex v : decFinList)
	   {
		   System.out.print(v+" ");
	   }
}
/*
 * Checks if this.graph is cyclic or not and returns a boolean.
 */
public boolean isCyclicGraph(){
	isCyclic(g.getVertex(1));
	return cycle;
}

/* Algorithm 2. Run DFS on g and add nodes to the front of the output list,
 *  in the order in which they finish.  
 *  If the Graph is aCyclic return a list of vertices in topological
 * ordering else returns null.
 *  
 * toplogicalOrder(g) 
 * it ← g.iterator() 
 * DFS(it) return decFinList 
 */
public LinkedList<Graph.Vertex> topologicalOrder2(){
	
	Iterator<Vertex> it=this.g.iterator();
	if(isCyclicGraph()){
		return null;
	}
	return dfs(it);
}
 /* 
  * Given a iterator on vertices perform DFS
  * 
 * DFS(it)
 * topNum ← g.size() 
 * time ← 0 
 * cno ← 0 
 * decFinList ← new Linked List of vertices
 *  for u in V do 
 *  u.seen ← false 
 *  while it.hasNext() do
 *   u ← it.next()
 *   if ! u.seen then
 *       cno++ 
 *       DFSVisit(u)
 */
public LinkedList<Graph.Vertex> dfs(Iterator<Vertex> it){
	topNum=g.size();
	time=0;
	cno=0;
	this.decFinList.clear();
	for (Vertex v : g) {
		Node vNode=getVertex(v);
		vNode.setSeen(false);
	}
	while(it.hasNext()){
		
		Vertex uVert=it.next();
		Node u=getVertex(uVert);
		if(!u.isSeen()){
			
			cno++;
			DfsVisit(uVert);
			
		}
	}
	return decFinList;
}

public void DfsVisit(Graph.Vertex source){
	/*
	 * DFSVisit(u)
	 * u.seen ← true 
	 * u.dis ← ++time 
	 * u.cno ← cno 
	 * for each edge (u,v) going out of u 
	 *  do if ! v.seen then
	 *  v.parent ← u 
	 *  DFSVisit(v)
	 * u.fin ← ++time 
	 * u.top ← topNum-- 
	 * decFinList.addFirst(u)
	 */	
	
	Node u=getVertex(source);
	u.setSeen(true);
	u.setDis(++time);
	u.setCno(cno);
	for(Graph.Edge e: source) {
		Graph.Vertex adjNode=e.otherEnd(source);
		Node v=getVertex(adjNode);
		if(!v.isSeen()){
			v.setParent(u);
            DfsVisit(adjNode);
		}
		u.setFin(++time);
		u.setTop(topNum--);
	}
	decFinList.addFirst(source);
}
	


/*
 *  Algorithm 1. Remove vertices with no incoming edges, one at a
 *  time, along with their incident edges, and add them to a list.
 *
 * topologicalOrder1(G=(V,E)): 
 * topNum ← 0 
 * q ← new Queue(Vertex) 
 * topList ←new List(Vertex) 
 * for u in V do 
 * u.inDegree ← u.revAdj.size()
 *  if u.inDegree= 0 then 
 *  q.add(u) 
 *  while q is not empty do 
 *  u ← q.remove() 
 *  u.top ← ++topNum
 * topList.add(u) 
 * for each edge (u,v) going out of u do
 *  v.inDegree-- 
 *  if v.inDegree = 0 then 
 *  q.add(v) 
 *  if topNum != |V| 
 *  then raise exception “Not a
 * DAG” return topList
 */
public LinkedList<Graph.Vertex> topologicalOrder1(){
   topNum=0;
   Queue<Vertex> q=new LinkedList<Vertex>();
   decFinList.clear();
   for(Vertex u : g){
	   Node uNode=getVertex(u);
	   uNode.setInDegree(u.revAdj.size());
	   if(uNode.inDegree==0){
		   q.add(u);
	   }
   }
   while(!q.isEmpty()){
	   Vertex u=q.poll();
	   getVertex(u).setTop(++topNum);
	   decFinList.add(u);
	   for(Graph.Edge e: u){
		   Graph.Vertex adjNode=e.otherEnd(u);
    		Node v=getVertex(adjNode);
		    v.setInDegree(v.getInDegree()-1);
		    if(v.getInDegree()==0){
		    	q.add(adjNode);
		    }
	   }
   }
   if(topNum!=g.n){
	   decFinList=null;
   }
   return decFinList;
}
/*
 * reverses the edges of the graph.
 */
public void reverseEdges(Graph g){
	for(Vertex v:g){
		LinkedList<Edge> temp=new LinkedList<Edge>();
		for( Edge e : v.revAdj){
			temp.add(e);
		}
		v.revAdj=v.adj;
		v.adj=temp;
	}
}
/* Question 2
 * Returns the total number of strongly connected components in a graph.
 * Run DFS on G and create a list of nodes in decreasing
 * finish time order.
 * 
 * reverse the graph
 * 
 * Run DFS using the order of the list output by the first DFS. 
 * Each DSF tree in the second DFS is a strongly connected component.
 */
public int stronglyConnectedComponents() { 
	LinkedList<Vertex> result=new LinkedList<Vertex>(dfs(this.g.iterator()));
	this.decFinList.clear();
	reverseEdges(g);
	LinkedList<Vertex> result2= dfs(result.iterator());
	return cno;
}
/*Question 4
 * Checks if the given graph isCyclic or not by
 * running DFS on the given graph, and checking
 *  if there are any back edges
 * 
 */
public void isCyclic(Graph.Vertex u){
	Node uNode=getVertex(u);
	uNode.setSeen(true);
	uNode.setIsOnStack(true);
	for(Graph.Edge e: u) {
		Graph.Vertex v=e.otherEnd(u);
		Node vNode=getVertex(v);
		
		if(!vNode.isSeen()){
		   isCyclic(v);
		} else if (vNode.isOnStack) {
			cycle=true;
			return ;
		}
	}
	uNode.setIsOnStack(false);
}

public static void main(String[] args) throws Exception{
	Scanner in;
	 if (args.length > 0) {
	 File inputFile = new File(args[0]);
	 in = new Scanner(inputFile);
	 } 
	 else {
		 in = new Scanner(System.in);
	 }
	Graph g=Graph.readGraph(in,true);
	GraphProblems t=new GraphProblems(g);
	System.out.println("Press 1 -TopologicalOrder1, 2-TopologicalOrder2, 3-IsCyclic graph, 4-No. of strongly connected components");
	int choice=in.nextInt();
	switch(choice){
	case 1:
		System.out.println("topologicalOrder1 : ");
		t.topologicalOrder1();
		t.printList();
		break;
	case 2:
		System.out.println("topologicalOrder2 : ");
		t.topologicalOrder2();
		t.printList();
		break;
	case 3:
		System.out.println("Is the graph cyclic (contains back edges) :"+t.isCyclicGraph());
		break;
	case 4 :
		System.out.println("No. of strongly connected components ="+t.stronglyConnectedComponents());
	    break;
	default:
		System.out.println("Invalid option!");
	}
}
}

/**
 * Sample input:
 * 11
17
1
11
1
11
4
1
4
1
1
9
11
1
4
9
1
11
3
1
11
6
1
6
3
1
10
6
1
3
10
1
2
3
1
8
2
1
5
8
1
2
7
1
7
8
1
5
7
1

5
4
1
Press 1 -TopologicalOrder1, 2-TopologicalOrder2, 3-IsCyclic graph, 4-No. of strongly connected components
4
No. of strongly connected components =4

 * */
