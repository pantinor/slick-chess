package chess.core;

import java.io.Serializable;
import chess.core.PieceType;


/**
 * A chess board layout containing all piece positions
 */
public class Board implements Constants, Serializable {

	private static final long serialVersionUID = 1L;
	
	public Piece[][] pieces; // the chess board is an 8 by 8 array of Pieces
	public Coord whiteKing; // coordinates of white king
	public Coord blackKing; // coordinates of black king
	
	public Block[][] blocks;//8 by 8 array of Blocks for determining mouse over positons

	public boolean turn = true;

	public PieceCounts pieceCounts;
	
	public Progress progress = new Progress();
	public String think = "";

	public Board() {
		initDefaultBoard();
		pieceCounts = new PieceCounts(this);
	}

	public void movePiece(Move m) {
		m.perform(this);
		pieceCounts.refreshPieceCount();
	}
	
	/**
	 * Initialize the block shapes
	 */
	public void initBlocks(int originX, int originY, int cellWidth, int cellHeight) {
		
		blocks = new Block[8][8];

		for (int c = 0; c < 8; c++) {
			for (int r = 0; r < 8; r++) {
				Block block = new Block(originX + c * cellWidth, originY + r * cellHeight, cellWidth, cellHeight);
				blocks[c][r] = block;
			}
		}
	}

	/**
	 * Initialize a default chess board with the default setup of pieces
	 */
	public void initDefaultBoard() {
		pieces = new Piece[8][8];

		// Pawns
		for (int c = 0; c < 8; c++) {
			pieces[c][1] = new Piece(PieceType.PAWN, true); // White
			pieces[c][6] = new Piece(PieceType.PAWN, false); // Black
		}
		
		// Other White pieces
		pieces[2][0] = pieces[5][0] = new Piece(PieceType.BISHOP, true);
		pieces[1][0] = pieces[6][0] = new Piece(PieceType.KNIGHT, true);
		pieces[0][0] = pieces[7][0] = new Piece(PieceType.ROOK, true);
		pieces[3][0] = new Piece(PieceType.QUEEN, true);
		pieces[4][0] = new Piece(PieceType.KING, true);
		
		// Other Black pieces
		pieces[2][7] = pieces[5][7] = new Piece(PieceType.BISHOP, false);
		pieces[1][7] = pieces[6][7] = new Piece(PieceType.KNIGHT, false);
		pieces[0][7] = pieces[7][7] = new Piece(PieceType.ROOK, false);
		pieces[3][7] = new Piece(PieceType.QUEEN, false);
		pieces[4][7] = new Piece(PieceType.KING, false);

		// Keep track of king coordinates
		whiteKing = new Coord(4, 0);
		blackKing = new Coord(4, 7);

		turn = true; // White's turn

	}

	
	public class Progress {
		int max = Integer.MAX_VALUE;
		int value = 0;
		public int getMax() {
			return max;
		}
		public int getValue() {
			return value;
		}
		public void setMax(int max) {
			this.max = max;
		}
		public void setValue(int value) {
			this.value = value;
		}
	}

}
