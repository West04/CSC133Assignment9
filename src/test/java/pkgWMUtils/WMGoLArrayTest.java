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

    // Helper Method for Visualizing Arrays not in GolArray
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

    @BeforeEach
    public void setUp() {
        golArray = new WMGoLArray(TEST_ROWS, TEST_COLS);
    }

    @Test
    public void testConstructor1() {
        System.out.println("Testing constructor 1");
        WMGoLArray newArray = new WMGoLArray("golarray_constructor_test1.txt");
        newArray.printArray();

        int[][] testArray = new int[][] {{1, 0}, {0, 1}};
        printNormalArray(testArray);

        int[][] array = newArray.getArray();

        assertArrayEquals(array, testArray);
    }

    @Test
    public void testConstructor2() {
        System.out.println("Testing constructor 2");
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
        System.out.println("Testing constructor 3");
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
        System.out.println("Testing update 1");
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
        System.out.println("Testing update 2");
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

    @Test
    public void testUpdate3() {
        System.out.println("Testing update 3");
        golArray.printArray();
        System.out.println();

        golArray.setAlive(0, 0);
        golArray.setAlive(1, 0);
        golArray.setAlive(2, 0);
        golArray.setAlive(3, 0);

        golArray.setAlive(0, 2);
        golArray.setAlive(1, 2);
        golArray.setAlive(2, 2);
        golArray.setAlive(3, 2);

        golArray.swapLiveAndNext();
        golArray.printArray();
        System.out.println();
        int[][] originalArray = golArray.getArray();
        int numOfUpdates = 5;

        for (int i = 0; i < numOfUpdates; i++) {
            golArray.onTickUpdate();
            golArray.swapLiveAndNext();
            golArray.printArray();
            System.out.println();

            assertArrayEquals(originalArray, golArray.getArray());
        }
    }

    @Test
    public void testUpdate4() {
        System.out.println("Testing update 4");

        WMGoLArray newGoLArray = new WMGoLArray("gol_input_1.txt");
        newGoLArray.printArray();

        boolean equalsFlag = true;
        int iterations = 0;
        int maxIterations = 100;

        int[][] afterArray = new int[0][0];
        int[][] beforeArray = new int[0][0];;

        while (equalsFlag || iterations > maxIterations) {
            beforeArray = newGoLArray.getArray();

            newGoLArray.onTickUpdate();
            newGoLArray.swapLiveAndNext();

            afterArray = newGoLArray.getArray();

            equalsFlag = !Arrays.deepEquals(beforeArray, afterArray);
            iterations++;
        }

        System.out.println();
        printNormalArray(afterArray);
        System.out.println();
        printNormalArray(beforeArray);

        assertArrayEquals(afterArray, beforeArray);
    }
}