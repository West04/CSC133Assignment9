package pkgWMUtils;

import java.awt.*;

public class WMGeometryManager {
    private final int NUM_COLS;
    private final int NUM_ROWS;
    private final int TOTAL;
    private final int SIZE;
    private final int[] WinWidthHeight;
    private final int OFFSET;
    private final int PADDING;
    private final WMPingPongArray myPPArray;

    protected WMGeometryManager(int maxRows, int maxCols, int offset, int size, int padding, int[] winWidthHeight) {
        NUM_COLS = maxCols;
        NUM_ROWS = maxRows;
        OFFSET = offset;
        SIZE = size;
        PADDING = padding;
        TOTAL = NUM_COLS * NUM_ROWS;
        WinWidthHeight = winWidthHeight;
        myPPArray = new WMPingPongArray(NUM_ROWS, NUM_COLS, 0, 1);
        myPPArray.randomizeInRange();

    }

    protected float[] generateTilesVertices(final int rowTiles, final int columnTiles) {
        return new float[1];
    }

    protected boolean fillArrayWithTileVertices(float[] vertices, int startIndex, float xmin, float ymin) {
        return false;
    }

    protected int[] generateTileIndices(final int totalTiles) {
        return new int[1];
    }

    protected boolean generateTilesVertices(final WMGoLArray myGoLA, float[] vertices) {
        return false;
    }
}
