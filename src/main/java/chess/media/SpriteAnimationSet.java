package chess.media;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import chess.core.PieceType;

public class SpriteAnimationSet implements Images {

    private AnimationSet whitePawn;
    private AnimationSet whiteKnight;
    private AnimationSet whiteBishop;
    private AnimationSet whiteRook;
    private AnimationSet whiteQueen;
    private AnimationSet whiteKing;

    private AnimationSet blackPawn;
    private AnimationSet blackKnight;
    private AnimationSet blackBishop;
    private AnimationSet blackRook;
    private AnimationSet blackQueen;
    private AnimationSet blackKing;

    public SpriteAnimationSet() throws SlickException {
        setAnimations();
    }

    public void setAnimations() {

        for (PieceType type : PieceType.values()) {
            setStore(initStore(type, true), type, true);
            setStore(initStore(type, false), type, false);
        }

    }

    public AnimationSet initStore(PieceType type, boolean white) {

        String sheetName = "";
        AnimationSet store = null;

        switch (type) {
            case PAWN:
                if (white) {
                    sheetName = "orc-standing.png";
                } else {
                    sheetName = "fairy-flying.png";
                }
                break;
            case KNIGHT:
                if (white) {
                    sheetName = "blue-knight-talking.png";
                } else {
                    sheetName = "black-knight-talking.png";
                }

                break;
            case BISHOP:
                if (white) {
                    sheetName = "magier-standing.png";
                } else {
                    sheetName = "ghost-standing.png";
                }

                break;
            case ROOK:
                if (white) {
                    sheetName = "red-sword-standing.png";
                } else {
                    sheetName = "devil-standing.png";
                }

                break;
            case QUEEN:
                if (white) {
                    sheetName = "queen-standing.png";
                } else {
                    sheetName = "witch-talking.png";
                }

                break;
            case KING:
                if (white) {
                    sheetName = "barbarian-king-standing.png";
                } else {
                    sheetName = "death-standing.png";
                }

                break;
        }

        try {
            store = new AnimationSet(sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return store;
    }

    public void setStore(AnimationSet store, PieceType type, boolean white) {
        switch (type) {
            case PAWN:
                if (white) {
                    whitePawn = store;
                } else {
                    blackPawn = store;
                }
                break;
            case KNIGHT:
                if (white) {
                    whiteKnight = store;
                } else {
                    blackKnight = store;
                }
                break;
            case BISHOP:
                if (white) {
                    whiteBishop = store;
                } else {
                    blackBishop = store;
                }
                break;
            case ROOK:
                if (white) {
                    whiteRook = store;
                } else {
                    blackRook = store;
                }
                break;
            case QUEEN:
                if (white) {
                    whiteQueen = store;
                } else {
                    blackQueen = store;
                }
                break;
            case KING:
                if (white) {
                    whiteKing = store;
                } else {
                    blackKing = store;
                }
                break;
        }
    }

    public AnimationSet getStore(PieceType type, boolean white) {
        switch (type) {
            case PAWN:
                if (white) {
                    return whitePawn;
                } else {
                    return blackPawn;
                }
            case KNIGHT:
                if (white) {
                    return whiteKnight;
                } else {
                    return blackKnight;
                }
            case BISHOP:
                if (white) {
                    return whiteBishop;
                } else {
                    return blackBishop;
                }
            case ROOK:
                if (white) {
                    return whiteRook;
                } else {
                    return blackRook;
                }
            case QUEEN:
                if (white) {
                    return whiteQueen;
                } else {
                    return blackQueen;
                }
            case KING:
                if (white) {
                    return whiteKing;
                } else {
                    return blackKing;
                }
        }
        return null;
    }

    public Image getCurrentFrame(PieceType type, boolean white, Direction dir) {
        AnimationSet store = getStore(type, white);
        Image frame = store.getCurrentFrame(dir);
        return frame;
    }

    public Animation getCurrentAnimation(PieceType type, boolean white, Direction dir) {
        AnimationSet store = getStore(type, white);
        if (store == null) {
            return null;
        }
        Animation anim = store.getAnimation(dir);
        return anim;
    }

}
