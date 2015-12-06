package chess.properties;

import java.util.prefs.*;

import org.newdawn.slick.geom.Vector2f;

import chess.SlickChess;
import chess.core.Coord;

public class ChessPreferences {

    private static Preferences prefs = Preferences.userNodeForPackage(SlickChess.class);

    public static void saveTurn(boolean turn) {
        prefs.putBoolean("turn", turn);
    }

    public static void saveWhiteKing(Coord king) {
        prefs.putInt("whiteKingX", king.x);
        prefs.putInt("whiteKingY", king.y);
    }

    public static void saveBlackKing(Coord king) {
        prefs.putInt("blackKingX", king.x);
        prefs.putInt("blackKingY", king.y);
    }

    public static Vector2f getPosition() {
        float[] coords = {prefs.getInt("xpos", 0), prefs.getInt("ypos", 0)};
        return new Vector2f(coords);
    }

    public static byte[][] getBoard() {
        byte[][] nboard = new byte[8][8];
        for (int i = 0; i < 8; i++) {
            nboard[i] = prefs.getByteArray("boardR" + i, new byte[8]);
        }
        return nboard;
    }

    public static Coord getWhiteKing() {
        return new Coord(prefs.getInt("whiteKingX", 0), prefs.getInt("whiteKingY", 0));
    }

    public static Coord getBlackKing() {
        return new Coord(prefs.getInt("blackKingX", 0), prefs.getInt("blackKingY", 0));
    }

    public static boolean getTurn() {
        return prefs.getBoolean("turn", true);
    }
}
