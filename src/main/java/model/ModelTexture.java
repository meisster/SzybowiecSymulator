package model;

import lombok.Getter;
import lombok.Setter;


public class ModelTexture {

    private int textureID;
    private int specularMapID;
    private boolean hasSpecularMap;
    private float shineDamper = 1;
    private float reflectivity = 0;

    private boolean transparent = false;
    private boolean useFakeLighting = false;

    public void setSpecularMap(int specularMap){
        this.specularMapID = specularMap;
        this.hasSpecularMap = true;
    }
    public boolean hasSpecularMap(){
        return hasSpecularMap;
    }
    public int getSpecularMapID() {
        return specularMapID;
    }

    public int getTextureID() {
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    public ModelTexture(int texture) {
        this.textureID = texture;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }
}
