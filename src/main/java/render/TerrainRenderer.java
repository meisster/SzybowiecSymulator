package render;

import entities.Entity;
import matrices.MatrixMath;
import matrices.Rotation;
import model.Loader;
import model.ModelTexture;
import model.RawModel;
import model.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.TerrainShader;
import terrain.Terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TerrainRenderer {

    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(List<Terrain> terrains) {
        for (Terrain terrain : terrains) {
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES,
                                terrain.getModel().getVertexCount(),
                                GL11.GL_UNSIGNED_INT, 0);
            unbindModel();
        }
    }

    private void unbindModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareTerrain(Terrain terrain) {
        bindModel(terrain.getModel());
        shader.loadShineVariable(terrain.getTexture().getShineDamper(),
                                 terrain.getTexture().getShineDamper());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getTexture().getTextureID());
    }

    private void loadModelMatrix(Terrain terrain) {
        Matrix4f transformationMatrix = createTransformationMatrix(terrain);
        shader.loadTransformationMatrix(transformationMatrix);
    }

    public static List<Terrain> createTerrain(ModelTexture texture, Loader loader, String heightMap){
        List<Terrain> objects = new ArrayList<>();
        objects.add(new Terrain(0, 0, loader, texture, heightMap));
        objects.add(new Terrain(-1, 0, loader, texture, heightMap));
        objects.add(new Terrain(-1, -1, loader, texture, heightMap));
        objects.add(new Terrain(0, -1, loader, texture, heightMap));

        return objects;
    }

    private void bindModel(RawModel model) {
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
    }

    private Matrix4f createTransformationMatrix(Terrain terrain) {
        return MatrixMath.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()),
                                                     new Rotation(0, 0, 0),
                                                     1);
    }
}
