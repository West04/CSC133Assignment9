package pkgWMUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;
import static pkgWMUtils.WMSPOT.*;
import java.io.File;
import java.util.Arrays;

public class WMGoLArrayTest {

    private WMGoLArray golArray;
    private final int TEST_ROWS = 4;
    private final int TEST_COLS = 4;

    @BeforeEach
    public void setUp() {
        golArray = new WMGoLArray(TEST_ROWS, TEST_COLS);
    }

    @Test
    public void testConstructor1() {
        WMGoLArray newArray = new WMGoLArray("golarray_constructor_test1.txt");
        newArray.printArray();

        int[][] testArray = new int[][] {{1, 0}, {0, 1}};
        printNormalArray(testArray);

        int[][] array = newArray.getArray();

        assertArrayEquals(array, testArray);
    }

    @Test
    public void testConstructor2() {
        golArray.copyToNextArray();

        golArray.setAlive(0,0);
        golArray.setAlive(1,1);
        golArray.setAlive(2,2);
        golArray.setAlive(3,3);

        golArray.swapLiveAndNext();
        int[][] array = golArray.getArray();
        golArray.printArray();

        int[][] testArray = new int[][] {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        printNormalArray(testArray);

        assertArrayEquals(testArray, array);
    }

    @Test
    public void testConstructor3() {
        int numOfLives = 3;
        int numOfRepeat = 5;

        for (int i = 0; i < numOfRepeat; i++) {
            WMGoLArray firstGoLArray = new WMGoLArray(TEST_ROWS, TEST_COLS, numOfLives);
            WMGoLArray secondGoLArray = new WMGoLArray(TEST_ROWS, TEST_COLS, numOfLives);

            int[][] firstArray = firstGoLArray.getArray();
            int[][] secondArray = secondGoLArray.getArray();

            firstGoLArray.printArray();
            System.out.println();
            secondGoLArray.printArray();
            System.out.println();

            assertFalse(Arrays.deepEquals(firstArray, secondArray));
        }
    }

    @Test
    public void testUpdate1() {
        int[][] originalArray = golArray.getArray();

        int numOfUpdates = 5;

        for (int i = 0; i < numOfUpdates; i++) {
            golArray.printArray();
            System.out.println();
            golArray.onTickUpdate();
            golArray.swapLiveAndNext();
        }

        golArray.swapLiveAndNext();
        int[][] updatedArray = golArray.getArray();

        assertArrayEquals(originalArray, updatedArray);
    }

    @Test
    public void testUpdate2() {
        golArray.printArray();
        System.out.println();
        int[][] originalArray = golArray.getArray();

        for (int row = 0; row < TEST_ROWS; row++) {
            for (int col = 0; col < TEST_COLS; col++) {
                golArray.setAlive(row, col);
            }
        }
        golArray.swapLiveAndNext();
        golArray.printArray();
        System.out.println();

        golArray.onTickUpdate();
        golArray.swapLiveAndNext();
        golArray.printArray();
        System.out.println();


        int[][] updatedArray = golArray.getArray();

        assertArrayEquals(originalArray, updatedArray);
    }

    public void printNormalArray(int[][] array) {
        System.out.println();
        for (int row = 0; row < array.length; row++) {
            System.out.print(row + "  ");

            for (int col = 0; col < array[0].length; col++) {
                System.out.print(array[row][col] + " ");
            } // for (int col = 0; col < numCols; col++)

            System.out.println();
        } // for (int row = 0; row < numRows; row++)
        System.out.println();
    }
}