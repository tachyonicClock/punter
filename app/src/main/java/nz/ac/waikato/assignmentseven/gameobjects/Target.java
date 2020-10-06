package nz.ac.waikato.assignmentseven.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import nz.ac.waikato.assignmentseven.ScoreHandler;
import nz.ac.waikato.assignmentseven.physics.Collision;
import nz.ac.waikato.assignmentseven.physics.Transform;

public class Target extends Circle {

    private float coolDown = 0.0f;
    private float effectSize = 0;

    public Target(Transform transform) {
        super(transform, 0, new Paint());
    }

    @Override
    public void onCollision(Collision collision) {
        if (!(collision.getOther(this) instanceof Ball)) return;
        ScoreHandler.GetInstance().GetCurrentScore().ChangeScore(1);
        effectSize += 100;
    }

    @Override
    public void onUpdate(Canvas canvas, float deltaTime) {
        super.onUpdate(canvas, deltaTime);
        if (coolDown >= 0) coolDown -= deltaTime;
        effectSize -= effectSize* deltaTime * 2;
    }

    @Override
    public void onDraw(Canvas canvas) {
        float x = transform.translation.x;
        float y = transform.translation.y;
        float m = transform.scale.magnitude();

        paint.setColor(Color.RED);
        canvas.drawCircle(x, y, m, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, m - m/3, paint);
        paint.setColor(Color.RED);
        canvas.drawCircle(x, y, m - 2*m/3, paint);

        paint.setAlpha(100);
        canvas.drawCircle(x,y, effectSize, paint);
    }
}
