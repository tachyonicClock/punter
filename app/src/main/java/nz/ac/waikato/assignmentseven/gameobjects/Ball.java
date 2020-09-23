package nz.ac.waikato.assignmentseven.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import nz.ac.waikato.assignmentseven.GameObject;
import nz.ac.waikato.assignmentseven.Input;
import nz.ac.waikato.assignmentseven.PhysicsObject;
import nz.ac.waikato.assignmentseven.physics.Transform;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class Ball extends PhysicsObject {

    Paint paint;

    public Ball(Vector2f translation) {
        super(100);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        transform.scale = transform.scale.multiply(50);
        transform.translation = translation;
    }

    @Override
    public void onUpdate(Canvas canvas, float deltaTime) {
        if (Input.getInstance().getTouchDown()){
            transform.translation = Input.getInstance().getTouchPosition();
            velocity = Input.getInstance().getTouchVelocity();
            paint.setColor(Color.RED);
        }else{
            paint.setColor(Color.GREEN);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawCircle(transform.translation.x, transform.translation.y, transform.scale.magnitude(), paint);
    }
}
