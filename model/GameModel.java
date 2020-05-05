package model;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.Observable;

import controller.GameEvent;
import controller.GameEvent.GameEventType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import utility.LevelManager;
import utility.Vector2D;
import utility.Vector3D;


public class GameModel extends Observable {
	
	private File botLevel = new File("src/files/levels/botLevel.txt");
	private File defaultLevel = new File("levels/Level1.txt");
	private File testLevel = new File("levels/test.txt");

	protected Environment env;
	protected Ball ball;
	
	private Float xCo, yCo, intersectX;
	
	//hole collision --> should be relative to course
	private double collisionRadius = 2.5;

	//must be set or a player can increase the velocity to a ridiculous amount to avoid gravity
	protected int maxVelocity = 500;

 	//stepsize of approximation function
	private int stepsize = 60;

	/*
	 * this timeline is keep updating the position of the ball after it has been hit
	 */
	private Timeline ballTimer;
	/*
	keeps track of the time that has passed in every frame of the ballTimer
	 */
	private int shotTime = 1;

	public GameModel() {
		ball = new Ball();
		env = LevelManager.loadLevel(botLevel);
		//System.out.println(env.toString());
	}
	
	public void startGame() {
		//setup ball timer
		ballTimer = new Timeline();
		KeyFrame mainFrame =
				new KeyFrame(Duration.millis(1000f / stepsize), e -> ballLoop());
		ballTimer.getKeyFrames().add(mainFrame);
		ballTimer.setCycleCount(Timeline.INDEFINITE);
		ballTimer.stop();

		//setup balls initial position
		setInitialPos();

		//tells the view that the game has started
		setChanged();
		notifyObservers(new GameEvent(GameEventType.START));
	}
	
	public boolean inWater() {
		return (ball.getPosition().z.get()<-1) ? true : false;
	}
	
	public void hitBall(Vector2D velocity) {
		ball.setVelocity(velocity);
		//starts the TimeLine for the ballLoop 
		ballTimer.play();
	}
	
	public void ballLoop() {
		//check if ball landed in water
		if(inWater()) {
			stopBall();
			startGame();
		}
		//check if the ball is in the hole
		if(checkTarget()) {
			stopBall();
			setChanged();
			notifyObservers(new GameEvent(GameEventType.WIN));
			//TODO
			startGame();
		}

		else { 
			updatePosition();
			reflectBall();
			updateHeight();
			setChanged();
			notifyObservers(new GameEvent(GameEventType.MOVEMENT));
		}
	}
	
	public void updateHeight() {
		 float h = env.calcHeight(ball.getPosition().toVector3D().toVector2D());
		 ball.getPosition().z.set(h);
	}
	
	public void setInitialPos() {
		ball.reset();

		float x = env.getStartX();
		float y = env.getStartY();

		ball.setPosition(x,y,0);

		updateHeight();

		stopBall();

		setChanged();
		notifyObservers(new GameEvent(GameEventType.MOVEMENT));
	}

	public void stopBall(){
		ballTimer.stop();
		shotTime = 0;

		ball.stopMovement();

		setChanged();
		notifyObservers(new GameEvent(GameEventType.BALLSTOP));
	}


	/*
	 * 
	 */		
	public void updatePosition() {
		float h = 1/(float)stepsize;

		runge_kutta(ball.getPosition().toVector3D(),ball.getVelocity(),h);

		//stopping condition (seperate friction force)

	}

	public Vector2D computeAcc(Vector2D pos, Vector2D vel) {
		//this is how it should work like
		//CoursePiece cp = calcCurrentPiece(new Vector3D(pos.x,pos.y,0));

		float g = env.getG();
		float f = env.getFriction(pos);
		//slope
		Vector2D dG = new Vector2D((float)Math.sin(Math.atan((double) pos.x)),
								   (float) Math.sin(Math.atan((double)pos.y)));

		Vector2D frictionForce = new Vector2D();
		frictionForce.x = (float) -(f*g*vel.x/(Math.sqrt(Math.pow(vel.x, 2) + Math.pow(vel.y, 2))));
		frictionForce.y = (float) -(f*g*vel.y/(Math.sqrt(Math.pow(vel.x, 2) + Math.pow(vel.y, 2))));

		/*//stopping condiditon
		if(Math.abs(xVel) <= Math.abs(xKinForce) && Math.abs(yVel) <= Math.abs(yKinForce)){
			stopBall();
		}*/

		Vector2D gravity = dG.scale(-g);

		Vector2D acc = gravity.add(frictionForce);
		return acc;
	}


	public void runge_kutta(Vector3D position, Vector2D vel, float h){
		Vector2D p = new Vector2D(position.x,position.y);

		//h*f(t,w0)
		Vector2D[] k1 = derivative(p,vel,new Vector2D(0,0), new Vector2D(0,0), 0);

		//h*f(t+0.5h,w0+0.5k1)
		Vector2D[] k2 = derivative(p,vel,k1[0].scale(0.5f), k1[1].scale(0.5f),h*0.5f);

		//h*f(t+0.5h,w0+0.5k2)
		Vector2D[] k3 = derivative(p, vel, k2[0].scale(0.5f), k2[1].scale(0.5f),h*0.5f);

		//h*f(t+h,w0+k3)
		Vector2D[] k4 = derivative(p, vel, k3[0], k3[1], h);

		Vector2D dv = (k1[1].add(k2[1].scale(2f)).add(k3[1].scale(2f)).add(k4[1])).scale(1/6f);
		Vector2D dp = (k1[0].add(k2[0].scale(2f)).add(k3[0].scale(2f)).add(k4[0])).scale(1/6f);

		ball.setVelocity(vel.add(dv.scale(h)));
		ball.setPosition(p.x+dp.x*h, p.y+dp.y*h,position.z);
		System.out.println(ball.getPosition().toVector3D().toString());
	}

