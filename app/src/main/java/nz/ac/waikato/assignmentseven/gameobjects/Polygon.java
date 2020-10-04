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
    private int n = 0;

    @Override
    public Collider getCollider() {
        return collider;
    }

    @Override
    public void onUpdate(Canvas canvas, float deltaTime) {
        if (n <= 0) {
            paint.setColor(Color.BLUE);
            return;
        }
        paint.setColor(Color.RED);
        n = 0;
    }

    @Override
    public void onCollision(Collision collision) {
        super.onCollision(collision);
        n = 1;
    }

    @Override
    public void onDraw(Canvas canvas) {
        path.reset();
        List<Vector2f> vertices = collider.getVertices();

//        Transform and draw polygon
        Vector2f start = vertices.get(0);
        start = transform.apply(start);
        path.moveTo(start.x, start.y);
        for (int i = 1; i < vertices.size(); i ++){
            Vector2f vertex = vertices.get(i);
            vertex = transform.apply(vertex);
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
