package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.GameModel;
import utility.Vector2D;
import view.SettingsView;

public class SettingsController{
	
	
	@FXML TextField inputGravity;
	@FXML TextField xStartField;
	@FXML TextField yStartField;
	@FXML TextField xTargetField;
	@FXML TextField yTargetField;

	private float newG;
	private GameModel model;
	private SettingsView view;
	
	public SettingsController(GameModel model, SettingsView view) {
		this.model = model;
		this.view = view;
	}
	
	@FXML
	public void submitChanges() {
		try {
			//TODO friction, exception handler (invalid position or invalid string)
			String g = inputGravity.getText();
			if(g.compareTo("")!=0){ //ignore if empty
				newG = Float.parseFloat(g);
				model.getEnv().setG(newG);
			}

			String xStart = xStartField.getText();
			String yStart = yStartField.getText();
			String xTarget = xTargetField.getText();
			String yTarget = yTargetField.getText();


			if(xStart.compareTo("")!=0) {//ignore if empty
				float xS = Float.parseFloat(xStart);
				model.getEnv().setStartX(xS);
			}

			if(yStart.compareTo("")!=0) {//ignore if empty
				float yS = Float.parseFloat(yStart);
				model.getEnv().setStartY(yS);
			}

			if(xTarget.compareTo("")!=0) {//ignore if empty
				float xT = Float.parseFloat(xTarget);
				model.getEnv().setTarget(new Vector2D(xT, model.getEnv().getTarget().y));
			}

			if(yTarget.compareTo("")!=0) {//ignore if empty
				float yT = Float.parseFloat(yTarget);
				model.getEnv().setTarget(new Vector2D(model.getEnv().getTarget().x, yT));
			}

			view.close();
		} catch (NumberFormatException e){
			inputGravity.setText("enter float number");
			xStartField.setText("enter float number");
			yStartField.setText("enter float number");
			xTargetField.setText("enter float number");
			yTargetField.setText("enter float number");
		}
    }

	
}