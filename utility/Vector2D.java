package utility;

public class Vector2D {
    public float x, y;

    public Vector2D() {
        x = 0;
        y = 0;
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /*
    adds a vector to this vector and returns a new one
     */
    public Vector2D add(Vector2D v){
        return new Vector2D(this.x+v.x, this.y + v.y);
    }

    /*
    adds a value to all dimensions of this vector and returns a new one
     */
    public Vector2D add(float v){
        return new Vector2D(this.x+v, this.y + v);
    }

    /*
    @return Vector2D a new vector with a scalar
     */
    public Vector2D scale(float s){
        return new Vector2D(x*s, y*s);
    }

    public String toString() {
        return "x: " + x + " y: " + y ;
    }
}
