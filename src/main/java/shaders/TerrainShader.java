package shaders;

public class TerrainShader extends Shader {
    private static final String VERTEX_FILE = "./src/main/resources/shaders/terrainVertexShader.glsl";
    private static final String FRAGMENT_FILE = "./src/main/resources/shaders/terrainFragmentShader.glsl";


    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformation_matrix");
        projectionMatrixLocation = super.getUniformLocation("projection_matrix");
        viewMatrixLocation = super.getUniformLocation("view_matrix");
        shineDamperLocation = super.getUniformLocation("shine_damper");
        reflectivityLocation = super.getUniformLocation("reflectivity");
        skyColourLocation = super.getUniformLocation("sky_colour");
        getLightsLocation();

    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "texture_coords");
        super.bindAttribute(2, "normal");
    }


    public void loadShineVariable(float damp, float reflect) {
        super.loadFloat(shineDamperLocation, damp);
        super.loadFloat(reflectivityLocation, reflect);
    }
}
