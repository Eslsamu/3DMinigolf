package ai;

public class Particle {

    /*
    cost of particle
     */
    protected float cost;

    /*
    cost of personal best (euclidean distance from target)
     */
    protected float costBest = Float.MAX_VALUE;

    /*
    personal minimum (vector)
     */
    protected float[] vP;
    /*
    current location (vector)
     */
    protected float[] vX;

    /*
    current velocity (vector) -> velocity in the swarm, not as a shot
     */
    protected float[] vV;

    public Particle(float[] vX, float[] vV, float[] vP){
        this.vV = vV;
        this.vP = vP;
        this.vX = vX;
    }

       /*
    see java.object.toString()
     */
    public String toString(){
        return "velVector: " + vV[0] + " " + vV[1] + " vX(location): " + vX[0] + " "+ vX[1]+
                " vP(minimum pers): " + vP[0] + " " +vP[1]+ " cost: " + cost
                + " costBest: " + costBest ;
    }
}
