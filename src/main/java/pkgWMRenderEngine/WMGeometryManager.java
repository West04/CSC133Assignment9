package pkgWMRenderEngine;

import pkgWMUtils.WMGoLArray;
import static pkgWMUtils.WMSPOT.*;

public class WMGeometryManager {
    private final int NUM_COLS;
    private final int NUM_ROWS;
    private final int TOTAL;
    private final int SIZE;
    private final int[] winWidthHeight;
    private final int OFFSET;
    private final int PADDING;
    private final int VERTICES_LENGTH;

    public WMGeometryManager(int maxRows, int maxCols, int offset, int size, int padding, int[] winWidthHeight) {
        this.NUM_COLS = maxCols;
        this.NUM_ROWS = maxRows;
        this.OFFSET = offset;
        this.SIZE = size;
        this.PADDING = padding;
        this.TOTAL = NUM_COLS * NUM_ROWS;
        this.VERTICES_LENGTH = TOTAL * VPT * FPV;

        this.winWidthHeight = winWidthHeight;
    }

    public int[] generateTileIndices(final int totalTiles) {
        int total_elements = totalTiles * EPT;
        int[] indices = new int[total_elements];
        int cur_tile = 0;

        for (int tile = 0; tile < totalTiles; tile++) {
            indices[cur_tile++] = tile * VPT;
            indices[cur_tile++] = tile * VPT + 1;
            indices[cur_tile++] = tile * VPT + 2;
            indices[cur_tile++] = tile * VPT;
            indices[cur_tile++] = tile * VPT + 2;
            indices[cur_tile++] = tile * VPT + 3;
        }
        return indices;
        // return new int[]{0, 1, 2, 0, 2, 3};
    }

    public float[] generateTilesVertices(final int rowTiles, final int columnTiles) {
        float[] vertices = new float[VPT * FPV * NUM_ROWS * NUM_COLS];
        float xmin = OFFSET;
        float ymin = winWidthHeight[1] - (SIZE + OFFSET);
        int index = 0;

        for (int row = 0; row < rowTiles; row++) {
            for (int col = 0; col < columnTiles; col++) {
                if (!fillArrayWithTileVertices(vertices, index, xmin, ymin)) {
                    System.out.println("There is a problem!");
                }
                index += VPT * FPV;
                xmin += SIZE + PADDING;
            }
            xmin = OFFSET;
            ymin -= (SIZE + PADDING);
        }

        return vertices;
    }

    public boolean fillArrayWithTileVertices(float[] vertices, int startIndex, float xmin, float ymin) {
        if (startIndex < 0 || startIndex >= VERTICES_LENGTH) {
            return false;
        }

        if (xmin > winWidthHeight[0] || ymin > winWidthHeight[1]) {
            return false;
        }

        if (vertices == null || vertices.length != VERTICES_LENGTH) {
            return false;
        }

        vertices[startIndex++] = xmin;
        vertices[startIndex++] = ymin;
        vertices[startIndex++] = xmin + SIZE;
        vertices[startIndex++] = ymin;
        vertices[startIndex++] = xmin + SIZE;
        vertices[startIndex++] = ymin + SIZE;
        vertices[startIndex++] = xmin;
        vertices[startIndex] = ymin + SIZE;

        return true;
    }

    public boolean generateTilesVertices(final WMGoLArray myGoL, float[] vertices) {
        if (vertices == null) {
            return false;
        }

        int[][] array = myGoL.getArray();

        if (array == null) {
            return false;
        }

        if (vertices.length != VERTICES_LENGTH || array.length != NUM_ROWS || array[0].length != NUM_COLS) {
            return false;
        }

        int index = 0;
        int rows = myGoL.getNumRows();
        int cols = myGoL.getNumCols();
        float xmin = OFFSET;
        float ymin = winWidthHeight[1] - (SIZE + OFFSET);
        float distanceBetween = SIZE + PADDING;

        for (int row = 0; row < rows; row++) {

            for (int col = 0; col < cols; col++) {

                if (array[row][col] == ALIVE) {

                    if (!fillArrayWithTileVertices(
                            vertices,
                            index,
                            xmin + (col * distanceBetween),
                            ymin - (row * distanceBetween)
                    )) {
                        return false;
                    }
                    index += VPT * FPV;
                }
            }
        }
        return true;
    }
}
