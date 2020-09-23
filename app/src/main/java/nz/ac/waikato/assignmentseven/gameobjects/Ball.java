package nz.ac.waikato.assignmentseven.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import nz.ac.waikato.assignmentseven.Input;
import nz.ac.waikato.assignmentseven.physics.Transform;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class Ball extends Circle {
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
                paint.setColor(Color.GREEN);
                break;
            case WAITING:
                paint.setColor(Color.BLUE);
                break;
            case YOOT:
                paint.setColor(Color.RED);
        }
    }

    public Ball(Vector2f translation) {
        super(new Transform(translation, 50), 1, new Paint());
        changeState(state);
        mass = 1;
    }

    @Override
//    Allows the user to pick the ball up and yeet it
    public void onUpdate(Canvas canvas, float deltaTime) {
        switch (state){
            case WAITING:
                if (Input.getTouchDown() &&
                        Input.getTouchPosition().subtract(transform.translation).magnitude() < transform.scale.magnitude())
                    changeState(State.PICKED_UP);
                break;
            case PICKED_UP:
                if (!Input.getTouchDown()) {
                    changeState(State.YOOT);
                    break;
                }
                transform.translation = Input.getTouchPosition();
                velocity = Input.getTouchVelocity();
                break;
            case YOOT:
                break;
        }
    }
}
