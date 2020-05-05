package view;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.Environment;
import model.GameModel;
import model.Surface;
import utility.PointCloudBuilder;
import utility.Vector3D;

public class MapView extends Group{

	private GameModel model;
	private double paneLength;
	private double paneWidth;
	
	private Circle ball;
	private Group pieces;
	private Circle hole;
	
	public MapView(GameModel model, double length, double width) {
		this.model = model;
		this.paneLength = length;
		this.paneWidth = width;
	}
	
	public void update() {
		this.getChildren().removeAll(ball,pieces,hole);
		addSurface();
		addBall();
		addHole();
	}
	
	public void addBall() {
		double fD = getDimensionFactor();
		//ballradius
		ball = new Circle(model.getTargetRadius());

		ball.centerYProperty().bind(model.getBall().getPosition().x.multiply(fD));
		ball.centerXProperty().bind(model.getBall().getPosition().y.multiply(fD));
		
		ball.setFill(Color.WHITE);
		
		getChildren().add(ball);
	}
	
	public void addHole() {
		double fD = getDimensionFactor();
		//hole radius 
		hole = new Circle(model.getEnv().getTarget().x*fD,model.getEnv().getTarget().y*fD,model.getTargetRadius());
		
		getChildren().add(hole);
	}
	
	//TODO fix displacement
	public void addSurface() {
		pieces = new Group();
		double fD = getDimensionFactor();
		Surface surface = model.getEnv().getSurface();
			//otherwise get a cloud of points of the coursepiece
			ArrayList<Vector3D> points = PointCloudBuilder.buildCloud(surface, surface.getLength()/Surface.INTERVAL_FRACTION);
			for(Vector3D point: points) {
				Rectangle area = new Rectangle(point.x*fD,point.y*fD,fD*surface.getWidth()/Surface.INTERVAL_FRACTION,
						fD*surface.getLength()/Surface.INTERVAL_FRACTION
						);
				Color c = surface.getTexture().getPixelReader().getColor((int)Math.min(surface.getLength() - 1, point.x), (int)Math.min(surface.getWidth() - 1, point.y));
				//check if in water
				if(point.z >= 0) {
					//increase red level to show height difference
					double redlevel = point.z/100;
					redlevel = redlevel*255;
					if (redlevel > 255)
						redlevel = 255;
					c = Color.rgb((int) redlevel,(int) (c.getGreen()*255),(int) (c.getBlue()*255));
				} else
				c = Color.BLUE;
				area.setFill(c);
				this.pieces.getChildren().add(area);
			}
			getChildren().add(this.pieces);
	}
	
	public double getDimensionFactor() {
		Environment env = model.getEnv();
		return env.getLength() > env.getWidth() ? paneLength/env.getLength() : paneWidth/env.getWidth();
	}
	
}
