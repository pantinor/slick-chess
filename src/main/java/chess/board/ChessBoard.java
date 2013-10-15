package chess.board;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import chess.core.*;
import chess.media.BoardMedia;

public abstract class ChessBoard implements Constants {
	
	public int xCell = 96;
	public int yCell = 96;
	
	public int boardWidth = xCell * 8;
	public int boardHeight = yCell * 8;

	public int topLeftBoardX = 0;
	public int topLeftBoardY = 0;

	protected GameContainer container = null;
	protected ChessGame game = null;
	protected Board board = null;
	protected BoardMedia boardMedia = null;
	protected ChessBoardVirtual virtual = null;

	public ChessBoard(GameContainer container, ChessGame game) {
		
		this.container = container;
		this.game = game;
		this.board = this.game.board;
		
		//this.boardMedia = new BoardMedia(BoardMedia.Type.SPRITES);
		this.boardMedia = new BoardMedia(BoardMedia.Type.IMAGE);
		
		topLeftBoardX = (container.getWidth() - boardWidth) / 2;
		topLeftBoardY = (container.getHeight() - boardHeight) / 2;

		this.board.initBlocks(topLeftBoardX, topLeftBoardY, xCell, yCell);
		
		this.virtual = new ChessBoardVirtual(game, this);
	}
	
	public ChessBoardVirtual getVirtualBoard() {
		return virtual;
	}
	
	public ChessGame getGame() {
		return game;
	}
	
	public BoardMedia getMedia() {
		return boardMedia;
	}
	
	public Board getBoard() {
		return board;
	}
	

	public abstract void render(GameContainer container, Graphics g) throws SlickException ;
	
	public abstract void update(GameContainer container, int delta) throws SlickException ;


}
