package nz.ac.waikato.assignmentseven.physics;
import nz.ac.waikato.assignmentseven.PhysicsObject;

/**
 * Collision detects when two physics objects collide with each other. And determines how the collision
 * should be resolved. Resolving a collision involves moving them apart from each other. The Collision
 * class defers the actual implementation resolution to the physics objects but provides parameters.
 *
 * This is roughly equivalent to the collision manifold described here:
 * https://gamedevelopment.tutsplus.com/tutorials/how-to-create-a-custom-2d-physics-engine-the-basics-and-impulse-resolution--gamedev-6331
 */
public class Collision {

//    PERCENT is used for positional correction to avoid objects penetrating each other
    private static float PERCENT = 0.05f;
//    SLOP refers to how far objects should be able to penetrate before we adjust their positions
    private static float SLOP = 0.01f;


//    The two objects that could be part of a collision
    private PhysicsObject objA;
    private PhysicsObject objB;

//    Penetration is how far the objects overlap with each other
    private float penetration;
//    Normal is the direction that the physics objects will be moved to resolve the collision
    private Vector2f normal = new Vector2f();
//    Impulse is a force along the normal that will resolve the collision
    private Vector2f impulse = new Vector2f();
//    Are the physics objects colliding?
    private boolean isColliding = false;

//    isCollision returns whether or not the objects are colliding
    public boolean isCollision(){
        return isColliding;
    }

    /**
     * getOther returns the other physics object in the collision
     *
     * @param obj One of the physics objects in the collision
     * @return The other physics object in the collision or null if the collision does not contain
     *         the provided physics object
     */
    public PhysicsObject getOther(PhysicsObject obj){
        if (obj == objA) return objB;
        if (obj == objB) return objA;
        return null;
    }

//    getPenetration returns how far the two shapes are overlapping
    public float getPenetration() {
        if (!isColliding) return 0;
        return penetration;
    }


    /**
     * getNormal returns the direction of the collision relative to the provided reference
     *
     * @param reference The origin of the normal
     * @return The collision normal. Will be a Zero vector if they are not colliding
     */
    public Vector2f getNormal(PhysicsObject reference) {
        if (!isColliding) return new Vector2f();
        if (reference == objB) return normal.invert();
        return normal;
    }

    /**
     * getImpulse returns the impulse of the resolution of the collision relative to the provided reference
     *
     * @param reference The origin of the impulse
     * @return An impulse that will resolve the collision. Will be a zero vector if they are not colliding
     */
    public Vector2f getImpulse(PhysicsObject reference){
        if (!isColliding) return new Vector2f();
        if (reference == objB) return impulse.invert();
        return impulse;
    }

    private static float clamp(float val, float min, float max){
        return Math.max(min, Math.min(max, val));
    }

//    CircleVsCircle calculates collisions between circles
    private void CircleVsCircle(CircleCollider a, CircleCollider b){
//        Total radius
        float r = a.getRadius() + b.getRadius();
//        Distance between circle centers
        float d = a.getCenter().subtract(b.getCenter()).magnitude();

//        Check collision
        isColliding = d < r;

//        Calculate collision parameters
        if (isCollision()){
            normal = a.getCenter().subtract(b.getCenter()).normalized();
            penetration = r - d;
        }
    }

//    RectVsCircle calculates collision between a rect and a circle
    private void RectVsCircle(RectangleCollider a, CircleCollider b){
        Vector2f aToB = a.getCenter().subtract(b.getCenter());

        float xExtent = a.getWidth()/2;
        float yExtent = a.getHeight()/2;

//        Get closest point/edge
        Vector2f closest = new Vector2f(
                clamp(aToB.x, -xExtent, xExtent),
                clamp(aToB.y, -yExtent, yExtent));

        boolean inside = false;
//        Circle is inside of the rect so we move the circle's center to the rect's edge
        if (closest.equal(aToB)){
            inside = true;
//            closest axis
            if(Math.abs(aToB.x) > Math.abs(aToB.y)){
                closest.x = Math.abs(xExtent) + SLOP;
            }else {
                closest.y = Math.abs(yExtent) + SLOP;
            }
        }

        normal = aToB.subtract(closest);
        float d = normal.magnitudeSquared();
        float r = b.getRadius();

//        Check collision
        isColliding = !(d > r * r && !inside);
        if (!isCollision()) return;

//        This is a bit of a performance hack holding off sqrt until needed
        d = (float) Math.sqrt(d);
        if (inside){
            normal = normal.normalized().invert();
            penetration = d;
        }else {
            normal = normal.normalized();
            penetration = r - d;
        }
    }

    private void RectVsRect(RectangleCollider a, RectangleCollider b){
//        TODO Implement collision
    }

//    Check a collision between any two types of collider
    private void checkCollision(Collider colA, Collider colB){
        if (colA instanceof CircleCollider && colB instanceof CircleCollider){
            CircleVsCircle((CircleCollider)colA, (CircleCollider)colB);
        }else if (colA instanceof RectangleCollider && colB instanceof CircleCollider){
            RectVsCircle((RectangleCollider)colA, (CircleCollider)colB);
        }else if (colA instanceof RectangleCollider && colB instanceof RectangleCollider){
            RectVsRect((RectangleCollider)colA, (RectangleCollider)colB);
        }
    }

    private void calculateImpulse() {
//        Calculate relative velocity
        Vector2f relV = objA.velocity.subtract(objB.velocity);

//        Calculate relative velocity in terms of the normal
        float velAlongNormal = relV.dotProduct(getNormal(objA));

//        Do not do anything if they are already resolving the collision
        if (velAlongNormal > 0)
            return;

//        Calculate the collisions restitution
        float e = Math.min(objA.restitution, objB.restitution);
//        Calculate impulse scalar
        float j = -(1+e) * velAlongNormal;
        j /= objA.inverseMass() + objB.inverseMass();

        impulse = getNormal(objA).multiply(j);
    }

//    positionalCorrection moves objects so they do not sink into each other because of floating point
//    errors
    private void positionalCorrection(){
        float correctionF = Math.max(getPenetration() - SLOP, 0.0f) / (objA.inverseMass() + objB.inverseMass());
        Vector2f correction = getNormal(objA).multiply(correctionF).multiply(-PERCENT);
        if(objA.mass != 0) objA.transform.translation = objA.transform.translation.subtract(correction);
        if(objB.mass != 0) objB.transform.translation = objB.transform.translation.add(correction);
    }

//    updatePhysicsObjects calls the onCollision callbacks
    public void updatePhysicsObjects(){
        calculateImpulse();
        positionalCorrection();
        objA.onCollision(this);
        objB.onCollision(this);
    }

//    updateCollision is used to recalculate the collision
    public void updateCollision(){
        isColliding = false;
        Collider colA = objA.getCollider();
        Collider colB = objB.getCollider();

//        Check collisions both ways around
        checkCollision(colA, colB);
        if(!isCollision()) {
//            Swap references
            PhysicsObject tmp = objA;
            objA = objB;
            objB = tmp;
            checkCollision(colB, colA);
        }
        if(!isCollision()) return;

//        Update Physics
        updatePhysicsObjects();
    }

    public Collision(PhysicsObject objA, PhysicsObject objB){
        this.objA = objA;
        this.objB = objB;
    }
}
