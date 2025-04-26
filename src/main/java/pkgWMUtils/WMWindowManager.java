package pkgWMUtils;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WMWindowManager {
    private static final GLFWFramebufferSizeCallback resizeWindow =
            new GLFWFramebufferSizeCallback(){
                @Override
                public void invoke(long window, int width, int height){
                    glViewport(0,0,width, height);
                }
            };
    private static WMWindowManager my_window;
    private static long glfwWindow;
    private static int win_height;
    private static int win_width;

    private WMWindowManager() {
        initGlfwWindow();
    } // private WMWindowManager()

    public void updateContextToThis() {
        glfwMakeContextCurrent(glfwWindow);
    } //  public void updateContextToThis()

    public void destroyGlfwWindow () {
        glfwDestroyWindow(glfwWindow);
    } // public void destroyGlfwWindow ()

    public boolean isGlfwWindowClosed() {
        return glfwWindowShouldClose(glfwWindow);
    } // public boolean isGlfwWindowClosed()

    public static WMWindowManager get(int width, int height) {
        if (my_window != null) {
            return my_window;
        }
        win_width = width;
        win_height = height;

        int defaultOrgX = 50;
        int defaultOrgY = 80;
        return init(width, height, defaultOrgX, defaultOrgY);
    } // public static WMWindowManager get(int width, int height)

    public static WMWindowManager get() {
        if (my_window != null) {
            return my_window;
        }
        int defaultWidth = 100;
        int defaultHeight = 100;

        int defaultOrgX = 50;
        int defaultOrgY = 80;
        return init(defaultWidth, defaultHeight, defaultOrgX, defaultOrgY);
    } //  public static WMWindowManager get()

    private static WMWindowManager init(int width, int height, int orgX, int orgY) {
        win_width = width;
        win_height = height;
        my_window = new WMWindowManager();
        setWindowPosition(orgX, orgY);

        return my_window;
    }

    protected static void setWinWidth(int winWidth, int winHeight) {
        win_width = winWidth;
        win_height = winHeight;
        glfwSetWindowSize(glfwWindow, winWidth, winHeight);
    } // protected static void setWinWidth(int int)

    public void enableResizeWindowCallback() {
        glfwSetFramebufferSizeCallback(glfwWindow, resizeWindow);
    } // public void enableResizeWindowCallback(...)

    public void swapBuffers() {
        glfwSwapBuffers(glfwWindow);
    } // public void swapBuffers()

    private static void initGlfwWindow() {
        GLFWErrorCallback errorCallback;
        GLFWKeyCallback keyCallback;
        GLFWFramebufferSizeCallback fbCallback;

        glfwSetErrorCallback(errorCallback =
                GLFWErrorCallback.createPrint(System.err));

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 8);

        glfwWindow = glfwCreateWindow(win_width, win_height, "CSC 133", NULL, NULL);

        if (glfwWindow == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(glfwWindow, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int
                    mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, true);
            }
        });

        glfwSetFramebufferSizeCallback(glfwWindow, fbCallback = new
                GLFWFramebufferSizeCallback() {
                    @Override
                    public void invoke(long window, int w, int h) {
                        if (w > 0 && h > 0) {
                            win_width = w;
                            win_height = h;
                        }
                    }
                });

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(glfwWindow, 0, 0);
        glfwMakeContextCurrent(glfwWindow);
        int VSYNC_INTERVAL = 1;
        glfwSwapInterval(VSYNC_INTERVAL);
        glfwShowWindow(glfwWindow);
    } // private static void initGlfwWindow()

    public int[] getWindowSize() {
        return new int[]{win_width, win_height};
    } //  public int[] getWindowSize()

    public static void setWindowPosition(int orgX, int orgY) {
        if (glfwWindow > 0) {
            glfwSetWindowPos(glfwWindow, orgX, orgY);
        }  //  if (glfwWindow > 0)
    }  //  public void setWindowPosition(...)

    public static WMWindowManager get(int width, int height, int orgX, int orgY) {
        return (WMWindowManager) init(width, height, orgX, orgY);
    }  //  public WMWindowManager get(...)
}