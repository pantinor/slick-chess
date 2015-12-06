package chess.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import chess.algorithms.*;
import chess.properties.*;

/**
 * ChessGame contains all the necessary information for a chess game, such as
 * the chess board, game and player parameters, timers, history, etc.
 */
public class ChessGame implements Constants, Serializable {

    private static final long serialVersionUID = 1L;

    public Board board; // The chess board
    public Move lastMove; // The last move made

    // Game parameters
    private GameParameters gameParameters;

    // Player parameters
    public PlayerParameters whiteParameters;
    public PlayerParameters blackParameters;

    // Player virtual clocks
    private VirtualClock whiteClock;
    private VirtualClock blackClock;

    // The State of the chess game
    public State state;

    // Game history
    private List<Move> allMoves = new ArrayList<Move>();

    // Active moves (the valid moves for the piece selected)
    private List<Move> activeMoves = new ArrayList<Move>();

    private boolean flipBoard = false;

    public MoveAlgorithm algorithm; // For Computer

    public ChessGame() {
        setUpDefaults();
    }

    public void setAlgorithm(MoveAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    private void setUpDefaults() {
        state = State.INITIALIZED;

        algorithm = new AlfaBeta();

        // Parameters
        gameParameters = new GameParameters();
        whiteParameters = new PlayerParameters("White", true);
        blackParameters = new PlayerParameters("Black", false); //computer player

        // Player clocks
        whiteClock = new VirtualClock(this);
        blackClock = new VirtualClock(this);

        allMoves = new ArrayList<Move>();

        board = new Board();

        lastMove = null;
    }

    public boolean getTurn() {
        return board.turn;
    }

    public int getCount(PieceType type, boolean white) {
        return board.pieceCounts.getCount(type, white);
    }

    public int getWhitePieceCount() {
        return board.pieceCounts.whiteCount;
    }

    public int getBlackPieceCount() {
        return board.pieceCounts.blackCount;
    }

    public void pause() {
        if (!state.equals(State.INITIALIZED) && !state.equals(State.WAITING)) {
            state = State.PAUSED;
            whiteClock.stopClock();
            blackClock.stopClock();
        }
    }

    public void resume() {
        if (state.equals(State.PAUSED)) {
            state = State.PLAYING;
            if (board.turn) {
                whiteClock.startClock();
            } else {
                blackClock.startClock();
            }
        }
    }

    public VirtualClock getClock(boolean white) {
        if (white) {
            return whiteClock;
        } else {
            return blackClock;
        }
    }

    public void setActiveMoves(List<Move> active) {
        activeMoves = active;
    }

    public List<Move> getActiveMoves() {
        return activeMoves;
    }

    public boolean isBoardFlipped() {
        return flipBoard;
    }

    public void setFlipBoard(boolean flip) {
        flipBoard = flip;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void movePiece(Move m) {

        m.perform(board);

        allMoves.add(m); // Add the move to the notation list

        lastMove = m; // Save this move as the lastMove

        switchTurn(); // Switch player turns

    }

    /**
     * Switch player turns
     */
    private void switchTurn() {
        board.turn = board.turn ? false : true;

        board.pieceCounts.refreshPieceCount();

        // Start or stop the appropriate clock relative to the turn
        if (!state.equals(State.GAMEOVER)) {
            if (board.turn) { // white
                whiteClock.startClock();
                blackClock.stopClock();
            } else { // black
                whiteClock.stopClock();
                blackClock.startClock();
            }
        }

        state = State.PLAYING;

        if ((!board.turn && !blackParameters.isUser()) || (board.turn && !whiteParameters.isUser())) {
            state = State.WAITING;
            algorithm.setBoard(board);
            new Computer(this, algorithm).start();
        }

        checkGameStatus();
    }

    public void checkGameStatus() {

        MoveAlgorithm ma = new AlfaBeta(this);

        if (ma.checkMate()) {
            System.out.println("GAME OVER");
            state = State.GAMEOVER;
            whiteClock.stopClock();
            blackClock.stopClock();
        }

        System.out.println("ended ? white:" + whiteClock.hasEnded() + " black:" + blackClock.hasEnded());
        System.out.println("running ? white:" + whiteClock.isRunning() + " black:" + blackClock.isRunning());

        if (whiteClock.hasEnded() || blackClock.hasEnded()) {
            state = State.GAMEOVER;
        }

        System.out.println("CheckMate: " + ma.checkMate());
    }

    public boolean canMove() {
        boolean playing = false;
        if (state == State.PLAYING || state == State.INITIALIZED) {
            playing = true;
        }
        return playing;
    }

    public int getMoveCount() {
        return allMoves.size();
    }

    public String getDateCreated() {
        if (gameParameters == null) {
            return "??";
        }
        return gameParameters.getDateCreated();
    }

    public String getTitle() {
        if (gameParameters == null) {
            return "??";
        }
        return gameParameters.getTitle();
    }

    public void setTitle(String title) {
        gameParameters.setTitle(title);
    }

    public String getWhiteName() {
        if (whiteParameters == null) {
            return "?? White";
        }
        return whiteParameters.getName();
    }

    public String getBlackName() {
        if (blackParameters == null) {
            return "?? Black";
        }
        return blackParameters.getName();
    }

    public void setWhiteName(String name) {
        whiteParameters.setName(name);
    }

    public void setBlackName(String name) {
        blackParameters.setName(name);
    }
}
