package view;


import java.awt.Point;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import model.Environment;
import model.GameModel;
import model.Obstacle;
import model.Tree;
import model.Wall;
import utility.Vector2D;

public class CourseView extends Group {
	
	/*
	 * TODO can be shifted into the model, to make the size of the ball dependent of the other components of the course
	 */
	public static final double BALL_RADIUS = 3.5;


	public static boolean inCourseCreator;
	
	
	/*
	 * rotation of the group object
	 */
	private Rotate xAxis;
	private Rotate yAxis;
	private Rotate zAxis;
	
	
	/*
	 * scale of the group object
	 */
	private Scale scale;
	
	private GameModel model;
	public Point currTile = new Point(0, 0);
	
	private Sphere ballView = new Sphere(CourseView.BALL_RADIUS);
	private Box currentTile;
	private Environment env; 
	
	public CourseView(GameModel model){
		this.model = model;
		this.env = model.getEnv();
		update();
	}
	
	public void update() {
		getChildren().clear();
		createCourse();
		createObstacles();
		createBall();
		createTarget();
		if (inCourseCreator)
			createCurrentTile();
		scale = new Scale(1,1,1);
		
		xAxis = new Rotate(0,0,0,0,Rotate.X_AXIS);
		yAxis = new Rotate(0,0,0,0,Rotate.Y_AXIS);
		zAxis = new Rotate(0,0,0,0,Rotate.Z_AXIS);
		
		//add rotation, transformation and scaling
		/*for (Node n : getChildren()) {
			n.getTransforms().addAll(xAxis, yAxis, zAxis, trans,scale);
		}*/
		getTransforms().addAll(xAxis, yAxis, zAxis, scale);
	}

	public void createCourse(){
		SurfaceView cpv = new SurfaceView(model.getEnv().getSurface());
		this.getChildren().add(cpv);
	}
	
	public void createCurrentTile() {
		currentTile = new Box(10,1,10);
		currentTile.setMaterial(new PhongMaterial(Color.YELLOW));
    	getChildren().add(currentTile);
	}

	public void createObstacles(){
		Environment env = model.getEnv();
		Obstacle[][] obstacleGrid = env.getObstacleGrid();

		for(int r = 0; r < obstacleGrid.length; r++){
			System.out.println();
			for(int c = 0; c < obstacleGrid[0].length; c++){
				if(obstacleGrid[r][c]==Tree.getInstance()) System.out.print("T");
				else if(obstacleGrid[r][c]==Wall.getInstance()) System.out.print("W");
				else System.out.print("0");
			}
		}

		float cellLength = env.getLength()/obstacleGrid.length;
		float cellWidth = env.getWidth()/obstacleGrid[0].length;
		System.out.println("celllength"+cellLength);
		System.out.println("celllwidth"+cellWidth);

		float centerX = env.getCenterX()-cellLength/2;
		float centerY = env.getCenterY()-cellWidth/2;

		for(int row = 0; row < obstacleGrid.length; row++){
			for(int col = 0; col < obstacleGrid[0].length; col++){
				System.out.println();
				System.out.println("row "+row);
				System.out.println("col "+col);
				if(obstacleGrid[row][col]==Wall.getInstance()) {
					//compute coordinates of wall
					float x = row*cellLength ;
					float z = col*cellWidth ;
					float y = model.getEnv().getSurface().interpolate(x,z);

					WallView wallView = new WallView(cellLength, cellWidth);
					wallView.setTranslateX(x - centerX);
					wallView.setTranslateY(y);
					wallView.setTranslateZ(z - centerY);

					this.getChildren().add(wallView);
				}
				else if(obstacleGrid[row][col]==Tree.getInstance()){

					//compute coordinates of tree
					float x = row*cellLength ;
					float z = col*cellWidth ;
					float y = model.getEnv().getSurface().interpolate(x,z);

					TreeView treeView = new TreeView(cellLength/2);
					treeView.setTranslateX(x-centerX);
					treeView.setTranslateY(y);
					treeView.setTranslateZ(z-centerY);
					this.getChildren().add(treeView);
				}
			}
		}
	}


	public void createBall(){
		//setupBall
		ballView.setMaterial(new PhongMaterial(Color.INDIANRED));
		ballView.setCullFace(CullFace.NONE);
		ballView.translateXProperty().bind(model.getBall().getPosition().x.subtract(model.getEnv().getCenterX()));
		ballView.translateYProperty().bind(model.getBall().getPosition().z.add(-BALL_RADIUS)); //y and z are switched in system
		ballView.translateZProperty().bind(model.getBall().getPosition().y.subtract(model.getEnv().getCenterY()));
		this.getChildren().add(ballView);
	}

	//works only on flat pieces
	public void createTarget() {
		Cylinder hole = new Cylinder(BALL_RADIUS,1);
		Vector2D t = model.getEnv().getTarget();
		Float z = model.getEnv().calcHeight(t);
		
		if (z != null) {
			hole.setTranslateX(t.x - model.getEnv().getCenterX());
			hole.setTranslateY(z);
			hole.setTranslateZ(t.y  - model.getEnv().getCenterY());
			hole.setMaterial(new PhongMaterial(Color.BLACK));
			this.getChildren().add(hole);			
		}
	}

	public void rotateX(double angle){
	    xAxis.setAngle(xAxis.getAngle()+angle);
	}
	
	public void rotateY(double angle){
	    yAxis.setAngle(yAxis.getAngle()+angle);
	}
	
	public void rotateZ(double angle){
	    zAxis.setAngle(zAxis.getAngle()+angle);
	}
	
	public void trans(double x, double y, double z) {
		setTranslateX(getTranslateX()+x);
		setTranslateY(getTranslateY()+y);
		setTranslateZ(getTranslateZ()+z);
	}
	
	public void addScale(double x, double y, double z) {
		scale.setX(scale.getX() + x);
		scale.setY(scale.getY() + y);
		scale.setZ(scale.getZ() + z);
	}
	
	public Scale getScale() {
		return scale;
	}
	
	public Sphere getBallView(){
		return ballView;
	}
	
	public Box getCurrentTile() {
		return currentTile;
	}

	
}
