package controller;

import java.awt.Point;
import java.util.Arrays;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import model.Environment;
import model.Obstacle;
import model.Surface;
import utility.LevelManager;
import utility.Vector2D;
import utility.Vector4DProperty;
import view.CourseCreatorView;
import view.Group3D;
import view.SurfaceView;

public class CourseCreatorController {

    public CourseCreatorView view;
    @FXML
    public TextField courseName;
    @FXML
    public TextField pointXField;
    @FXML
    public TextField pointYField;
    @FXML
    public TextField lengthField;
    @FXML
    public TextField widthField;
    @FXML
    public TextField xTargetField;
    @FXML
    public TextField yTargetField;
    @FXML
    public TextField xStartField;
    @FXML
    public TextField yStartField;
    @FXML
    public Label nodeX;
    @FXML
    public Label nodeY;
    @FXML
    public TextField heightInput;
    @FXML
    public TextField frictionInput;
    
    public ObservableList<Vector4DProperty> listPoints;
    private Float[][] heightValues, frictionValues;
    private int xPointsAmount;
    private int yPointsAmount;
    private Environment env;
    private Group3D group3D;
    private Point currTile;
    

    public CourseCreatorController(CourseCreatorView view) {
        this.view = view;
        heightValues = new Float[5][5];
        frictionValues = new Float[5][5];
        for (int i = 0; i < heightValues.length; i++) {
        	for (int j = 0; j < heightValues[i].length; j++) {
        		heightValues[i][j] = 0f;
        		frictionValues[i][j] = 0f;
        	}
        }
    }
    
    @FXML
    public void toggleHeightMap() {
    	SurfaceView.showHeightMap = !SurfaceView.showHeightMap;
    	view.showCourse(env);
    }

    @FXML
    public void createCourse() {
    	computeCourse();
    	LevelManager.saveLevel("TestLevel1", env);
    }
    
