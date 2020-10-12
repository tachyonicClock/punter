package nz.ac.waikato.assignmentseven;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import nz.ac.waikato.assignmentseven.gameobjects.Ball;
import nz.ac.waikato.assignmentseven.gameobjects.Gizmos;
import nz.ac.waikato.assignmentseven.gameobjects.Rect;
import nz.ac.waikato.assignmentseven.gameobjects.ScoreDisplay;
import nz.ac.waikato.assignmentseven.gameobjects.Target;
import nz.ac.waikato.assignmentseven.mapgenerator.MapGenerator;
import nz.ac.waikato.assignmentseven.physics.Collision;
import nz.ac.waikato.assignmentseven.physics.Transform;

public class GameView extends View {
    private final static float MAX_TIME_STEP = 1 / 30f;
    Paint paint;
    private GameWorld world = new GameWorld();

    //    We need to keep track of when the last draw was in order to accurately calculate physics
    private long previousDraw = System.currentTimeMillis();
    private boolean needsSetup = false;

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

    public void start(){
        needsSetup = true;
    }

    void gameLoop(Canvas canvas) {
//        deltaTime is the amount of time since the last game loop
        float deltaTime = (float) (System.currentTimeMillis() - previousDraw) / 1000f;
        previousDraw = System.currentTimeMillis();

//        If we have not been updated in a little while, it is very possible we have been paused. To
//        avoid very large time-steps that can break things we add a max time-step
        if (deltaTime > MAX_TIME_STEP) deltaTime = MAX_TIME_STEP;

//        Physics Loop
        for (PhysicsObject gameObject : world.getPhysicsObjectSet()) {
            gameObject.onPhysicsUpdate(deltaTime);
        }

//        Perform collisions
        for (Collision collision : world.getCollisions()) {
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
        if (needsSetup) {
            startGame(canvas);
            needsSetup = false;
        }

        gameLoop(canvas);
        invalidate();
    }

    //    setupGame is used for setup that cannot be done in the constructor since it needs the canvas
    private void startGame(@NotNull Canvas canvas) {
//        Level/Game Setup that needs access to a fully constructed class AND the canvas
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);

//        Add walls to the environment
        float thickness = 1000;
        float width = canvas.getWidth();
        float height = canvas.getHeight();
        world.add(new Rect(width / 2, -thickness / 2f, width, thickness, paint, 0));
        world.add(new Rect(width / 2, height + thickness / 2f, width, thickness, paint, 0));
        world.add(new Rect(-thickness / 2f, height / 2, thickness, height - 1, paint, 0));
        world.add(new Rect(width + thickness / 2f, height / 2, thickness, height - 1, paint, 0));

//        Map generator generates map
        MapGenerator.generateMap(width, height, world);

        // Ensure the existence of a target
        int targets = 0;
        for (GameObject gameObject : world.getGameObjects()) {
            if(gameObject instanceof Target) targets ++;
        }
        if (targets < 1) {
            restartGame();
            startGame(canvas);
        }

//        Score board shows current score
        Paint textPaint = new Paint();
        textPaint.setTextSize(100);
        world.add(new ScoreDisplay(new Transform(width / 2, 200, 20), textPaint));

//        Add throwable ball
        Ball ball = new Ball();
        world.add(ball);

//        Add debug info
        world.add(Gizmos.getInstance());

//        RUN ON START EVENTS
        for (GameObject gameObject : world.getGameObjects()) {
            gameObject.onStart(canvas, getContext());
        }
    }

    public void restartGame() {
        Log.i("game", "GAME RESTARTED");
        world = new GameWorld();
        needsSetup = true;
    }

    private void init() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorAccent, getContext().getTheme()));
        setOnTouchListener(Input.getInstance());
    }

}
