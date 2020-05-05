package ai;

import model.Environment;
import model.GameModel;
import utility.Vector2D;

public class SwarmBot extends AbstractBot{

    /*
    swarm of particles
     */
    protected Particle[] swarm;

    /*
    particle with global minimum cost
     */
    protected Particle pG;

    /*
    target location in simulation environment
     */
    public float targetX;
    public float targetY;

    /*
    maximum velocity magnitude
     */
    protected float mV;

    /*
    vV inertia-coefficient
     */
    protected float w = 0.25f;

    /*
    wdamp reduces inertia-coefficient at each epoch
     */
    protected float wdamp = 0.95f;

    /*
    vP acceleration-coefficient
     */
    protected float c1 = 0.5f;

    /*
    vG acceleration-coefficient
     */
    protected float c2 = 0.5f;

    public SwarmBot(GameModel model) {
        super.simulation = new SimulationModel(model);

        targetX = simulation.getEnv().getTarget().x;
        targetY = simulation.getEnv().getTarget().y;
    }

    @Override
    public float[] findShot() {
        //init swarm
        int populationSize = 20;
        swarm = new Particle[populationSize];
        setupParticles();

        //run
        iterations = 0;
        int epoch = 0;
        while(iterations < maxIterations) {
            if(epoch>maxIterations/100) {
                System.out.println("reset");
                setupParticles();
            }
            double averageDistance = 0;
            epoch++;
            //System.out.println("---epoch "+epoch);
            //TODO seperate two for loops to first get new location and then evaluate
            for(int i = 0; i < swarm.length; i++) {
                iterations++;
                float[] vX = swarm[i].vX;

                //simulate shot
                simulation.reset();
                simulation.run(new Vector2D(vX[0],vX[1]));

                //return if shot hit the target
                if(simulation.checkTarget()) return new float[]{vX[0],vX[1]};
                //else evaluate position cost
                else {
                    float distance = simulation.closestDistance;
                    //System.out.println("dist:"+distance);
                    //assign new particle cost
                    swarm[i].cost = distance;
                    averageDistance+=distance;

                    //assign new personal minimum
                    if(swarm[i].costBest>distance) swarm[i].costBest = distance;

                    //assign new global minimum
                    if(pG == null || pG.cost>distance) {
                        //System.out.println("new global best ---------------------");
                        //System.out.println(swarm[i].toString());
                        pG = swarm[i];
                    }
                }
            }
            for(int i = 0; i < swarm.length; i++){
                //System.out.println("move particle "+i);
                //System.out.println(swarm[i].toString());
                //let particle move
                newLocation(swarm[i]);
            }
            w = w*wdamp;
            averageDistance = averageDistance/swarm.length;
            //System.out.println("average "+ averageDistance);
        }
        //return best candidate if shot couldn't be found in max iterations
        //System.out.println(pG.cost);
        //System.out.println(pG.costBest);
        return pG.vX;
    }

    /*
   @param i particle to move
   new velocity = inertia-term + cognitive component + social component
    */
    public void newLocation(Particle i) {
        float[] r1 = new float[]{(float) Math.random(), (float) Math.random()};
        float[] r2 = new float[]{(float) Math.random(), (float) Math.random()};


        //System.out.println("intertia: "+w*i.vV[0]+" "+w*i.vV[1]);
        //System.out.println("cognitive: "+c1*r1[0]*(i.vP[0]-i.vX[0])+" "+c1*r1[1]*(i.vP[1]-i.vX[1]));
        //System.out.println("social: "+c2*r2[0]*(pG.vX[0]-i.vX[0])+" "+c2*r2[1]*(pG.vX[1]-i.vX[1]));

        i.vV[0] = w*i.vV[0] + c1*r1[0]*(i.vP[0]-i.vX[0]) + c2*r2[0]*(pG.vX[0]-i.vX[0]);
        i.vV[1] = w*i.vV[1] + c1*r1[1]*(i.vP[1]-i.vX[1]) + c2*r2[1]*(pG.vX[1]-i.vX[1]);

        //change sign of velocity if particle reaches boundary of velocity magnitude
        if(Math.abs(i.vX[0]+i.vV[0])>=Math.abs(mV)) {
            i.vV[0] = - i.vV[0]/2;
        }
        if(Math.abs(i.vX[1]+i.vV[1])>=Math.abs(mV)){
            i.vV[1] = - i.vV[1]/2;
        }

        //System.out.println("vV: "+i.vV[0]+" "+i.vV[1]);

        i.vX[0] += i.vV[0];
        i.vX[1] += i.vV[0];
    }

    /*
    initializes an existing population of particles with randomized start locations
     */
    public void setupParticles(){
        //get approximate shot direction
        Environment env = simulation.getEnv();
        float xVel = env.getTarget().x - env.getStartX();
        float yVel = env.getTarget().y - env.getStartY();

        //compute start velocity dependant on maximum velocity
        mV = simulation.getMaxVelocity();
        if(Math.abs(xVel)>Math.abs(yVel)){
            yVel = yVel*mV/Math.abs(xVel);
            xVel = mV*Math.signum(xVel);
        } else{
            xVel = xVel*mV/Math.abs(yVel);
            yVel = mV*Math.signum(yVel);
        }

        for(int i = 0; i < swarm.length; i++){
            //assign random location in start direction
            float velX = (float) (xVel*Math.pow(Math.random(),2));
            float velY = (float) (yVel*Math.pow(Math.random(),2));
            //init with random location, empty vel, and initial location as best location
            swarm[i] = new Particle(new float[]{velX, velY}, new float[2], new float[]{velX, velY});
        }
    }
}
