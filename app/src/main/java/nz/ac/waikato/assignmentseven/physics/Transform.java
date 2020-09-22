package nz.ac.waikato.assignmentseven.physics;

public class Transform {
    public Vector2f translation = new Vector2f();
    public Vector2f scale = new Vector2f(1,1);
    public float rotation = 0f;

    public Transform(float xPos, float yPos, float scale){
        this.translation.x = xPos;
        this.translation.y = yPos;
        this.scale.multiply(scale);
    }

    public Transform(Vector2f translation, float scale) {
        this.translation = translation;
        this.scale = this.scale.multiply(scale);
    }

    public Transform(){

    }
}
