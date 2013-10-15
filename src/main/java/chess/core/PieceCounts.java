package chess.core;

import java.io.Serializable;

/**
 * PieceCounts is used by ChessGame. PieceCounts keeps track of the number of
 * pieces: for each player and for each type of piece.
 */
public class PieceCounts implements Constants, Serializable {

	private static final long serialVersionUID = 1L;
	
	public int whiteCount;
	public int blackCount;

	public int whitePawns;
	public int blackPawns;
	public int whiteKnights;
	public int blackKnights;
	public int whiteBishops;
	public int blackBishops;
	public int whiteRooks;
	public int blackRooks;
	public int whiteQueens;
	public int blackQueens;
	public int whiteKings;
	public int blackKings;

	private Board board;

	public PieceCounts(ChessGame game) {
		this.board = game.board;
	}

	public PieceCounts(Board board) {
		this.board = board;
	}

	public void refreshPieceCount() {
		defaultValues();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (board.pieces[row][col] != null) {
					inc(board.pieces[row][col].type, board.pieces[row][col].white);
				}
			}
		}
	}

	private void defaultValues() {
		whiteCount = blackCount = 0;
		whitePawns = blackPawns = 0;
		whiteKnights = blackKnights = 0;
		whiteBishops = blackBishops = 0;
		whiteRooks = blackRooks = 0;
		whiteQueens = blackQueens = 0;
		whiteKings = blackKings = 0;
	}

	private void inc(PieceType type, boolean white) {
		int inc = 1;
		if (white) {
			whiteCount += inc;
			switch (type) {
			case PAWN:
				whitePawns += inc;
				break;
			case KNIGHT:
				whiteKnights += inc;
				break;
			case BISHOP:
				whiteBishops += inc;
				break;
			case ROOK:
				whiteRooks += inc;
				break;
			case QUEEN:
				whiteQueens += inc;
				break;
			case KING:
				whiteKings += inc;
				break;
			}
		} else {
			blackCount += inc;
			switch (type) {
			case PAWN:
				blackPawns += inc;
				break;
			case KNIGHT:
				blackKnights += inc;
				break;
			case BISHOP:
				blackBishops += inc;
				break;
			case ROOK:
				blackRooks += inc;
				break;
			case QUEEN:
				blackQueens += inc;
				break;
			case KING:
				blackKings += inc;
				break;
			}
		}
	}

	/**
	 * Get the count for a piece type and white
	 * 
	 * @param type
	 * @param white
	 * @return
	 */
	public int getCount(PieceType type, boolean white) {
		if (white) {
			switch (type) {
			case PAWN:
				return whitePawns;
			case KNIGHT:
				return whiteKnights;
			case BISHOP:
				return whiteBishops;
			case ROOK:
				return whiteRooks;
			case QUEEN:
				return whiteQueens;
			case KING:
				return whiteKings;
			}
		} else {
			switch (type) {
			case PAWN:
				return blackPawns;
			case KNIGHT:
				return blackKnights;
			case BISHOP:
				return blackBishops;
			case ROOK:
				return blackRooks;
			case QUEEN:
				return blackQueens;
			case KING:
				return blackKings;
			}
		}
		return 0;
	}

}