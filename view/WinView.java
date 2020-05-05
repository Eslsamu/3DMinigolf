package view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class WinView {
	
	private final AnchorPane root;
	private Stage winStage;
	private Scene winScene;
	
	public WinView() throws IOException {
		// read FXML file and setup UI
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/files/fxml/winScreen.fxml"));
	    root = fxmlLoader.load();
	    
	    ImageView imv = new ImageView("/files/pictures/winpic.jpg");
	    root.getChildren().add(imv);
	    
	    winScene = new Scene(root, 300, 200);
		winStage = new Stage();
		winStage.setScene(winScene);
		winStage.show();
	}
	
}
