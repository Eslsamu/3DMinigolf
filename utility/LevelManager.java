package utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import model.*;

public class LevelManager {

	public static Environment loadLevel(File file) {
		Float[][] values, friction;
		Obstacle[][] obstacleGrid;
		int amountTrees, amountWalls;
		int sideLengthX, sideLengthY;
		float g, targetX, targetY, startX, startY, width, length;
		Surface surface;
		try {
			Scanner scanner = new Scanner(file);
			//gravitation
			g = Float.parseFloat(scanner.nextLine());
			//coordinates of the target
			String line = scanner.nextLine();
			targetX = Float.parseFloat(line.substring(0, line.indexOf(';')));
			targetY = Float.parseFloat(line.substring(line.indexOf(';') + 1, line.length()));
			//coordinates of the start
			line = scanner.nextLine();
			startX = Float.parseFloat(line.substring(0, line.indexOf(';')));
			startY = Float.parseFloat(line.substring(line.indexOf(';') + 1, line.length()));
			//total lengthwidth of the environment
			line = scanner.nextLine();
			width = Float.parseFloat(line.substring(0,line.indexOf(';')));
			length = Float.parseFloat(line.substring(line.indexOf(';')+1,line.length()));
			line = scanner.nextLine();
			sideLengthX = Integer.parseInt(line.substring(0, line.indexOf(';')));
			line = updateLine(line);
			sideLengthY = Integer.parseInt(line.substring(0, line.length()));

			//cell dimensions for the obstacle grid
			line = scanner.nextLine();
			int rows = Integer.parseInt(line.substring(0,line.indexOf(';')));
			int cols = Integer.parseInt(line.substring(line.indexOf(';') + 1,line.length()));
			obstacleGrid = new Obstacle[rows][cols];

			//amount of trees and wall cells
			line = scanner.nextLine();
			amountTrees = Integer.parseInt(line.substring(0,line.indexOf(';')));
			amountWalls = Integer.parseInt(line.substring(line.indexOf(';') + 1,line.length()));

			//singleton instances
			Wall wall = Wall.getInstance();
			Tree tree = Tree.getInstance();

			line = scanner.nextLine();
			values = new Float[sideLengthY][sideLengthX];
			for (int i = 0; i < values.length; i++) {
				for (int j = 0; j < values[0].length; j++) {
					try  {
						values[i][j] = Float.parseFloat(line.substring(0, line.indexOf(';')));						
					} catch (NumberFormatException e) {
						values[i][j] = null;
					}
					line = updateLine(line);
				}
			}
			friction = new Float[sideLengthY][sideLengthX];
			line = scanner.nextLine();
			for (int i = 0; i < values.length; i++) {
				for (int j = 0; j < values[0].length; j++) {
					try {
						friction[i][j] = Float.parseFloat(line.substring(0, line.indexOf(';')));
					} catch (NumberFormatException e) {
						friction[i][j] = null;
					}
					line = updateLine(line);
				}
			}
			//trees
			line = scanner.nextLine();
			for(int i = 0; i < amountTrees; i++){
				int row = Integer.parseInt(line.substring(0, line.indexOf(',')));
				int col = Integer.parseInt(line.substring(line.indexOf(',') + 1, line.indexOf(';')));
				line = updateLine(line);
				obstacleGrid[row][col] = tree;
			}

			//inner walls
			line = scanner.nextLine();
			for(int i = 0; i < amountWalls; i++){
				int row = Integer.parseInt(line.substring(0, line.indexOf(',')));
				int col = Integer.parseInt(line.substring(line.indexOf(',') + 1, line.indexOf(';')));
				line = updateLine(line);
				obstacleGrid[row][col] = wall;
			}

			//outer walls
			for(int row = 0; row < obstacleGrid.length; row++){
				obstacleGrid[row][0] = wall;
				obstacleGrid[row][cols-1] = wall;
			}
			for(int col = 0; col < obstacleGrid[0].length; col++){
				obstacleGrid[0][col] = wall;
				obstacleGrid[rows-1][col] = wall;
			}

			surface = new Surface(values, friction, width, length);
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		Environment env = new Environment(surface, obstacleGrid, g, targetX, targetY,startX ,startY);
		return env;
	}
	
	public static void saveLevel(String name, Environment env) {
		try (FileWriter fw = new FileWriter("src/files/levels/" + name + ".txt", false))
		{
			fw.write("");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (FileWriter fw = new FileWriter("src/files/levels/" + name + ".txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) 
		{
			fw.flush();
			out.println(env.getG());
			out.println(env.getTarget().x + ";" + env.getTarget().y);
			out.println(env.getStartX() + ";"  + env.getStartY());
			out.println(env.getWidth() + ";" + env.getLength());
			out.println(env.getSurface().getPoints().length + ";" + env.getSurface().getPoints()[0].length);
			for (Float[] fs : env.getSurface().getPoints()) {
				for(float f : fs) {
					out.print(f + ";");
				}
			}
			for (Float[] fs : env.getSurface().getFrictions()) {
				for(float f : fs) {
					out.print(f + ";");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Environment> getTestEnvironments(String directory){
		File dir = new File(directory);
		File[] directoryListing = dir.listFiles();

		ArrayList<Environment> envList = new ArrayList<Environment>();

		if (directoryListing != null) {
			for (File child : directoryListing) {
				Environment env = loadLevel(child);
				envList.add(env);
			}
		} else {
			// Handle the case where dir is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another process that deletes
			// directories.
		}
		return envList;
	}

	private static String updateLine(String line) {
		return line.substring(line.indexOf(';') + 1, line.length());
	}

}



