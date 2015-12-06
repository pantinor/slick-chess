package chess.core;

import chess.SlickChess;
import chess.algorithms.*;

/**
 * Computer Player
 */
public class Computer extends Thread {

    private MoveAlgorithm algorithm;
    private ChessGame game;

    Computer(ChessGame game) {
        setPriority(Thread.MIN_PRIORITY);
        setDaemon(true);
        this.game = game;
    }

    Computer(ChessGame game, MoveAlgorithm algorithm) {
        setPriority(Thread.MIN_PRIORITY);
        setDaemon(true);
        this.game = game;
        this.algorithm = algorithm;
        this.algorithm.setGame(game);
    }

    public void setAlgorithm(MoveAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void run() {

        if (algorithm == null) {
            algorithm = new AlfaBeta(game);
        } else {
            algorithm.setGame(game);
        }

        game.movePiece(algorithm.reply(game.board.turn));

        SlickChess.chessBoard.getVirtualBoard().configureSlide();

    }
}
