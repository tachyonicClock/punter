package nz.ac.waikato.assignmentseven.gameobjects;

import android.graphics.Canvas;
import android.graphics.Paint;

import nz.ac.waikato.assignmentseven.PhysicsObject;
import nz.ac.waikato.assignmentseven.physics.CircleCollider;
import nz.ac.waikato.assignmentseven.physics.Collider;
import nz.ac.waikato.assignmentseven.physics.Transform;

public class Circle extends PhysicsObject {
    protected Paint paint;
    private Collider collider = new CircleCollider(this);

    @Override
    public Collider getCollider() {
        return collider;
    }

    @Override
    public void onUpdate(Canvas canvas, float deltaTime) {
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(transform.translation.x, transform.translation.y, transform.scale.magnitude(), paint);
    }

    public Circle(Transform transform, float mass, Paint paint){
        this.transform = new Transform(transform);
        this.paint = new Paint(paint);
        this.mass = mass;
    }
}
