package nz.ac.waikato.assignmentseven;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;

import nz.ac.waikato.assignmentseven.physics.Transform;

public abstract class GameObject {

//    transform the location of the GameObject in the world
    public Transform transform = new Transform();

    /***
     * onUpdate is called on every draw event and is responsible for updating the entities state
     *
     * @param canvas the canvas the GameObject will be drawn to
     * @param deltaTime the change in time since the last update. This lets the physics be normal
     *               on slow and fast devices, rather than assuming a constant time step
     */
    public abstract void onUpdate(Canvas canvas, float deltaTime);

    /***
     * onDraw is called on every draw event and is responsible for redrawing the entity
     *
     * @param canvas the canvas the GameObject will be drawn to
     */
    public abstract void onDraw(Canvas canvas);

    /**
     * onStart is called when the game is first started. It is supplied the android context
     * so that it can access resources for setup
     * @param canvas the canvas that the game will be played on
     * @param context the context that the GameView is in
     */
    public void onStart(Canvas canvas, Context context){};
}
