package ai;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import model.Ball;
import model.GameModel;
import utility.Vector2D;

public class GeometricBot extends AbstractBot{

	//change in angle
	private static final float initialDeltaV = 5f;
	//change in force
	private static final float initialDeltaF = 1.2f;

	public GeometricBot(GameModel model){
		super.simulation = new SimulationModel(model);
	}


	/**
	 * @return
	 */
	//TODO velocity increase
	@Override
	public float[] findShot(){
		//draw a line to the target
		//assumes target is on the current course piece
		float targetX = simulation.getEnv().getTarget().x;
		float targetY = simulation.getEnv().getTarget().y;
		
		float startX = simulation.getEnv().getStartX();
		float startY = simulation.getEnv().getStartY();

		float xVel = (targetX-startX);
		float yVel = (targetY-startY);

		//System.out.println(xVel);
		//System.out.println(yVel);

		//compute start velocity dependant on maximum velocity
		float mV = simulation.getMaxVelocity();
		if(Math.abs(xVel)>Math.abs(yVel)){
			yVel = yVel*mV/Math.abs(xVel);
			xVel = mV*Math.signum(xVel);
		} else{
			xVel = xVel*mV/Math.abs(yVel);
			yVel = mV*Math.signum(yVel);
		}

		//System.out.println(xVel);
		//System.out.println(yVel);

		//
		iterations = 0;
		for(int i = 1; i < 5; i++) {

			xVel = xVel / i ;
			yVel = yVel / i ;

			//System.out.println(xVel);
			//System.out.println(yVel);

			//second last deviation
			float lD = Float.MAX_VALUE;
			float dV = initialDeltaV;
			float dF = initialDeltaF;

			while (iterations < maxIterations*i/5) {
				//System.out.println(iterations);
				iterations++;
				simulation.reset();
				simulation.run(new Vector2D(xVel,yVel));

				if (simulation.checkTarget()) {
					//hole in one
					//System.out.println("found perfect hit");
					return new float[]{xVel, yVel};
				} else {
					//System.out.println("xvel: "+xVel);
					//System.out.println("yvel: "+yVel);
					float lX = simulation.closestPosition[0];
					float lY = simulation.closestPosition[1];
					float deviation = (lX - startX) * (targetY - startY) - (lY - startY) * (targetX - startX);
					/*
					System.out.println("deviation = " + deviation);
					System.out.println("landed X:" + lX);
					System.out.println("landed Y:" + lY);
					System.out.println("closest dist:" + simulation.closestDistance);
					*/

					//System.out.println();
					//detect bouncing around a value
					if (lD == deviation && deviation != 0 && iterations > 1) {
						//decrease step size
						dV = dV / 2;
						//System.out.println("dv: " + dV);
					}

					//save deviation in every second step
					if (iterations % 2 == 0) lD = deviation;

					if (deviation > 0) {
						xVel -= dV * Math.signum(targetY - startY);
						yVel += dV * Math.signum(targetX - startX);
					} else if (deviation < 0) {
						xVel += dV * Math.signum(targetY - startY);
						yVel -= dV * Math.signum(targetX - startX);
					} else {
						/*System.out.println("more power needed " + iterations);
						System.out.println(xVel);
						System.out.println(yVel);
						*/
						if ((xVel += dF) <= simulation.getMaxVelocity() && (yVel += dF) <= simulation.getMaxVelocity()) {
							xVel *= dF;
							yVel *= dF;
						} else if(xVel == simulation.getMaxVelocity()) {
							//System.out.println("random change in velocity");
							xVel = (float) (xVel * Math.random());
							yVel = (float) (xVel * Math.random());
							dV =  initialDeltaV;
						} else{
							dF /= 2;
						}
					}
					//System.out.println("state="+state);
				}
			}
		}
		//System.out.println("couldn't find a shot");
		return new float[]{xVel, yVel};
	}

}

