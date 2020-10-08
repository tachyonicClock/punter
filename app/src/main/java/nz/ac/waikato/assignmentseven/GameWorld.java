package nz.ac.waikato.assignmentseven;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import nz.ac.waikato.assignmentseven.physics.Collision;
import nz.ac.waikato.assignmentseven.physics.Transform;

public class GameWorld {
    // Store the game state in HashSets
    private Set<PhysicsObject> physicsObjectSet = new HashSet<>();
    private Set<GameObject> gameObjectHashSet = new HashSet<>();
    private Set<Collision> collisionHashSet = new HashSet<>();

    // get all collision manifolds
    public Set<Collision> getCollisions() {
        return collisionHashSet;
    }

    // get all physics objects
    public Set<PhysicsObject> getPhysicsObjectSet() {
        return physicsObjectSet;
    }

    // get all game objects
    public Set<GameObject> getGameObjects() {
        return gameObjectHashSet;
    }

    // add a PhysicsObject to the game world
    public void add(PhysicsObject obj) {
        // Add all possible collision manifolds
        for (PhysicsObject gameObject : physicsObjectSet) {
            if (!obj.equals(gameObject))
                collisionHashSet.add(new Collision(obj, gameObject));
        }

        // Add to sets
        obj.calculateInertia();
        physicsObjectSet.add(obj);
        gameObjectHashSet.add(obj);
    }

    public void add(Transform transform, List<GameObject> objectList) {
        for (GameObject gameObject : objectList) {
            gameObject.transform = transform.applyTran(gameObject.transform);
            add(gameObject);
        }
    }

    // add a GameObject to the game world
    public void add(GameObject obj) {
        if (obj instanceof PhysicsObject) {
            add((PhysicsObject) obj);
            return;
        }

        gameObjectHashSet.add(obj);
    }

    // remove a PhysicsObject from the game world
    public void remove(PhysicsObject obj) {
        // Iterate over list and remove all collision with the object in them
        Iterator<Collision> iter = collisionHashSet.iterator();
        while (iter.hasNext()) {
            if (iter.next().contains(obj)) {
                iter.remove();
            }
        }
        // Remove from both sets
        physicsObjectSet.remove(obj);
        gameObjectHashSet.remove(obj);
    }

    // remove a GameObject from the game world
    public void remove(GameObject obj) {
        if (obj instanceof PhysicsObject) {
            remove((PhysicsObject) obj);
            return;
        }

        gameObjectHashSet.remove(obj);
    }
}
