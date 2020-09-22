package nz.ac.waikato.assignmentseven;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import nz.ac.waikato.assignmentseven.gameobjects.Circle;
import nz.ac.waikato.assignmentseven.physics.Transform;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class Game extends View {
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

//        TODO: physics Loop will go here

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

        if (needsSetup){
            setupGame(canvas);
            needsSetup = false;
        }
        gameLoop(canvas);
        invalidate();
    }

//    setupGame is used for setup that cannot be done in the constructor since it needs the canvas
    private void setupGame(Canvas canvas){
//        Level/Game Setup that needs access to a fully constructed class AND the canvas

//        TODO replace with setup for an actual game
//        Demo drawing a bunch of circles
        Random rnd = new Random();
        int bound = canvas.getWidth();

        for (int i = 0; i < 500; i++) {
            Vector2f pos = new Vector2f(canvas.getWidth(), canvas.getHeight());
            pos = pos.multiply(0.5f);
            pos = pos.add(new Vector2f(rnd.nextInt(bound)-bound/2, rnd.nextInt(bound)-bound/2));
            paint.setARGB(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            gameObjects.add(new Circle(new Transform(pos, rnd.nextInt(100)), new Paint(paint)));
        }
    }

    private void init(){
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorAccent, getContext().getTheme()));
    }

    public Game(Context context) {
        super(context);
        init();
    }

    public Game(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Game(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Game(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

}
