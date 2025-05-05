package pkgCSC133;

import pkgWMRenderEngine.WMRenderer;
import pkgWMUtils.WMGoLArray;
import pkgWMUtils.WMWindowManager;

import static java.lang.Thread.sleep;

public class Driver {
    public static void main(String[] args) {
        final int numRows = 6, numCols = 6, polyLength = 10, polyOffset = 5, polyPadding = 5;
        final int winWidth = (polyLength + polyPadding) * numCols + 2 * polyOffset;
        final int winHeight = (polyLength + polyPadding) * numRows + 2 * polyOffset;
        final int winOrgX = 50, winOrgY = 80;
        final WMWindowManager myWM = WMWindowManager.get(winWidth, winHeight, winOrgX, winOrgY);

        final WMGoLArray myGoL = new WMGoLArray(args[0]);
        myGoL.printArray();

        final WMRenderer myRenderer = new WMRenderer(myWM, polyOffset, polyPadding, polyLength, numRows, numCols, myGoL);

        myWM.updateContextToThis();

        myRenderer.render();

        while (!myWM.isGlfwWindowClosed()) {
            myGoL.onTickUpdate();

            myRenderer.render();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void proj6(WMGoLArray myGoL) {
        final int numRows = 6, numCols = 7, polyLength = 50, polyOffset = 10, polyPadding = 20;
        final int winWidth = (polyLength + polyPadding) * numCols + 2 * polyOffset;
        final int winHeight = (polyLength + polyPadding) * numRows + 2 * polyOffset;
        final int winOrgX = 50, winOrgY = 80;
        final WMWindowManager myWM = WMWindowManager.get(winWidth, winHeight, winOrgX, winOrgY);
        final WMRenderer myRenderer = new WMRenderer(myWM, polyOffset, polyPadding, polyLength, numRows, numCols, myGoL);
        myRenderer.render();

    }
}
