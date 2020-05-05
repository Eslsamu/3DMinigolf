package ai;

import model.GameModel;
import utility.Vector2D;

public class SimulationModel extends GameModel{
	

	protected boolean landed;

	/*
	closest position the ball has been to the target
	 */
	protected float[] closestPosition;

	/*
	distance of closest position
	 */
	protected float closestDistance;

	public SimulationModel(GameModel model) {
		//init everything important to copy
		super.maxVelocity = model.getMaxVelocity();
		setEnv(model.getEnv());

	}
	
	
	public void run(Vector2D vel) {
		//init closest distance to start position
		closestPosition = new float[]{env.getStartX(),env.getStartY()};
		closestDistance = getDistance();

		//inject velocity
		getBall().setVelocity(vel);

		//simulated game loop
		while(!landed) {
			if(inWater()) {
				ball.setPosition(ball.getOldPosition());
				return;
			}
			if(checkTarget()) {
				return;
			}
			else { 
			updatePosition();
			//update closest distance to target
			float distance = getDistance();
			if(closestDistance>distance) {
				//Vector2D closestPosition = new Vector2D(ball.getPosition().x.get(),ball.getPosition().y.get());
				closestDistance = distance;
			}/*

			//bot doesn't want to let it bounce of walls for now
			if(calcCurrentPiece(getBall().getPosition().toVector3D())==null) {
				ball.setPosition(ball.getOldPosition());
				return;
			}*/
			
			updateHeight();
			}
		}
	}

	@Override
	public void stopBall(){
		landed = true;
	}
	
	public void reset(){
		//position the ball
		setInitialPos();
		landed = false;

	}

	/*
	return current euclidean distance to target
	 */
	public float getDistance(){
		return (float) Math.sqrt(Math.pow(ball.getPosition().x.get()-env.getTarget().x,2)+
				Math.pow(ball.getPosition().y.get()- env.getTarget().y,2));
	}
}
