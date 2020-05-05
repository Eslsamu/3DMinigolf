package application;
	


import java.io.OutputStream;
import java.io.PrintStream;

import controller.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.GameModel;
import view.MainView;
import view.SettingsView;
import view.WinView;


public class Minigolf3D extends Application{
	
	public static Stage mainWindow;
	
	@Override
	public void start(Stage mainWindow) throws Exception {
		PrintStream dummyStream = new PrintStream(new OutputStream(){
		    public void write(int b) {
		        // NO-OP
		    }
		});
		//System.setOut(dummyStream);
		Minigolf3D.mainWindow = mainWindow;
		
		MainView view = new MainView();
		System.out.println("view instanciated");
		
		mainWindow.setScene(view.getScene());
		mainWindow.setResizable(true);
		mainWindow.show();
	}
	
	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
