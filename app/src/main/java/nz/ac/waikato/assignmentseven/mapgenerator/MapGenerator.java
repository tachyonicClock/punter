package nz.ac.waikato.assignmentseven.mapgenerator;

import android.graphics.Paint;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import nz.ac.waikato.assignmentseven.GameObject;
import nz.ac.waikato.assignmentseven.GameWorld;
import nz.ac.waikato.assignmentseven.gameobjects.Circle;
import nz.ac.waikato.assignmentseven.gameobjects.Randomiser;
import nz.ac.waikato.assignmentseven.gameobjects.Rect;
import nz.ac.waikato.assignmentseven.gameobjects.Slowness;
import nz.ac.waikato.assignmentseven.gameobjects.Target;
import nz.ac.waikato.assignmentseven.physics.Transform;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public class MapGenerator {
    // Random used to generate a map
    private static Random rand = new Random();

    private static float randFloat(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }

    // MapQuad is a class that can generate a 1/4 map
    public abstract static class MapQuad {
        protected List<GameObject> objectGroup = new LinkedList<>();
        protected Paint p = new Paint();

        public List<GameObject> getMapQuad(){
            // Reset properties
            objectGroup = new LinkedList<>();
            p = new Paint();

            return generateMapQuad();
        }
        protected abstract List<GameObject> generateMapQuad();
    }

    // This is an array of objects that will create unique quadrants for the map
    private static MapQuad[] allMapQuads = new MapQuad[]{
            new MapQuad() {
                @Override
                public List<GameObject> generateMapQuad() {

                    // An empty quad

                    if (rand.nextBoolean()) {
                        // Add wall
                        p.setColor(ColourMeanings.UNMOVABLE);
                        Rect rect = new Rect(0.4f, 1f, 0.9f, 0.1f, p, 0);
                        objectGroup.add(rect);
                    }

                    return objectGroup;
                }
            },
            new MapQuad() {
                @Override
                public List<GameObject> generateMapQuad() {

                    // A randomiser obj
                    for (int i = 0; i < rand.nextInt(5); i++) {
                        objectGroup.add(new Randomiser(new Transform(0.5f, 0.5f, 0.04f), 1));
                    }

                    for (int i = 0; i < rand.nextInt(5); i++) {
                        objectGroup.add(new Slowness(new Transform(0.5f, 0.5f, 0.04f), 2));
                    }

                    return objectGroup;
                }
            },
            new MapQuad() {
                @Override
                public List<GameObject> generateMapQuad() {
                    // Add rotating rectangle
                    p.setColor(ColourMeanings.UNMOVABLE);
                    Rect rect = new Rect(0.5f, 0.5f, 0.4f, 0.2f, p, 50000);
                    rect.angularVelocity = randFloat(-20f, 20f);
                    objectGroup.add(rect);

                    return objectGroup;
                }
            },
            new MapQuad() {
                @Override
                public List<GameObject> generateMapQuad() {
                    // Add movable rectangle
                    p.setColor(ColourMeanings.MOVABLE_MEDIUM);
                    Rect rect = new Rect(0.5f, 0.5f, randFloat(0.2f, 0.8f), randFloat(0.2f, 0.8f), p, randFloat(1, 5));
                    objectGroup.add(rect);

                    return objectGroup;
                }
            },
            new MapQuad() {
                @Override
                public List<GameObject> generateMapQuad() {
                    // Add a bunch of targets
                    if (rand.nextBoolean())
                        objectGroup.add(new Target(new Transform(0.1f, 0.1f, 0.04f)));
                    if (rand.nextBoolean())
                        objectGroup.add(new Target(new Transform(0.65f, 0.1f, 0.04f)));
                    if (rand.nextBoolean())
                        objectGroup.add(new Target(new Transform(0.1f, 0.65f, 0.04f)));
                    if (rand.nextBoolean())
                        objectGroup.add(new Target(new Transform(0.65f, 0.65f, 0.04f)));

                    // Add Wall
                    if (rand.nextBoolean()) {
                        Paint p = new Paint();
                        p.setColor(ColourMeanings.UNMOVABLE);
                        Rect rect = new Rect(0.4f, 1f, 0.9f, 0.1f, p, 0);
                        objectGroup.add(rect);
                    }

                    return objectGroup;
                }
            },
            new MapQuad() {
                @Override
                public List<GameObject> generateMapQuad() {
                    // Add targets
                    objectGroup.add(new Target(new Transform(0.8f, 0.8f, 0.04f)));
                    objectGroup.add(new Target(new Transform(0.2f, 0.2f, 0.04f)));

                    // Add obstacle
                    p.setColor(ColourMeanings.UNMOVABLE);
                    float size = randFloat(0.1f, 0.6f);
                    Rect rect = new Rect(0.5f, 0.5f, size, size, p, 0);
                    rect.transform.setRotationInDegrees(randFloat(0, 360));
                    objectGroup.add(rect);

                    if (rand.nextBoolean()) {
                        // Add wall
                        rect = new Rect(0.4f, 1f, 0.9f, 0.1f, p, 0);
                        objectGroup.add(rect);
                    }

                    return objectGroup;
                }
            },
            new MapQuad() {
                @Override
                public List<GameObject> generateMapQuad() {
                    // Add target
                    objectGroup.add(new Target(new Transform(0.5f, 0.5f, 0.04f)));

                    // Add circle
                    p.setColor(ColourMeanings.MOVABLE_MEDIUM);
                    objectGroup.add(new Circle(new Transform(1f - 0.2f, 1f - 0.2f, 0.04f), 1, p));
                    return objectGroup;
                }
            },
            new MapQuad() {
                @Override
                public List<GameObject> generateMapQuad() {
                    // Add multiple randomized circles
                    p.setColor(ColourMeanings.MOVABLE_DAMPENED);
                    float mass = randFloat(0.5f, 2);
                    float size = randFloat(0.03f, 0.06f);
                    float padding = randFloat(0.2f, 0.35f);
                    float dampening = randFloat(0, 1f);
                    objectGroup.add(new Circle(new Transform(1f - padding, 1f - padding, size), mass, dampening, p));
                    objectGroup.add(new Circle(new Transform(padding, 1f - padding, size), mass, dampening, p));
                    objectGroup.add(new Circle(new Transform(1f - padding, padding, size), mass, dampening, p));
                    objectGroup.add(new Circle(new Transform(padding, padding, size), mass, dampening, p));

                    // Add a target or a circle
                    if (rand.nextBoolean())
                        objectGroup.add(new Target(new Transform(0.5f, 0.5f, 0.04f)));
                    else
                        objectGroup.add(new Circle(new Transform(0.5f, 0.5f, size), mass, dampening, p));

                    return objectGroup;
                }
            },
            new MapQuad() {
                @Override
                public List<GameObject> generateMapQuad() {
                    // Add target
                    objectGroup.add(new Target(new Transform(0.2f, 0.2f, 0.04f)));

                    // Add protective rectangle
                    p.setColor(ColourMeanings.MOVABLE_MEDIUM);
                    Rect rect = new Rect(0.5f, 0.5f, 0.8f, 0.15f, p, 10);
                    rect.transform.setRotationInDegrees(-45);
                    rect.dampening = 0.02f;
                    rect.angularDampening = 0.02f;
                    objectGroup.add(rect);

                    return objectGroup;
                }
            },
            new MapQuad() {
                @Override
                public List<GameObject> generateMapQuad() {
                    // Add unmovable object
                    p.setColor(ColourMeanings.UNMOVABLE);
                    float size = randFloat(0.1f, 0.6f);
                    Rect rect = new Rect(0.5f, 0.5f, size, size, p, 0);
                    objectGroup.add(rect);

                    // Add target
                    if (rand.nextBoolean())
                        objectGroup.add(new Target(new Transform(0.15f, 0.15f, 0.04f)));

                    return objectGroup;
                }
            }
    };

    // getRandomQuad creates a random quad from the allMapQuads list
    private static List<GameObject> getRandomQuad() {
        return allMapQuads[rand.nextInt(allMapQuads.length)].getMapQuad();
    }

    /**
     * GenerateMap randomly creates a map and transform it to the desired size
     *
     * @param width  Width of the canvas
     * @param height Height of the canvas
     * @param world  The GameWorld that the map should be generated for
     */
    public static void generateMap(float width, float height, GameWorld world) {
        // Create 4 random quads for each of the quadrants. The quadrants are rotated to best fill the space
        world.add(new Transform(new Vector2f(0, 0), new Vector2f(width/2f, height/2f), 0), getRandomQuad());
        world.add(new Transform(new Vector2f(width, 0), new Vector2f(height/2f, width/2f), 90), getRandomQuad());
        world.add(new Transform(new Vector2f(width, height), new Vector2f(width/2f, height/2f), 180), getRandomQuad());
        world.add(new Transform(new Vector2f(0, height), new Vector2f(height/2f, width/2f), 270), getRandomQuad());
    }
}
