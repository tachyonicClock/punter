package nz.ac.waikato.assignmentseven.physics;

import nz.ac.waikato.assignmentseven.PhysicsObject;

public abstract class Collider {

    PhysicsObject body;

    /**
     * Check weather this collider is colliding with the other collider
     * @param other the other collider
     * @return Are they colliding?
     */
    public abstract boolean isColliding(Collider other);

    /**
     * Get the normal of a collision
     * @param other
     * @return
     */
    public abstract Vector2f collisionNormal(Collider other);

    public Collider(PhysicsObject body) {
        this.body = body;
    }
}
