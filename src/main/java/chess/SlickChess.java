package chess;

import java.awt.Font;

import org.apache.log4j.xml.DOMConfigurator;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.RoundedRectangle;

import chess.board.ChessBoard;
import chess.board.ChessBoardMain;
import chess.board.PiecesPanel;
import chess.core.ChessGame;
import chess.core.VirtualClock;

public class SlickChess extends BasicGame {

    public static ChessBoard chessBoard;
    public static ChessGame currentGame;
    public static PiecesPanel piecesPanel;

    public static TrueTypeFont font14Bold;
    public static TrueTypeFont font12;
    public static TrueTypeFont font10;

    public static void main(String[] args) {

        try {

            DOMConfigurator.configure("src/main/resources/logging.xml");

            SlickChess sc = new SlickChess();
            AppGameContainer container = new AppGameContainer(sc);
            //container.setDisplayMode(container.getScreenWidth(), container.getScreenHeight(), false);
            container.setDisplayMode(1200, 900, false);
            container.setShowFPS(false);
            container.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public SlickChess() {
        super("SlickChess");
    }

    @Override
    public void init(GameContainer container) throws SlickException {

        Image cursor = new Image("cursor.png");
        container.setMouseCursor(cursor.getScaledCopy(32, 32), 0, 0);

        font14Bold = new TrueTypeFont(new Font("Verdana", Font.BOLD, 14), false);
        font12 = new TrueTypeFont(new Font("Verdana", Font.PLAIN, 12), false);
        font10 = new TrueTypeFont(new Font("Verdana", Font.PLAIN, 10), false);

        currentGame = new ChessGame();
        chessBoard = new ChessBoardMain(container, currentGame);
        piecesPanel = new PiecesPanel(chessBoard.getBoard(), chessBoard.getMedia());

    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        chessBoard.render(container, g);
        renderPlayerPanel(container, g);
        piecesPanel.render(container, g);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        chessBoard.update(container, delta);
        piecesPanel.update(container, delta);
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        super.mousePressed(button, x, y);
        chessBoard.getVirtualBoard().mousePressed(button, x, y);
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        super.mouseDragged(oldx, oldy, newx, newy);
        chessBoard.getVirtualBoard().mouseDragged(oldx, oldy, newx, newy);
    }

    @Override
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        super.mouseMoved(oldx, oldy, newx, newy);
        chessBoard.getVirtualBoard().mouseMoved(oldx, oldy, newx, newy);
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        super.mouseReleased(button, x, y);
        chessBoard.getVirtualBoard().mouseReleased(button, x, y);
    }

    class Player {

        String name;
        String time;
        boolean white;
        boolean active;
        boolean user;
        VirtualClock vc;

        Player(String name, boolean white, boolean active, boolean user, VirtualClock vc) {
            this.name = name;
            this.white = white;
            this.active = active;
            this.user = user;
            this.vc = vc;
            this.time = vc.getTimeToString();
        }
    }

    private void renderPlayerPanel(GameContainer container, Graphics g) {

        int panelSize = chessBoard.topLeftBoardX / 2;
        int spacing = 10;
        int num_players = 2;

        Player[] players = new Player[2];
        players[0] = new Player(currentGame.getWhiteName(), true, currentGame.getTurn(), currentGame.whiteParameters.isUser(), currentGame.getClock(true));
        players[1] = new Player(currentGame.getBlackName(), false, !currentGame.getTurn(), currentGame.blackParameters.isUser(), currentGame.getClock(false));

        int border = 10;

        RoundedRectangle r = new RoundedRectangle(
                container.getWidth() - panelSize - border, // x
                panelSize, // y
                panelSize + spacing * 2, // width
                panelSize * num_players + spacing * num_players + spacing, // height
                15); // radius on corner

        g.setFont(font12);
        g.setColor(Color.darkGray);
        g.fill(r);
        g.draw(r);

        border = 5;

        int inner_rect_y = panelSize + spacing;

        for (int i = 0; i < num_players; i++) {

            RoundedRectangle inner_rect = new RoundedRectangle(
                    container.getWidth() - panelSize - border, // x
                    inner_rect_y, // y
                    panelSize + spacing * 2, // width
                    panelSize + spacing, // height
                    6); // radius on corner

            inner_rect_y += panelSize + spacing;

            g.setColor(Color.darkGray.brighter());
            g.fill(inner_rect);
            g.draw(inner_rect);

        }

        int y = panelSize;

        for (Player p : players) {

            int x = container.getWidth() - panelSize + spacing;

            Color color = p.active ? Color.yellow : Color.gray;
            g.setColor(color);

            g.drawString(p.name, x + 3, y + spacing);
            g.drawString(p.user ? "(human)" : "(computer)", x + 3, y + spacing + spacing);
            g.drawString(p.time, x + 3, y + spacing * 4);

            y += panelSize + spacing;

        }
    }

}
