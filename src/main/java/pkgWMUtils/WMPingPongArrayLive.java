package pkgWMUtils;

public class WMPingPongArrayLive extends WMPingPongArray {
    final int DEAD = 0;
    final int ALIVE = 1;

    public WMPingPongArrayLive(int numRows, int numCols, int numLiveColumns) {
        super(numRows, numCols, 0, 1);
        super.swapLiveAndNext();
        addLiveCellsToNext(numLiveColumns);
        super.swapLiveAndNext();
        randomizeViaFisherYatesKnuth();
    }

    private void addLiveCellsToNext(int numLiveCells) {

        for (int index = 0; index < numLiveCells; index++) {
            int row = index / super.numRows;
            int col = index % super.numCols;

            super.setCell(row, col, ALIVE);
        } // for (int index = 0; index < numLiveCells; index++)
    } // private void addLiveCellsToNext(int numLiveCells)

    public int countLiveDegreeTwoNeighbors(int row, int col) {
        int[][] tempArray = super.getArray();
        int count = 0;

        int nextRow = (row + 1) % numRows; // 1
        int nextCol = (col + 1) % numCols; // 1
        int prevRow = (numRows + row - 1) % numRows; // 6
        int prevCol = (numCols + col - 1) % numCols; // 6
        int next2Row = (row + 2) % numRows;  // 2
        int next2Col = (col + 2) % numCols; // 2
        int prev2Row = (numRows + row - 2) % numRows;  // 5
        int prev2Col = (numCols + col - 2) % numCols; // 5

        count += tempArray[prev2Row][next2Col]; // 5,2
        count += tempArray[prev2Row][nextCol]; // 5,1
        count += tempArray[prev2Row][col]; // 5,0
        count += tempArray[prev2Row][prevCol];  // 5,6
        count += tempArray[prev2Row][prev2Col]; // 5,5
        count += tempArray[prevRow][prev2Col]; // 6,5
        count += tempArray[row][prev2Col]; // 0,5
        count += tempArray[nextRow][prev2Col]; // 1,5
        count += tempArray[next2Row][prev2Col]; // 2,5
        count += tempArray[next2Row][prevCol]; // 2,6
        count += tempArray[next2Row][col]; // 2,0
        count += tempArray[next2Row][nextCol]; // 2,1
        count += tempArray[next2Row][next2Col]; // 2,2
        count += tempArray[nextRow][next2Col]; // 1,2
        count += tempArray[row][next2Col]; // 0,2
        count += tempArray[prevRow][next2Col]; // 6,2

        return count;
    } // public int countLiveDegreeTwoNeighbors(int row, int col)
} // public class WMPingPongArrayLive extends WMPingPongArray