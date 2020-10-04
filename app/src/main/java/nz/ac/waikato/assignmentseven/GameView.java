package nz.ac.waikato.assignmentseven;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import org.jetbrains.annotations.NotNull;

import nz.ac.waikato.assignmentseven.gameobjects.Ball;
import nz.ac.waikato.assignmentseven.gameobjects.Circle;
import nz.ac.waikato.assignmentseven.gameobjects.Rect;
import nz.ac.waikato.assignmentseven.physics.Collision;
import nz.ac.waikato.assignmentseven.physics.Transform;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class GameView extends View {
    Paint paint;

    private final static float MAX_TIME_STEP = 1/60f;

    private GameWorld world = new GameWorld();

    private ScoreHandler scoreHandler = new ScoreHandler();

//    We need to keep track of when the last draw was in order to accurately calculate physics
    private long previousDraw = System.currentTimeMillis();
    private boolean needsSetup = true;

    void gameLoop(Canvas canvas) {
//        deltaTime is the amount of time since the last game loop
        float deltaTime = (float) (System.currentTimeMillis() - previousDraw)/1000f;
        previousDraw = System.currentTimeMillis();

//        If we have not been updated in a little while, it is very possible we have been paused. To
//        avoid very large time-steps that can break things we add a max time-step
        if (deltaTime > MAX_TIME_STEP) deltaTime = MAX_TIME_STEP;

//        Physics Loop
        for (GameObject gameObject : world.getPhysicsObjectSet()) {
            if (gameObject instanceof PhysicsObject){
                ((PhysicsObject) gameObject).onPhysicsUpdate(deltaTime);
            }
        }

//        Perform collisions
        for (Collision collision : world.getCollisions()){
            collision.updateCollision();
        }

//        Update Loop
        for (GameObject gameObject : world.getGameObjects()) {
            gameObject.onUpdate(canvas, deltaTime);
        }

//        Render Loop
        for (GameObject gameObject : world.getGameObjects()) {
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
        world.add(new Rect( canvas.getWidth()/2f, -100, canvas.getWidth(), 100, paint, 0));
        world.add(new Rect( canvas.getWidth()/2f, canvas.getHeight()+100, canvas.getWidth(), 100, paint, 0));
        world.add(new Rect( -100, canvas.getHeight()/2f, 100, canvas.getHeight(), paint, 0));
        world.add(new Rect( canvas.getWidth()+100, canvas.getHeight()/2f, 100, canvas.getHeight(), paint, 0));

//        Test rectangle
        world.add(new Rect( canvas.getWidth()/2f, 800, 200, 100, paint, 0));

//        Add throwable ball
        Vector2f pos = new Vector2f(canvas.getWidth(), canvas.getHeight()*2 - 300);
        pos = pos.multiply(0.5f);
        world.add(new Ball(pos));

//        Test circle
       Transform transform = new Transform(canvas.getWidth()/2f, 500, 80);
        world.add(new Circle(transform, 10, paint));
    }

    public void restartGame() {
        Log.i("game", "GAME RESTARTED");
        world = new GameWorld();
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
