package model;

import utility.Vector2D;
import utility.Vector3D;
import utility.Vector3DProperty;


public class Ball {

	private Vector3DProperty position;
	private Vector2D velocity;
	private Vector2D acceleration;

	/*
	 * last position of the ball
	 * is used to determine a collision
	 */
	private Vector3D oldPosition;


	public Ball() { 
		position = new Vector3DProperty(0,0,0);
		velocity = new Vector2D(0,0);
		acceleration = new Vector2D(0,0);
	}
	
	public Ball(Vector3D position) {
		this.position = new Vector3DProperty(position);
		velocity = new Vector2D(0,0);
		acceleration = new Vector2D(0,0);
	}
	
	public Vector3DProperty getPosition(){
		return position;
	}
	
	public void setPosition(Vector3DProperty position) {
		oldPosition = this.position.toVector3D();
		this.position.x.set(position.x.get());
		this.position.y.set(position.y.get());
		this.position.z.set(position.z.get());
	}
	
	public void setPosition(Vector3D position){
		oldPosition = this.position.toVector3D();
		this.position.x.set(position.x);
		this.position.y.set(position.y);
		this.position.z.set(position.z);
	}

	public void setPosition(float x, float y, float z){
		oldPosition = this.position.toVector3D();
		this.position.x.set(x);
		this.position.y.set(y);
		this.position.z.set(z);
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public void setVelocity(float x, float y){
		this.velocity.x = x;
		this.velocity.y = y;
	}

	public void reset(){
		this.position.x.set(0);
		this.position.y.set(0);
		this.position.z.set(0);

		stopMovement();
	}

	public void stopMovement(){
		this.velocity.x = 0;
		this.velocity.y = 0;

		this.acceleration.x = 0;
		this.acceleration.y = 0;
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setAcceleration(Vector2D acceleration){
		this.acceleration = acceleration;
	}

	public Vector3D getOldPosition(){
		return oldPosition;
	}

	public Vector2D getAcceleration() {
		return acceleration;
	}
}
