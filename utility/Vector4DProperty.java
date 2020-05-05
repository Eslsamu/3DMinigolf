package utility;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Vector4DProperty {

    public FloatProperty x = new SimpleFloatProperty();
    public FloatProperty y = new SimpleFloatProperty();
    public FloatProperty z = new SimpleFloatProperty();
    public FloatProperty f = new SimpleFloatProperty();

    public Vector4DProperty(float x, float y, float z, float f) {
        this.x.setValue(x);
        this.y.setValue(y);
        this.z.setValue(z);
        this.f.setValue(f);
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
    
    public Float getF() {
    	return f.get();
    }

    public Vector3D toVector3D(){
        return new Vector3D(this.x.get(),this.y.get(),this.z.get());
    }
    
    @Override
    public String toString() {
    	return "[x=" + x.get() + ", y=" + y.get() + ", z=" + z.get() + "]";
    }
}
