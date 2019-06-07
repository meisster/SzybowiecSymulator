package test;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Plane;
import matrices.Rotation;
import model.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import render.DisplayManager;
import render.EntityRenderer;
import render.RenderEngine;
import render.TerrainRenderer;
import terrain.Terrain;

import java.util.List;

public class Test {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
        Loader loader = new Loader();

        RawModel planeModel = OBJLoader.loadObj("piper", loader);
        TexturedModel texturedPlaneModel = new TexturedModel(planeModel,
                                                        new ModelTexture(loader.loadTexture("piper_diffuse")));
        texturedPlaneModel.getTexture().setSpecularMap(loader.loadTexture("piper_specular"));
        texturedPlaneModel.getTexture().setShineDamper(5);
        texturedPlaneModel.getTexture().setReflectivity(0.4f);
        RawModel grass = OBJLoader.loadObj("grassModel", loader);
        TexturedModel texturedGrass = new TexturedModel(grass,
                                                        new ModelTexture(loader.loadTexture("grassTexture")));
        texturedGrass.getTexture().setUseFakeLighting(true);
        texturedGrass.getTexture().setTransparent(true);
        texturedGrass.getTexture().setReflectivity(0);

        Plane plane = new Plane(texturedPlaneModel,
                                 new Vector3f(0, 0, 10),
                                 new Rotation(0, 0, 0), 1);
        Camera camera = new Camera(plane);
        ModelTexture terrainTextureModel = new ModelTexture(loader.loadTexture("terrain"));
        terrainTextureModel.setReflectivity(0.1f);
        terrainTextureModel.setShineDamper(0.2f);
        List<Terrain> terrains = TerrainRenderer.createTerrain(
                terrainTextureModel,
                loader,
                "terrain_height2");
        List<Entity> plants = EntityRenderer.createRandomObjects(texturedGrass, terrains, 5000);

        RenderEngine renderEngine = new RenderEngine();
        while (!Display.isCloseRequested()) {
            camera.move();
            plane.move();
            plants.forEach(renderEngine::processEntity);
            terrains.forEach(renderEngine::processTerrain);
            renderEngine.processEntity(plane);

            renderEngine.render(light, camera);
            // game logic
            // render geometry
            DisplayManager.updateDisplay();
        }
        renderEngine.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}
