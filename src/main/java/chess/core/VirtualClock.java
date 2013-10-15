package chess.core;

import javax.swing.Timer;
import java.awt.event.*;
import java.io.Serializable;

public class VirtualClock implements Constants, Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean timeIsRunning = false;
	private boolean hasEnded = false;
	private long time = TEN_MINUTES;
	private long lastSysTime;
	private Timer clockTimer;
	private ChessGame game;

	transient ActionListener listener;

	public VirtualClock(ChessGame game) {
		this.game = game;
		listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateClock();
			}
		};
		clockTimer = new Timer(250, listener);

	}

	public void addActionListener(ActionListener ac) {
		clockTimer.addActionListener(ac);
	}

	private void updateClock() {
		long thisSysTime = System.currentTimeMillis();
		if (timeIsRunning) {
			time -= thisSysTime - lastSysTime;
			if (time <= 0) {
				time = 0;
				stopClock();
				hasEnded = true;
				game.checkGameStatus();
			}
		}
		lastSysTime = System.currentTimeMillis();
	}

	public boolean hasEnded() {
		return hasEnded;
	}

	public void setEnabled(boolean enable) {
		if (!enable)
			stopClock();
	}

	public boolean isRunning() {
		return timeIsRunning;
	}

	public void startClock() {
		if (timeIsRunning)
			return;
		timeIsRunning = true;
		lastSysTime = System.currentTimeMillis();
		clockTimer.start();
	}

	public void stopClock() {
		timeIsRunning = false;
		clockTimer.stop();
		updateClock();
	}

	/**
	 * Resets the clock to the initial time.
	 */
	public void resetClock() {
		time = FIVE_MINUTES;
		// time = settings.whiteTime;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	public String getTimeToString() {
		return getHours() + ":" + getMinutes() + ":" + getSeconds();
	}

	private String getHours() {
		int hours = (int) (time / 1000 / 60 / 60);
		String sHrs = ("" + hours).length() == 2 ? "" + hours : "0" + hours;

		return sHrs;
	}

	private String getMinutes() {
		int hours = Integer.parseInt(getHours());
		int minutes = (int) (60 * (time / 1000.0 / 60.0 / 60.0 - hours));
		String sMin = ("" + minutes).length() == 2 ? "" + minutes : "0" + minutes;

		return sMin;
	}

	private String getSeconds() {
		int hours = Integer.parseInt(getHours());
		int minutes = Integer.parseInt(getMinutes());
		int seconds = (int) (60 * (time / 1000.0 / 60.0 - minutes - hours * 60));
		String sSec = ("" + seconds).length() == 2 ? "" + seconds : "0" + seconds;

		return sSec;
	}

}
