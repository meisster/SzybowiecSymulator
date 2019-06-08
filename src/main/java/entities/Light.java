package entities;

import lombok.*;
import org.lwjgl.util.vector.Vector3f;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class Light {
    private Vector3f position;
    private Vector3f colour;
    @Builder.Default
    private Vector3f attenuation = new Vector3f(1, 0, 0);

}

