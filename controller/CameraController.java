package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import view.Group3D;

public class CameraController {

	static final double CAMERA_INITIAL_DISTANCE         = -1800;
	static final double CAMERA_NEAR_CLIP                = 1;
	static final double CAMERA_FAR_CLIP                 = 5000.0;

	private PerspectiveCamera camera;
	private Group3D group3D;
	
	private double xDrag, yDrag;

	public CameraController(PerspectiveCamera camera, Group3D group3D) {
		this.camera = camera;
		this.group3D = group3D;
		/*group3D.getGameScene().addEventHandler(KeyEvent.ANY,
				event -> handleKeys(event));*/
		group3D.getGameScene().getParent().setOnScroll(e -> handleScroll(e));
		group3D.getGameScene().getParent().setOnMouseDragged(e -> handleMouse(e));
		group3D.getGameScene().getParent().setOnMouseMoved(e -> handleMouse(e));
		group3D.setOnMouseMoved(e -> 
		{{/*System.out.println(((e.getX() - group3D.getCourseView().getTranslateX())) + 
				" : " + ((e.getY() - group3D.getCourseView().getTranslateZ())));
		System.out.println(group3D.getModel().getEnv().getLength() * (1 - group3D.getCourseView().getScale().getX()) / 2);*/
		
		}}
		);
		initCamera();
	}

	public void initCamera(){
		System.out.println("Initializing Camera...");
		camera.setNearClip(CAMERA_NEAR_CLIP);
		camera.setFarClip(CAMERA_FAR_CLIP);
		camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);

		//binds the x position of the camera to the ball
		Sphere ball = group3D.getCourseView().getBallView();
		ball.translateXProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				camera.setTranslateX(newValue.doubleValue());
			}
		});

	}

	private void handleScroll(ScrollEvent event){
		group3D.getCourseView().addScale(event.getDeltaY() * 0.01, event.getDeltaY() * 0.01, event.getDeltaY() * 0.01);
		/*group3D.getCourseView().getScale().setX(event.getDeltaY() * 0.01 + group3D.getCourseView().getScale().getX());
		group3D.getCourseView().getScale().setY(event.getDeltaY() * 0.01 + group3D.getCourseView().getScale().getY());
		group3D.getCourseView().getScale().setZ(event.getDeltaY() * 0.01 + group3D.getCourseView().getScale().getZ());*/
	}

	private void handleMouse(MouseEvent event){
		if (event.getButton() == MouseButton.PRIMARY) {
			group3D.getCourseView().rotateY(event.getScreenX() - xDrag);
			group3D.getCourseView().rotateX(event.getScreenY() - yDrag);
		}
		if (event.getButton() == MouseButton.SECONDARY) {
			group3D.getCourseView().trans(event.getScreenX() - xDrag,  event.getScreenY() - yDrag, 0);
		}
		yDrag = event.getScreenY();
		xDrag = event.getScreenX();	
	}
}
