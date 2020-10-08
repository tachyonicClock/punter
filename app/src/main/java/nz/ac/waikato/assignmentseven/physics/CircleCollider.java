package nz.ac.waikato.assignmentseven.physics;

import nz.ac.waikato.assignmentseven.PhysicsObject;

public class CircleCollider extends Collider {
    public CircleCollider(PhysicsObject parent) {
        super(parent);
    }

    /**
     * getRadius returns the magnitude of the scale
     */
    public float getRadius() {
        return body.transform.scale.magnitude();
    }
}
