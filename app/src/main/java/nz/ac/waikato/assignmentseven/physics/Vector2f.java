package nz.ac.waikato.assignmentseven.physics;

import org.jetbrains.annotations.NotNull;

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

    public float dotProduct(@NotNull Vector2f p2){
        return x*p2.x + y*p2.y;
    }

    public Vector2f divide(float s){
        return new Vector2f(x / s, y / s);
    }


    public Vector2f add(@NotNull Vector2f p2){
        return new Vector2f(x + p2.x, y + p2.y);
    }

    public Vector2f subtract(@NotNull Vector2f p2){
        return new Vector2f(x - p2.x, y - p2.y);

    }

    public Vector2f normalized(){
        float m = magnitude();
        if (m == 0) return new Vector2f();
        return new Vector2f(x/m, y/m);
    }

    public Vector2f largestComponent(){
        if (x*x > y*y) return new Vector2f(x, 0);
        else return new Vector2f(0, y);
    }

    public Vector2f clamp(float min, float max){
        if (magnitude() <= min) return normalized().multiply(min);
        if (magnitude() >= max) return normalized().multiply(max);
        return this;
    }

    public boolean equal(@NotNull Vector2f other){
        return x == other.x && y == other.y;
    }

    public float magnitude(){
        return (float) Math.sqrt(x*x + y*y);
    }

    public float magnitudeSquared(){return x*x + y*y; }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                ", magnitude=" + magnitude() +
                '}';
    }

    public Vector2f(@NotNull Vector2f position) {
        this.x = position.x;
        this.y = position.y;
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
