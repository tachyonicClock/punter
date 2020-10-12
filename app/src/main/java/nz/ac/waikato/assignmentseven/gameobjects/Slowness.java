package nz.ac.waikato.assignmentseven.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

import nz.ac.waikato.assignmentseven.AudioHandler;
import nz.ac.waikato.assignmentseven.PhysicsObject;
import nz.ac.waikato.assignmentseven.audio.AudioMeanings;
import nz.ac.waikato.assignmentseven.mapgenerator.ColourMeanings;
import nz.ac.waikato.assignmentseven.physics.Collision;
import nz.ac.waikato.assignmentseven.physics.Transform;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class Slowness extends Circle {

    private float coolDown = 0.0f;
    private float effectSize = 0;
    private Random rand = new Random();

    public Slowness(Transform transform, float mass) {

        super(transform, mass, new Paint());

        // Slow the objects on collision
        dampening = 0.9f;
        restitution = 0.5f;
    }

    @Override
    public void onCollision(Collision collision) {
        if (collision.getOther(this) instanceof Ball)  AudioHandler.getInstance().playSoundSimultaneously(AudioMeanings.RECT_COLLISION);
    }

    @Override
    public void onDraw(Canvas canvas) {
        float x = transform.translation.x;
        float y = transform.translation.y;
        float m = transform.scale.magnitude();

        paint.setColor(ColourMeanings.SLOWNESS_VIOLET);
        canvas.drawCircle(x, y, m, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, m - m/3, paint);
        paint.setColor(ColourMeanings.SLOWNESS_VIOLET);
        canvas.drawCircle(x, y, m - 2*m/3, paint);
    }
}