	public Vector2D[] derivative(Vector2D pos, Vector2D vel, Vector2D d, Vector2D d2, float dt){
		Vector2D p_new = pos.add(d.scale(dt));
		Vector2D v_new = vel.add(d2.scale(dt));

		Vector2D d_new = v_new;
		Vector2D d2_new = computeAcc(p_new, v_new);
		return new Vector2D[]{d_new,d2_new};
	}

	private Point checkCollision(){
		Vector2D b = ball.getPosition().toVector3D().toVector2D();

		Obstacle[][] grid = env.getObstacleGrid();

		//determine position of ball in the grid
		int row = (int) ((b.y * grid.length / env.getLength()));
		int col = (int) ((b.x * grid[0].length / env.getWidth()));
		System.out.println("Row: " + row + " : Col: " + col);
		if (grid[row][col] != null)
			return new Point(col, row);
		return null;
	}

	private void reflectBall() {
		Point collision = checkCollision();
		//System.out.println(collision);
		if(collision != null) {
			Vector3D pos = ball.getPosition().toVector3D();
			Vector3D oldPos = ball.getOldPosition();

			//get current and previous coordinates of the ball
			float x = pos.x;
			float y = pos.y;
			float oldX = oldPos.x;
			float oldY = oldPos.y;
			
			
			float cellLength = 0;
			float cellWidth = 0;
			float leftTopX = 0;
			float leftTopY = 0;
			
			Line2D l = new Line2D.Float(oldX, oldY, x, y);
			if (env.getObstacleGrid()[collision.y][collision.x] instanceof Tree) {
				cellLength = env.getCellLength() / 2;
				cellWidth = env.getCellWidth() / 2;
				leftTopX = collision.x * env.getCellLength() + cellLength / 2;
				leftTopY = collision.y * env.getCellWidth() + cellWidth / 2;
			}
			if (env.getObstacleGrid()[collision.y][collision.x] instanceof Wall) {
				cellLength = env.getCellLength();
				cellWidth = env.getCellWidth();
				leftTopX = collision.x * cellLength;
				leftTopY = collision.y * cellWidth;
			}
			System.out.println("CellLength: " + cellLength + " : CellWidth: " + cellWidth + " : LeftTopX: " + leftTopX + " : LeftTopY: " + leftTopY + " : currentXY: " + x + " : " + y + " : oldXY: " + oldX + " : " + oldY);
			//Objects have just 4 faces
			//TOP FACE'
			if (l.intersectsLine(new Line2D.Float(leftTopX, leftTopY, leftTopX + cellLength, leftTopY))) {
				ball.setVelocity(ball.getVelocity().x, -ball.getVelocity().y);
				ball.setPosition(oldPos);
				return;
			}
			//RIGHT FACE
			if (l.intersectsLine(new Line2D.Float(leftTopX + cellLength, leftTopY, leftTopX + cellLength, leftTopY + cellWidth))) {
				ball.setVelocity(-ball.getVelocity().x, ball.getVelocity().y);
				ball.setPosition(oldPos);
				return;
			}
			//BOTTOM FACE
			if (l.intersectsLine(new Line2D.Float(leftTopX, leftTopY + cellWidth, leftTopX + cellLength, leftTopY + cellWidth))) {
				ball.setVelocity(ball.getVelocity().x, -ball.getVelocity().y);
				ball.setPosition(oldPos);
				return;
			}
			//LEFT FACE
			if (l.intersectsLine(new Line2D.Float(leftTopX, leftTopY, leftTopX, leftTopY + cellWidth))) {
				ball.setVelocity(-ball.getVelocity().x, ball.getVelocity().y);
				ball.setPosition(oldPos);
				return;
			}
		}
	}
	
	/*private void findXIntersect(float wall, Vector3D pos, Vector3D oldPos){
		if((wall> pos.x && wall< oldPos.x) || (wall< pos.x && wall> oldPos.x)) {
			xCo = wall;
			// (xCo, intersectY) is an intersection point
		}
	}

	private void findYIntersect(float wall, float m, float q, Vector3D pos, Vector3D oldPos){
		if((wall> pos.y && wall< oldPos.y)||(wall< pos.y && wall> oldPos.y)) {
			intersectX = (wall - q)/m;
			yCo = wall;
			// (intersectX, yCo) is an intersection point
		}
	}
		
		
	private void findFirstIntersect(Vector3D pos, Vector3D oldPos) {
		//TODO Fix which option is chosen
		Vector2D vel = ball.getVelocity();
		if (xCo == null) {
			ball.setVelocity(vel.x,-vel.y);
		} else if (yCo == null) {
			ball.setVelocity(-vel.x,vel.y);
		} else if(xCo - oldPos.x <= intersectX - oldPos.x) {
			//(xCo, intersectY) first intersect (-1)* X Vel
			ball.setVelocity(-vel.x,vel.y);
		} else {
			//(intersectX, yCo) first intersect (-1)* Y vel
			ball.setVelocity(vel.x,-vel.y);
		}
		ball.setPosition(oldPos.x,oldPos.y, oldPos.z);
	}*/
	

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}
	
	public boolean checkTarget() {
		float distance = (float)(Math.pow((ball.getPosition().x.get() - env.getTarget().x), 2)
				+ Math.pow((ball.getPosition().y.get() - env.getTarget().y), 2));
		return distance <= Math.pow(collisionRadius, 2);
	}

	public int getStepsize() {
		return stepsize;
	}

	public int getMaxVelocity() {return maxVelocity;}

	public double getTargetRadius(){
		return collisionRadius;
	}
}
