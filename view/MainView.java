package view;

import java.io.IOException;

import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import model.GameModel;

public class MainView{
	
	private BorderPane root;
	
	private Scene scene;
	private SubScene gameScene;

	private Pane mapPane;
	
	private Group3D group3D;
	private MapView map;
	

	private MainController controller;
	
	private GameModel model;
	
	public MainView() throws IOException{
		//TODO will be initialized somewhere else
		//TODO singleton
		model = new GameModel();

		//initialize controller
		controller = new MainController(model);

		// read FXML file and setup UI
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/files/fxml/ui.fxml"));
		fxmlLoader.setController(controller);
		root = fxmlLoader.load();
		
		//set scene
		scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());

		//set game group
		group3D = new Group3D(model, fxmlLoader);

		//set map
		mapPane = (Pane) fxmlLoader.getNamespace().get("mapPane");
		map = new MapView(model, getMapPane().getPrefWidth(), getMapPane().getPrefHeight());
		mapPane.getChildren().add(map);

		//bind group3D and map via controller with gameModel
		controller.bindModeltoView(group3D, map);

		//test sliders
		Slider s1 = new Slider();
		s1.setMax(500);
		s1.valueProperty().addListener((arg,oldV,newV) ->
				group3D.getCourseView().rotateX(oldV.doubleValue() - newV.doubleValue()));;
		FlowPane testPane = (FlowPane) fxmlLoader.getNamespace().get("bottomPane");
		testPane.getChildren().add(s1);

	}
	
	public BorderPane getRoot() {
		return root;
	}

	public void setRoot(BorderPane root) {
		this.root = root;
	}
	

	public Pane getMapPane() {
		return mapPane;
	}

	public void setMapPane(Pane mapPane) {
		this.mapPane = mapPane;
	}

	public Scene getScene() {
		return scene;
	}

	public SubScene getGameScene(){return gameScene;}

	public GameModel getModel(){return model;}
}
