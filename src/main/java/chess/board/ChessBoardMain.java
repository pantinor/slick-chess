package chess.board;

import java.awt.Point;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.RoundedRectangle;

import chess.SlickChess;
import chess.algorithms.AlfaBeta;
import chess.algorithms.MoveAlgorithm;
import chess.core.Block;
import chess.core.ChessGame;
import chess.core.Move;
import chess.core.Piece;
import chess.media.Direction;

/**
 * ChessBoardMain is the main and large ChessBoard. This ChessBoard is what the
 * ChessBoardVirtual is based on.
 */
public class ChessBoardMain extends ChessBoard {

//	private static final int ANIM_INTERVAL = 100;
//	private int tilNextFrame = ANIM_INTERVAL;
    public ChessBoardMain(GameContainer container, ChessGame game) {
        super(container, game);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {

//		tilNextFrame -= delta;
//
//		if (tilNextFrame < 0) {
//			tilNextFrame += ANIM_INTERVAL;
//		} else {
//			return;
//		}
        try {

            for (byte c = 0; c < 8; c++) {
                for (byte r = 0; r < 8; r++) {
                    if (board.pieces[c][r] != null) {
                        Block block = board.blocks[c][r];
                        Animation anim = boardMedia.getCurrentAnimation(board.pieces[c][r].type, board.pieces[c][r].white, block.getDirection());
                        if (anim != null) {
                            anim.update(delta);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {

        try {
            paintBoard(container, g);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (game != null) {
            MoveAlgorithm ma = new AlfaBeta(game);
            if (ma.checkMate()) {
                System.out.println("CHECKMATE");
            }
        }
    }

    public void paintBoard(GameContainer container, Graphics g) throws Exception {

        paintBorder(container, g);

        for (byte c = 0; c < 8; c++) {
            for (byte r = 0; r < 8; r++) {
                paintCell(container, g, c, r);
            }
        }

        paintActiveSpots(container, g);
        paintArrows(container, g);

        paintPieces(container, g);

    }

    public void paintCell(GameContainer container, Graphics g, byte c, byte r) throws Exception {

        if ((c + r) % 2 == 1) {
            g.setColor(new Color(80, 124, 224));
        } else {
            g.setColor(new Color(167, 191, 246));
        }

        g.fillRect(topLeftBoardX + c * xCell, topLeftBoardY + r * yCell, xCell, yCell);

        if (virtual.getMouseOn().x == c && virtual.getMouseOn().y == r) {
            if ((c + r) % 2 != 0) {
                g.setColor(new Color(140, 162, 181));
            } else {
                g.setColor(new Color(206, 214, 239));
            }

            g.fillRect(topLeftBoardX + c * xCell, topLeftBoardY + r * yCell, xCell, yCell);
        }

		//debugging text
//		Block block = board.blocks[c][r];
//		Piece piece = board.b[c][r];
//		g.setColor(Color.black);
//		g.setFont(font10);
//		g.drawString("x:"+block.getShape().getX()+" y:"+block.getShape().getY(), topLeftBoardX + c * xCell, topLeftBoardY + r * yCell);
//		g.drawString(""+ piece, topLeftBoardX + c * xCell, topLeftBoardY + 20 + r * yCell);
    }

    public void paintBorder(GameContainer container, Graphics g) throws Exception {

        int border = 60;

        RoundedRectangle r = new RoundedRectangle(
                container.getWidth() - boardWidth - topLeftBoardX - border,
                container.getHeight() - boardHeight - topLeftBoardY - border,
                boardWidth + border * 2,
                boardHeight + border * 2,
                15);

        g.setColor(Color.darkGray);
        g.fill(r);
        g.draw(r);

        border = 45;

        RoundedRectangle inner_rect = new RoundedRectangle(
                container.getWidth() - boardWidth - topLeftBoardX - border,
                container.getHeight() - boardHeight - topLeftBoardY - border,
                boardWidth + border * 2,
                boardHeight + border * 2,
                15);

        g.setColor(Color.darkGray.brighter());
        g.fill(inner_rect);
        g.draw(inner_rect);

        // draw legend
        g.setFont(SlickChess.font14Bold);
        g.setColor(Color.white);

        for (byte i = 0; i < 8; i++) {
            g.drawString(COORD_X[(i)], topLeftBoardX + i * xCell + xCell / 2 - 4, topLeftBoardY + boardHeight + 20);
            g.drawString("" + (i + 1), topLeftBoardX - 20, topLeftBoardY + boardHeight - i * yCell - yCell / 2 + 5);
        }

    }

    /**
     * This method is called by the paintBoard method to paint both animate
     * (still) and inanimate (sliding or dragging) pieces in the appropriate
     * board cells on the graphic
     */
    private void paintPieces(GameContainer container, Graphics g) throws Exception {

        for (byte c = 0; c < 8; c++) {
            for (byte r = 0; r < 8; r++) {

                Block block = board.blocks[c][r];
                Piece piece = board.pieces[c][r];

                if (piece != null) {

                    Image image = boardMedia.getCurrentFrame(piece.type, piece.white, block.getDirection());

                    if ((!virtual.isPieceDrag() || (virtual.getPieceSelectedCoord().y) != r || virtual.getPieceSelectedCoord().x != c)) {

                        if (!virtual.isPieceSliding() || c != virtual.getLastMove().x2 || r != virtual.getLastMove().y2) {

                            image.draw(topLeftBoardX + c * xCell, topLeftBoardY + r * yCell);

                        } else if (virtual.getLastMove().p != null) {

                            Image capImage = boardMedia.getCurrentFrame(virtual.getLastMove().p.type, virtual.getLastMove().p.white, Direction.SOUTH);
                            capImage.draw(topLeftBoardX + (virtual.getLastMove().x2) * xCell, topLeftBoardY + virtual.getLastMove().y2 * yCell);
                        }
                    }

                } else {

                    // Draw the sliding piece if there is one and if the sliding piece was originally in this cell
                    if (virtual.isPieceSliding() && game.getLastMove().x1 == c && game.getLastMove().y1 == r) {

                        Piece slidingPiece = board.pieces[game.getLastMove().x2][game.getLastMove().y2];
                        Image image = boardMedia.getCurrentFrame(slidingPiece.type, slidingPiece.white, Direction.SOUTH);
                        image.draw(topLeftBoardX + virtual.getSlidingPieceX(), topLeftBoardY + virtual.getSlidingPieceY());

                        // Continue the sliding progress (set up the next position)
                        virtual.continueSlide();
                    }
                }
            }
        }

        // Draw the piece being dragged, if there is one
        if (virtual.isPieceDrag()) {
            Image image = boardMedia.getCurrentFrame(virtual.getPieceSelected().type, virtual.getPieceSelected().white, Direction.SOUTH);
            image.draw((int) virtual.getDraggingPieceX(), (int) virtual.getDraggingPieceY());
        }
    }

    private void paintActiveSpots(GameContainer container, Graphics g) throws Exception {

        if (virtual == null) {
            throw new Exception("virtual is null.");
        }

        if (game == null) {
            throw new Exception("game is null.");
        }

        // Active spots
        if (virtual.isPieceClicked() || virtual.isPieceDrag()) {
            if (game.getActiveMoves() != null) {
                if (game.getActiveMoves().size() != 0) {

                    Move a = (Move) game.getActiveMoves().get(0);

                    for (int i = 0; i < game.getActiveMoves().size(); i++) {

                        a = (Move) game.getActiveMoves().get(i);

                        if ((a.x2 + a.y2) % 2 == 1) {
                            g.setColor(Color.green);
                        } else {
                            g.setColor(Color.green.darker());
                        }

                        g.fillOval(topLeftBoardX + (a.x2) * xCell + 5, topLeftBoardY + a.y2 * yCell + 5, xCell - 11, yCell - 11);

                    }
                }
            }
        }

    }

    /**
     * Paint arrows to indicate the last game move.
     */
    private void paintArrows(GameContainer con, Graphics g) {

        if (!virtual.isPieceSliding()) {
            if (virtual.getLastMove() != null) {
                Move m = virtual.getLastMove();
                Color color = virtual.getLastMove().p != null ? Color.red : Color.blue;
                drawArrow(con, g,
                        new Point(xCell / 2 + topLeftBoardX + m.x1 * xCell, yCell / 2 + topLeftBoardY + m.y1 * yCell),
                        new Point(xCell / 2 + topLeftBoardX + m.x2 * xCell, yCell / 2 + topLeftBoardY + m.y2 * yCell),
                        (int) (xCell * 0.10),
                        color);
            }
        }
    }

    /**
     * Construct and draw an arrow.
     *
     * @param g - the graphic to draw on
     * @param p1 - from this point
     * @param p2 - to this point
     * @param width - the arrow width
     * @param color - the arrow color
     */
    private void drawArrow(GameContainer con, Graphics g, Point p1, Point p2, int width, Color color) {

        int length = (int) Math.round(p1.distance(p2)) - xCell * 2 / 5;
        int tip = 2 * width;

        int[] x = {0, 0, length - tip, length - tip, length, length - tip, length - tip,};
        int[] y = {-width, width, width, tip, 0, -tip, -width,};

        Polygon poly = new Polygon();
        for (int i = 0; i < x.length; i++) {
            poly.addPoint(x[i], y[i]);
        }

        double theta_rad = Math.atan2(p2.y - p1.y, p2.x - p1.x);
        double angle = (theta_rad / Math.PI * 180) + (theta_rad > 0 ? 0 : 360);

        g.rotate(p1.x, p1.y, (float) angle);

        g.translate(p1.x, p1.y);

        g.setColor(color);
        g.fill(poly);

        g.setColor(Color.black);
        g.draw(poly);

        g.resetTransform();
    }

}
