package pkgWMUtils;

public class WMGoLArray extends WMPingPongArray{

    public WMGoLArray(final int rows, final int cols) {
        super(rows, cols, 0, 1);
    }

    public WMGoLArray(int numRows, int numCols, int numAlive) {
        super(numRows, numCols, 0, 1);
    }

    /*
    public WMGoLArray(final String myDataFile) {

    }
    */

    public void onTickUpdate() {

    }

    public int liveCellCount() {
        return 0;
    }
}
