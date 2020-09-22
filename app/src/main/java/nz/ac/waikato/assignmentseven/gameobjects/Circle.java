package nz.ac.waikato.assignmentseven.gameobjects;

import android.graphics.Canvas;
import android.graphics.Paint;

import nz.ac.waikato.assignmentseven.GameObject;
import nz.ac.waikato.assignmentseven.physics.Transform;

public class Circle extends GameObject {
    private Paint paint;

    @Override
    public void onUpdate(Canvas canvas, float deltaTime) {
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(transform.translation.x, transform.translation.y, transform.scale.magnitude(), paint);
    }

    public Circle(Transform transform, Paint paint){
        this.transform = transform;
        this.paint = paint;
    }

}
