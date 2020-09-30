package nz.ac.waikato.assignmentseven.physics;

import org.jetbrains.annotations.NotNull;

public class Transform {
    public Vector2f translation = new Vector2f();
    public Vector2f scale = new Vector2f(1,1);
    public float rotation = 0f;

    @Override
    @NotNull
    public String toString() {
        return "Transform{" +
                "translation=" + translation +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }

    public Transform(float xPos, float yPos, float scale){
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

//    Transform copy constructor
    public Transform(@org.jetbrains.annotations.NotNull Transform transform) {
        translation = new Vector2f(transform.translation);
        scale = new Vector2f(transform.scale);
        rotation = transform.rotation;
    }

    public Transform(){
    }
}
