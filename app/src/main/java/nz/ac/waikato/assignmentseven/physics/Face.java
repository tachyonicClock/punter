package nz.ac.waikato.assignmentseven.physics;

public class Face {
    public Vector2f a;
    public Vector2f b;

    public Face(Vector2f a, Vector2f b){
        this.a = a;
        this.b = b;
    }
    public Face(Face face){
        a = face.a;
        b = face.b;
    }
}
