package utility;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Vector3DProperty {

    public FloatProperty x = new SimpleFloatProperty();
    public FloatProperty y = new SimpleFloatProperty();
    public FloatProperty z = new SimpleFloatProperty();

    public Vector3DProperty(float x, float y, float z) {
        this.x.setValue(x);
        this.y.setValue(y);
        this.z.setValue(z);
    }
    
    public Vector3DProperty(Vector3D position) {
    	 this.x.setValue(position.x);
         this.y.setValue(position.y);
         this.z.setValue(position.z);
	}

	public Float getX() {
    	return x.get();
    }
    
    public Float getY() {
    	return y.get();
    }
    
    public Float getZ() {
    	return z.get();
    }

    public Vector3D toVector3D(){
        return new Vector3D(this.x.get(),this.y.get(),this.z.get());
    }
    
    @Override
    public String toString() {
    	return "[x=" + x.get() + ", y=" + y.get() + ", z=" + z.get() + "]";
    }

	public Vector2D toVector2D() {
		return new Vector2D(x.get(), y.get());
	}
}
