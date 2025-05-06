package pkgWMRenderEngine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import pkgWMUtils.WMGoLArray;
import pkgWMUtils.WMWindowManager;
import static pkgWMUtils.WMSPOT.*;

import org.joml.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static java.lang.Thread.sleep;
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
    private static final int OGL_MATRIX_SIZE = 16;
    private Matrix4f viewProjMatrix;
    private FloatBuffer myFloatBuffer;
    private int vpMatLocation;
    private int renderColorLocation;

    public WMRenderer(WMWindowManager myWM, final int offset, final int padding, final int size, final int numRows, final int numCols) {
        this.myFloatBuffer = BufferUtils.createFloatBuffer(OGL_MATRIX_SIZE);
        this.viewProjMatrix = new Matrix4f();
        this.renderColorLocation = 0;

        this.OFFSET = offset;
        this.NUM_ROWS = numRows;
        this.NUM_COLS = numCols;
        this.PADDING = padding;
        this.SIZE = size;
        this.winWidthHeight = myWM.getWindowSize();

        this.myWM = myWM;

        myGM = new WMGeometryManager(NUM_ROWS, NUM_COLS, OFFSET, SIZE, PADDING, winWidthHeight);

    } // public WMRenderer(WMWindowManager myWM, final int offset, final int padding, final int size, final int numRows, final int numCols)

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
    } // private void initOpenGL()

    private void renderObjects(float[] vertices, int[] indices) {
        glfwPollEvents();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        int vbo = glGenBuffers();
        int ibo = glGenBuffers();
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
    } // private void renderObjects(float[] vertices, int[] indices)

    public void renderAllTiles() {
        glfwPollEvents();
        initOpenGL();

        float[] vertices = myGM.generateTilesVertices(NUM_ROWS, NUM_COLS);
        int[] indices = myGM.generateTileIndices(NUM_ROWS * NUM_COLS);

        while (!myWM.isGlfwWindowClosed()) {
            renderObjects(vertices, indices);
        }

        /* Process window messages in the main thread */
        while (!myWM.isGlfwWindowClosed()) {
            glfwWaitEvents();
        }

        myWM.destroyGlfwWindow();
    } // public void renderAllTiles()

    public void renderGoLArray(final WMGoLArray goLArray) {
        glfwPollEvents();
        initOpenGL();

        int liveCount = goLArray.liveCellCount();
        float[] vertices = new float[liveCount * FPV * VPT];

        myGM.generateTilesVertices(goLArray, vertices);
        int[] indices = myGM.generateTileIndices(liveCount * EPT);

        renderObjects(vertices, indices);
    } // public void renderGoLArray(final WMGoLArray goLArray)
} // public class WMRenderer
