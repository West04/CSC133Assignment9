package pkgWMRenderEngine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pkgWMUtils.WMGoLArray;

import static org.junit.jupiter.api.Assertions.*;
import static pkgWMUtils.WMSPOT.*;

public class WMGeometryManagerTest {

    private static final int DEFAULT_TEST_ROWS = 2;
    private static final int DEFAULT_TEST_COLS = 2;
    private static final int DEFAULT_OFFSET = 10;
    private static final int DEFAULT_TILE_SIZE = 20;
    private static final int DEFAULT_PADDING = 10;
    private static final int[] DEFAULT_WINDOW_SIZE = {800, 800};

    private WMGeometryManager geometryManager;

    @BeforeEach
    public void setUp() {
        geometryManager = new WMGeometryManager(
                DEFAULT_TEST_ROWS,
                DEFAULT_TEST_COLS,
                DEFAULT_OFFSET,
                DEFAULT_TILE_SIZE,
                DEFAULT_PADDING,
                DEFAULT_WINDOW_SIZE
        );
    }

    @Test
    public void testGenerateTileIndices() {
        int totalTiles = 3;
        int[] indices = geometryManager.generateTileIndices(totalTiles);

        assertEquals(totalTiles * EPT, indices.length);

        for (int tile = 0; tile < totalTiles; tile++) {
            int baseVertex = tile * 4;
            int baseIndex = tile * EPT;

            int[] expectedIndices = {
                    baseVertex, baseVertex + 1, baseVertex + 2,
                    baseVertex, baseVertex + 2, baseVertex + 3
            };

            for (int i = 0; i < EPT; i++) {
                assertEquals(expectedIndices[i], indices[baseIndex + i]);
            }
        }
    }

    @Test
    public void testGenerateVertices1() {
        float[] vertices = geometryManager.generateTilesVertices(DEFAULT_TEST_ROWS, DEFAULT_TEST_COLS);

        assertEquals(DEFAULT_TEST_ROWS * DEFAULT_TEST_COLS * VPT * FPV, vertices.length);

        for (int row = 0; row < DEFAULT_TEST_ROWS; row++) {
            for (int col = 0; col < DEFAULT_TEST_COLS; col++) {
                int vertexDataStartIndex = (row * DEFAULT_TEST_COLS + col) * VPT * FPV;
                verifySingleTileVerticesAtIndex(vertices, vertexDataStartIndex, row, col,
                        DEFAULT_OFFSET, DEFAULT_TILE_SIZE, DEFAULT_PADDING, DEFAULT_WINDOW_SIZE);
            }
        }
    }


    @Test
    public void testGenerateVertices2() {
        WMGoLArray golArray = new WMGoLArray(DEFAULT_TEST_ROWS, DEFAULT_TEST_COLS);
        for (int row = 0; row < DEFAULT_TEST_ROWS; row++) {
            for (int col = 0; col < DEFAULT_TEST_COLS; col++) {
                golArray.setAlive(row, col);
            }
        }
        golArray.swapLiveAndNext();

        float[] vertices = new float[DEFAULT_TEST_ROWS * DEFAULT_TEST_COLS * VPT * FPV];
        boolean success = geometryManager.generateTilesVertices(golArray, vertices);

        assertTrue(success);
        assertEquals(DEFAULT_TEST_ROWS * DEFAULT_TEST_COLS, golArray.liveCellCount());

        for (int row = 0; row < DEFAULT_TEST_ROWS; row++) {
            for (int col = 0; col < DEFAULT_TEST_COLS; col++) {
                int vertexDataStartIndex = (row * DEFAULT_TEST_COLS + col) * VPT * FPV;
                verifySingleTileVerticesAtIndex(vertices, vertexDataStartIndex, row, col,
                        DEFAULT_OFFSET, DEFAULT_TILE_SIZE, DEFAULT_PADDING, DEFAULT_WINDOW_SIZE);
            }
        }
    }

    @Test
    public void testGenerateVertices3() {
        int testRows = 16;
        int testCols = 16;
        int testOffset = 5;
        int testTileSize = 15;
        int testPadding = 5;
        int[] testWindowSize = {400, 400};

        WMGoLArray golArray = new WMGoLArray(testRows, testCols);
        WMGeometryManager customGeoManager = new WMGeometryManager(
                testRows, testCols, testOffset, testTileSize, testPadding, testWindowSize
        );

        int row1 = 15, col1 = 5;
        int row2 = 7,  col2 = 7;
        int row3 = 1,  col3 = 15;
        golArray.setAlive(row1, col1);
        golArray.setAlive(row2, col2);
        golArray.setAlive(row3, col3);
        golArray.swapLiveAndNext();

        assertEquals(3, golArray.liveCellCount());

        float[] vertices = new float[golArray.liveCellCount() * VPT * FPV];
        boolean success = customGeoManager.generateTilesVertices(golArray, vertices);
        assertTrue(success);
        assertEquals(3 * VPT * FPV, vertices.length);

        int liveCellCounter = 0;
        int[][] liveArray = golArray.getArray();

        for (int row = 0; row < testRows; row++) {
            for (int col = 0; col < testCols; col++) {
                if (liveArray[row][col] == ALIVE) {
                    int vertexDataStartIndex = liveCellCounter * VPT * FPV;

                    verifySingleTileVerticesAtIndex(vertices, vertexDataStartIndex, row, col,
                            testOffset, testTileSize, testPadding, testWindowSize);

                    liveCellCounter++;
                }
            }
        }
        assertEquals(golArray.liveCellCount(), liveCellCounter);
    }

    private void verifySingleTileVerticesAtIndex(float[] vertices, int vertexDataStartIndex,
                                                 int expectedRow, int expectedCol,
                                                 int offset, int tileSize, int padding, int[] windowSize) {
        float expectedXMin = offset + expectedCol * (tileSize + padding);
        float expectedYMin = windowSize[1] - offset - tileSize - expectedRow * (tileSize + padding);

        float[][] expectedVertices = {
                {expectedXMin,            expectedYMin},
                {expectedXMin + tileSize, expectedYMin},
                {expectedXMin + tileSize, expectedYMin + tileSize},
                {expectedXMin,            expectedYMin + tileSize}
        };

        for (int vertexIndex = 0; vertexIndex < VPT; vertexIndex++) {
            int actualVertexOffset = vertexDataStartIndex + vertexIndex * FPV;

            assertTrue(actualVertexOffset + 1 < vertices.length);

            float actualX = vertices[actualVertexOffset];
            float actualY = vertices[actualVertexOffset + 1];

            float expectedX = expectedVertices[vertexIndex][0];
            float expectedY = expectedVertices[vertexIndex][1];

            float delta = 0.001f;
            assertEquals(expectedX, actualX, delta);
            assertEquals(expectedY, actualY, delta);
        }
    }
}