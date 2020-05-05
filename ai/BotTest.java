package ai;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import model.Environment;
import model.GameModel;
import model.Obstacle;
import model.Surface;
import utility.LevelManager;
import utility.Vector2D;

public class BotTest {

    private File resultFile = new File("files/test_results/results.txt");

    //init test environments
    private ArrayList<Environment> envs = new ArrayList<>();
    
    //amount of environments
    private final int ENVCOUNT = 10;

    //amount of variations per environment
    int vars;

    //operation and target/start location of envs, for set of all simulations
    double[][] envsData;
    int[] alg1O;
    int[] alg2O;

    //init test objects
    GameModel model = new GameModel();
    AbstractBot botG = new GeometricBot(model);
    AbstractBot botS = new SwarmBot(model);

    //process outcome
    int sumO1 = 0;
    int sumO2 = 0;

    //operations max (value first, index second)
    public double[] alg1OMax = {0,0};
    double[] alg2OMax = {0,0};

    //operations min
    double[] alg1OMin = {0,0};
    double[] alg2OMin = {0,0};

    //best difference of operations for each alg
    Environment bigDifEnv = null;
    double[] alg1OD = {0,0};
    double[] alg2OD = {0,0};

    //average amount of operations
    double alg1OA = 0;
    double alg2OA = 0;

    public BotTest(int vars) {
        //get test environments
    	for (int i = 0; i < ENVCOUNT; i++) {
    		envs.add(generateRandomEnvironment(5, 10, 5, 10, 200, 500, 200, 500, 20, 100));
    	}
        this.vars = vars;

        //init
        envsData = new double[envs.size() * vars][4];
        alg1O = new int[envs.size() * vars];
        alg2O = new int[envs.size() * vars];
    }

    public void runTest(){

        //run test simulations
        for (int i = 0; i < vars*envs.size(); i++) {
            Environment env = envs.get(i/vars);

            //init a random target and start location
            double[] tP = getRandomLocation(env);
            env.setTarget(new Vector2D((float)tP[0],(float)tP[1]));

            double[] sP = getRandomLocation(env);
            env.setStartX((float) sP[0]);
            env.setStartY((float) sP[1]);

            //assign environment to bot simulation
            model.setEnv(env);
            botG.simulation = new SimulationModel(model);
            botS.simulation = new SimulationModel(model);

            System.out.println("tX + tY "+tP[0] + " " +tP[1]);
            System.out.println("sX + sY "+sP[0] + " " +sP[1]);

            //run the alg
            botG.findShot();
            botS.findShot();

            //store outcome and clone of environment
            alg1O[i] = botG.iterations;
            //alg2O[i] = botG.iterations;
            envsData[i] = new double[]{i/vars,tP[0],tP[1],sP[0],sP[1]};
        }

        for(int i = 0; i < alg1O.length; i++){
            if(alg1O[i] < alg1OMin[0]) {
                alg1OMin[0] = alg1O[i];
                alg1OMin[1] = i;
            }
            if(alg1O[i] > alg1OMax[0]) {
                alg1OMax[0] = alg1O[i];
                alg1OMax[1] = i;
            }
            //TODO save maxmin of alg2
            //TODO save index to construct environment
            //if(alg1O[i]/alg2O[i] > alg1OD) alg1OD = alg1O[i]/alg2O[i];
            //if(alg2O[i]/alg1O[i] > alg2OD) alg1OD = alg1O[i]/alg2O[i];
            sumO1 += alg1O[i];
            sumO2 += alg2O[i];
        }

        //operation average
        alg1OA = (double) (sumO1)/alg1O.length;
        alg2OA = (double) (sumO2)/alg2O.length;

        try {
            System.out.println("creating file");
            createOutcomeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createOutcomeFile() throws IOException {
        resultFile.createNewFile();
        PrintWriter writer = new PrintWriter(resultFile);

        String data = "Results: \n";
        data = "Average: "+ alg1OA + " " + alg2OA;
        writer.println(data);

        data = "Sum: " + sumO1 + " " + sumO2;
        writer.println(data);

        //minimum amount for (number of env, target pos, start pos)
        data = "Min1: " + alg1OMin[0] + " for " + envVarToString(envsData[(int)alg1OMin[1]]);
        writer.println(data);

        //minimum amount for (number of env, target pos, start pos)
        data = "Min2: " + alg2OMin[0] + " for " + envVarToString(envsData[(int)alg2OMin[1]]);
        writer.println(data);

        //max amount for ...
        data = "Max1: " + alg1OMax[0] + " for " + envVarToString(envsData[(int)alg1OMax[1]]);
        writer.println(data);


        //max amount for ...
        data = "Max2: " + alg2OMax[0] + " for " + envVarToString(envsData[(int)alg2OMax[1]]);
        writer.println(data);

        for(int i = 0 ; i < alg1O.length; i++){
            data = "Operations 1: " + alg1O[i] + " for " +envVarToString(envsData[i]);
            writer.println(data);
            data = "Operations 2: " + alg2O[i] + " for " +envVarToString(envsData[i]);
            writer.println(data);
        }
        writer.close();

    }

    public String envVarToString(double[] envData){
        return "env nr: " +envData[0]
                + ", tX: " + envData[1] + ", tY: " + envData[2]
                + ", sX: " + envData[3] + ", sY: " + envData[4];
    }
	
    static int currentEnv = 0;
    
	public Environment generateRandomEnvironment(int minXPoints, int maxXPoints, int minYPoints, int maxYPoints, int minWidth, int maxWidth, int minLength, int maxLength, float lowerBound, float upperBound) {
		Random rand = new Random();
		int pointsX = rand.nextInt(maxXPoints - minXPoints) + minXPoints;
		int pointsY = rand.nextInt(maxYPoints - minYPoints) + minYPoints;
		Float[][] points = new Float[pointsY][pointsX];
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[0].length; j++) {
				points[i][j] = lowerBound + (float)Math.random() * (upperBound - lowerBound);
			}
		}
		float width = rand.nextInt(maxWidth - minWidth) + minWidth;
		float length = rand.nextInt(maxLength - minLength) + minLength;
		Environment env = new Environment(new Surface(points, new Float[points.length][points[0].length], width, length), new Obstacle[][]{},
        9.81f, 0, 0, 0, 0);
		double[] start = getRandomLocation(env);
		double[] target = getRandomLocation(env);
		env.setStartX((float)start[0]);
		env.setStartY((float)start[1]);
		env.setTarget(new Vector2D((float) target[0], (float) target[1]));
		LevelManager.saveLevel("TestLevel" + currentEnv, env);
		currentEnv++;
		return env;
	}

    public double[] getRandomLocation(Environment env) {
        double[] p = new double[2];


        //TODO minimum distance of target and start
        p[0] = (double) ((int)(Math.random()*100)*env.getLength()/100); //x
        p[1] = (double) ((int)(Math.random()*100)*env.getWidth()/100); //y

        return p;
    }
}

