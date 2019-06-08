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

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        DisplayManager.createDisplay();
        List<Light> lights= new ArrayList<>();
        lights.add(Light.builder()
                        .position(new Vector3f(3000,2000,2000))
                        .colour(new Vector3f(1,1,1))
                        .build());
        lights.add(Light.builder().position(new Vector3f(0, 1, 0))
                        .colour(new Vector3f(2*0.258f, 2*0.647f, 2*0.9607f))
                        .attenuation(new Vector3f(1, 0.01f, 0.002f))
                        .build());

        Loader loader = new Loader();

        RawModel planeModel = OBJLoader.loadObj("piper", loader);
        TexturedModel texturedPlaneModel = new TexturedModel(planeModel,
                                                        new ModelTexture(loader.loadTexture("piper_diffuse")));
        texturedPlaneModel.getTexture().setSpecularMap(loader.loadTexture("piper_specular"));
        texturedPlaneModel.getTexture().setShineDamper(20);
        texturedPlaneModel.getTexture().setReflectivity(0.7f);
        Plane plane = new Plane(texturedPlaneModel,
                                 new Vector3f(0, 0, 10),
                                 new Rotation(0, 0, 0), 1);

        Camera camera = new Camera(plane);

        ModelTexture terrainTextureModel = new ModelTexture(loader.loadTexture("terrain"));
        terrainTextureModel.setReflectivity(0.05f);
        terrainTextureModel.setShineDamper(5f);
        List<Terrain> terrains = TerrainRenderer.createTerrain(
                terrainTextureModel,
                loader,
                "terrain_height2");

        RawModel grass = OBJLoader.loadObj("fern", loader);
        TexturedModel grassTextured = new TexturedModel(grass,
                                                        new ModelTexture(loader.loadTexture("grassTexture")));
        grassTextured.getTexture().setReflectivity(0);
        List<Entity> grasses = EntityRenderer.createRandomObjects(grassTextured, terrains, 2500);

        RawModel tree3 = OBJLoader.loadObj("lowPolyTree", loader);
        TexturedModel tree3TexturedModel = new TexturedModel(tree3,
                                                             new ModelTexture(loader.loadTexture("lowPolyTree")));
        tree3TexturedModel.getTexture().setReflectivity(0);
        List<Entity> trees3 = EntityRenderer.createRandomObjects(tree3TexturedModel, terrains, 50);


        RenderEngine renderEngine = new RenderEngine();
        while (!Display.isCloseRequested()) {
            plane.move();
            camera.move();
            grasses.forEach(renderEngine::processEntity);
            //trees3.forEach(renderEngine::processEntity);
            terrains.forEach(renderEngine::processTerrain);
            renderEngine.processEntity(plane);

            renderEngine.render(lights, camera);
            // game logic
            // render geometry
            DisplayManager.updateDisplay();
        }
        renderEngine.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

}
