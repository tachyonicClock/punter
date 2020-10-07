package nz.ac.waikato.assignmentseven.mapgenerator;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import nz.ac.waikato.assignmentseven.GameObject;
import nz.ac.waikato.assignmentseven.GameWorld;
import nz.ac.waikato.assignmentseven.gameobjects.Circle;
import nz.ac.waikato.assignmentseven.gameobjects.Rect;
import nz.ac.waikato.assignmentseven.gameobjects.Target;
import nz.ac.waikato.assignmentseven.physics.Transform;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class MapGenerator {
    private static Random rand = new Random();

    private static float randFloat(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }

    public abstract static class MapQuad {
//        I define a map quad as an object group containing objects that will be scaled to fit a
//        quarter screen.
        public abstract List<GameObject> getMapQuad();
    }

//    This is an array of objects that will create unique quadrants for the map
    private static MapQuad[] allMapQuads = new MapQuad[]{
        new MapQuad(){
            @Override
            public List<GameObject> getMapQuad() {
//                Empty quad
                return new LinkedList<>();
            }
        },
        new MapQuad(){
            @Override
            public List<GameObject> getMapQuad() {
//                Just a target
                List<GameObject> objectGroup = new LinkedList<>();
                objectGroup.add(new Target(new Transform(0.5f, 0.5f, 0.04f)));
                return objectGroup;
            }
        },
        new MapQuad(){
            @Override
            public List<GameObject> getMapQuad() {
//                A target and some blue circles
                List<GameObject> objectGroup = new LinkedList<>();
                Paint p = new Paint();
                p.setColor(ColourMeanings.MOVABLE_MEDIUM);
                objectGroup.add(new Target(new Transform(0.5f, 0.5f, 0.04f)));
                objectGroup.add(new Circle(new Transform(1f-0.2f, 1f-0.2f, 0.04f), 1, p));
                return objectGroup;
            }
        },
        new MapQuad(){
            @Override
            public List<GameObject> getMapQuad() {
//                So many circles in a 5 dot pattern
                List<GameObject> objectGroup = new LinkedList<>();
                Paint p = new Paint();
                p.setColor(ColourMeanings.MOVABLE_MEDIUM);
                float mass = randFloat(0.1f, 1) ;
                float size =  randFloat(0.02f, 0.04f) ;
                float padding = randFloat(0.2f, 0.35f);
                objectGroup.add(new Circle(new Transform(1f-padding, 1f-padding, size), mass, p));
                objectGroup.add(new Circle(new Transform(padding, 1f-padding, size), mass, p));
                objectGroup.add(new Circle(new Transform(1f-padding, padding, size), mass, p));
                objectGroup.add(new Circle(new Transform(padding, padding, size), mass, p));
                objectGroup.add(new Circle(new Transform(0.5f, 0.5f, size), mass, p));
                return objectGroup;
            }
        },
//        TODO Fix bug where normals are not inverted correctly and thus break when -1 scales are used
//        new MapQuad(){
//            @Override
//            public List<GameObject> getMapQuad() {
////                A protected target
//                List<GameObject> objectGroup = new LinkedList<>();
//                Paint p = new Paint();
//                p.setColor(ColourMeanings.MOVABLE_MEDIUM);
//                Rect rect = new Rect(0.5f,0.5f, 0.8f, 0.15f, p, 5);
//                objectGroup.add(new Target(new Transform(0.2f, 0.2f, 0.04f)));
//                rect.transform.setRotationInDegrees(-45);
//                objectGroup.add(rect);
//                return objectGroup;
//            }
//        }
    };

    public static List<GameObject> getRandomQuad(){
        return allMapQuads[rand.nextInt(allMapQuads.length)].getMapQuad();
    }

    public static void generateMap(float width, float height, GameWorld world){
        world.add(new Transform(new Vector2f(0, 0), new Vector2f(width/2f, height/2f)), getRandomQuad());
        world.add(new Transform(new Vector2f(width, 0), new Vector2f(-width/2f, height/2f)), getRandomQuad());
        world.add(new Transform(new Vector2f(0, height), new Vector2f(width/2f, -height/2f)), getRandomQuad());
        world.add(new Transform(new Vector2f(width, height), new Vector2f(-width/2f, -height/2f)), getRandomQuad());



    }
}
