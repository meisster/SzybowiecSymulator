package model;

import lombok.SneakyThrows;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private static final Loader INSTANCE = new Loader();
    private List<Integer> vaoList = new ArrayList<>();
    private List<Integer> vboList = new ArrayList<>();
    private List<Integer> textureList = new ArrayList<>();

    public static Loader getInstance() {
        return INSTANCE;
    }


    public RawModel loadToVAO(FloatBuffer vertices, FloatBuffer texCoords, FloatBuffer normals, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributesList(0, 3, vertices);
        storeDataInAttributesList(1, 2, texCoords);
        storeDataInAttributesList(2, 3, normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    public void cleanUp() {
        vaoList.forEach(GL30::glDeleteVertexArrays);
        vboList.forEach(GL15::glDeleteBuffers);
        textureList.forEach(GL11::glDeleteTextures);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaoList.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributesList(int attributeNumber, int coordinateSize, FloatBuffer data) {
        int vboID = GL15.glGenBuffers();
        vboList.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW); // we dont want to edit the data
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // unbind the current VBO
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    @SneakyThrows(IOException.class)
    public int loadTexture(String fileName) {
        Texture texture;
        texture = TextureLoader.getTexture("PNG", new FileInputStream("./src/main/resources/textures/"
            + fileName
            + ".png"));
        return loadTexture(texture);

    }

    private int loadTexture(Texture texture) {
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
        if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
            float amount = Math.min(8f,
                GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
        }
        int textureID = texture.getTextureID();
        textureList.add(textureID);
        return textureID;
    }

    @SneakyThrows(IOException.class)
    public int loadTextureFromJPG(String fileName) {
        Texture texture;
        texture = TextureLoader.getTexture("JPG",
            new FileInputStream("./src/main/resources/textures/"
                + fileName
                + ".jpg"));
        return loadTexture(texture);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vboList.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
