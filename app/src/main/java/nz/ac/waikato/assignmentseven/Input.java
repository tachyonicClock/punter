package nz.ac.waikato.assignmentseven;

import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class Input implements View.OnTouchListener {

    private static Input instance = new Input();

    private VelocityTracker velocityTracker;

    private Vector2f touchPosition = new Vector2f();
    private boolean isTouchDown = false;

    public Vector2f getTouchVelocity(){
        if (!isTouchDown) return new Vector2f();

        velocityTracker.computeCurrentVelocity(1000);
        return new Vector2f(velocityTracker.getXVelocity(), velocityTracker.getYVelocity());

    }

    public Vector2f getTouchPosition(){
        return touchPosition;
    }

    public boolean getTouchDown(){
        return isTouchDown;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
//                Setup velocity tracker
                if (velocityTracker == null)
                    velocityTracker = VelocityTracker.obtain();
                else
                    velocityTracker.clear();

                isTouchDown = true;
                touchPosition.x = motionEvent.getX();
                touchPosition.y = motionEvent.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(motionEvent);
                touchPosition.x = motionEvent.getX();
                touchPosition.y = motionEvent.getY();
                break;

//            Either up or cancel
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouchDown = false;
                break;
            default:
        }
        return true;
    }

    public static Input getInstance(){
        return instance;
    }
}
