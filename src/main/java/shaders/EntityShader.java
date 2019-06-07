package shaders;

import entities.Camera;
import entities.Light;
import matrices.MatrixMath;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class EntityShader extends Shader {

    private static final String VERTEX_FILE = "./src/main/resources/shaders/vertexShader.glsl";
    private static final String FRAGMENT_FILE = "./src/main/resources/shaders/fragmentShader.glsl";

    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int lightPositionLocation;
    private int lightColourLocation;
    private int shineDamperLocation;
    private int reflectivityLocation;
    private int useFakeLightingLocation;
    private int skyColourLocation;
    private int specularMapLocation;
    private int usesSpecularMapLocation;
    private int modelTextureLocation;


    public EntityShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformation_matrix");
        specularMapLocation = super.getUniformLocation("specular_map");
        usesSpecularMapLocation = super.getUniformLocation("use_specular_map");
        modelTextureLocation = super.getUniformLocation("texture_map");
        projectionMatrixLocation = super.getUniformLocation("projection_matrix");
        viewMatrixLocation = super.getUniformLocation("view_matrix");
        lightPositionLocation = super.getUniformLocation("light_pos");
        lightColourLocation = super.getUniformLocation("light_col");
        shineDamperLocation = super.getUniformLocation("shine_damper");
        reflectivityLocation = super.getUniformLocation("reflectivity");
        useFakeLightingLocation = super.getUniformLocation("use_fake_lighting");
        skyColourLocation = super.getUniformLocation("sky_colour");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "texture_coords");
        super.bindAttribute(2, "normal");
    }

    public void loadSkyColour(float r, float g, float b) {
        super.loadVector(skyColourLocation, new Vector3f(r, g, b));
    }

    public void loadUseSpecularMap(boolean useSpecularMap) {
        super.loadBoolean(usesSpecularMapLocation, useSpecularMap);
    }

    public void loadTransformationMatrix(Matrix4f transformationMatrix) {
        super.loadMatrix(transformationMatrixLocation, transformationMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix(projectionMatrixLocation, projectionMatrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = MatrixMath.createViewMatrix(camera);
        super.loadMatrix(viewMatrixLocation, viewMatrix);
    }

    public void loadLight(Light light) {
        super.loadVector(lightPositionLocation, light.getPosition());
        super.loadVector(lightColourLocation, light.getColour());
    }

    public void loadShineVariable(float damp, float reflect) {
        super.loadFloat(shineDamperLocation, damp);
        super.loadFloat(reflectivityLocation, reflect);
    }

    public void loadFakeLighting(boolean useFakeLighting) {
        super.loadBoolean(useFakeLightingLocation, useFakeLighting);
    }
}
