package chess.media;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import chess.core.Constants;
import chess.core.PieceType;

public final class BoardMedia implements Constants {

    private Images animationSet;

    public enum Type {

        IMAGE, SPRITES, BAM
    };

    public BoardMedia(Type type) {
        try {
            switch (type) {

                case IMAGE:
                    animationSet = new ImageSet();
                    break;
                case SPRITES:
                    animationSet = new SpriteAnimationSet();
                    break;
                case BAM:
                    animationSet = new PieceAnimationSet();
                    break;
            }

        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public Images getAnimationSet() {
        return animationSet;
    }

    public Image getCurrentFrame(PieceType type, boolean white, Direction dir) {
        Image frame = animationSet.getCurrentFrame(type, white, dir);
        return frame;
    }

    public Animation getCurrentAnimation(PieceType type, boolean white, Direction dir) {
        Animation anim = animationSet.getCurrentAnimation(type, white, dir);
        return anim;
    }

}
