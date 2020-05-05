package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Graph {
	
	Node[][] graph;
	Node goal;
	Node start;
	ArrayList<Point> path ;
	
	PriorityQueue<Node> q = new PriorityQueue<Node>(25, new Comparator<Node>() {
		@Override
		public int compare(Node i, Node s) {
			return (i.getF() - s.getF());
		}
		
	});
	
	public Graph(Obstacle[][] o, Point s, Point g) {
	
	graph = new Node[o.length][o[0].length];
	for(int i = 0; i<o.length; i++) {
		for(int j = 0; j<o[0].length; j++) {
			if(o[i][j] instanceof Wall) {
				graph[i][j] = new Node(i,j,1);}
			else if(o[i][j] instanceof Tree) {
				graph[i][j] = new Node(i,j,2);}
			else {
				graph[i][j] = new Node(i,j,0);
			}
		}
	}
	
	goal = graph[g.x][g.y];
	start = graph[s.x][s.y];
	start.initialize(goal);
	}
	
	
	public ArrayList<Point> run(Node n, Node goal) {
		System.out.println("Node: " +  n.x + ", " + n.y);
		if(n == goal) {
			path(n);
			return path;
		}
		if(n.checked == false) {
			n.checked = true;
			addNeighbours(n.x,n.y);
			run(q.remove(),goal);
		}
		return path;
	}
	
	public void path(Node n){
		path.add(new Point(n.x,n.y));
		if(n!= start) {
			path(n.prev);
		}
	}
	
	public Node getNode(int x, int y) {
		return graph[x][y];
	}
	
	public void addNeighbours(int x, int y) {
		for(int i = Math.max(0, x-1); i<= Math.min(graph.length - 1, x+1); i++){
			for(int j = Math.max(0, y-1); j<= Math.min(graph[0].length - 1, y+1); j++){
				if(i!= x || j!= y) {	
					if(graph[i][j].queued == false) {
						if(i!=x && j!=y) {
							if(graph[i][y].value != 1 && graph[x][j].value != 1) {
								graph[i][j].setF(graph[x][y], goal);
								q.add(graph[i][j]);
								graph[i][j].queued = true;	
								//System.out.println("This neighbour added to q " + graph[i][j].x + graph[i][j].y);
							}
						}
						else {
							graph[i][j].setF(graph[x][y], goal);
							q.add(graph[i][j]);
							graph[i][j].queued = true;
							//System.out.println("This neighbour added to q " + graph[i][j].x + graph[i][j].y);
						}}
									}
				}
		}
	}
	
	public void printPath(Node n) {
		System.out.println("Node: " + n.x + ", " + n.y);
		if(n!=start) {
			printPath(n.prev);
		}
	}
	
	
	
}