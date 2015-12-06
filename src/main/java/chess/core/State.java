package chess.core;

import java.io.Serializable;

public final class State implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final State BEFOREINITIALIZATION = new State(0);
    public static final State INITIALIZED = new State(1);
    public static final State WAITING = new State(2);
    public static final State PLAYING = new State(3);
    public static final State PAUSED = new State(4);
    public static final State GAMEOVER = new State(5);

    private int state;

    private State() {
    }

    private State(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public static State newInstance() {
        return new State();
    }

    public void setState(State state) {
        this.state = state.getState();
    }

}
