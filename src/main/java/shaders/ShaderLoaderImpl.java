package shaders;

public class ShaderLoaderImpl extends ShaderLoader {

    private static final String VERTEX_FILE = "./src/main/resources/shaders/vertexShader.glsl";
    private static final String FRAGMENT_FILE = "./src/main/resources/shaders/fragmentShader.glsl";

    public ShaderLoaderImpl() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "texture_coords");
    }
}
