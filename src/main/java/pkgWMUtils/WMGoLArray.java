package pkgWMUtils;

import static pkgWMUtils.WMSPOT.*;

public class WMGoLArray extends WMPingPongArray{

    public WMGoLArray(final int rows, final int cols) {
        super(rows, cols, 0, 1);
    }

    public WMGoLArray(int numRows, int numCols, int numLiveColumns) {
        super(numRows, numCols, 0, 1);
        super.swapLiveAndNext();
        addLiveCellsToNext(numLiveColumns);
        super.swapLiveAndNext();
        randomizeViaFisherYatesKnuth();
    }

    private void addLiveCellsToNext(int numLiveCells) {

        for (int index = 0; index < numLiveCells; index++) {
            int row = index / getNumRows();
            int col = index % getNumCols();

            super.setCell(row, col, ALIVE);
        } // for (int index = 0; index < numLiveCells; index++)
    } // private void addLiveCellsToNext(int numLiveCells)

    public WMGoLArray(final String myDataFile) {
        super(myDataFile);
    }

    public void onTickUpdate() {
        int nearestNeighbour;
        copyToNextArray();

        for (int row = 0; row < getNumRows(); row++) {
            for (int col = 0; col < getNumCols(); col++) {
                nearestNeighbour = getNNSum(row, col);

                if (nearestNeighbour == 3) {
                    setAlive(row, col);
                } else if (nearestNeighbour < 2 || nearestNeighbour > 3) {
                    setDead(row, col);
                }
            }
        }
    }

    public int liveCellCount() {
        int count = 0;
        int[][] liveArray = getArray();

        for (int row = 0; row < getNumRows(); row++) {

            for (int col = 0; col < getNumCols(); col++) {

                if (liveArray[row][col] == ALIVE) {
                    count++;
                }

            } // for (int col = 0; col < getNumCols(); col++)
        } // for (int row = 0; row < getNumRows(); row++)

        return count;
    }

    public void setCell(int row, int col, int newValue) {
        if (newValue == DEAD) {
            setDead(row, col);
        } else {
            setAlive(row, col);
        }
    }

    public void setAlive(int row, int col) {
        super.setCell(row, col, ALIVE);
    }

    public void setDead(int row, int col) {
        super.setCell(row, col, DEAD);
    }
}
