package pkgWMRenderEngine;

import pkgWMUtils.WMGoLArray;
import pkgWMUtils.WMPingPongArray;

public class WMGeometryManager {
    private final int NUM_COLS;
    private final int NUM_ROWS;
    private final int TOTAL;
    private final int SIZE;
    private final int[] WinWidthHeight;
    private final int OFFSET;
    private final int PADDING;
    private WMPingPongArray myPPArray;

    protected WMGeometryManager(int maxRows, int maxCols, int offset, int size, int padding, int[] winWidthHeight) {
        NUM_COLS = maxCols;
        NUM_ROWS = maxRows;
        OFFSET = offset;
        SIZE = size;
        PADDING = padding;
        TOTAL = NUM_COLS * NUM_ROWS;
        WinWidthHeight = winWidthHeight;
    }

    protected int[] generateTileIndices(final int totalTiles) {
        return new int[1];
    }

    protected float[] generateTilesVertices(final int rowTiles, final int columnTiles) {
        return new float[1];
    }

    protected boolean fillArrayWithTileVertices(float[] vertices, int startIndex, float xmin, float ymin) {
        return false;
    }

    protected boolean generateTilesVertices(final WMGoLArray myGoLA, float[] vertices) {
        myPPArray = myGoLA;

        int rows = myGoLA.getNumRows();
        int cols = myGoLA.getNumCols();

        float[] calculatedVertices = generateTilesVertices(rows, cols);

        return false;
    }
}
