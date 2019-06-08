package model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class ModelTexture {

    private int textureID;
    private int specularMapID;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean hasSpecularMap;
    private float shineDamper = 1;
    private float reflectivity = 0;

    private boolean transparent = false;
    private boolean useFakeLighting = false;

    public ModelTexture(String fileName) {
        this.textureID = Loader.getInstance().loadTexture(fileName);
    }

    public ModelTexture(int textureID) {
        this.textureID = textureID;
    }

    public ModelTexture setSpecularMap(String specularMap) {
        this.specularMapID = Loader.getInstance().loadTextureFromJPG(specularMap);
        this.hasSpecularMap = true;
        return this;
    }

    public boolean hasSpecularMap() {
        return hasSpecularMap;
    }

    public boolean useFakeLighting() {
        return useFakeLighting;
    }

}
