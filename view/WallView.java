package view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class WallView extends Group{

    private float height = 10;
    private Box wall;

    public WallView(float length, float width){
        wall = new Box(length, height, width);
        wall.setMaterial(new PhongMaterial(Color.FIREBRICK));
        this.getChildren().add(wall);
    }

    public float getHeight(){
        return height;
    }
}
