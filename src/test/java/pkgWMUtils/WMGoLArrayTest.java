package pkgWMUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;
import static pkgWMUtils.WMSPOT.*;
import java.io.File;

public class WMGoLArrayTest {

    private WMGoLArray golArray;
    private final int TEST_ROWS = 10;
    private final int TEST_COLS = 10;

    @BeforeEach
    public void setUp() {
        golArray = new WMGoLArray(TEST_ROWS, TEST_COLS);
    }

    @Test
    public void testUpdate1() {
        int[][] golArray.getArray();

        golArray.onTickUpdate();


    }
}