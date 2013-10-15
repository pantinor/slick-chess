package chess.board;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import chess.core.Board;
import chess.core.PieceCounts;
import chess.core.PieceType;
import chess.media.BoardMedia;
import chess.media.Direction;

public class PiecesPanel {

	private List<PieceLabel> whitePieces = new ArrayList<PieceLabel>();
	private List<PieceLabel> blackPieces = new ArrayList<PieceLabel>();

	BoardMedia boardMedia;
	Board board;

	public PiecesPanel(Board board, BoardMedia boardMedia) {
		this.board = board;
		this.boardMedia = boardMedia;
		buildPiecePanel();
	}
	
	private void buildPiecePanel() {

		// White piece panel
		for (int i = 0; i < 8; i++)
			whitePieces.add(new PieceLabel(PieceType.PAWN, true));
		whitePieces.add(new PieceLabel(PieceType.KNIGHT, true));
		whitePieces.add(new PieceLabel(PieceType.KNIGHT, true));
		whitePieces.add(new PieceLabel(PieceType.BISHOP, true));
		whitePieces.add(new PieceLabel(PieceType.BISHOP, true));
		whitePieces.add(new PieceLabel(PieceType.ROOK, true));
		whitePieces.add(new PieceLabel(PieceType.ROOK, true));
		whitePieces.add(new PieceLabel(PieceType.QUEEN, true));
		whitePieces.add(new PieceLabel(PieceType.KING, true));

		// Black piece panel
		for (int i = 0; i < 8; i++)
			blackPieces.add(new PieceLabel(PieceType.PAWN, false));
		blackPieces.add(new PieceLabel(PieceType.KNIGHT, false));
		blackPieces.add(new PieceLabel(PieceType.KNIGHT, false));
		blackPieces.add(new PieceLabel(PieceType.BISHOP, false));
		blackPieces.add(new PieceLabel(PieceType.BISHOP, false));
		blackPieces.add(new PieceLabel(PieceType.ROOK, false));
		blackPieces.add(new PieceLabel(PieceType.ROOK, false));
		blackPieces.add(new PieceLabel(PieceType.QUEEN, false));
		blackPieces.add(new PieceLabel(PieceType.KING, false));

	}

	class PieceLabel {

		private Image image;
		public PieceType type;
		public boolean white;
		private boolean active = true;

		public PieceLabel(PieceType type, boolean white) {

			this.type = type;
			this.white = white;

			image = boardMedia.getCurrentFrame(type, white, Direction.SOUTH).getScaledCopy(0.50f);
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public boolean isActive() {
			return active;
		}
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
		
		int leftside = 5;
		int y = 50;
		
		g.setColor(Color.red);
		
		for (PieceLabel label : whitePieces) {
			label.image.draw(leftside, y);
			g.drawString(label.active? "":"X", leftside, y);
			y += label.image.getHeight();
			
		}
		
		leftside = 60;
		y = 50;
		
		for (PieceLabel label : blackPieces) {
			label.image.draw(leftside, y);
			g.drawString(label.active? "":"X", leftside, y);
			y += label.image.getHeight();
		}

	}

	public void update(GameContainer container, int delta) throws SlickException {
		refreshPieceCount(board.pieceCounts);
	}

	public void refreshPieceCount(PieceCounts counts) {

		int whitePawns = counts.getCount(PieceType.PAWN, true);
		int blackPawns = counts.getCount(PieceType.PAWN, false);
		int whiteKnights = counts.getCount(PieceType.KNIGHT, true);
		int blackKnights = counts.getCount(PieceType.KNIGHT, false);
		int whiteBishops = counts.getCount(PieceType.BISHOP, true);
		int blackBishops = counts.getCount(PieceType.BISHOP, false);
		int whiteRooks = counts.getCount(PieceType.ROOK, true);
		int blackRooks = counts.getCount(PieceType.ROOK, false);
		int whiteQueens = counts.getCount(PieceType.QUEEN, true);
		int blackQueens = counts.getCount(PieceType.QUEEN, false);
		int whiteKings = counts.getCount(PieceType.KING, true);
		int blackKings = counts.getCount(PieceType.KING, false);

		for (PieceLabel label : whitePieces) {
			if (label.white) {
				switch (label.type) {
				case PAWN:
					if (whitePawns-- > 0)
						label.setActive(true);
					else
						label.setActive(false);
					break;
				case KNIGHT:
					if (whiteKnights-- > 0)
						label.setActive(true);
					else
						label.setActive(false);
					break;
				case BISHOP:
					if (whiteBishops-- > 0)
						label.setActive(true);
					else
						label.setActive(false);
					break;
				case ROOK:
					if (whiteRooks-- > 0)
						label.setActive(true);
					else
						label.setActive(false);
					break;
				case QUEEN:
					if (whiteQueens-- > 0)
						label.setActive(true);
					else
						label.setActive(false);
					break;
				case KING:
					if (whiteKings-- > 0)
						label.setActive(true);
					else
						label.setActive(false);
					break;
				}
			}
		}

		for (PieceLabel label : blackPieces) {
			if (!label.white) {
				switch (label.type) {
				case PAWN:
					if (blackPawns-- > 0)
						label.setActive(true);
					else
						label.setActive(false);
					break;
				case KNIGHT:
					if (blackKnights-- > 0)
						label.setActive(true);
					else
						label.setActive(false);
					break;
				case BISHOP:
					if (blackBishops-- > 0)
						label.setActive(true);
					else
						label.setActive(false);
					break;
				case ROOK:
					if (blackRooks-- > 0)
						label.setActive(true);
					else
						label.setActive(false);
					break;
				case QUEEN:
					if (blackQueens-- > 0)
						label.setActive(true);
					else
						label.setActive(false);
					break;
				case KING:
					if (blackKings-- > 0)
						label.setActive(true);
					else
						label.setActive(false);
					break;
				}
			}
		}

	}



}
