package nz.ac.waikato.assignmentseven.physics;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

import nz.ac.waikato.assignmentseven.PhysicsObject;

public class PolygonCollider extends Collider {
    private List<Vector2f> vertices;
    private List<Vector2f> normals;

    public PolygonCollider(PhysicsObject body, List<Vector2f> vertices, List<Vector2f> normals) {
        super(body);
        this.vertices = vertices;
        this.normals = normals;
    }

    public static Face findIncidentFace(PolygonCollider refPoly, PolygonCollider incPoly, int referenceIndex) {
        Vector2f refNormal = refPoly.getWorldNormal(referenceIndex);

        int incidentFace = 0;
        float minDot = Float.MAX_VALUE;
        for (int i = 0; i < incPoly.size(); ++i) {
            float dot = refNormal.dotProduct(incPoly.getWorldNormal(i));
            if (dot < minDot) {
                minDot = dot;
                incidentFace = i;
            }
        }

        Vector2f v1 = new Vector2f(incPoly.getWorldVertex(incidentFace));
        incidentFace = incidentFace + 1 >= (int) incPoly.size() ? 0 : incidentFace + 1;
        Vector2f v2 = new Vector2f(incPoly.getWorldVertex(incidentFace));
        return new Face(v1, v2);

    }

    /**
     * rectangleVertices returns the vertices of a square
     */
    @NotNull
    public static List<Vector2f> rectangleVertices() {
        List<Vector2f> vertices = new LinkedList<>();
        vertices.add(new Vector2f(-1, 1));
        vertices.add(new Vector2f(-1, -1));
        vertices.add(new Vector2f(1, -1));
        vertices.add(new Vector2f(1, 1));
        return vertices;
    }

    /**
     * rectangleNormals returns the normals of a square
     */
    public static List<Vector2f> rectangleNormals() {
        List<Vector2f> normals = new LinkedList<>();
        normals.add(new Vector2f(-1, 0));
        normals.add(new Vector2f(0, -1));
        normals.add(new Vector2f(1, 0));
        normals.add(new Vector2f(0, 1));
        return normals;
    }

    //    The extreme point along a direction within a polygon
    public Vector2f getSupport(Vector2f dir) {
        float bestProjection = Float.NEGATIVE_INFINITY;
        Vector2f bestVertex = null;

        for (Vector2f vertex : getVertices()) {
            float projection = vertex.dotProduct(dir);

            if (projection > bestProjection) {
                bestProjection = projection;
                bestVertex = vertex;
            }
        }
        return bestVertex;
    }

    //    get transformed points by index
    public Vector2f getWorldVertex(int i) {
        return body.transform.apply(vertices.get(i));
    }

    public Vector2f getWorldNormal(int i) {
        return body.transform.applyRot(normals.get(i));
    }

    //    get points in object space
    public Vector2f getVertex(int i) {
        return vertices.get(i);
    }

    public Vector2f getNormal(int i) {
        return normals.get(i);
    }

    //    get list of untransformed points
    public List<Vector2f> getVertices() {
        return vertices;
    }

    public List<Vector2f> getNormals() {
        return normals;
    }

    public int size() {
        return vertices.size();
    }

    @Override
    public String toString() {
        String str = "PolygonCollider{points=[";
        for (Vector2f vert : vertices) {
            str += "(" + vert.x + "," + vert.y + ")";
        }
        str += ", transform=" + body.transform.toString();
        return str;
    }

    public static class AxisOfLeastPenetration {
        public final float distance;
        public final Vector2f vertex;
        public final int faceIndex;

        public AxisOfLeastPenetration(@NotNull PolygonCollider a, @NotNull PolygonCollider b) {
            float bestDistance = Float.NEGATIVE_INFINITY;
            int bestIndex = 0;
            Transform tranB = b.body.transform;
            Transform invTranB = b.body.transform.inverse();

            for (int i = 0; i < a.size(); i++) {
//            ALL CALCULATIONS ARE DONE IN B's SPACE WITH WORLD SCALE APPLIED
//            Get normal in B's space
                Vector2f n = a.getWorldNormal(i);
                n = invTranB.applyRot(n);

//            Retrieve support point from B along -n. In B's model space
                Vector2f s = tranB.applyScale(b.getSupport(n.invert()));

                Vector2f v = invTranB.applyTranRot(a.getWorldVertex(i));
                float d = n.dotProduct(s.subtract(v));

                if (d > bestDistance) {
                    bestDistance = d;
                    bestIndex = i;
                }
            }
            this.vertex = a.getWorldVertex(bestIndex);
            this.faceIndex = bestIndex;
            this.distance = bestDistance;
        }
    }
}
