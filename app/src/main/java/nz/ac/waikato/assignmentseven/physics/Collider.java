package nz.ac.waikato.assignmentseven.physics;

import nz.ac.waikato.assignmentseven.PhysicsObject;

/**
 * Base class for a physics collider
 */
public class Collider {
    protected PhysicsObject body;

    public Collider(PhysicsObject body) {
        this.body = body;
    }

    public Vector2f getOrigin() {
        return new Vector2f(body.transform.translation);
    }
}
