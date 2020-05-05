package view;

import controller.TestController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class TestView {

    private TestController controller;
    private BorderPane root;

    public TestView() throws IOException {
        this.controller = new TestController();
        // read FXML file and setup UI
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/files/fxml/test_screen.fxml"));
        fxmlLoader.setController(controller);
        root = fxmlLoader.load();

        System.out.println(root.getPrefWidth()+" " + root.getPrefHeight());
        Scene scene = new Scene(root,root.getPrefWidth(),root.getPrefHeight());

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
