package pkgCSC133;

import pkgWMRenderEngine.WMRenderer;
import pkgWMUtils.WMGoLArray;
import pkgWMUtils.WMWindowManager;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.GLFW.glfwWaitEvents;

public class Driver {
    private static final int sleepTime = 300;

    public static void main(String[] args) {
        int rows, cols;
        int offset, padding, size;
        int winOrgX = 0, winOrgY = 25;

        WMGoLArray myGoL;

        if (args.length == 1) {

            myGoL = new WMGoLArray(args[0]);

            rows = myGoL.getNumRows();
            cols = myGoL.getNumCols();
            offset = 10;
            padding = 10;
            size = 50;


        } else {

            rows = 200;
            cols = 750;
            offset = 2;
            padding = 2;
            size = 4;
            int numOfLiveCells = (int) (((float) rows * (float) cols) * 0.20);

            myGoL = new WMGoLArray(rows, cols, numOfLiveCells);
        }

        final int winWidth = (size + padding) * cols + 2 * offset;
        final int winHeight = (size + padding) * rows + 2 * offset;

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
    } // public static void fileRun(String[] args)
}