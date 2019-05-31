package test;

import model.Loader;
import model.RawModel;
import org.lwjgl.opengl.Display;
import render.DisplayManager;
import render.RenderEngine;
import shaders.StaticShader;

public class Test {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Loader loader = new Loader();
        RenderEngine renderEngine = new RenderEngine();
        StaticShader shader = new StaticShader();


        float[] vertices = {
                -0.5f, 0.5f, 0f, //V0
                -0.5f, -0.5f, 0f, //V1
                0.5f, -0.5f, 0f, //V2
                0.5f, 0.5f, 0f, //V3
        };
        int[] indices = {
                0,1,3,
                3,1,2
        };

        RawModel model = loader.loadToVAO(vertices, indices);
        while (!Display.isCloseRequested()) {
            renderEngine.prepare();
            shader.start();
            renderEngine.render(model);
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
