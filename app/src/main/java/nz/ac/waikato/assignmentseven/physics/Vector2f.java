package nz.ac.waikato.assignmentseven.physics;

/**
 * Vector2 is a 2D vector.
 * It is often used to represent velocity, position, or acceleration
 */
public class Vector2f {
    public float x;
    public float y;

    public Vector2f invert(){
        return new Vector2f(-x, -y);
    }

    public Vector2f multiply(float s){
        return new Vector2f(x * s, y * s);
    }

    public Vector2f dotProduct(Vector2f p2){
        return new Vector2f(x * p2.x, y * p2.y);
    }

    public Vector2f divide(float s){
        return new Vector2f(x / s, y / s);
    }


    public Vector2f add(Vector2f p2){
        return new Vector2f(x + p2.x, y + p2.y);
    }

    public Vector2f subtract(Vector2f p2){
        return new Vector2f(x - p2.x, y - p2.y);

    }

    public Vector2f normalized(){
        float m = magnitude();
        if (m == 0) return new Vector2f();
        return new Vector2f(x/m, y/m);
    }

    public float magnitude(){
        return (float) Math.sqrt(x*x + y*y);
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                ", magnitude=" + magnitude() +
                '}';
    }

    public Vector2f(Vector2f position) {
        x = position.x;
        y = position.y;
    }

    public Vector2f(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2f(){
        this.x = 0;
        this.y = 0;
    }
}
