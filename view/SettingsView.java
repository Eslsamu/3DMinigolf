package view;

import java.io.IOException;

import controller.SettingsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.GameModel;

public class SettingsView {
	
	private final AnchorPane root;
	private Scene settingsScene;
	private Stage settingsStage;

	private GameModel model;
	private SettingsController controller;

	public SettingsView(GameModel model) throws IOException {
		controller = new SettingsController(model,this);
		this.model = model;

		// read FXML file and setup UI
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/files/fxml/settings.fxml"));
		fxmlLoader.setController(controller);
		root = fxmlLoader.load();

		settingsScene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());

		settingsStage = new Stage();
		settingsStage.setScene(settingsScene);
		settingsStage.show();
	}

	public void close(){
		settingsStage.close();
	}
}
