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


//    omega is the rotational velocity. In radians/second
    public float angularVelocity = 0;
//    torque is the rotational force that will be applied
    public float torque = 0;
//    inertia is the rotational inertia of the object
    public float inertia = 0;

    public abstract Collider getCollider();

    public float inverseMass(){
        if (mass == 0)  return 0;
        return 1 / mass;
    }

    public float inverseInertia(){
        if (inertia == 0)  return 0;
        return 1.0f/inertia;
    }

    public Vector2f getForce(){
        return new Vector2f(force);
    }

    public void addForce(Vector2f f){
        force = force.add(f);
    }

    public void addTorque(float torque){
        this.torque += torque;
    }

    public void applyImpulse(Vector2f impulse, Vector2f point){
        force = force.add(impulse);
        torque += point.crossProduct(impulse);
    }

    public void onCollision(Collision collision){
    }

    void onPhysicsUpdate(float deltaTime) {
        if (mass == 0) return;
//        PERFORM INTEGRATION USING TIME-SLICES

//        Integrate Forces
        velocity = velocity.add(force.multiply(inverseMass()));
        angularVelocity += torque * inverseInertia();

//        Integrate Velocity
        transform.translation = transform.translation.add(velocity.multiply(deltaTime));
        transform.setRotationInRadians(transform.getRotationInRadians() + angularVelocity * deltaTime);

//        Clear forces
        force.setZero();
        torque = 0;
    }

    public PhysicsObject(){
    }

    public PhysicsObject(float mass){
        this.mass = mass;
    }

}
