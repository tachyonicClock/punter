package nz.ac.waikato.assignmentseven.physics;

import nz.ac.waikato.assignmentseven.PhysicsObject;

public class Collider {

    protected PhysicsObject body;

    public Vector2f getCenter(){
        return new Vector2f(body.transform.translation);
    }

    public Collider(PhysicsObject body) {
        this.body = body;
    }
}
