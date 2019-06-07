package model;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import entities.Entity;
import model.Loader;
import model.RawModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

public class OBJLoader {

    public static RawModel loadObj(String fileName, Loader loader) {
        try {
            InputStream inputStream = new FileInputStream("./src/main/resources/objects/" + fileName + ".obj");
            Obj obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream));

            int[] indices = ObjData.getFaceVertexIndicesArray(obj);
            FloatBuffer vertices = ObjData.getVertices(obj);
            FloatBuffer texCoords = ObjData.getTexCoords(obj, 2, true);
            FloatBuffer normals = ObjData.getNormals(obj);
            return loader.loadToVAO(vertices, texCoords, normals, indices);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Error, couldn't load object: " + fileName + "properly!");
        return null;
    }

}