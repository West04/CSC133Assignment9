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

    protected WMGeometryManager(int maxRows, int maxCols, int offset, int size, int padding, int[] winWidthHeight) {
        this.NUM_COLS = maxCols;
        this.NUM_ROWS = maxRows;
        this.OFFSET = offset;
        this.SIZE = size;
        this.PADDING = padding;
        this.TOTAL = NUM_COLS * NUM_ROWS;
        this.VERTICES_LENGTH = TOTAL * VPT * FPV;

        this.winWidthHeight = winWidthHeight;
    }

    protected int[] generateTileIndices(final int totalTiles) {
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

    protected float[] generateTilesVertices(final int rowTiles, final int columnTiles) {
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

    protected boolean fillArrayWithTileVertices(float[] vertices, int startIndex, float xmin, float ymin) {
        if (startIndex < 0 || startIndex >= VERTICES_LENGTH) {
            System.out.println("Problem startIndex: " + startIndex);
            return false;
        }

        if (xmin > winWidthHeight[0] || ymin > winWidthHeight[1]) {
            System.out.println("Problem with xmin: " + xmin + ", ymin: " + ymin);
            return false;
        }

        if (vertices == null) {
            System.out.println("Problem with vertices, is it null");
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

    protected boolean generateTilesVertices(final WMGoLArray myGoL, final float[] vertices) {
        if (vertices == null) {
            System.out.println("Vertices is null");
            return false;
        }
        if (myGoL == null) {
            System.out.println("MyGoL is null");
            return false;
        }

        int[][] array = myGoL.getArray();
        if (array == null) {
            System.out.println("Array is null");
            return false;
        }

        int rows = myGoL.getNumRows();
        int cols = myGoL.getNumCols();
        int index = 0;
        float distanceBetween = SIZE + PADDING;

        // Calculate base position
        float xmin = OFFSET;
        float ymin = winWidthHeight[1] - (SIZE + OFFSET);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (array[row][col] == ALIVE) {
                    float currentX = xmin + (col * distanceBetween);
                    float currentY = ymin - (row * distanceBetween);

                    if (!fillArrayWithTileVertices(vertices, index, currentX, currentY)) {
                        System.out.println("Failed to fill vertices for cell at [" + row + "," + col + "]");
                        return false;
                    }

                    index += VPT * FPV;
                }
            }
        }

        return true;
    }
}
