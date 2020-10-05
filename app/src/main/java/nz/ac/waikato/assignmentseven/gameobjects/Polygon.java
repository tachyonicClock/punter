package nz.ac.waikato.assignmentseven.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

import nz.ac.waikato.assignmentseven.PhysicsObject;
import nz.ac.waikato.assignmentseven.physics.Collider;
import nz.ac.waikato.assignmentseven.physics.Collision;
import nz.ac.waikato.assignmentseven.physics.PolygonCollider;
import nz.ac.waikato.assignmentseven.physics.Transform;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class Polygon extends PhysicsObject {

    private PolygonCollider collider;
    private Paint paint;
    private Path path = new Path();

    @Override
    public Collider getCollider() {
        return collider;
    }

    @Override
    public void onUpdate(Canvas canvas, float deltaTime) {
    }

    @Override
    public void onDraw(Canvas canvas) {
        path.reset();

//        Draw vertices
        Vector2f start = collider.getWorldVertex(0);
        path.moveTo(start.x, start.y);
        for (int i = 1; i < collider.size(); i ++){
            Vector2f vertex = collider.getWorldVertex(i);
            path.lineTo(vertex.x, vertex.y);
        }
        path.lineTo(start.x, start.y);

        canvas.drawPath(path, paint);
    }

    public Polygon(Transform transform, Paint paint, float mass, List<Vector2f> vertices, List<Vector2f> normals){
        super();
        this.paint = paint;
        this.transform = transform;
        this.mass = mass;
        collider = new PolygonCollider(this, vertices, normals);
    }
}
