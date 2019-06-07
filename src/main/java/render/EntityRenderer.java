package render;

import entities.Entity;
import matrices.MatrixMath;
import matrices.Rotation;
import model.ModelTexture;
import model.RawModel;
import model.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.EntityShader;
import terrain.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EntityRenderer {

    private EntityShader shader;

    public EntityRenderer(EntityShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }
    private Matrix4f createTransformationMatrix(Entity entity) {
        return MatrixMath.createTransformationMatrix(entity.getPosition(), // calculate entity matrix
                                                     entity.getRotation(),
                                                     entity.getScale());
    }

    private void unbindModel() {
        RenderEngine.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    public void render(Map<TexturedModel,List<Entity>> entities){
        for(TexturedModel model:entities.keySet()){
            prepareTextures(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES,
                                    model.getRawModel().getVertexCount(),
                                    GL11.GL_UNSIGNED_INT, 0);
            }
            unbindModel();
        }
    }

    private void prepareTextures(TexturedModel model){
        bindModel(model.getRawModel());
        if(model.getTexture().isTransparent()){
            RenderEngine.disableCulling();
        }
        shader.loadFakeLighting(model.getTexture().isUseFakeLighting());
        shader.loadShineVariable(model.getTexture().getShineDamper(),
                                 model.getTexture().getReflectivity());
        shader.loadUseSpecularMap(model.getTexture().hasSpecularMap());
        bindTextures(model);
        if(model.getTexture().hasSpecularMap()){
            GL13.glActiveTexture(GL13.GL_TEXTURE1);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getSpecularMapID());
        }
    }

    private void bindTextures(TexturedModel model){
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
    }

    private void prepareInstance(Entity entity){
        Matrix4f transformationMatrix = createTransformationMatrix(entity);
        shader.loadTransformationMatrix(transformationMatrix);
    }

    private void bindModel(RawModel model) {
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
    }

    public static List<Entity> createRandomObjects(TexturedModel texture, List<Terrain> terrains, int howMany){
        List<Entity> objects = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < howMany; i++){
            Terrain terrain = null;
            float x = random.nextFloat() * 1000 - 400;
            float z = random.nextFloat() * 1000 - 400;
            if(x < 0 && z < 0) terrain = terrains.get(2);
            else if(x < 0 && z > 0) terrain = terrains.get(1);
            else if(x > 0 && z < 0) terrain = terrains.get(3);
            else if(x > 0 && z > 0) terrain = terrains.get(0);
            float y = terrain.getHeightOfPoint(x, z);
            if(y == 0)
                objects.add(new Entity(texture, new Vector3f(x, y, z), new Rotation(0,0,0), 1));
        }
        return objects;
    }

/*    public void render(Entity entity, EntityShader shader) {
        RawModel model = entity.getTexturedModel().getRawModel();
        bindModel(model);
        Matrix4f transformationMatrix = createTransformationMatrix(entity);
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadProjectionMatrix(projectionMatrix);
        shader.loadShineVariable(entity.getTexturedModel().getTexture().getShineDamper(),
                                 entity.getTexturedModel().getTexture().getShineDamper());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getTexturedModel().getTexture().getID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
    }*/
}
