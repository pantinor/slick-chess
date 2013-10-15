package chess.board;

import chess.algorithms.MoveAlgorithm;
import chess.core.Block;
import chess.core.Board;
import chess.core.ChessGame;
import chess.core.Constants;
import chess.core.Coord;
import chess.core.Move;
import chess.core.Piece;
import chess.core.PieceType;
import chess.core.State;
import chess.media.Direction;

public class ChessBoardVirtual implements Constants {

	private double draggingPieceX = 0;
	private double draggingPieceY = 0;
	private int dragFromX = 0;
	private int dragFromY = 0;
	private boolean pieceDrag = false;
	private boolean pieceClicked = false;

	private double slidePieceLength;
	private double slidePieceAngle;
	private double slidePieceSpeed = 4;
	private boolean pieceSliding;

	private double slidingPieceX = 0;
	private double slidingPieceY = 0;

	private Coord mouseOn = new Coord(-1, -1);

	private Piece pieceSelected = new Piece(PieceType.KING, true);
	private Coord pieceSelectedCoord = new Coord(-1, -1);

	protected Board board;
	protected ChessGame game;
	protected ChessBoard chessBoard;


	public ChessBoardVirtual(ChessGame game, ChessBoard chessBoard) {
		
		this.chessBoard = chessBoard;
		this.game = game;
		this.board = game.board;
		this.pieceSliding = false;
		
	}


	public boolean isPieceDrag() {
		return pieceDrag;
	}

	public boolean isPieceClicked() {
		return pieceClicked;
	}

	public int getDraggingPieceX() {
		return (int) draggingPieceX;
	}

	public int getDraggingPieceY() {
		return (int) draggingPieceY;
	}

	public int getSlidingPieceX() {
		return (int) slidingPieceX;
	}

	public int getSlidingPieceY() {
		return (int) slidingPieceY;
	}

	public Coord getMouseOn() {
		return mouseOn;
	}

	public Piece getPieceSelected() {
		return pieceSelected;
	}

	public Coord getPieceSelectedCoord() {
		return pieceSelectedCoord;
	}

	public boolean isBoardFlipped() {
		if (game != null)
			return game.isBoardFlipped();
		else
			return false;
	}

	public boolean isPieceSliding() {
		return pieceSliding;
	}

	public Move getLastMove() {
		return game.getLastMove();
	}
	

	public void mousePressed(int button, int x, int y) {

		if (game != null && board != null) {
			if (!game.state.equals(State.WAITING)) {
				search: for (int c = 0; c < 8; c++) {
					for (int r = 0; r < 8; r++) {
						
						int bx = chessBoard.topLeftBoardX + c * chessBoard.xCell;
						int cx = chessBoard.topLeftBoardX + c * chessBoard.xCell + chessBoard.xCell;
						int dx = chessBoard.topLeftBoardY + r * chessBoard.yCell;
						int ex = chessBoard.topLeftBoardY + r * chessBoard.yCell + chessBoard.yCell;		
								
						if (bx <= x && x < cx && dx <= y && y < ex) {
							if (board.pieces[c][r] != null) {
								if ((game.getTurn() && board.pieces[c][r].white) || (!game.getTurn() && !board.pieces[c][r].white)) {

									pieceClicked = true;
									pieceSelected = board.pieces[c][r];
									pieceSelectedCoord.setCoord(c,r);

									updateActiveMoves();

									preparePieceDrag(x, y, new Coord(c, r));

									break search;
								}
							}
						}
					}
				}
			}
		}
	}


