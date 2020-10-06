package nz.ac.waikato.assignmentseven.gameobjects;

import android.graphics.Canvas;
import android.graphics.Paint;

import nz.ac.waikato.assignmentseven.GameObject;
import nz.ac.waikato.assignmentseven.ScoreHandler;
import nz.ac.waikato.assignmentseven.physics.Transform;

public class ScoreDisplay extends GameObject {

    private Paint textPaint;

    @Override
    public void onUpdate(Canvas canvas, float deltaTime) {

    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText(""+ScoreHandler.GetInstance().GetCurrentScore().GetScore(),transform.translation.x, transform.translation.y, textPaint);
    }

    public ScoreDisplay(Transform transform, Paint paint){
        this.transform = transform;
        this.textPaint = paint;
        paint.setTextAlign(Paint.Align.CENTER);
    }
}
