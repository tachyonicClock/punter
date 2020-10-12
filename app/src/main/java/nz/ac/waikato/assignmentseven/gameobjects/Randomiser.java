package nz.ac.waikato.assignmentseven.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

import nz.ac.waikato.assignmentseven.AudioHandler;
import nz.ac.waikato.assignmentseven.PhysicsObject;
import nz.ac.waikato.assignmentseven.ScoreHandler;
import nz.ac.waikato.assignmentseven.audio.AudioMeanings;
import nz.ac.waikato.assignmentseven.mapgenerator.ColourMeanings;
import nz.ac.waikato.assignmentseven.physics.Collision;
import nz.ac.waikato.assignmentseven.physics.Transform;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class Randomiser extends Circle {

    private float coolDown = 0.0f;
    private float effectSize = 0;
    private Random rand = new Random();

    public Randomiser(Transform transform, float mass) {
        super(transform, mass, new Paint());
    }

    @Override
    public void onCollision(Collision collision) {
        if (collision.getOther(this) instanceof Randomiser) return;
        PhysicsObject other = collision.getOther(this);

        other.addForce(new Vector2f(rand.nextInt(1000), rand.nextInt(1000)));
        other.addForce(collision.getNormal(other).multiply(1000));

        if (collision.getOther(this) instanceof Ball) AudioHandler.getInstance().playSoundSimultaneously(AudioMeanings.SLOWDOWN_COLLISION);

    }


    @Override
    public void onDraw(Canvas canvas) {
        float x = transform.translation.x;
        float y = transform.translation.y;
        float m = transform.scale.magnitude();

        paint.setColor(ColourMeanings.RANDOMISER_MAIN);
        canvas.drawCircle(x, y, m, paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(x - m/2, y - m/2, x + m/2, y + m/2, paint);

    }
}
