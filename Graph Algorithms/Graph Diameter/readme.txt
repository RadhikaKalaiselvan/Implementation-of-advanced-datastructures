Implemented breadth-first search (BFS), and solved the problem of finding the diameter of a tree that works as follows:
Run BFS, starting at an arbitrary node as root.  Let u be a node at maximum distance from the root.  Run BFS again, with u as the root.
Output diameter: path from u to a node at maximum distance from u

How to run?
Run GraphDiameterFinder.java, this file accepts a file as input or console input.
For console input enter the number of nodes in graph, then enter no. of edges, enter the edges in the following format: 1 2 0 (if there exists an edge from node 1 to node 2 of weight 0 which means an undirected graph)
