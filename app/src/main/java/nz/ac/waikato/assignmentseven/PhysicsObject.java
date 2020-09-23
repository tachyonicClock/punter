package nz.ac.waikato.assignmentseven;

import android.util.Log;

import nz.ac.waikato.assignmentseven.physics.Collider;
import nz.ac.waikato.assignmentseven.physics.Vector2f;

public abstract class PhysicsObject extends GameObject {
//    The velocity of the object
    public Vector2f velocity = new Vector2f();
//    Force that is currently being applied to the object
    public Vector2f force = new Vector2f();
//    A measure of how much matter is in the thing
    public float mass = 1;
//    A measure of how bouncy the material is
    public float restitution = 1;

    public void onCollision(PhysicsObject other, Vector2f normal){
        Log.i("game", "Collision! " + this.toString());
//        Calculate relative velocity
        Vector2f relV = velocity.subtract(other.velocity);

//        Calculate relative velocity in terms of the normal
        float velAlongNormal = relV.dotProduct(normal);

//        Do not do anything if they are already resolving the collision
        if (velAlongNormal > 0)
            return;

//        Calculate the collisions restitution
        float e = Math.min(restitution, other.restitution);
//        Calculate impulse scalar
        float j = -(1+e) * velAlongNormal;
        j /= 1 / mass + 1 / other.mass;

        Vector2f impulse = normal.multiply(j);
        force = impulse;
//        velocity = velocity.add(impulse.multiply(1/mass));
    }

    public abstract Collider getCollider();

    void onPhysicsUpdate(float deltaTime) {
//        v = v + at where a = F/m
        velocity = velocity.add(force.divide(mass));
//        only apply the forces once every second
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
