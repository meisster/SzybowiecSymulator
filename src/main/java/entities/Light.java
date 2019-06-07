package entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.util.vector.Vector3f;

@AllArgsConstructor
@Getter
@Setter
public class Light {
    private Vector3f position;
    private Vector3f colour;
}
