package shaders;

import entities.Camera;
import entities.Light;
import matrices.MatrixMath;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.stream.IntStream;

public abstract class Shader {

    static final int MAX_LIGHTS = 4;
    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
    int transformationMatrixLocation;
    int projectionMatrixLocation;
    int viewMatrixLocation;
    int[] lightPositionLocation;
    int[] lightColourLocation;
    int shineDamperLocation;
    int reflectivityLocation;
    int skyColourLocation;
    int[] attenuationLocation;
    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;


    Shader(String vertexFile, String fragmentFile) {
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShader(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.out.println("Could not compile shader.");
        }
        return shaderID;
    }

    void getLightsLocation() {
        lightPositionLocation = new int[MAX_LIGHTS];
        lightColourLocation = new int[MAX_LIGHTS];
        attenuationLocation = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            lightPositionLocation[i] = getUniformLocation("light_pos[" + i + "]");
            lightColourLocation[i] = getUniformLocation("light_col[" + i + "]");
            attenuationLocation[i] = getUniformLocation("attenuation[" + i + "]");
        }
    }

    public void loadLights(List<Light> lights) {
        IntStream.range(0, MAX_LIGHTS).forEach(i -> {
            if (i < lights.size()) {
                loadVector(lightPositionLocation[i], lights.get(i).getPosition());
                loadVector(lightColourLocation[i], lights.get(i).getColour());
                loadVector(attenuationLocation[i], lights.get(i).getAttenuation());
            } else { // load empty light if not defined
                loadVector(lightPositionLocation[i], new Vector3f(0, 0, 0));
                loadVector(lightColourLocation[i], new Vector3f(0, 0, 0));
                loadVector(attenuationLocation[i], new Vector3f(1, 0, 0));
            }
        });
    }

    public void loadTransformationMatrix(Matrix4f transformationMatrix) {
        loadMatrix(transformationMatrixLocation, transformationMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        loadMatrix(projectionMatrixLocation, projectionMatrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = MatrixMath.createViewMatrix(camera);
        loadMatrix(viewMatrixLocation, viewMatrix);
    }

    public void loadSkyColour(float r, float g, float b) {
        loadVector(skyColourLocation, new Vector3f(r, g, b));
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    abstract void getAllUniformLocations();

    int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    void loadBoolean(int location, boolean value) {
        GL20.glUniform1f(location, value ? 1 : 0);
    }

    void loadMatrix(int location, Matrix4f matrix) {
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrixBuffer);
    }

    void loadVector(int location, Vector3f vector) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

}
