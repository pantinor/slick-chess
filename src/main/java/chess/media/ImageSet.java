package chess.media;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import chess.core.PieceType;

public class ImageSet implements Images {

	private Image whitePawn;
	private Image whiteKnight;
	private Image whiteBishop;
	private Image whiteRook;
	private Image whiteQueen;
	private Image whiteKing;

	private Image blackPawn;
	private Image blackKnight;
	private Image blackBishop;
	private Image blackRook;
	private Image blackQueen;
	private Image blackKing;

	public ImageSet() throws SlickException {
		setAnimations();
	}

	public void setAnimations() throws SlickException {
		
		SpriteSheet sheet = new SpriteSheet("icon-sprites.png", 96, 96);

		for (PieceType type : PieceType.values()) {
			setImage(init(sheet, type, true), type, true);
			setImage(init(sheet, type, false), type, false);
		}

	}

	public Image init(SpriteSheet sheet, PieceType type, boolean white) {

		Image image = null;

		switch (type) {
		case PAWN:
			if (white)
				image = sheet.getSprite(4, 1);
			else {
				image = sheet.getSprite(4, 0);
			}
			break;
		case KNIGHT:
			if (white)
				image = sheet.getSprite(5, 1);
			else {
				image = sheet.getSprite(5, 0);
			}

			break;
		case BISHOP:
			if (white)
				image = sheet.getSprite(2, 1);
			else {
				image = sheet.getSprite(2, 0);
			}

			break;
		case ROOK:
			if (white)
				image = sheet.getSprite(3, 1);
			else {
				image = sheet.getSprite(3, 0);
			}

			break;
		case QUEEN:
			if (white)
				image = sheet.getSprite(1, 1);
			else {
				image = sheet.getSprite(1, 0);
			}

			break;
		case KING:
			if (white)
				image = sheet.getSprite(0, 1);
			else {
				image = sheet.getSprite(0, 0);
			}

			break;
		}

		return image;
	}

	public void setImage(Image image, PieceType type, boolean white) {
		switch (type) {
		case PAWN:
			if (white)
				whitePawn = image;
			else
				blackPawn = image;
			break;
		case KNIGHT:
			if (white)
				whiteKnight = image;
			else
				blackKnight = image;
			break;
		case BISHOP:
			if (white)
				whiteBishop = image;
			else
				blackBishop = image;
			break;
		case ROOK:
			if (white)
				whiteRook = image;
			else
				blackRook = image;
			break;
		case QUEEN:
			if (white)
				whiteQueen = image;
			else
				blackQueen = image;
			break;
		case KING:
			if (white)
				whiteKing = image;
			else
				blackKing = image;
			break;
		}
	}

	public Image getImage(PieceType type, boolean white) {
		switch (type) {
		case PAWN:
			if (white)
				return whitePawn;
			else
				return blackPawn;
		case KNIGHT:
			if (white)
				return whiteKnight;
			else
				return blackKnight;
		case BISHOP:
			if (white)
				return whiteBishop;
			else
				return blackBishop;
		case ROOK:
			if (white)
				return whiteRook;
			else
				return blackRook;
		case QUEEN:
			if (white)
				return whiteQueen;
			else
				return blackQueen;
		case KING:
			if (white)
				return whiteKing;
			else
				return blackKing;
		}
		return null;
	}

	public Image getCurrentFrame(PieceType type, boolean white, Direction dir) {
		Image frame = getImage(type, white);
		return frame;
	}

	public Animation getCurrentAnimation(PieceType type, boolean white, Direction dir) {
		return null;
	}

}