	public void mouseReleased(int button, int x, int y) {

		if ((pieceDrag || pieceClicked))
			search: for (int c = 0; c < 8; c++) {
				for (int r = 0; r < 8; r++) {
					
					int bx = chessBoard.topLeftBoardX + c * chessBoard.xCell;
					int cx = chessBoard.topLeftBoardX + c * chessBoard.xCell + chessBoard.xCell;
					int dx = chessBoard.topLeftBoardY + r * chessBoard.yCell;
					int ex = chessBoard.topLeftBoardY + r * chessBoard.yCell + chessBoard.yCell;		
							
					if (bx <= x && x < cx && dx <= y && y < ex) {
					
						if (game.canMove()) {
							searchMoves: if (game.getActiveMoves() != null) {
								for (int i = 0; i < game.getActiveMoves().size(); i++) {
									Move m = game.getActiveMoves().get(i);
									if (m.x2 == c &&  m.y2 == r) {
										game.movePiece(m);
										configureSlide();
										break searchMoves;
									}
								}
							}
						}

						if (c != pieceSelectedCoord.x || r !=  pieceSelectedCoord.y)
							pieceClicked = false;

						if (c == pieceSelectedCoord.x && r ==  pieceSelectedCoord.y)
							pieceClicked = true;

						pieceDrag = false;

						break search;
					}
				}
			}

		pieceDrag = false;
	}


	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (pieceDrag) {
			pieceClicked = false;
			draggingPieceX = newx - dragFromX; // set x coord for drag
			draggingPieceY = newy - dragFromY; // set y coord for drag

		}
		mouseOnBlock(newx, newy);
	}


	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
				
		//System.out.println("x="+newx + " y=" + newy);
			
		//set pieces facing direction relative to the mouse location
		for (int c = 0; c < 8; c++) {
			for (int r = 0; r < 8; r++) {
				//check if piece is in this block
				if (board.pieces[c][r] == null) 
					continue;
				
				Block b = board.blocks[c][r];
				b.setDirection(getDirectionRelativeMouse(newx, newy, b.getShape().getCenterX(), b.getShape().getCenterY()));
			}
		}

		mouseOnBlock(newx, newy);
	}
	
	private void mouseOnBlock(int x, int y) {
		for (int c = 0; c < 8; c++) {
			for (int r = 0; r < 8; r++) {
				if (board.blocks[c][r].isMouseOver(x, y)) {
					mouseOn.setCoord(c, r);
					return;
				}
			}
		}
		mouseOn.setCoord(-1, -1);
	}
	
	private Direction getDirectionRelativeMouse(float mouseX, float mouseY, float originX, float originY) {
		float ang = (float) Math.toDegrees(Math.atan2(mouseX - originX, originY - mouseY));
		if (ang < 0) {
			ang = 360 + ang;
		}
		ang = (ang + 90 + 22.5f) % 360;
		ang /= 45f;
		return Direction.forIndex((int) ang);
	}


	private void preparePieceDrag(int x, int y, Coord boardBlock) {
		pieceDrag = true;
		dragFromX = chessBoard.xCell / 2; // how far from left
		dragFromY = chessBoard.yCell / 2; // how far from top
		draggingPieceX = x - dragFromX; // set x coord for drag
		draggingPieceY = y - dragFromY; // set y coord for drag
	}

	@SuppressWarnings("unchecked")
	public void updateActiveMoves() {
		MoveAlgorithm ma = new MoveAlgorithm(board);
		game.setActiveMoves(ma.getRealAll(new Coord(pieceSelectedCoord.x, pieceSelectedCoord.y)));
	}

	public void continueSlide() {
		if (pieceSliding) {
			slidingPieceX = (slidingPieceX + slidePieceSpeed * Math.sin(Math.toRadians(slidePieceAngle)));
			slidingPieceY = (slidingPieceY - slidePieceSpeed * Math.cos(Math.toRadians(slidePieceAngle)));
			slidePieceLength -= slidePieceSpeed;
			if (slidePieceLength - slidePieceSpeed <= 0) {
				pieceSliding = false;
			}
		}
	}

	public void configureSlide() {

		slidingPieceX = game.getLastMove().x1 * chessBoard.xCell;
		slidingPieceY = game.getLastMove().y1 * chessBoard.yCell;
		
		int endX = game.getLastMove().x2 * chessBoard.xCell;
		int endY = game.getLastMove().y2 * chessBoard.yCell;
		
		slidePieceLength = Math.sqrt(Math.pow(Math.abs(slidingPieceX - endX), 2) + Math.pow(Math.abs(slidingPieceY - endY), 2));
		slidePieceAngle = -Math.toDegrees(Math.atan((slidingPieceX - endX) / (slidingPieceY - endY)));
		
		if (endY > slidingPieceY)
			slidePieceAngle += 180;

		pieceSliding = true;
	}

}
