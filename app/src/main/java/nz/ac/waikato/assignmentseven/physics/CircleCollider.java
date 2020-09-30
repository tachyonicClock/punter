package nz.ac.waikato.assignmentseven.physics;
import nz.ac.waikato.assignmentseven.PhysicsObject;

public class CircleCollider extends Collider {

    public float getRadius(){
        return body.transform.scale.magnitude();
    }

    public CircleCollider(PhysicsObject parent) {
        super(parent);
    }

}
