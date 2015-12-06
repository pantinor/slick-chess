package chess.core;

import java.io.Serializable;

public class Coord implements Constants, Serializable {

    private static final long serialVersionUID = 1L;

    public byte x; // Column
    public byte y; // Row

    public Coord(int x, int y) {
        setCoord(x, y);
    }

    public void setCoord(int x, int y) {
        this.x = (byte) x;
        this.y = (byte) y;
    }

    public String toString() {
        return COORD_X[x] + (y + 1);
    }
}
