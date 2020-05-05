package view;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import model.Surface;
import utility.PointCloudBuilder;
import utility.Vector3D;


/*
 * a graphical representation of a course piece 
 */
public class SurfaceView extends Group {
	
	//Defines the interval between every point computed in the point cloud
	public static final int INTERVAL_FRACTION = 50;
	public float interval;
	private Surface surface;
	private ArrayList<Vector3D> points;
	private Color[] heightMap = new Color[] {Color.DARKBLUE, Color.BLUE, Color.CORNFLOWERBLUE, Color.DARKGREEN, Color.GREEN, Color.DARKOLIVEGREEN, Color.DARKRED, Color.RED};
	public static boolean showHeightMap;
	private float centerX, centerY;
	
	Image diff = new Image("files/pictures/grass_diffuse.jpg");

	public SurfaceView(Surface surface) {
		this.surface = surface;
		centerX = surface.getLength() / 2;
		centerY = surface.getWidth() / 2;
		interval = surface.getLength()/INTERVAL_FRACTION;
		computePoints();
		if (showHeightMap) {
			createCubes();
		} else {
			createMesh();
			createWater();
		}
	}
	
	public void computePoints() {
		points = PointCloudBuilder.buildCloud(surface, interval);
	}
	
	public void createWater() {
		for(Vector3D point : points) {
			if (point.z <= -1) {
				Box box = new Box(interval,1,interval); 
				box.setTranslateX(point.x - centerX);
				box.setTranslateY(point.z);
				box.setTranslateZ(point.y - centerY);
				this.getChildren().add(box);
				box.setTranslateY(1);
				box.setMaterial(new PhongMaterial(Color.AQUA));
			}
		}
	}
	
	public void createMesh() {
		TriangleMesh mesh = new TriangleMesh();
		Vector3D point = null;
		for(int i = 0; i < points.size(); i++) {
			point = points.get(i);
			mesh.getPoints().addAll(point.x - centerX,point.z,point.y - centerY);

			mesh.getTexCoords().addAll(1, 1, 0, 1, 0, 1, 0, 0);
		}
		int inf = (int)Math.sqrt(points.size()) - 1;
		for(int i = 0; i < inf;i++){
			for(int j = 0; j < inf; j++){
				//top left
				int v1 = j+(i*(inf+1));
				//top right
				int v2 = j+((i+1)*(inf+1));
				//bottom right
				int v3 = v2+1;
				//bottom left
				int v4 = v1+1;

				
				//bottom left triangle
				mesh.getFaces().addAll(v1,v1,v3,v3,v4,v4);
				//top right triangle
				mesh.getFaces().addAll(v1,v1,v2,v2,v3,v3);
			}
		}
		//TODO add texture
		MeshView meshView = new MeshView(mesh);
		meshView.setCullFace(CullFace.NONE);
		meshView.setDrawMode(DrawMode.FILL);
		PhongMaterial mat = new PhongMaterial();
		mat.setDiffuseMap(surface.getTexture());
		meshView.setMaterial(mat);
		
		this.getChildren().add(meshView);
	}
	
	public void createCubes() {
		//compute highest point
		Vector3D max = points.get(1);
		for(Vector3D point : points){
			if(point.z > max.z) max = point;
		}

		double maxZ = max.z;
		for(Vector3D point : points) {
			Box box = new Box(interval,0,interval); 
			box.setTranslateX(point.x - centerX);
			box.setTranslateY(point.z / 2); 
			box.setTranslateZ(point.y - centerY);
			this.getChildren().add(box);
			if(point.z >= -1) { //check  if ball is in water and set the height accordingly
				box.setHeight(point.z + 1);
				PhongMaterial mat = new PhongMaterial(heightMap[(int) (point.z / maxZ * (heightMap.length - 1))]);
				mat.setDiffuseMap(diff);
				box.setMaterial(mat);
			} else {
				box.setHeight(1);
				box.setTranslateY(1);
				box.setMaterial(new PhongMaterial(Color.AQUA));
			}
		}
	}
	public ArrayList<Vector3D> getPoints() {
		return points;
	}
}
