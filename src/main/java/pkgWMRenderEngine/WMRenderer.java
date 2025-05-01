package pkgWMRenderEngine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import pkgWMUtils.WMWindowManager;

import org.joml.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWaitEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public class WMRenderer {
    private WMWindowManager myWM;
    private WMGeometryManager myGM;
    private int shader_program;
    private int NUM_COLS;
    private int NUM_ROWS;
    private int PADDING;
    private int OFFSET;
    private int SIZE;
    private int[] winWidthHeight;
    private final int EPT = 6;  // Element Per Tile
    private final int FPV = 2;  // Float Per Vertex
    private final int VPT = 4;  // Vertices Per Tile
    private static final int OGL_MATRIX_SIZE = 16;
    private Matrix4f viewProjMatrix;
    private FloatBuffer myFloatBuffer;
    private int vpMatLocation;
    private int renderColorLocation;

    public WMRenderer(WMWindowManager myWM) {
        this.myWM = myWM;
        this.winWidthHeight = myWM.getWindowSize();
        this.myFloatBuffer = BufferUtils.createFloatBuffer(OGL_MATRIX_SIZE);
        this.viewProjMatrix = new Matrix4f();
        this.renderColorLocation = 0;
    }

    private void initOpenGL() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glViewport(0, 0, winWidthHeight[0], winWidthHeight[1]);
        glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        this.shader_program = glCreateProgram();
        int vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs,
                "uniform mat4 viewProjMatrix;" +
                        "void main(void) {" +
                        " gl_Position = viewProjMatrix * gl_Vertex;" +
                        "}");
        glCompileShader(vs);
        glAttachShader(shader_program, vs);
        int fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs,
                "uniform vec3 color;" +
                        "void main(void) {" +
                        " gl_FragColor = vec4(0.6f, 0.7f, 0.1f, 1.0f);" +
                        "}");
        glCompileShader(fs);
        glAttachShader(shader_program, fs);
        glLinkProgram(shader_program);
        glUseProgram(shader_program);
        vpMatLocation = glGetUniformLocation(shader_program, "viewProjMatrix");
    }

    private void renderObjects() {
        while (!myWM.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            int vbo = glGenBuffers();
            int ibo = glGenBuffers();
            float[] vertices = myGM.generateTilesVertices(NUM_ROWS, NUM_COLS);
            int[] indices = myGM.generateTileIndices(NUM_ROWS * NUM_COLS);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.
                    createFloatBuffer(vertices.length).
                    put(vertices).flip(), GL_DYNAMIC_DRAW);
            glEnableClientState(GL_VERTEX_ARRAY);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, (IntBuffer) BufferUtils.
                    createIntBuffer(indices.length).
                    put(indices).flip(), GL_DYNAMIC_DRAW);
            glVertexPointer(2, GL_FLOAT, 0, 0L);
            viewProjMatrix.setOrtho(0.0f, winWidthHeight[0], 0.0f, winWidthHeight[1], 0.0f, 10.0f);
            glUniformMatrix4fv(vpMatLocation, false,
                    viewProjMatrix.get(myFloatBuffer));
            glUniform3f(renderColorLocation, 1.0f, 0.498f, 0.153f);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            final int VTD = indices.length;
            glDrawElements(GL_TRIANGLES, VTD, GL_UNSIGNED_INT, 0L);
            myWM.swapBuffers();
        }
    }

    public void render(final int offset, final int padding, final int size, final int numRows, final int numCols) {
        this.OFFSET = offset;
        this.NUM_ROWS = numRows;
        this.NUM_COLS = numCols;
        this.PADDING = padding;
        this.SIZE = size;

        myGM = new WMGeometryManager(NUM_ROWS, NUM_COLS, OFFSET, SIZE, PADDING, winWidthHeight);

        myWM.updateContextToThis();
        renderLoop();
        myWM.destroyGlfwWindow();
    }

    public void renderLoop() {
        glfwPollEvents();
        initOpenGL();
        renderObjects();
        /* Process window messages in the main thread */
        while (!myWM.isGlfwWindowClosed()) {
            glfwWaitEvents();
        }
    }
}
