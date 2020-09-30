package nz.ac.waikato.assignmentseven.physics;

import nz.ac.waikato.assignmentseven.PhysicsObject;

public class RectangleCollider extends Collider {
    private boolean inverted = false;

    public boolean isInverted(){
        return inverted;
    }

    public float getLeft(){
        return body.transform.translation.x - body.transform.scale.x;
    }

    public float getRight(){
        return body.transform.translation.x + body.transform.scale.x;
    }

    public float getTop(){
        return body.transform.translation.y - body.transform.scale.y;
    }

    public float getBottom(){
        return body.transform.translation.y + body.transform.scale.y;
    }

    public float getHeight(){
        return body.transform.scale.y * 2;
    }

    public float getWidth(){
        return body.transform.scale.x * 2;
    }

    public RectangleCollider(PhysicsObject body) {
        super(body);
    }

    public RectangleCollider(PhysicsObject body, boolean inverted){
        super(body);
        this.inverted = inverted;
    }
}
