package pkgWMUtils;

import java.io.*;
import java.util.*;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WMPingPongArray {
    protected final int numRows;
    protected final int numCols;
    protected final int totalLength;

    private int[][] liveArray;
    private int[][] nextArray;

    public WMPingPongArray(int numRows, int numCols, int randMin, int randMax) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.totalLength = numRows * numCols;

        this.liveArray = new int[numRows][numCols];
        this.nextArray = new int[numRows][numCols];

        Random rand = new Random();

        for (int row = 0; row < numRows; row++) {

            for (int col = 0; col < numCols; col++) {
                nextArray[row][col] = rand.nextInt(randMin, randMax);
            } // for (int col = 0; col < numCols; col++)

        } // for (int row = 0; row < numRows; row++)
    } // public WMPingPongArray(int numRows, int numCols, int randMin, int randMax)

    public WMPingPongArray(String dataFileName) {
        int[][] readArray = readFile(dataFileName);

        if (readArray == null) {
            System.out.println("File not read correct");
            System.exit(0);
        }

        this.numRows = readArray.length;
        this.numCols = readArray[0].length;
        this.totalLength = numRows * numCols;

        this.liveArray = new int[numRows][numCols];
        this.nextArray = new int[numRows][numCols];

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                nextArray[row][col] = readArray[row][col];
            }
        }

        swapLiveAndNext();
    } // public WMPingPongArray(String dataFileName)

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void printArray() {
        for (int row = 0; row < numRows; row++) {
            System.out.print(row + "  ");

            for (int col = 0; col < numCols; col++) {
                System.out.print(liveArray[row][col] + " ");
            } // for (int col = 0; col < numCols; col++)

            System.out.println();
        } // for (int row = 0; row < numRows; row++)
    } // public void printArray

    public void swapLiveAndNext() {
        int[][] temp = liveArray;
        liveArray = nextArray;
        nextArray = temp;
    } // public void swapLiveAndNext

    public void randomizeViaFisherYatesKnuth() {
        Random randInt = new Random();
        int[][] tempArray = getArray();

        for (int index = 0; index < totalLength - 1; index++) {
            int randIndex = randInt.nextInt(index, totalLength); // Pick a random index >= index

            // Calculates initial row/col and random row/col
            int row1 = index / numCols;
            int col1 = index % numCols;
            int row2 = randIndex / numCols;
            int col2 = randIndex % numCols;

            // Swap elements in liveArray itself
            int temp = tempArray[row1][col1];
            tempArray[row1][col1] = tempArray[row2][col2];
            tempArray[row2][col2] = temp;

        } // for (int index = 0; index < totalLength; index++)
        nextArray = tempArray;
    } // public void randomizeViaFisherYatesKnuth


    public void randomizeInRange() {
        int[][] tempArray = new int[numRows][numCols];

        // Initialize Array to 0 to (totalLength - 1) in order
        for (int index = 0; index < totalLength; index++) {
            int row = index / numCols;
            int col = index % numCols;

            tempArray[row][col] = index;
        } // for (int index = 0; index < totalLength; index++)

        nextArray = tempArray; // Only can write to nextArray
        swapLiveAndNext(); // randomizeViaFisherYatesKnuth reads from liveArray so swap here
        randomizeViaFisherYatesKnuth(); // ends this putting randomized array in nextArray

    } // public void randomizeInRange()

    public void save(String dataFileName) {
        fileWriter(dataFileName);
    }

    public void save() {
        String defaultSaveFile = "ppa_data.txt";
        fileWriter(defaultSaveFile);
    }

    private void fileWriter(String dataFileName) {
        int defaultInt = 99;
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(dataFileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        writer.println(defaultInt);
        writer.println(numRows + " " + numCols);

        for (int row = 0; row < numRows; row++) {
            String rowContents = "";
            rowContents += (row + "  ");

            for (int col = 0; col < numCols; col++) {
                rowContents += liveArray[row][col];
                rowContents += " ";
            } // for (int col = 0; col < numCols; col++)

            writer.println(rowContents);
        } // for (int row = 0; row < numRows; row++)

        writer.close();
    }

    public static WMPingPongArray loadFile(String dataFileName) {
        int[][] array = readFile(dataFileName);
        int defaultMin = 0;
        int defaultMax = 1;

        if (array == null) {
            System.out.println("File not read correct");
            System.exit(0);
        }

        WMPingPongArray newBoard = new WMPingPongArray(array.length, array[0].length, defaultMin, defaultMax);

        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                newBoard.setCell(row, col, array[row][col]);
            }
        }

        return newBoard;

    } // public void loadFile(String dataFileName)

    protected static int[][] readFile(String dataFileName) {
        try {
            Scanner scanner = new Scanner(new File(dataFileName));

            // Get default value
            int defaultValue = Integer.parseInt(scanner.nextLine());

            // Get dimensions
            String[] arrayDimensions = scanner.nextLine().split(" ");
            int rows = Integer.parseInt(arrayDimensions[0]);
            int cols = Integer.parseInt(arrayDimensions[1]);

            int[][] array = new int[rows][cols];

            for (int i = 0; i < rows; i++) {

                for (int j = 0; j < cols; j++) {
                    array[i][j] = defaultValue;
                }
            }

            int row = -1;

            while (scanner.hasNextLine() && row < rows) {

                Scanner lineReader = new Scanner(scanner.nextLine());
                row = lineReader.nextInt();
                int col = 0;

                while (lineReader.hasNextInt() && col < cols) {
                    int value = lineReader.nextInt();

                    array[row][col] = value;

                    col++;
                } // while (lineReader.hasNextInt() && col < cols)

                row++;
                lineReader.close();
            } // while (scanner.hasNextLine() && row < rows)

            scanner.close();

            return array;
        } // try
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int[][] getArray() {
        int[][] cloneArray = new int[numRows][numCols];

        for (int row = 0; row < numRows; row++) {

            for (int col = 0; col < numCols; col++) {
                cloneArray[row][col] = liveArray[row][col];
            }

        } // for (int row = 0; row < numRows; row++)
        return cloneArray;
    } // public int[][] getArray()

    public void copyToNextArray() {
        nextArray = getArray();
    }

    public void setCell(int row, int col, int newValue) {
        nextArray[row][col] = newValue;
    }

    private record RCPair (int row, int col) { } // private record RCPair(...)

    private RCPair[] getNearestNeighborsArray(int orgRow, int orgCol) {
        int numOfNN = 8;
        RCPair[] nearestNeighbors = new RCPair[numOfNN];

        // Base rows and cols
        int nextCol = (orgCol + 1) % numCols;
        int nextRow = (orgRow + 1) % numRows;
        int prevCol = (numCols + orgCol - 1) % numCols;
        int prevRow = (numRows + orgRow - 1) % numRows;

        // All Nearest Neighbors
        nearestNeighbors[0] = new RCPair(prevRow, prevCol); // Top Left
        nearestNeighbors[1] = new RCPair(prevRow, orgCol); // Top Mid
        nearestNeighbors[2] = new RCPair(prevRow, nextCol); // Top Right
        nearestNeighbors[3] = new RCPair(orgRow, nextCol); // Mid Right
        nearestNeighbors[4] = new RCPair(nextRow, nextCol); // Bottom Right
        nearestNeighbors[5] = new RCPair(nextRow, orgCol); // Bottom Mid
        nearestNeighbors[6] = new RCPair(nextRow, prevCol); // Bottom Left
        nearestNeighbors[7] = new RCPair(orgRow, prevCol); // Mid Left

        // Return list of nearest neighbor row/col pairs
        return nearestNeighbors;

    } // private RCPair[] getNearestNeighborsArray(int orgRow, int orgCol)

    private int getNNSum(RCPair[] nearestNeighbor, int[][] tempArray) {
        int sum = 0;

        for (int index = 0; index < nearestNeighbor.length; index++) {

            RCPair pair = nearestNeighbor[index];
            int col = pair.col;
            int row = pair.row;

            sum += tempArray[row][col];
        } // for (int index = 0; index < nearestNeighbor.length; index++)

        return sum;
    } // private int getNNSum(RCPair[] nearestNeighbor, int[][] tempArray)

    public void updateToNearestNNSum() {
        int[][] tempArray = getArray();

        for (int row = 0; row < numRows; row++) {

            for (int col = 0; col < numCols; col++) {
                RCPair[] pairs = getNearestNeighborsArray(row, col);
                int sum = getNNSum(pairs, tempArray);

                nextArray[row][col] = sum;
            } // for (int col = 0; col < numCols; col++)

        } // for (int row = 0; row < numRows; row++)
    } // public void updateToNearestNNSum()
}