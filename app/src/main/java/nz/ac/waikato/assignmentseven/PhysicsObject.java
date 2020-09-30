package nz.ac.waikato.assignmentseven;

import nz.ac.waikato.assignmentseven.physics.Collider;
import nz.ac.waikato.assignmentseven.physics.Collision;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public abstract class PhysicsObject extends GameObject {
//    The velocity of the object
    public Vector2f velocity = new Vector2f();
//    Force that is currently being applied to the object
    private Vector2f force = new Vector2f();
//    A measure of how much matter is in the thing
    public float mass = 1;
//    A measure of how bouncy the material is
    public float restitution = 1;

    public abstract Collider getCollider();

    public float inverseMass(){
        if (mass == 0)  return 0;
        return 1 / mass;
    }

    public Vector2f getForce(){
        return new Vector2f(force);
    }

    public void addForce(Vector2f f){
        force = force.add(f);
    }

    public void onCollision(Collision collision){
        force = force.add(collision.getImpulse(this));
    }



    void onPhysicsUpdate(float deltaTime) {
//        v = v + at where a = F/m
        velocity = velocity.add(force.multiply(inverseMass()));
//        only apply the forces once
        force = new Vector2f();
//        s = s + vt
        transform.translation = transform.translation.add(velocity.multiply(deltaTime));
    }

    public PhysicsObject(){
    }

    public PhysicsObject(float mass){
        this.mass = mass;
    }
}
