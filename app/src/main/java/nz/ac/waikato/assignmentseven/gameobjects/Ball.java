package nz.ac.waikato.assignmentseven.gameobjects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;

import java.util.ResourceBundle;

import nz.ac.waikato.assignmentseven.Input;
import nz.ac.waikato.assignmentseven.R;
import nz.ac.waikato.assignmentseven.physics.Transform;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class Ball extends Circle {

//    Colours for different states
    private int pickedUpColour = Color.RED;
    private int ballColour = Color.BLUE;
    private int thrownColour = Color.BLACK;

    private float size = 45;
    private Vector2f center = new Vector2f();
    private Paint throwArea = new Paint();
    private float throwRadius = 200;

//    States that the ball can be in
    enum State {
        WAITING,
        PICKED_UP,
//        YOOT being the past tense of yeet meaning to throw...
        YOOT
    }
    State state = State.WAITING;

//    changeState changes the state and the balls colour
    private void changeState(State state){
        this.state = state;
        switch (state){
            case PICKED_UP:
                paint.setColor(pickedUpColour);
                break;
            case WAITING:
                paint.setColor(ballColour);
                break;
            case YOOT:
                paint.setColor(thrownColour);
        }
    }

    public Ball() {
        super(new Transform(), 1, new Paint());
        transform.scale.x = size;
        transform.scale.y = size;
        changeState(state);
        mass = 1;
    }

    @Override
    public void onStart(Canvas canvas, Context context) {
        Resources resources = context.getResources();
        Resources.Theme t = context.getTheme();
        ballColour = resources.getColor(R.color.ball, t);
        pickedUpColour = resources.getColor(R.color.ballPickedUp, t);
        thrownColour = resources.getColor(R.color.ballThrown, t);
        throwArea.setColor(resources.getColor(R.color.throwArea, t));
        throwArea.setAlpha(100);

        changeState(state);
        center.x = canvas.getWidth()/2f;
        center.y = canvas.getHeight()/2f;
        transform.translation = new Vector2f(center);
    }

    @Override
//    Allows the user to pick the ball up and yeet it
    public void onUpdate(Canvas canvas, float deltaTime) {
        switch (state){
            case WAITING:
                if (Input.getTouchDown() &&
                        Input.getTouchPosition().subtract(transform.translation).magnitude() < transform.scale.magnitude()*2f)
                    changeState(State.PICKED_UP);
                break;
            case PICKED_UP:
                if (!Input.getTouchDown() ||
                        transform.translation.subtract(center).magnitude() > throwRadius) {
                    changeState(State.YOOT);
                    break;
                }

                addForce(transform.translation.subtract(Input.getTouchPosition()).clamp(0, 1000).multiply(-1000*deltaTime));
                velocity = velocity.multiply(0.1f * deltaTime);

                break;
            case YOOT:
                break;
        }
        // Add a max velocity
        velocity = velocity.clamp(0, 5000);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (state != State.YOOT) canvas.drawCircle(center.x, center.y, throwRadius, throwArea);
        super.onDraw(canvas);
        dampening = 0.8f;
    }
}
