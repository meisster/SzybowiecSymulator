package shaders;

import entities.Camera;
import entities.Light;
import matrices.MatrixMath;
import org.lwjgl.util.vector.Matrix4f;

public class TerrainShader extends Shader{
    private static final String VERTEX_FILE = "./src/main/resources/shaders/terrainVertexShader.glsl";
    private static final String FRAGMENT_FILE = "./src/main/resources/shaders/terrainFragmentShader.glsl";

    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int lightPositionLocation;
    private int lightColourLocation;
    private int shineDamperLocation;
    private int reflectivityLocation;

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformation_matrix");
        projectionMatrixLocation = super.getUniformLocation("projection_matrix");
        viewMatrixLocation = super.getUniformLocation("view_matrix");
        lightPositionLocation = super.getUniformLocation("light_pos");
        lightColourLocation = super.getUniformLocation("light_col");
        shineDamperLocation = super.getUniformLocation("shine_damper");
        reflectivityLocation = super.getUniformLocation("reflectivity");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "texture_coords");
        super.bindAttribute(2, "normal");
    }

    public void loadTransformationMatrix(Matrix4f transformationMatrix){
        super.loadMatrix(transformationMatrixLocation, transformationMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix){
        super.loadMatrix(projectionMatrixLocation, projectionMatrix);
    }
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = MatrixMath.createViewMatrix(camera);
        super.loadMatrix(viewMatrixLocation, viewMatrix);
    }
    public void loadLight(Light light){
        super.loadVector(lightPositionLocation, light.getPosition());
        super.loadVector(lightColourLocation, light.getColour());
    }

    public void loadShineVariable(float damp, float reflect){
        super.loadFloat(shineDamperLocation, damp);
        super.loadFloat(reflectivityLocation, reflect);
    }
}
