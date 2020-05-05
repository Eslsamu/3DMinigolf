package controller;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import ai.SwarmBot;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Ball;
import model.GameModel;
import utility.LevelManager;
import utility.Vector2D;
import view.CourseCreatorView;
import view.Group3D;
import view.MapView;
import view.SettingsView;
import view.SurfaceView;
import view.TestView;
import view.WinView;

public class MainController implements Observer{
	
	private GameModel model;
	private Stage stage;
	private Group3D group3D;
	private MapView map;

	//private SettingsController settings;
	
	@FXML Slider sliderRight;
	@FXML Slider sliderLeft;
	@FXML Button hitButton;
	@FXML Label xPositionLabel;
	@FXML Label yPositionLabel;
	@FXML Label xVelLabel;
	@FXML Label yVelLabel;
	@FXML Label slopeXLabel;
	@FXML Label slopeYLabel;
	@FXML Label xAccLabel;
	@FXML Label yAccLabel;
	@FXML Label frictionLabel;
	@FXML TextField xVelField;
	@FXML TextField yVelField;
	@FXML MenuItem constants;
	@FXML ToggleButton toggleBot;
	
	public MainController(GameModel model) {
		this.model = model;
	}
	
	public void startBot() {
	    SwarmBot bot = new SwarmBot(model);
		float[] vel = bot.findShot();
		xVelField.setText(Float.toString(vel[0]));
		yVelField.setText(Float.toString(vel[1]));
	}

	public void testBot() {
        try {
            new TestView();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@FXML
	public void toggleHeightMap() {
		SurfaceView.showHeightMap = !SurfaceView.showHeightMap;
		group3D.getCourseView().update();
	}
    
    /*
     * is activated when hit button is clicked
     */
    @FXML
    public void hit() {
    	float xVel= 0;
        float yVel = 0;
    	try {
    	   	xVel = Float.parseFloat(xVelField.getText());
    	}
    	catch(NumberFormatException e) {
    	   	xVelField.setText("enter value");
    	}
    	try {
    	  	yVel = Float.parseFloat(yVelField.getText());
    	}
    	catch(NumberFormatException e) {
    	   	yVelField.setText("enter value");
    	}
    	model.hitBall(new Vector2D(xVel,yVel));
    }
    
    public void close(){
    	System.exit(0);
    }
    
    public void startGame(){
    	model.startGame(); 
    }
    
    @FXML
    public void openFile(){
    	FileChooser fileChooser = new FileChooser();
    	File file = fileChooser.showOpenDialog(stage);
    	
    	model.setEnv(LevelManager.loadLevel(file));
    	model.startGame();
    }
    
    @FXML
    public void openSettings() throws IOException {
    	new SettingsView(model);
    }

    @FXML
	public void openCourseCreator() throws IOException {
    	new CourseCreatorView();
	}

    public void gameWon() throws IOException {
    	new WinView();
    }
    
    /*
     * updates the labels in each frame
     * (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
	@Override
	public void update(Observable arg0, Object arg1) {
		GameEvent event = (GameEvent) arg1;
		switch(event.getEventType()) {
			case MOVEMENT:
				Ball b = model.getBall();
				yPositionLabel.setText("Y: " + b.getPosition().y.get());
				xPositionLabel.setText("X: " + b.getPosition().x.get());
				xVelLabel.setText("xVel: " + b.getVelocity().x);
				yVelLabel.setText("yVel: " + b.getVelocity().y);
				xAccLabel.setText("xAcc: " + b.getAcceleration().x);
				yAccLabel.setText("yAcc: " + b.getAcceleration().y);
				slopeXLabel.setText("xθ: " + model.getEnv().calcSlope(b.getPosition().toVector2D()).x);
				slopeYLabel.setText("yθ: " + model.getEnv().calcSlope(b.getPosition().toVector2D()).y);
				frictionLabel.setText("f: "+ model.getEnv().getFriction(b.getPosition().toVector2D()));
				break;
			case START:
				map.update();
				group3D.update();
				break;
			case WIN:
				try {
					gameWon();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
		case BALLSTOP:
			break;
		default:
			break;
		}
	}

	public void bindModeltoView(Group3D group3D, MapView map) {
		this.group3D = group3D;
		this.map = map;

		model.addObserver(this);
	}
}
