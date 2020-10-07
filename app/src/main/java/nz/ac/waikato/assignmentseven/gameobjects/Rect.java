package nz.ac.waikato.assignmentseven.gameobjects;

import android.graphics.Canvas;
import android.graphics.Paint;

import nz.ac.waikato.assignmentseven.physics.Collision;
import nz.ac.waikato.assignmentseven.physics.PolygonCollider;
import nz.ac.waikato.assignmentseven.physics.Transform;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class Rect extends Polygon {

    @Override
    public void calculateInertia() {
        inertia = (transform.scale.x * transform.scale.x + transform.scale.y * transform.scale.y)/6f * mass;
    }

    public Rect(Transform transform, Paint paint, float mass)
    {
        super(transform, paint, mass, PolygonCollider.rectangleVertices(), PolygonCollider.rectangleNormals());
        calculateInertia();
    }

    public Rect(float x, float y, float width, float height, Paint paint, float mass){
        super(new Transform(new Vector2f(x, y), new Vector2f(width/2, height/2)), paint, mass, PolygonCollider.rectangleVertices(), PolygonCollider.rectangleNormals());
        calculateInertia();
    }
}
