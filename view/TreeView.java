package view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;

public class TreeView  extends Group {
    private Sphere bush;
    private Cylinder stem;

    private float stemHeight = 20;

    public TreeView(float radius){
        this.stem = new Cylinder(radius, stemHeight);
        stem.setMaterial(new PhongMaterial(Color.BROWN));
        this.bush = new Sphere(radius*2);
        bush.setMaterial(new PhongMaterial(Color.GREEN));
        bush.setTranslateY(stemHeight);
        this.getChildren().addAll(stem,bush);
    }
}
