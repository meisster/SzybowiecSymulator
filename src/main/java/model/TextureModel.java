package model;

import lombok.Getter;

@Getter
public class TextureModel {
    private RawModel rawModel;
    private ModelTexture texture;

    public TextureModel(RawModel rawModel, ModelTexture texture) {
        this.rawModel = rawModel;
        this.texture = texture;
    }
}
