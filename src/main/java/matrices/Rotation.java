package matrices;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rotation {
    private float xRotation;
    private float yRotation;
    private float zRotation;

    public Rotation(float rotateX, float rotateY, float rotateZ) {
        this.xRotation = rotateX;
        this.yRotation = rotateY;
        this.zRotation = rotateZ;
    }

    public void addRotationX(float dx){
        this.xRotation += dx;
    }
    public void addRotationY(float dy){
        this.yRotation += dy;
    }
    public void addRotationZ(float dz){
        this.zRotation += dz;
    }
}
