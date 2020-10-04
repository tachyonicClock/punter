package nz.ac.waikato.assignmentseven.gameobjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.LinkedList;
import java.util.Queue;

import nz.ac.waikato.assignmentseven.GameObject;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

/**
 *  Gizmos are used to aid with visual debugging, by offering a singleton interface
 */
public class Gizmos extends GameObject {

    private Paint paint = new Paint();
    private Queue<DrawablePoint> points = new LinkedList<>();
    private Queue<DrawableLine> lines = new LinkedList<>();

    private static Gizmos instance = null;

    private class DrawablePoint{
        Vector2f position;
        float radius;
        int colour;
        public DrawablePoint(Vector2f position, float radius, int colour){
            this.position = position;
            this.radius = radius;
            this.colour = colour;
        }
    }
    private class DrawableLine{
        Vector2f a;
        Vector2f b;
        float thickness;
        int colour;
        public DrawableLine(Vector2f a, Vector2f b, float thickness, int colour){
            this.a = a;
            this.b = b;
            this.thickness = thickness;
            this.colour = colour;
        }
    }

    public void drawLine(Vector2f a, Vector2f b, float thickness, int colour){
        lines.add(new DrawableLine(a, b, thickness, colour));
    }

    public void drawPoint(Vector2f position, float radius, int colour){
        points.add(new DrawablePoint(position, radius, colour));
    }

    public void drawPoint(Vector2f position){
        points.add(new DrawablePoint(position, 10, Color.argb(127, 255, 0, 0)));
    }

    @Override
    public void onUpdate(Canvas canvas, float deltaTime) {
    }

    @Override
    public void onDraw(Canvas canvas) {
        while (!points.isEmpty()){
            DrawablePoint point = points.remove();
            paint.setColor(point.colour);
            canvas.drawCircle(point.position.x, point.position.y, point.radius, paint);
        }
        while (!lines.isEmpty()){
            DrawableLine line = lines.remove();
            paint.setStrokeWidth(line.thickness);
            paint.setColor(line.colour);
            canvas.drawLine(line.a.x, line.a.y, line.b.x, line.b.y, paint);
        }
    }

    public static Gizmos getInstance() {
        if (instance == null) instance = new Gizmos();
        return instance;
    }
}
