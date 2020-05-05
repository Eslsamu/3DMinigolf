package view;

import java.io.IOException;

import controller.CourseCreatorController;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Environment;
import model.GameModel;
import utility.Vector4DProperty;

public class CourseCreatorView {

    private BorderPane root;
    private Scene scene;

    private Group3D group3D;
    private GameModel model;

    private CourseCreatorController controller;

    private TableView<Vector4DProperty> table;

    @SuppressWarnings({ "unchecked" })
	public CourseCreatorView() throws IOException {
        this.controller = new CourseCreatorController(this);

        // read FXML file and setup UI
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/files/fxml/courseCreator.fxml"));
        fxmlLoader.setController(controller);
        root = fxmlLoader.load();
        
        //set scene
        scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());

        //create dummy model to showcase the created environment#
        model = new GameModel();

        CourseView.inCourseCreator = true;
        //set game group
        group3D = new Group3D(model, fxmlLoader);

        //init listeners
        TextField pointXField = (TextField) fxmlLoader.getNamespace().get("pointXField");
        TextField pointYField = (TextField) fxmlLoader.getNamespace().get("pointYField");
        
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        
        controller.setGroup3D(group3D);
        controller.computeCourse();

    }

    public void showCourse(Environment env){
        model.setEnv(env);
        group3D.update();
    }
}
