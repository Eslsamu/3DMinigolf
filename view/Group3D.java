package view;

import controller.CameraController;
import javafx.fxml.FXMLLoader;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.GameModel;

public class Group3D extends Group {

    private SubScene gameScene;
    private CourseView courseView;
    private PerspectiveCamera camera;
    private CameraController cameraController;

    private GameModel model;

    public Group3D(GameModel model, FXMLLoader fxmlLoader){
        this.model = model;
        //set game scene
        gameScene = (SubScene) fxmlLoader.getNamespace().get("gameScene");
        //bind size to the parent anchor pane
        gameScene.heightProperty().bind(((Pane) fxmlLoader.getNamespace().get("gamePane")).heightProperty().multiply(0.8));
        gameScene.widthProperty().bind(((Pane) fxmlLoader.getNamespace().get("gamePane")).widthProperty().multiply(0.8));

        gameScene.setRoot(this);

        //set course view
        courseView = new CourseView(model);
        this.getChildren().add(courseView);

        //bind course view layout position to game scene
        gameScene.heightProperty().addListener((arg,oldV,newV) -> courseView.setLayoutX(newV.doubleValue()*0.3));
        gameScene.widthProperty().addListener((arg,oldV,newV) -> courseView.setLayoutY(newV.doubleValue()*0.3));

        //set camera
        camera = new PerspectiveCamera();
        this.getChildren().add(camera);
        cameraController = new CameraController(camera, this);

        //set ambient light
        AmbientLight light = new AmbientLight();
        this.getChildren().add(light);
    }

    public void update() {
    	courseView.update();
    }

    public CourseView getCourseView() {
        return courseView;
    }

    public SubScene getGameScene() {
        return gameScene;
    }

    public GameModel getModel() {
        return model;
    }
}
