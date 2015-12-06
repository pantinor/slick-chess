package chess.core;

import java.io.Serializable;

public class Piece implements Constants, Serializable {

    private static final long serialVersionUID = 1L;

    public PieceType type;
    public boolean white;

    public Piece(PieceType t, boolean w) {
        white = w;
        setType(t);
    }

    public void setType(PieceType type) {
        if (this.type != type) {
            this.type = type;
        }
    }

    public int getCost() {
        return (white ? 1 : -1) * type.cost;
    }

    @Override
    public String toString() {
        return String.format("%s %s", type, white);
    }

}