    public void setGroup3D(Group3D group3D) {
    	this.group3D = group3D;
    	heightInput.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (oldValue) {
					heightValues[currTile.y][currTile.x] = Float.parseFloat(heightInput.getText());
					if (heightValues[currTile.y][currTile.x] != null && frictionValues[currTile.y][currTile.x] != null) {	
						computeCourse();
						group3D.requestFocus();
					}
				}
			}
    	});
    	frictionInput.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (oldValue) {
					frictionValues[currTile.y][currTile.x] = Float.parseFloat(frictionInput.getText());
					if (heightValues[currTile.y][currTile.x] != null && frictionValues[currTile.y][currTile.x] != null) {	
						computeCourse();
						group3D.requestFocus();
					}
				}
			}
    		
    	});
    	group3D.getScene().setOnKeyPressed(e -> handleKeys(e));
    }
    
    private void handleKeys(KeyEvent e) {
    	switch (e.getCode()) {
    	case D:
    		currTile.setLocation(currTile.x + 1, currTile.y);
    		if (currTile.x == heightValues[0].length) {
    			heightValues = refactorArray(heightValues, 0, 1);
    			frictionValues = refactorArray(frictionValues, 0, 1);
    			computeCourse();    			
    		}
    		break;
    	case A:
    		currTile.setLocation(currTile.x - 1, currTile.y);
    		if (currTile.x < 0) {
    			heightValues = refactorArray(heightValues, 0, -1);
    			frictionValues = refactorArray(frictionValues, 0, -1);
    			computeCourse();    			
    		}
    		break;
    	case W:
    		currTile.setLocation(currTile.x, currTile.y - 1);
    		if (currTile.y < 0) {
    			heightValues = refactorArray(heightValues, -1, 0);
    			frictionValues = refactorArray(frictionValues, -1, 0);
    			computeCourse();    			
    		}
    		break;
    	case S:
    		currTile.setLocation(currTile.x, currTile.y + 1);
    		if (currTile.y == heightValues.length) {
    			heightValues = refactorArray(heightValues, 1, 0);
    			frictionValues = refactorArray(frictionValues, 1, 0);
    			computeCourse();    			
    		}
    		break;
    	case DELETE:
    		heightValues[currTile.y][currTile.x] = null;
    		frictionValues[currTile.y][currTile.x] = null;
    		computeCourse();
    		break;
    	default:
    		break;
    	}
    	setTilePosition();
    }
    
    public static Float[][] refactorArray(Float[][] array, int rowPosition, int colPosition) {
    	Float[][] copy = new Float[array.length + (rowPosition == 0 ? 0 : 1)][array[0].length + (colPosition == 0 ? 0 : 1)];
    	System.out.println("Refactoring Array: " + Arrays.deepToString(array) + " : rowPosition: " + rowPosition + " : colPosition: " + colPosition);
    	int realI = rowPosition > -1 ? 0 : 1;
    	int realJ = colPosition > -1 ? 0 : 1;
    	for (int i = 0; i < array.length; i++) {
    		for (int j = 0; j < array[0].length; j++) {
    			copy[realI][realJ] = array[i][j];
    			realJ++;
    		}
    		realI++;
    		realJ = colPosition > -1 ? 0 : 1;
    	}
    	if (rowPosition != 0) {
    		for (int i = 0; i < array[0].length; i++) {
    			copy[rowPosition == -1 ? 0 : array.length][i] = 0f;
    		}
    	}
    	if (colPosition != 0) {
    		for (int i = 0; i < array.length; i++) {
    			copy[i][colPosition == -1 ? 0 : array[0].length] = 0f;
    		}
    	}
    	System.out.println("Returning: " + Arrays.deepToString(copy));
    	return copy;
    }
    
    private void setTilePosition() {
    	findClosestTile();
    	double translateX = env.getLength()/(env.getSurface().getPoints()[0].length - 1)*currTile.x;
		double translateY = env.getWidth()/(env.getSurface().getPoints().length - 1)*currTile.y;
    	group3D.getCourseView().getCurrentTile().setTranslateX(translateX - env.getCenterX());
    	group3D.getCourseView().getCurrentTile().setTranslateZ(translateY - env.getCenterY());
    	System.out.println(translateX + " : " + translateY);
    	Float height = env.calcHeight(new Vector2D((float)translateX, (float)translateY));
    	if (height == null)
    		height = 0f;
    	group3D.getCourseView().getCurrentTile().setTranslateY(height);
    	nodeX.setText(String.valueOf(currTile.x + 1));
    	nodeY.setText(String.valueOf(currTile.y + 1));
    	heightInput.setText(String.valueOf(env.getSurface().getPoints()[currTile.y][currTile.x]));
    	frictionInput.setText(String.valueOf(env.getSurface().getFrictions()[currTile.y][currTile.x]));
    }
    
    private void findClosestTile() {
    	if (currTile.x < 0)
    		currTile.setLocation(0, currTile.y);
    	if (currTile.y < 0)
    		currTile.setLocation(currTile.x, 0);
    	if (currTile.x >= env.getSurface().getPoints()[0].length)
    		currTile.setLocation(env.getSurface().getPoints()[0].length - 1, currTile.y);
    	if (currTile.y >= env.getSurface().getPoints().length)
    		currTile.setLocation(currTile.x, env.getSurface().getPoints().length - 1);
    }

    public void computeCourse() {
        try {
            float xT = Float.parseFloat(xTargetField.getText());
            float yT = Float.parseFloat(yTargetField.getText());
            float xS = Float.parseFloat(xStartField.getText());
            float yS = Float.parseFloat(yStartField.getText());
            float length = Float.parseFloat(lengthField.getText());
            float width = Float.parseFloat(widthField.getText());
            Surface surface = new Surface(heightValues, frictionValues, width, length);
            env = new Environment(surface, new Obstacle[5][5], 9.81f, xT, yT, xS, yS);
            view.showCourse(env);
            currTile = group3D.getCourseView().currTile;
            setTilePosition();
        }catch (NumberFormatException e){
            xTargetField.setText("enter float");
            yTargetField.setText("enter float");
            xStartField.setText("enter float");
            yStartField.setText("enter float");
        }
        heightValues = env.getSurface().getPoints();
        frictionValues = env.getSurface().getFrictions();
    }

    @FXML
    public void addTableItems(){
    	System.out.println("Test");
        listPoints = FXCollections.observableArrayList();
        try {
            xPointsAmount = Integer.parseInt(pointXField.getText());
            yPointsAmount = Integer.parseInt(pointYField.getText());
            int length = Integer.parseInt(this.lengthField.getText());
            int width = Integer.parseInt(this.widthField.getText());
        for(int i = 0; i < xPointsAmount; i++){
            for(int j = 0; j < yPointsAmount ; j++){
                listPoints.add(new Vector4DProperty(length/(xPointsAmount - 1)*i,
                                     width/(yPointsAmount - 1)*j,
                                     0, 0
                ));
            }
        }
        System.out.println(listPoints.size());

        } catch (NumberFormatException e){
            pointXField.setText("5");
            pointYField.setText("5");
            widthField.setText("200");
            lengthField.setText("200");
        }
    }
}
