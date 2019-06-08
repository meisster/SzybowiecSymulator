package entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import matrices.Rotation;
import model.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

@Getter
@Setter
@Builder
public class Entity {
    private TexturedModel texturedModel;
    private Vector3f position;
    private Rotation rotation;
    private float scale;

    public Entity(TexturedModel texturedModel, Vector3f position, Rotation rotation, float scale) {
        this.texturedModel = texturedModel;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public void changeRotation(float x, float y, float z) {
        rotation.addRotationX(x);
        rotation.addRotationY(y);
        rotation.addRotationZ(z);
    }

    public void changePosition(float x, float y, float z) {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }
}
