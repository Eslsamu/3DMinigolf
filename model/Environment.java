package model;

import java.util.Arrays;

import utility.Vector2D;

public class Environment {

	private Surface surface;
	private float g;
	private Vector2D target;
	private Vector2D start;
	private float length;
	private float width;
	private Obstacle[][] obstacleGrid;
	private float cellLength;
	private float cellWidth;

	public Environment(Surface surface, Obstacle[][] obstacleGrid, float g, float targetX, float targetY, float startX, float startY) {
		System.out.println(Arrays.deepToString(obstacleGrid));
		this.surface = surface;
		this.obstacleGrid = obstacleGrid;
		this.width = surface.getWidth();
		this.length = surface.getLength();
		this.setG(g);
		target = new Vector2D(targetX,targetY);
		start = new Vector2D(startX, startY);
		
		cellLength = length/obstacleGrid[0].length;
		cellWidth = width/obstacleGrid.length;
	}

	public Float calcHeight(Vector2D v) {
		return surface.interpolate(v.x, v.y);
	}

	public Vector2D calcSlope(Vector2D v) {
		Float x = surface.interpolateDX(v.x, v.y);
		Float y = surface.interpolateDY(v.x, v.y);
		return new Vector2D(x, y);
	}
	
	public Float getFriction(Vector2D v) {
		return surface.getFriction(v.x, v.y);
	}
	
	public Surface getSurface() {
		return surface;
	}

	public float getG() {
		return g;
	}

	public void setG(float g) {
		this.g = g;
	}

	
	public Vector2D getTarget(){
		return target;
	}

	public void setTarget(Vector2D t){
		this.target = t;
	}
	
	public float getStartX() { return start.x; }

	public float getStartY() { return start.y; }

	public void setStartX(float startX) {
		start.x = startX;
	}

	public void setStartY(float startY) {
		start.y = startY;
	}
	
	public Obstacle[][] getObstacleGrid(){
		return obstacleGrid;
	}

	public float getCenterX() {
		return length / 2;
	}
	
	public float getCenterY() {
		return width / 2;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getLength() {
		return length;
	}
	
	public float getCellWidth() {
		return cellWidth;
	}
	
	public float getCellLength() {
		return cellLength;
	}

}
