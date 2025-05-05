package pkgWMRenderEngine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pkgWMUtils.WMGoLArray;
import pkgWMUtils.WMSPOT;

import static org.junit.jupiter.api.Assertions.*;
import static pkgWMUtils.WMSPOT.*;

public class WMGeometryManagerTest {

    private WMGeometryManager geometryManager;
    private final int TEST_ROWS = 4;
    private final int TEST_COLS = 4;
    private final int OFFSET = 10;
    private final int SIZE = 20;
    private final int PADDING = 5;
    private final int[] WINDOW_SIZE = {800, 600};

    @BeforeEach
    public void setUp() {
        geometryManager = new WMGeometryManager(
                TEST_ROWS,
                TEST_COLS,
                OFFSET,
                SIZE,
                PADDING,
                WINDOW_SIZE
        );
    }

    @Test
    public void testGenerateTileIndices() {
        int totalTiles = 3;
        int[] indices = geometryManager.generateTileIndices(totalTiles);

        assertEquals(totalTiles * EPT, indices.length, "Indices array should have 6 elements per tile");

        // Check pattern for the first tile: [0,1,2,0,2,3]
        assertEquals(0, indices[0], "First vertex of first triangle of first tile should be 0");
        assertEquals(1, indices[1], "Second vertex of first triangle of first tile should be 1");
        assertEquals(2, indices[2], "Third vertex of first triangle of first tile should be 2");
        assertEquals(0, indices[3], "First vertex of second triangle of first tile should be 0");
        assertEquals(2, indices[4], "Second vertex of second triangle of first tile should be 2");
        assertEquals(3, indices[5], "Third vertex of second triangle of first tile should be 3");

        // Check pattern for the second tile: [4,5,6,4,6,7]
        assertEquals(4, indices[6], "First vertex of first triangle of second tile should be 4");
        assertEquals(5, indices[7], "Second vertex of first triangle of second tile should be 5");
        assertEquals(6, indices[8], "Third vertex of first triangle of second tile should be 6");
        assertEquals(4, indices[9], "First vertex of second triangle of second tile should be 4");
        assertEquals(6, indices[10], "Second vertex of second triangle of second tile should be 6");
        assertEquals(7, indices[11], "Third vertex of second triangle of second tile should be 7");
    }

    @Test
    public void testGenerateVertices1() {
        float[] vertices = geometryManager.generateTilesVertices(TEST_ROWS, TEST_COLS);


    }

}