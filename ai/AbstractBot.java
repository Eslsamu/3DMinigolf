package ai;

import model.GameModel;

public abstract class AbstractBot {

    public int maxIterations = 100000;

    public int iterations;

    public SimulationModel simulation;

    public abstract float[] findShot();
}
