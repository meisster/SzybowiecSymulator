package model;

import lombok.Getter;

@Getter
public class TexturedModel {
    private RawModel rawModel;
    private ModelTexture texture;

    public TexturedModel(String modelName, ModelTexture texture) {
        this.rawModel = OBJLoader.loadObj(modelName);
        this.texture = texture;
    }

    public TexturedModel(String modelName, String textureName) {
        this.rawModel = OBJLoader.loadObj(modelName);
        this.texture = new ModelTexture(textureName);
    }

    public ModelTexture getTexture() {
        return texture;
    }
}
