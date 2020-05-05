package model;

public class Node {
	
	Node prev;
	boolean checked;
	boolean queued;
	int x;
	int y;
	int value;
	
	int g;
	int h;
	int f;
	
	public Node(int x, int y, int value) {
		this.x = x;
		this.y = y;
		this.value = value;
		if(value!= 0) {
			queued = true;
		}
	}
	
	
	public void calcDistance(Node goal) {
		h = Math.abs(x - goal.x) + Math.abs(y - goal.y);
	}
	
	
	public void setF(Node prevNode, Node goal) {
		prev = prevNode;
		g = prev.g + 1;
		h = Math.abs(x - goal.x) + Math.abs(y - goal.y);
		this.f = g + h;
	}
	
	public void initialize(Node goal) {
		g = 0;
		h = Math.abs(x - goal.x) + Math.abs(y - goal.y);
		this.f = g + h;
		queued = true;
	}
	
	public int getF() {
		return f;
	}
	
	
	
}