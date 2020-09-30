package nz.ac.waikato.assignmentseven.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import nz.ac.waikato.assignmentseven.PhysicsObject;
import nz.ac.waikato.assignmentseven.physics.Collider;
import nz.ac.waikato.assignmentseven.physics.Collision;
import nz.ac.waikato.assignmentseven.physics.RectangleCollider;
import nz.ac.waikato.assignmentseven.physics.Transform;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class Rect extends PhysicsObject {
    private RectangleCollider collider;
    private Paint paint;
    private float n = 0;

    @Override
    public Collider getCollider() {
        return collider;
    }

    @Override
    public void onUpdate(Canvas canvas, float deltaTime) {
        if (n < 0){
            paint.setColor(Color.BLUE);
        }else{
            n -= deltaTime;
        }
    }

    @Override
    public void onCollision(Collision collision) {
        super.onCollision(collision);
        paint.setColor(Color.RED);
        n = 0.1f;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(collider.getLeft(), collider.getTop(), collider.getRight(), collider.getBottom(), paint);
    }

    public Rect(float x, float y, float width, float height, Paint paint, float mass){
        this.paint = new Paint(paint);
        this.transform = new Transform(new Vector2f(x, y), new Vector2f(width, height));
        this.collider = new RectangleCollider(this);
        this.mass = mass;
    }
}
