package chess.properties;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import chess.core.Constants;

public class GameParameters implements Constants, Serializable {

	private static final long serialVersionUID = 1L;
	
	private String dateCreated = new Date().toString();
	private String gameTitle = dateCreated;
	private long countDown = FIVE_MINUTES;

	private String dateCreatedName = "game.created";
	private String countDownName = "count.down";

	protected void setDefaults(Properties defaults) {
		defaults.put(dateCreatedName, dateCreated);
		defaults.put(countDownName, new Long(countDown / ONE_SECOND).toString());
	}

	protected void updateSettingsFromProperties() {
	}

	protected void updatePropertiesFromSettings() {
	}

	public String toString() {
		return "[" + "dateCreated=" + dateCreated + "," + "countDown=" + countDown + "]";
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public String getTitle() {
		return gameTitle;
	}

	public void setTitle(String name) {
		gameTitle = name;
	}
}
