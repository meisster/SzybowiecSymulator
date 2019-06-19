package test;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Plane;
import model.Loader;
import model.ModelTexture;
import model.TexturedModel;
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
        List<Light> lights = new ArrayList<>();
        //Light up the screen with sun entity
        lights.add(Light.builder()
                        .position(new Vector3f(3000, 2000, 2000))
                        .colour(new Vector3f(1, 1, 1))
                        .build());
        //Test point-light entity(blue colour)
        lights.add(Light.builder().position(new Vector3f(0, 1, 0))
                        .colour(new Vector3f(2 * 0.258f, 2 * 0.647f, 2 * 0.9607f))
                        .attenuation(new Vector3f(1, 0.01f, 0.002f))
                        .build());

        //Create our default plane and initialize with basic settings
        Plane plane = Plane.createDefaultPlane();
        plane.getTexturedModel().getTexture().setSpecularMap("piper_specular").setShineDamper(20).setReflectivity(1f);

        //Set our camera to follow the plane entity(can put any entity)
        Camera camera = new Camera(plane);

        //Create 4 grids of terrain
        ModelTexture terrainTexture = new ModelTexture("terrain");
        terrainTexture.setReflectivity(0.05f);
        terrainTexture.setShineDamper(5f);
        List<Terrain> terrains = TerrainRenderer.createTerrain(terrainTexture, "terrain_height2");

        //Populate the terrain with fern/grass
        TexturedModel fern = new TexturedModel("fern", "grassTexture");
        fern.getTexture().setReflectivity(0);
        List<Entity> grasses = EntityRenderer.createRandomObjects(fern, terrains, 5000);

        //Populate with trees
        TexturedModel tree3TexturedModel = new TexturedModel("lowPolyTree", "lowPolyTree");
        tree3TexturedModel.getTexture().setReflectivity(0);
        List<Entity> trees3 = EntityRenderer.createRandomObjects(tree3TexturedModel, terrains, 50);

        RenderEngine renderEngine = new RenderEngine();
        while (!Display.isCloseRequested()) {
            //Update position of a plane and camera based on inputs
            plane.move(terrains.get(plane.getCurrentQuarter()));
            camera.move();
            //Process our entities and terrain(map model-texture)
            grasses.forEach(renderEngine::processEntity);
            trees3.forEach(renderEngine::processEntity);
            terrains.forEach(renderEngine::processTerrain);
            renderEngine.processEntity(plane);
            //Render our screen
            renderEngine.render(lights, camera);
            // game logic
            // render geometry
            DisplayManager.updateDisplay();
        }
        renderEngine.cleanUp();
        Loader.getInstance().cleanUp();
        DisplayManager.closeDisplay();
    }

}
