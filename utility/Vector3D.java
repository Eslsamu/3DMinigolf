package utility;

public class Vector3D {
    public float x, y, z;

    public Vector3D() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /*
    adds a value to each dimension of this vector and returns a new vector
     */
    public Vector3D add(float v){
        return new Vector3D(x+v,y+v,z+v);
    }

    public Vector2D toVector2D(){
        return new Vector2D(x,y);
    }

    public String toString() {
        return "x: " + x + " y: " + y + " z: " +z;
    }
}
