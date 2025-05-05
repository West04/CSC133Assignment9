package pkgWMUtils; // Use the same package as your original class

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir; // For creating temporary directories for file tests

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WMPingPongArrayTest {

    private WMPingPongArray ppa3x4;
    private final int ROWS = 3;
    private final int COLS = 4;
    private final int RAND_MIN = 0;
    private final int RAND_MAX = 10; // Exclusive max for Random.nextInt

    @TempDir
    Path tempDir; // JUnit Jupiter will create and manage a temporary directory

    // Helper method to create a sample test file
    private Path createTestDataFile(String content) throws IOException {
        Path filePath = tempDir.resolve("test_data.txt");
        Files.writeString(filePath, content);
        return filePath;
    }

    // Helper method to compare two 2D arrays
    private boolean arraysAreEqual(int[][] arr1, int[][] arr2) {
        if (arr1 == null || arr2 == null || arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] == null || arr2[i] == null || !Arrays.equals(arr1[i], arr2[i])) {
                return false;
            }
        }
        return true;
    }


    @BeforeEach
    void setUp() {
        // Initialize a common instance before each test
        ppa3x4 = new WMPingPongArray(ROWS, COLS, RAND_MIN, RAND_MAX);
        // Note: The initial state is in nextArray, call swap to move it to liveArray for testing methods that read liveArray
        ppa3x4.swapLiveAndNext();
    }

    @Test
    void constructorWithRandomValues_InitializesCorrectly() {
        WMPingPongArray ppa = new WMPingPongArray(5, 6, 10, 20);
        assertEquals(5, ppa.getNumRows(), "Constructor should set correct number of rows.");
        assertEquals(6, ppa.getNumCols(), "Constructor should set correct number of cols.");
        assertEquals(30, ppa.getTotalLength(), "Constructor should set correct total length.");

        // Check if values are within range (difficult to test exact random values)
        // We check nextArray as values are initially populated there
        // Accessing private members directly is not possible/ideal in tests,
        // so we'll test indirectly or make getArray/swap more testable if needed.
        // For this test, we'll assume the constructor populates nextArray, then swap.
        ppa.swapLiveAndNext(); // Move generated values to liveArray
        int[][] internalArray = ppa.getArray(); // Get a copy of liveArray
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 6; c++) {
                assertTrue(internalArray[r][c] >= 10 && internalArray[r][c] < 20,
                        "Values should be within the specified random range [10, 20).");
            }
        }
    }

    @Test
    void constructorWithFile_InitializesCorrectly() throws IOException {
        String fileContent = "99\n" + // Default value (ignored by constructor logic?)
                "2 3\n" + // Dimensions
                "0  1 2 3\n" + // Row 0 data
                "1  4 5 6\n";  // Row 1 data
        Path testFile = createTestDataFile(fileContent);

        WMPingPongArray ppaFromFile = new WMPingPongArray(testFile.toString());

        assertEquals(2, ppaFromFile.getNumRows(), "File constructor should read correct rows.");
        assertEquals(3, ppaFromFile.getNumCols(), "File constructor should read correct cols.");
        assertEquals(6, ppaFromFile.getTotalLength(), "File constructor should calculate correct total length.");

        // Constructor reads into readArray, copies to nextArray, then swaps to liveArray
        int[][] expected = {{1, 2, 3}, {4, 5, 6}};
        assertTrue(arraysAreEqual(expected, ppaFromFile.getArray()), "File constructor should load data correctly into liveArray.");
    }

    @Test
    void constructorWithFile_FileNotFound() {
        // Test what happens if the file doesn't exist.
        // The current implementation calls System.exit(0), which is hard to test.
        // Consider refactoring to throw an exception (e.g., FileNotFoundException or a custom one)
        // instead of System.exit() for better testability and error handling.

        // Example if it threw an exception:
        // assertThrows(FileNotFoundException.class, () -> {
        //     new WMPingPongArray("non_existent_file.txt");
        // });
        System.out.println("NOTE: Testing constructor with non-existent file relies on manual check or refactoring System.exit.");
        // Due to System.exit(0), this test case can't automatically verify failure without external setup.
    }


    @Test
    void getNumRows() {
        assertEquals(ROWS, ppa3x4.getNumRows(), "getNumRows should return the correct number of rows.");
    }

    @Test
    void getNumCols() {
        assertEquals(COLS, ppa3x4.getNumCols(), "getNumCols should return the correct number of cols.");
    }

    @Test
    void getTotalLength() {
        assertEquals(ROWS * COLS, ppa3x4.getTotalLength(), "getTotalLength should return rows * cols.");
    }

    @Test
    void swapLiveAndNext() {
        // 1. Get initial state of liveArray
        int[][] initialLive = ppa3x4.getArray(); // Gets a copy of liveArray

        // 2. Modify nextArray (e.g., by setting a cell)
        ppa3x4.setCell(0, 0, 999); // Modifies nextArray

        // 3. Get a representation of nextArray *before* swap
        //    Need a way to access nextArray's state. Add a getter or test indirectly.
        //    Let's assume we can modify liveArray temporarily to check nextArray
        WMPingPongArray tempPpa = new WMPingPongArray(ROWS, COLS, 0, 1); // Create a known state PPA
        tempPpa.setCell(0, 0, 999); // Set the same value in its nextArray
        int[][] expectedNext = tempPpa.getArray(); // This is tricky, getArray returns liveArray
        // We need a getNextArray() for a proper test, or test via side effects.

        // Let's test by swapping, checking, swapping back, and checking again.
        ppa3x4.swapLiveAndNext();
        int[][] liveAfterFirstSwap = ppa3x4.getArray(); // Should now contain the modified array (with 999)

        ppa3x4.swapLiveAndNext();
        int[][] liveAfterSecondSwap = ppa3x4.getArray(); // Should be back to the initial state

        // Assert: Check if the value 999 is present after the first swap
        assertEquals(999, liveAfterFirstSwap[0][0], "Cell (0,0) should be 999 after first swap.");

        // Assert: Check if the arrays are back to original after the second swap
        assertTrue(arraysAreEqual(initialLive, liveAfterSecondSwap), "Arrays should be back to initial state after two swaps.");
    }

    @Test
    void randomizeViaFisherYatesKnuth() {
        // FYK shuffles the *liveArray* and puts the result in *nextArray*
        int[][] originalLive = ppa3x4.getArray(); // Copy of liveArray

        ppa3x4.randomizeViaFisherYatesKnuth(); // Shuffles liveArray -> nextArray

        // We need a way to get nextArray to verify the result.
        // Let's swap to bring the result into liveArray for checking.
        ppa3x4.swapLiveAndNext();
        int[][] shuffledLive = ppa3x4.getArray();

        assertEquals(ROWS, shuffledLive.length, "Shuffled array should have the same number of rows.");
        assertEquals(COLS, shuffledLive[0].length, "Shuffled array should have the same number of columns.");

        // Check if the elements are the same, just rearranged
        Set<Integer> originalElements = new HashSet<>();
        Set<Integer> shuffledElements = new HashSet<>();
        long originalSum = 0;
        long shuffledSum = 0;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                originalElements.add(originalLive[r][c]);
                shuffledElements.add(shuffledLive[r][c]);
                originalSum += originalLive[r][c];
                shuffledSum += shuffledLive[r][c];
            }
        }

        assertEquals(originalSum, shuffledSum, "Sum of elements should be the same after shuffling.");
        // Note: If duplicates exist, comparing sets might not be sufficient. Sum is a better check.
        // For a thorough check, sort both arrays (flattened) and compare.

        // It's hard to test *randomness*, but we can check if it's different (most likely)
        assertFalse(arraysAreEqual(originalLive, shuffledLive), "Shuffled array should ideally be different from the original (high probability).");
    }

    @Test
    void randomizeInRange() {
        // Sets nextArray to 0..totalLength-1, swaps, randomizes live->next
        ppa3x4.randomizeInRange(); // Result ends up in nextArray

        // Swap to bring the result into liveArray for checking
        ppa3x4.swapLiveAndNext();
        int[][] randomizedRange = ppa3x4.getArray();

        assertEquals(ROWS, randomizedRange.length);
        assertEquals(COLS, randomizedRange[0].length);

        // Check if all numbers from 0 to totalLength-1 are present exactly once
        Set<Integer> expectedNumbers = new HashSet<>();
        for (int i = 0; i < ppa3x4.getTotalLength(); i++) {
            expectedNumbers.add(i);
        }

        Set<Integer> actualNumbers = new HashSet<>();
        boolean duplicatesFound = false;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (!actualNumbers.add(randomizedRange[r][c])) {
                    duplicatesFound = true;
                }
            }
        }

        assertFalse(duplicatesFound, "Randomize in range should not produce duplicate values.");
        assertEquals(expectedNumbers, actualNumbers, "Randomize in range should contain all numbers from 0 to N-1.");
    }


    @Test
    void saveAndLoadFile() throws IOException {
        // 1. Setup initial state (use a predictable one)
        WMPingPongArray ppaToSave = new WMPingPongArray(2, 2, 0, 1); // Creates 0..3 in nextArray
        ppaToSave.setCell(0, 0, 10);
        ppaToSave.setCell(0, 1, 20);
        ppaToSave.setCell(1, 0, 30);
        ppaToSave.setCell(1, 1, 40);
        ppaToSave.swapLiveAndNext(); // Move 10,20,30,40 to liveArray

        int[][] expectedData = {{10, 20}, {30, 40}};

        // 2. Save the state
        Path savedFilePath = tempDir.resolve("save_test.txt");
        ppaToSave.save(savedFilePath.toString());

        // 3. Verify file content (optional but good for debugging)
        // String content = Files.readString(savedFilePath);
        // System.out.println("Saved file content:\n" + content); // Manual inspection

        // 4. Load the state back using the static loadFile method
        WMPingPongArray ppaLoaded = WMPingPongArray.loadFile(savedFilePath.toString());

        // 5. Verify the loaded state
        assertNotNull(ppaLoaded, "Loaded PPA should not be null.");
        assertEquals(2, ppaLoaded.getNumRows(), "Loaded PPA should have correct rows.");
        assertEquals(2, ppaLoaded.getNumCols(), "Loaded PPA should have correct cols.");
        // loadFile puts data into nextArray and returns the object. Let's swap to check live.
        ppaLoaded.swapLiveAndNext(); // Swaps the loaded data (which was put in nextArray by setCell) into liveArray.
        // NOTE: loadFile's implementation needs review. It creates a new PPA
        // (which initializes nextArray randomly), then sets cells (modifying nextArray again),
        // and returns. The loaded data seems to end up in nextArray.
        // Let's verify nextArray state if possible, or swap and verify liveArray.


        assertTrue(arraysAreEqual(expectedData, ppaLoaded.getArray()), "Loaded array data should match saved data.");

        // Test default save()
        Path defaultSavedFilePath = Path.of("ppa_data.txt"); // Assumes it saves in the current working directory
        try {
            ppaToSave.save(); // Use default filename
            assertTrue(Files.exists(defaultSavedFilePath), "Default save file should exist.");
            WMPingPongArray ppaLoadedDefault = WMPingPongArray.loadFile(defaultSavedFilePath.toString());
            assertNotNull(ppaLoadedDefault);
            ppaLoadedDefault.swapLiveAndNext(); // Swap loaded data to live array for comparison
            assertTrue(arraysAreEqual(expectedData, ppaLoadedDefault.getArray()), "Loaded default save file data should match.");
        } finally {
            // Clean up the default file if it was created
            Files.deleteIfExists(defaultSavedFilePath);
        }
    }

    @Test
    void readFile_ValidFile() throws IOException {
        // This tests the protected static readFile method
        String fileContent = "0\n" + // Default value
                "2 3\n" + // Dimensions
                "0  10 20 30\n" +
                "1  40 50 60\n";
        Path testFile = createTestDataFile(fileContent);

        int[][] result = WMPingPongArray.readFile(testFile.toString());

        int[][] expected = {{10, 20, 30}, {40, 50, 60}};
        assertNotNull(result);
        assertTrue(arraysAreEqual(expected, result), "readFile should parse the file correctly.");
    }

    @Test
    void readFile_InvalidFormat() throws IOException {
        // Test file with incorrect dimension format or non-integer data
        // The current implementation might throw NumberFormatException or others.
        String fileContent = "0\n" +
                "2 THREE\n" + // Invalid column dimension
                "0 1 2 3\n" +
                "1 4 5 6\n";
        Path testFile = createTestDataFile(fileContent);

        // Depending on where the error occurs, different exceptions might happen.
        // Or it might return null/incorrect data if parsing fails gracefully (which it doesn't seem to).
        assertThrows(NumberFormatException.class, () -> {
            WMPingPongArray.readFile(testFile.toString());
        }, "readFile should throw exception for invalid number format.");
    }

    @Test
    void readFile_FileNotFound() {
        // Test the static readFile when the file doesn't exist
        int[][] result = WMPingPongArray.readFile("surely_non_existent_file.txt");
        assertNull(result, "readFile should return null if file not found.");
        // Note: The original method prints stack trace but returns null, which is testable.
    }


    @Test
    void getArray_ReturnsCopy() {
        int[][] array1 = ppa3x4.getArray();
        int[][] array2 = ppa3x4.getArray();

        assertNotSame(array1, array2, "getArray should return a new copy each time.");
        assertTrue(arraysAreEqual(array1, array2), "Copies returned by getArray should be equal in content.");

        // Modify the copy and check if the original is unchanged
        array1[0][0] = -500; // Modify the copy
        int[][] originalStill = ppa3x4.getArray(); // Get a fresh copy of the internal state

        assertNotEquals(-500, originalStill[0][0], "Modifying the returned copy should not affect the internal liveArray.");
    }

    @Test
    void copyToNextArray() {
        // 1. Set liveArray to a known state (or use the state after setup)
        int[][] currentLive = ppa3x4.getArray();

        // 2. Modify nextArray to be different initially
        ppa3x4.setCell(0, 0, 999); // nextArray[0][0] is now 999

        // 3. Perform the copy
        ppa3x4.copyToNextArray(); // nextArray should now be a copy of liveArray

        // 4. Verify nextArray's state. Requires a getter for nextArray or indirect check.
        // Let's swap and check liveArray.
        ppa3x4.swapLiveAndNext(); // Now liveArray should hold the result of the copy
        int[][] resultInLive = ppa3x4.getArray();

        assertTrue(arraysAreEqual(currentLive, resultInLive), "After copyToNextArray and swap, liveArray should match the original liveArray state.");
        assertNotEquals(999, resultInLive[0][0], "The modification to nextArray should have been overwritten by the copy.");
    }


    @Test
    void setCell() {
        // setCell modifies nextArray
        ppa3x4.setCell(1, 2, 12345);

        // Verify the change in nextArray. Need getter or indirect check.
        // Swap live and next, then check the new liveArray.
        ppa3x4.swapLiveAndNext();
        int[][] currentLive = ppa3x4.getArray();

        assertEquals(12345, currentLive[1][2], "setCell should modify the correct cell in nextArray.");
    }

    @Test
    void getNearestNeighborsArray_MidBoard() {
        // For a 3x4 board
        WMPingPongArray.RCPair[] neighbors = ppa3x4.getNearestNeighborsArray(1, 1);
        assertEquals(8, neighbors.length);

        Set<String> expectedNeighbors = Set.of(
                "0,0", "0,1", "0,2", // Top row (r=0, c=0,1,2)
                "1,2",             // Mid right (r=1, c=2)
                "2,2", "2,1", "2,0", // Bottom row (r=2, c=2,1,0)
                "1,0"              // Mid left (r=1, c=0)
        );

        Set<String> actualNeighbors = new HashSet<>();
        for(WMPingPongArray.RCPair pair : neighbors) {
            actualNeighbors.add(pair.row() + "," + pair.col());
            // System.out.println("Neighbor: " + pair.row() + "," + pair.col()); // Debug print
        }
        assertEquals(expectedNeighbors, actualNeighbors, "Neighbors for (1,1) in 3x4 grid are incorrect.");
    }

    @Test
    void getNearestNeighborsArray_CornerWrapAround() {
        // For a 3x4 board, test top-left corner (0,0)
        WMPingPongArray.RCPair[] neighbors = ppa3x4.getNearestNeighborsArray(0, 0);
        assertEquals(8, neighbors.length);

        Set<String> expectedNeighbors = Set.of(
                "2,3", "2,0", "2,1", // Top row (prevRow=2, c=3,0,1)
                "0,1",             // Mid right (r=0, c=1)
                "1,1", "1,0", "1,3", // Bottom row (nextRow=1, c=1,0,3)
                "0,3"              // Mid left (r=0, c=3)
        );

        Set<String> actualNeighbors = new HashSet<>();
        for(WMPingPongArray.RCPair pair : neighbors) {
            actualNeighbors.add(pair.row() + "," + pair.col());
            // System.out.println("Corner Neighbor: " + pair.row() + "," + pair.col()); // Debug print
        }
        assertEquals(expectedNeighbors, actualNeighbors, "Neighbors for (0,0) wrap-around in 3x4 grid are incorrect.");
    }

    @Test
    void getNNSum() {
        // Setup a predictable liveArray
        WMPingPongArray ppaSumTest = new WMPingPongArray(3, 3, 0, 1); // Doesn't matter random range
        // Set specific values in nextArray, then swap to liveArray
        int val = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                ppaSumTest.setCell(r, c, val++); // 0, 1, 2 / 3, 4, 5 / 6, 7, 8
            }
        }
        ppaSumTest.swapLiveAndNext(); // liveArray now holds 0..8

        // Calculate sum for center cell (1,1), whose value is 4
        // Neighbors are: (0,0)=0, (0,1)=1, (0,2)=2, (1,2)=5, (2,2)=8, (2,1)=7, (2,0)=6, (1,0)=3
        int expectedSum = 0 + 1 + 2 + 5 + 8 + 7 + 6 + 3;
        int actualSum = ppaSumTest.getNNSum(1, 1);

        assertEquals(expectedSum, actualSum, "Nearest neighbor sum for center cell (1,1) is incorrect.");

        // Calculate sum for corner cell (0,0), whose value is 0
        // Neighbors (with wrap): (2,2)=8, (2,0)=6, (2,1)=7, (0,1)=1, (1,1)=4, (1,0)=3, (1,2)=5, (0,2)=2
        int expectedCornerSum = 8 + 6 + 7 + 1 + 4 + 3 + 5 + 2;
        int actualCornerSum = ppaSumTest.getNNSum(0, 0);

        assertEquals(expectedCornerSum, actualCornerSum, "Nearest neighbor sum for corner cell (0,0) with wrap-around is incorrect.");
    }

    @Test
    void updateToNearestNNSum() {
        // Setup a predictable liveArray
        WMPingPongArray ppaUpdateTest = new WMPingPongArray(3, 3, 0, 1);
        int val = 1; // Use 1-based values for simplicity
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                ppaUpdateTest.setCell(r, c, val++); // 1..9
            }
        }
        ppaUpdateTest.swapLiveAndNext(); // liveArray now holds 1..9

        // Perform the update (reads liveArray, writes to nextArray)
        ppaUpdateTest.updateToNearestNNSum();

        // Swap to bring the results into liveArray for checking
        ppaUpdateTest.swapLiveAndNext();
        int[][] result = ppaUpdateTest.getArray();

        // Expected sums (calculated manually based on 1..9 grid with wrap-around)
        // Center (1,1): Neighbors are 1,2,3, 6,9,8, 7,4. Sum = 40.
        // Corner (0,0): Neighbors are 9,7,8, 2,5,4, 6,3. Sum = 44.
        // ... calculate others if needed, or just check a few key cells
        int expectedCenterSum = 1+2+3+6+9+8+7+4; // Sum of neighbors of 5
        int expectedCornerSum = 9+7+8+2+5+4+6+3; // Sum of neighbors of 1 (wrap around)

        assertEquals(expectedCenterSum, result[1][1], "Updated value for center cell (1,1) should be the sum of its neighbors.");
        assertEquals(expectedCornerSum, result[0][0], "Updated value for corner cell (0,0) should be the sum of its neighbors.");
        // Can add more assertions for other cells
    }
}