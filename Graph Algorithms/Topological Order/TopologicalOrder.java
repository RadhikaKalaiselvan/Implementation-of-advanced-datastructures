package cs6301.g23;

import java.awt.List;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import cs6301.g23.Graph.Edge;
import cs6301.g23.Graph.Vertex;
/**
* @author Radhika Kalaiselvan
*Version 1.0 - 9/17/17
*/
public class TopologicalOrder extends GraphAlgorithm<TopologicalOrder.Node>{

	public static int time;
    public static int cno;
    public static int topNum;
    int graphSize;
    public static int components=0;
    LinkedList<Graph.Vertex> decFinList=new LinkedList<Graph.Vertex>();
	static boolean cycle=false;
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
 * toplogicalOrder(g) 
 * it ← g.iterator() 
 * DFS(it) return decFinList 
 */
public TopologicalOrder(Graph g) {
	super(g);
	graphSize=g.n;
	this.node=new Node[graphSize];
	for(int i=0;i<graphSize;i++)
    node[i]=new Node();
}

public void printList(){
	   for(Vertex v : decFinList)
	   {
		   System.out.print(v+" ");
	   }
}

public boolean isCyclicGraph(){
	isCyclic(g.getVertex(1));
	return cycle;
}

public LinkedList<Graph.Vertex> topologicalOrder(Graph g){
	
	Iterator<Vertex> it=g.iterator();
	if(isCyclicGraph()){
		return null;
	}
	return dfs(it);
}
 /* 
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
public int stronglyConnectedComponents() { 
	LinkedList<Vertex> result=new LinkedList<Vertex>(dfs(this.g.iterator()));
	this.decFinList.clear();
	reverseEdges(g);
	LinkedList<Vertex> result2= dfs(result.iterator());
	return cno;
}

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
	TopologicalOrder t=new TopologicalOrder(g);
	System.out.println(t.isCyclicGraph());
	//System.out.println(t.stronglyConnectedComponents());
	/*t.topologicalOrder(g);
	t.printList();
	System.out.println("topological order 2");
	t.topologicalOrder1();
	t.printList();*/
}
}
