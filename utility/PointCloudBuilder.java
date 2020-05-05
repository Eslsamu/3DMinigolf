package utility;

import java.util.ArrayList;

import model.Surface;


public class PointCloudBuilder {
	
	/**
	 * 
	 * 
	 * @throws Exception
	 * @return ArrayList<Vector3D> a list of points
	 */
	public static ArrayList<Vector3D> buildCloud(Surface s, float INTERVAL) {
		ArrayList<Vector3D> pointList = new ArrayList<Vector3D>();
		for (float x = 0; x <= s.getLength(); x+= INTERVAL) {
			for (float y = 0; y <= s.getWidth(); y+= INTERVAL) {
				Float z = s.interpolate(x, y);
				if (z != null)
					pointList.add(new Vector3D(x,y,z));
		    	//pointList.add(new Vector3D(x,y,0));
			}
		}
		/*for (int x = 0; x <= 1; x+= 1) {
			for (float y = 0; y <= 1; y+= INTERVAL) {
				values.put("x", (double)x);
				values.put("y", (double)y);
				System.out.println(expression);g
				float z = (float)new Expression(expression, values).evaluateExpression();
				for (float z2 = 0; z2 <= z; z2+= INTERVAL) {
					pointList.add(new Vector3D(x,y,z2));
				}
			}
		}
		for (float x = 0; x <= 1; x+= INTERVAL) {
			for (int y = 0; y <= 1; y+= 1) {
				values.put("x", (double)x);
				values.put("y", (double)y);
				System.out.println(expression);
				float z = (float)new Expression(expression, values).evaluateExpression();
				for (float z2 = 0; z2 <= z; z2+= INTERVAL) {
					pointList.add(new Vector3D(x,y,z2));
				}
			}
		}*/ 
		return pointList;
	}
	
}
