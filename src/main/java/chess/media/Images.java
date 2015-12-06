package chess.media;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import chess.core.PieceType;

public interface Images {

    public Image getCurrentFrame(PieceType type, boolean white, Direction dir);

    public Animation getCurrentAnimation(PieceType type, boolean white, Direction dir);

}
