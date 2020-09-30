package nz.ac.waikato.assignmentseven;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nz.ac.waikato.assignmentseven.gameobjects.Ball;
import nz.ac.waikato.assignmentseven.gameobjects.Circle;
import nz.ac.waikato.assignmentseven.gameobjects.Rect;
import nz.ac.waikato.assignmentseven.physics.Collision;
import nz.ac.waikato.assignmentseven.physics.Transform;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class GameView extends View {
    Paint paint;

//    gameObjects is a list of all gameObjects in the game that need to be drawn
    private List<GameObject> gameObjects = new LinkedList<>();

//    We need to keep track of when the last draw was in order to accurately calculate physics
    private long previousDraw = System.currentTimeMillis();
    private boolean needsSetup = true;

    void gameLoop(Canvas canvas) {
//        deltaTime is the amount of time since the last game loop
        float deltaTime = (float) (System.currentTimeMillis() - previousDraw)/1000f;
        previousDraw = System.currentTimeMillis();

//        Calculate Collisions
//        Generate a set of all possible collisions
        Set<Pair<PhysicsObject, PhysicsObject>> possibleCollisions = new HashSet<Pair<PhysicsObject, PhysicsObject>>();
        for (GameObject gameObjectA : gameObjects) {
            for (GameObject gameObjectB : gameObjects) {
                if(gameObjectA != gameObjectB && gameObjectA instanceof PhysicsObject && gameObjectB instanceof PhysicsObject)
                {
                    Pair<PhysicsObject, PhysicsObject> pair = new Pair<>((PhysicsObject)gameObjectA, (PhysicsObject)gameObjectB);
                    Pair<PhysicsObject, PhysicsObject> reversePair = new Pair<>(pair.second, pair.first);
//                    Do not add a duplicate pair
                    if (possibleCollisions.contains(pair))  continue;
                    if (possibleCollisions.contains(reversePair))  continue;
//                    Add it if it is not a duplicate
                    possibleCollisions.add(pair);
                }
            }
        }

        //        Physics Loop
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof PhysicsObject){
                ((PhysicsObject) gameObject).onPhysicsUpdate(deltaTime);
            }
        }
//        Perform collisions
        for (Pair<PhysicsObject, PhysicsObject> pair : possibleCollisions){
            Collision collision = new Collision(pair.first, pair.second);
            collision.updateCollision();
        }

//        Update Loop
        for (GameObject gameObject : gameObjects) {
            gameObject.onUpdate(canvas, deltaTime);
        }

//        Render Loop
        for (GameObject gameObject : gameObjects) {
            gameObject.onDraw(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        run game setup
        if (needsSetup){
            setupGame(canvas);
            needsSetup = false;
        }

        gameLoop(canvas);
        invalidate();
    }

//    setupGame is used for setup that cannot be done in the constructor since it needs the canvas
    private void setupGame(@NotNull Canvas canvas){
//        TODO replace with setup for an actual game
//        Level/Game Setup that needs access to a fully constructed class AND the canvas
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);

//        Add walls to the environment
        gameObjects.add(new Rect( canvas.getWidth()/2f, -100, canvas.getWidth(), 100, paint, 0));
        gameObjects.add(new Rect( canvas.getWidth()/2f, canvas.getHeight()+100, canvas.getWidth(), 100, paint, 0));
        gameObjects.add(new Rect( -100, canvas.getHeight()/2f, 100, canvas.getHeight(), paint, 0));
        gameObjects.add(new Rect( canvas.getWidth()+100, canvas.getHeight()/2f, 100, canvas.getHeight(), paint, 0));

//        Test rectangle
        gameObjects.add(new Rect( canvas.getWidth()/2f, 800, 200, 100, paint, 0));

//        Add throwable ball
        Vector2f pos = new Vector2f(canvas.getWidth(), canvas.getHeight()*2 - 300);
        pos = pos.multiply(0.5f);
        gameObjects.add(new Ball(pos));

//        Test circle
       Transform transform = new Transform(canvas.getWidth()/2f, 500, 80);
       gameObjects.add(new Circle(transform, 10, paint));
    }

    public void restartGame() {
        Log.i("game", "GAME RESTARTED");
        gameObjects = new LinkedList<>();
        needsSetup = true;
    }

    private void init(){
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorAccent, getContext().getTheme()));

        setOnTouchListener(Input.getInstance());
    }

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

}
