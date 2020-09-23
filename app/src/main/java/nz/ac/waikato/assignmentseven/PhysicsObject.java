package nz.ac.waikato.assignmentseven;

import nz.ac.waikato.assignmentseven.physics.Vector2f;

public abstract class PhysicsObject extends GameObject {
    public Vector2f velocity = new Vector2f();
    public Vector2f force = new Vector2f();
    public float mass;

    void onPhysicsUpdate(float deltaTime) {
//        v = v + at where a = F/m
        velocity = velocity.add(force.divide(mass).multiply(deltaTime));
//        only apply the forces once every second
        force = force.subtract(force.multiply(deltaTime));
//        s = s + vt
        transform.translation = transform.translation.add(velocity.multiply(deltaTime));
    }

    public PhysicsObject(){
        mass = 1;
    }

    public PhysicsObject(float mass){
        this.mass = mass;
    }
}
