package pkgCSC133;

import pkgWMRenderEngine.WMRenderer;
import pkgWMUtils.WMGoLArray;
import pkgWMUtils.WMWindowManager;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.GLFW.glfwWaitEvents;

public class Driver {
    private static final int sleepTime = 300;

    public static void main(String[] args) {
        // randomRun();

        fileRun(args);
    }

    public static void fileRun(String[] args) {
        final WMGoLArray myGoL = new WMGoLArray(args[0]);

        final int rows = myGoL.getNumRows(), cols = myGoL.getNumCols();
        final int offset = 10, padding = 5, size = 50;

        final int winWidth = (size + padding) * cols + 2 * offset;
        final int winHeight = (size + padding) * rows + 2 * offset;

        final int winOrgX = 0, winOrgY = 25;

        final WMWindowManager myWM = WMWindowManager.get(winWidth, winHeight, winOrgX, winOrgY);
        final WMRenderer myRenderer = new WMRenderer(myWM, offset, padding, size, rows, cols);

        myWM.updateContextToThis();

        while (!myWM.isGlfwWindowClosed()) {

            myRenderer.renderGoLArray(myGoL);
            myGoL.onTickUpdate();
            myGoL.swapLiveAndNext();

            try {
                sleep(sleepTime);
            }

            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } // while (!myWM.isGlfwWindowClosed())

        while (!myWM.isGlfwWindowClosed()) {
            glfwWaitEvents();
        }

        myWM.destroyGlfwWindow();
    } // public static void fileRun(String[] args)

    public static void randomRun() {
        final WMGoLArray myGoL = new WMGoLArray(400, 1080, 55000);

        final int rows = myGoL.getNumRows(), cols = myGoL.getNumCols();
        final int offset = 1, padding = 1, size = 2;

        final int winWidth = (size + padding) * cols + 2 * offset;
        final int winHeight = (size + padding) * rows + 2 * offset;

        final int winOrgX = 0, winOrgY = 25;

        final WMWindowManager myWM = WMWindowManager.get(winWidth, winHeight, winOrgX, winOrgY);
        final WMRenderer myRenderer = new WMRenderer(myWM, offset, padding, size, rows, cols);

        myWM.updateContextToThis();

        while (!myWM.isGlfwWindowClosed()) {

            myRenderer.renderGoLArray(myGoL);
            myGoL.onTickUpdate();
            myGoL.swapLiveAndNext();

            try {
                sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } // while (!myWM.isGlfwWindowClosed())

        while (!myWM.isGlfwWindowClosed()) {
            glfwWaitEvents();
        }

        myWM.destroyGlfwWindow();
    } // public static void randomRun()
}
