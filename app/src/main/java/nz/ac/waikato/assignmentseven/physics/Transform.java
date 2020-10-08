package nz.ac.waikato.assignmentseven.physics;

import org.jetbrains.annotations.NotNull;

public class Transform {
    public Vector2f translation = new Vector2f();
    public Vector2f scale = new Vector2f(1.41421356237, 1.41421356237);
    private float degrees = 0f;
    private float radians = 0f;

    public Transform(float xPos, float yPos) {
        this.translation.x = xPos;
        this.translation.y = yPos;
    }

    public Transform(float xPos, float yPos, float scale) {
        this.translation.x = xPos;
        this.translation.y = yPos;
        this.scale = this.scale.multiply(scale);
    }

    public Transform(Vector2f translation, float scale) {
        this.translation = translation;
        this.scale = this.scale.multiply(scale);
    }

    public Transform(Vector2f translation, Vector2f scale) {
        this.translation = translation;
        this.scale = scale;
    }

    public Transform(Vector2f translation, Vector2f scale, float degrees) {
        this.translation = translation;
        this.scale = scale;
        setRotationInDegrees(degrees);
    }

    //    Transform copy constructor
    public Transform(@NotNull Transform transform) {
        translation = new Vector2f(transform.translation);
        scale = new Vector2f(transform.scale);
        setRotationInDegrees(0);
    }

    public Transform() {
    }

    public float getRotationInDegrees() {
        return degrees;
    }

    public void setRotationInDegrees(float degrees) {
        this.degrees = degrees;
        this.radians = (float) Math.toRadians(degrees);
    }

    public float getRotationInRadians() {
        return radians;
    }

    public void setRotationInRadians(float radians) {
        this.radians = radians;
        this.degrees = (float) Math.toDegrees(radians);
    }

    // Rotate the point about 0,0
    public Vector2f applyRot(@NotNull Vector2f pt) {
        float B = getRotationInRadians();
        return new Vector2f(Math.cos(B) * pt.x - Math.sin(B) * pt.y, Math.sin(B) * pt.x + Math.cos(B) * pt.y);
    }

    // Translate the point
    public Vector2f applyTran(@NotNull Vector2f pt) {
        return pt.add(translation);
    }

    // Scale the point
    public Vector2f applyScale(@NotNull Vector2f pt) {
        return new Vector2f(scale.x * pt.x, scale.y * pt.y);
    }

    // Apply multiple transforms in specific orders
    public Vector2f applyTranRot(@NotNull Vector2f pt) {
        return applyRot(applyTran(pt));
    }

    // Apply all transforms in the correct order scale -> rotation -> transform
    public Vector2f apply(@NotNull Vector2f pt) {
        return applyTran(applyRot(applyScale(pt)));
    }

    // Apply all transforms in the reverse order transform --> rotation --> scale
    public Vector2f reverseApply(@NotNull Vector2f pt) {
        return applyScale(applyRot(applyTran(pt)));
    }

    public Transform applyTran(Transform transform) {
        Transform out = new Transform(transform);
        out.translation = apply(transform.translation);
        out.scale = applyScale(transform.scale);
        out.setRotationInRadians(transform.getRotationInRadians() + getRotationInRadians());
        return out;
    }

    @Override
    @NotNull
    public String toString() {
        return "Transform{" +
                "translation=" + translation +
                ", scale=" + scale +
                ", rotation=" + getRotationInDegrees() +
                "deg}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transform transform = (Transform) o;

        if (Float.compare(transform.degrees, degrees) != 0) return false;
        if (Float.compare(transform.radians, radians) != 0) return false;
        if (!translation.equals(transform.translation)) return false;
        return scale.equals(transform.scale);
    }

    @Override
    public int hashCode() {
        int result = translation.hashCode();
        result = 31 * result + scale.hashCode();
        result = 31 * result + (degrees != +0.0f ? Float.floatToIntBits(degrees) : 0);
        result = 31 * result + (radians != +0.0f ? Float.floatToIntBits(radians) : 0);
        return result;
    }

    //    Inverse returns a transform object that can undo this objects transformations
    public Transform inverse() {
        Transform inverted = new Transform();
        inverted.translation = translation.invert();
        if (scale.x != 0) inverted.scale.x = 1 / scale.x;
        if (scale.y != 0) inverted.scale.y = 1 / scale.y;
        inverted.setRotationInRadians(-getRotationInRadians());
        return inverted;
    }
}
