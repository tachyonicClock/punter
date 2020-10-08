package nz.ac.waikato.assignmentseven.physics;

/**
 * Represents a face or edge between to vertices
 */
public class Face {
    public Vector2f a;
    public Vector2f b;

    public Face(Vector2f a, Vector2f b) {
        this.a = a;
        this.b = b;
    }
}
