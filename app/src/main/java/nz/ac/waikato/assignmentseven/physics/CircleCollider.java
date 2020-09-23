package nz.ac.waikato.assignmentseven.physics;
import nz.ac.waikato.assignmentseven.PhysicsObject;

public class CircleCollider extends Collider {

    public CircleCollider(PhysicsObject parent) {
        super(parent);
    }

    @Override
    public boolean isColliding(Collider other) {
//        Circle vs circle collision
        if (other instanceof CircleCollider) {
            return body.transform.translation.subtract(other.body.transform.translation).magnitude()
                    < body.transform.scale.magnitude() + other.body.transform.scale.magnitude();
        }
        return false;
    }

    @Override
    public Vector2f collisionNormal(Collider other) {
        if (other instanceof CircleCollider) {
            return body.transform.translation.subtract(other.body.transform.translation).normalized();
        }
        return null;
    }
}
