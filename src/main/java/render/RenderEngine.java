package render;

import entities.Camera;
import entities.Entity;
import entities.Light;
import matrices.MatrixMath;
import model.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shaders.EntityShader;
import shaders.TerrainShader;
import terrain.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderEngine {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 4000;

    private Matrix4f projectionMatrix;

    private EntityRenderer entityRenderer;
    private EntityShader shader = new EntityShader();

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    public RenderEngine() {
        enableCulling();
        this.projectionMatrix = MatrixMath.createProjectionMatrix(FOV, FAR_PLANE, NEAR_PLANE);
        entityRenderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    public void render(Light light, Camera camera){
        prepare(); // clear color and depth buffer
        shader.start();
        shader.loadLight(light); // load light to uniform variables
        shader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        shader.stop();
        terrainShader.start();
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        terrains.clear();
        entities.clear();
    }
    public void processTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void processEntity(Entity entity){
        TexturedModel model = entity.getTexturedModel();
        List<Entity> batch = entities.get(model);
        if(batch!=null){
            batch.add(entity);
        }else{
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(model, newBatch);
        }
    }

    public static void enableCulling(){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }
    public static void disableCulling(){
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void cleanUp(){
        shader.cleanUp();
        terrainShader.cleanUp();
    }
    private void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.258f, 0.647f, 0.9607f, 1);
    }
}
