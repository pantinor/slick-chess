package chess.properties;

import java.io.Serializable;
import java.util.Properties;

public class PlayerParameters implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String playerNm = "Default";
	private static final String playerNmName = "player.name";
	private boolean isUser = true;
	
	public PlayerParameters(String name, boolean isUser) {
		setName(name);
		setUser(isUser);
	}

	protected void setDefaults(Properties defaults) {
		defaults.put(playerNmName, playerNm);
	}

	protected void updateSettingsFromProperties() {
	}

	protected void updatePropertiesFromSettings() {
	}

	public String toString() {
		return "[playerName=" + playerNm + "]";
	}

	public void setName(String name) {
		playerNm = name;
	}

	public String getName() {
		return playerNm;
	}

	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}

	public boolean isUser() {
		return isUser;
	}

}