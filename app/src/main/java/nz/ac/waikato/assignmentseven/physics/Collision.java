package nz.ac.waikato.assignmentseven.physics;

import java.util.LinkedList;
import java.util.List;

import nz.ac.waikato.assignmentseven.PhysicsObject;
import nz.ac.waikato.assignmentseven.gameobjects.Gizmos;

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
    private static float PERCENT = 0.001f;
//    SLOP refers to how far objects should be able to penetrate before we adjust their positions
    private static float SLOP = 0.2f;

//    The two objects that could be part of a collision
    private PhysicsObject objA;
    private PhysicsObject objB;

//    Penetration is how far the objects overlap with each other
    private float penetration;
//    Normal is the direction that the physics objects will be moved to resolve the collision
    private Vector2f normal = new Vector2f();
//    Are the physics objects colliding?
    private boolean isColliding = false;

    private List<Vector2f> contactPoints = new LinkedList<>();

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

    public boolean contains(PhysicsObject obj){
        return obj == objA || obj == objB;
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

//    CircleVsCircle calculates collisions between circles
    private void CircleVsCircle(CircleCollider a, CircleCollider b){
//        Total radius
        float r = a.getRadius() + b.getRadius();
//        Distance between circle centers
        float d = a.getOrigin().subtract(b.getOrigin()).magnitude();

//        Check collision
        isColliding = d < r;

//        Calculate collision parameters
        if (isCollision()){
            normal = a.getOrigin().subtract(b.getOrigin()).normalized();
            contactPoints.add(normal.multiply(d).add(a.getOrigin()));
            penetration = r - d;
        }
    }

    private void PolyVsPoly(PolygonCollider a, PolygonCollider b){
//        Are they colliding?
        PolygonCollider.AxisOfLeastPenetration penetrationA = new PolygonCollider.AxisOfLeastPenetration(a, b);
        if (penetrationA.distance > 0.0f) return;
        PolygonCollider.AxisOfLeastPenetration penetrationB = new PolygonCollider.AxisOfLeastPenetration(b, a);
        if (penetrationB.distance > 0.0f) return;

//        Determine which shape contains reference face
        boolean flip;
        int referenceIndex = 0;
        PolygonCollider refPoly; // Reference
        PolygonCollider incPoly; // Incident
        if (ImpulseMath.gt(penetrationA.distance, penetrationB.distance)){
            refPoly = a;
            incPoly = b;
            referenceIndex = penetrationA.faceIndex;
            flip = false;
        }else{
            refPoly = b;
            incPoly = a;
            referenceIndex = penetrationB.faceIndex;
            flip = true;
        }

//        The incident face is the face that is being hit
        Face incidentFace = PolygonCollider.findIncidentFace(refPoly, incPoly, referenceIndex);

//        The reference face is the other face involved in the collision
        Face referenceFace = new Face(refPoly.getWorldVertex(referenceIndex),
                                      refPoly.getWorldVertex(referenceIndex + 1 == refPoly.size()? 0 : referenceIndex + 1 ) );
        Vector2f sidePlaneNormal = referenceFace.b.subtract(referenceFace.a).normalized();
        Vector2f refFaceNormal = new Vector2f( sidePlaneNormal.y, -sidePlaneNormal.x );

        float refC = refFaceNormal.dotProduct(incidentFace.a);

        normal = flip ? refFaceNormal : refFaceNormal.invert();

        float separation = refFaceNormal.dotProduct(incidentFace.a) - refC;
        if (separation <= 0.0f)
        {
            contactPoints.add(incidentFace.a);
            penetration = -separation;
        }

        separation = refFaceNormal.dotProduct(incidentFace.b) - refC;
        if (separation <= 0.0f)
        {
            contactPoints.add(incidentFace.b);
            penetration += -separation;
            penetration /= contactPoints.size();
        }

        isColliding = true;
    }

    private void CircleVsPoly(CircleCollider a, PolygonCollider b){

//        Get transform objects. These are used to transform the basis
        Transform invTF = b.body.transform.inverse();
        Transform tf = b.body.transform;

//        Get the circle center in the Polygon basis
        Vector2f circleCenter = invTF.reverseApply(a.getOrigin());

//        Get the face with the largest separation
        float separation = Float.NEGATIVE_INFINITY;
        int faceNormal = 0;
        for (int i = 0; i < b.size(); i++){
            float s = b.getNormal(i).dotProduct(circleCenter.subtract(b.getVertex(i)));
            if (s > a.getRadius()) return;
            if (s > separation)
            {
                separation = s;
                faceNormal = i;
            }
        }

//        Get the closest face's vertices. v1 and v2 describe the closest face
        Vector2f v1 = b.getVertex(faceNormal);
        int i2 = faceNormal + 1 < b.size() ? faceNormal + 1 : 0;
        Vector2f v2 = b.getVertex(i2);

//        Determine which voronoi region is relevant
        float dot1 = circleCenter.subtract(v1).dotProduct(v2.subtract(v1));
        float dot2 = circleCenter.subtract(v2).dotProduct(v1.subtract(v2));
        penetration = a.getRadius() - separation;

        // Hits vertex
        if(dot1 <= 0.0f || dot2 <= 0.0f)
        {
//            Which vertex was hit?
            Vector2f impactVertex = (dot1 <= 0.0f? v1 : v2);
            Vector2f diff = circleCenter.subtract(impactVertex);
            if(tf.apply(diff).subtract(b.getOrigin()).magnitude() > a.getRadius())
                return;

            normal = tf.applyRot(impactVertex.subtract(circleCenter).normalized().invert());
            contactPoints.add(tf.apply(impactVertex));
        }
        // Hits face face
        else
        {
            Vector2f n = b.getNormal(faceNormal);
            Vector2f distanceAlongNormal = n.multiply(circleCenter.subtract(v1).dotProduct(n));
            if( tf.apply(distanceAlongNormal).subtract(b.getOrigin()).magnitude() > a.getRadius())
                return;

            normal = tf.applyRot(n);
            contactPoints.add(normal.invert().multiply(a.getRadius()).add(a.getOrigin()));
        }
        isColliding = true;
    }

//    Check a collision between any two types of collider
    private void checkCollision(Collider colA, Collider colB){
        if (colA instanceof CircleCollider && colB instanceof CircleCollider)
        {
            CircleVsCircle((CircleCollider)colA, (CircleCollider)colB);
        }
        else if (colA instanceof CircleCollider && colB instanceof PolygonCollider)
        {
            CircleVsPoly((CircleCollider) colA, (PolygonCollider) colB);
        }
        else if (colA instanceof PolygonCollider && colB instanceof PolygonCollider)
        {
            PolyVsPoly((PolygonCollider) colA, (PolygonCollider) colB);
        }
    }

    private void applyImpulse(Vector2f contact) {
//        Calculate radius from COM to contact points
        Vector2f ra = contact.subtract(objA.transform.translation);
        Vector2f rb = contact.subtract(objB.transform.translation);

//        Relative velocity
        Vector2f rv = objB.velocity.add(Vector2f.crossProduct(objB.angularVelocity, rb)).subtract(
                      objA.velocity.add(Vector2f.crossProduct(objA.angularVelocity, ra)));
//        Relative velocity along normal
        float contactVel = rv.dotProduct(normal.invert());

//        Do not resolve if they are already separating
        if (contactVel > 0) return;

//        Calculate inverse mass and inertia at the point
        float raCrossN = ra.crossProduct(normal.invert());
        float rbCrossN = rb.crossProduct(normal.invert());
        float invMassSum = objA.inverseMass() + objB.inverseMass()
                + raCrossN*raCrossN * objA.inverseInertia()
                + rbCrossN*rbCrossN * objB.inverseInertia();

//        Impulse scalar
        float e = Math.min(objA.restitution, objB.restitution);
        float j = -(1.0f + e) * contactVel;
        j /= invMassSum;
        j /= contactPoints.size();

//        Apply impulse to objects
        Vector2f impulse = normal.invert().multiply(j);
        objA.applyImpulse(impulse.invert(), ra);
        objB.applyImpulse(impulse, rb);
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
        for (Vector2f contact: contactPoints) {
            applyImpulse(contact);
        }
        positionalCorrection();
        objA.onCollision(this);
        objB.onCollision(this);
    }

//    updateCollision is used to recalculate the collision
    public void updateCollision(){
        isColliding = false;
        penetration = 0;
        contactPoints.clear();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Collision collision = (Collision) o;
        return collision.contains(objA) && collision.contains(objB);
    }

    @Override
    public int hashCode() {
//        The hashcode only cares about the physics objects in the collision, and does not care about
//        order. Therefore an XOR is an appropriate way to combine them
        return objA.hashCode() ^ objB.hashCode();
    }

    public Collision(PhysicsObject objA, PhysicsObject objB){
        if (objA.equals(objB)) throw new IllegalArgumentException("Expected objA and objB to be different");
        this.objA = objA;
        this.objB = objB;
    }
}
