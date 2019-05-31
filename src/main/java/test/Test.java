package test;

import model.Loader;
import model.ModelTexture;
import model.RawModel;
import model.TextureModel;
import org.lwjgl.opengl.Display;
import render.DisplayManager;
import render.RenderEngine;
import shaders.ShaderLoaderImpl;

public class Test {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Loader loader = new Loader();
        RenderEngine renderEngine = new RenderEngine();
        ShaderLoaderImpl shader = new ShaderLoaderImpl();


        float[] vertices = {
                -0.5f, 0.5f, 0f, //V0
                -0.5f, -0.5f, 0f, //V1
                0.5f, -0.5f, 0f, //V2
                0.5f, 0.5f, 0f, //V3
        };
        int[] indices = {
                0, 1, 3,
                3, 1, 2
        };
        float[] textureCoords = {
                0,0,
                0,1,
                1,1,
                1,0
        };

        RawModel model = loader.loadToVAO(vertices, indices, textureCoords);
        ModelTexture modelTexture = new ModelTexture(loader.loadTexture("square-texture"));
        TextureModel textureModel = new TextureModel(model, modelTexture);

        while (!Display.isCloseRequested()) {
            renderEngine.prepare();
            shader.start();
            renderEngine.render(textureModel);
            shader.stop();
            // game logic
            // render geometry
            DisplayManager.updateDisplay();
        }
        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}
