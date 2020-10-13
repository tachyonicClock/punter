package nz.ac.waikato.assignmentseven.physics;

import org.jetbrains.annotations.NotNull;

/**
 * Vector2 is a 2D vector.
 * It is often used to represent velocity, position, or acceleration
 */
public class Vector2f {
    public float x;
    public float y;

    public Vector2f(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public Vector2f(@NotNull Vector2f position) {
        this.x = position.x;
        this.y = position.y;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f() {
        this.x = 0;
        this.y = 0;
    }

    public static Vector2f crossProduct(float s, @NotNull Vector2f a) {
        return new Vector2f(-s * a.y, s * a.x);
    }

    public Vector2f invert() {
        return new Vector2f(-x, -y);
    }

    public Vector2f multiply(float s) {
        return new Vector2f(x * s, y * s);
    }

    public float dotProduct(@NotNull Vector2f p2) {
        return x * p2.x + y * p2.y;
    }

    public Vector2f divide(float s) {
        return new Vector2f(x / s, y / s);
    }

    public Vector2f add(@NotNull Vector2f p2) {
        return new Vector2f(x + p2.x, y + p2.y);
    }

    public Vector2f subtract(@NotNull Vector2f p2) {
        return new Vector2f(x - p2.x, y - p2.y);

    }

    public Vector2f normalized() {
        float m = magnitude();
        if (m == 0) return new Vector2f();
        return new Vector2f(x / m, y / m);
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setZero() {
        x = 0;
        y = 0;
    }

    public float crossProduct(@NotNull Vector2f p2) {
        return x * p2.y - y * p2.x;
    }

    public Vector2f crossProduct(float s) {
        return new Vector2f(s * y, -s * x);
    }

    public Vector2f clamp(float min, float max) {
        if (magnitude() <= min) return normalized().multiply(min);
        if (magnitude() >= max) return normalized().multiply(max);
        return this;
    }

    public boolean equal(@NotNull Vector2f other) {
        return x == other.x && y == other.y;
    }

    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float magnitudeSquared() {
        return x * x + y * y;
    }

    @NotNull
    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                ", magnitude=" + magnitude() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2f vector2f = (Vector2f) o;

        if (Float.compare(vector2f.x, x) != 0) return false;
        return Float.compare(vector2f.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }
}
