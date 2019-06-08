package shaders;

public class EntityShader extends Shader {

    private static final String VERTEX_FILE = "./src/main/resources/shaders/vertexShader.glsl";
    private static final String FRAGMENT_FILE = "./src/main/resources/shaders/fragmentShader.glsl";


    private int useFakeLightingLocation;
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
        shineDamperLocation = super.getUniformLocation("shine_damper");
        reflectivityLocation = super.getUniformLocation("reflectivity");
        useFakeLightingLocation = super.getUniformLocation("use_fake_lighting");
        skyColourLocation = super.getUniformLocation("sky_colour");
        getLightsLocation();
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "texture_coords");
        super.bindAttribute(2, "normal");
    }

    public void loadUseSpecularMap(boolean useSpecularMap) {
        super.loadBoolean(usesSpecularMapLocation, useSpecularMap);
    }

/*    public void loadTransformationMatrix(Matrix4f transformationMatrix) {
        super.loadMatrix(transformationMatrixLocation, transformationMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix(projectionMatrixLocation, projectionMatrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = MatrixMath.createViewMatrix(camera);
        super.loadMatrix(viewMatrixLocation, viewMatrix);
    }*/

    public void loadShineVariable(float damp, float reflect) {
        super.loadFloat(shineDamperLocation, damp);
        super.loadFloat(reflectivityLocation, reflect);
    }

    public void loadFakeLighting(boolean useFakeLighting) {
        super.loadBoolean(useFakeLightingLocation, useFakeLighting);
    }
}
