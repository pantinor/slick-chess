package chess.properties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.newdawn.slick.Color;
import chess.core.Constants;

public class BoardParameters implements Constants, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final static BoardParameters param1;
	private final static BoardParameters param2;
	private final static BoardParameters param3;
	private final static BoardParameters param4;
	private final static BoardParameters param5;
	
	public static List<BoardParameters> params = new ArrayList<BoardParameters>();;
	public static final Map<String, Color> settings = new HashMap<String, Color>();;
	
	static {
		settings.put("board.primaryCell", new Color(80, 124, 224));

		param1 = new BoardParameters();
		param1.setName("Default");
		
		param2 = new BoardParameters();
		param2.setPrimaryCell(Color.yellow);
		param2.setAlternateCell(Color.green);
		param2.setName("Yellow Green");
		
		param3 = new BoardParameters();
		param3.setPrimaryCell(Color.orange);
		param3.setAlternateCell(Color.blue);
		param3.setName("Orange Blue");
		
		param4 = new BoardParameters();
		param4.setPrimaryCell(Color.red);
		param4.setAlternateCell(Color.pink);
		param4.setName("Red Pink");
		
		param5 = new BoardParameters();
		param5.setPrimaryCell(Color.gray);
		param5.setAlternateCell(Color.lightGray);
		param5.setName("Gray");

		params.add(param1);
		params.add(param2);
		params.add(param3);
		params.add(param4);
		params.add(param5);
	}
	
	private boolean allowCursorChange = true;
	private boolean allowPieceDrag = true;

	private Color alternateCell = new Color(167, 191, 246);
	private String alternateCellName = "board.alternateCell";

	private String name = "default";

	private Color primaryCell = new Color(80, 124, 224);
	private String primaryCellName = "board.primaryCell";
	private boolean showActiveSpots = true;
	private boolean showArrows = true;
	private boolean showLegend = true;

	private boolean showMouseOver = true;

	private boolean willPieceDragCenter = true;
	private boolean willPieceSlide = true;
	
	public static BoardParameters getInstance() {
		return param1;
	}

	public BoardParameters() {
	}

	public boolean allowCursorChange() {
		return allowCursorChange;
	}

	public boolean allowPieceDrag() {
		return allowPieceDrag;
	}

	public Color getAlternateCell() {
		return alternateCell;
	}

	public Color getPrimaryCell() {
		return primaryCell;
	}

	public void setAllowCursorChange(boolean show) {
		allowCursorChange = show;
	}

	public void setAlternateCell(Color color) {
		alternateCell = color;
	}

	protected void setDefaults(Properties defaults) {
		defaults.put(primaryCellName, primaryCell.toString());
		defaults.put(alternateCellName, alternateCell.toString());
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrimaryCell(Color color) {
		primaryCell = color;
	}

	public void setShowActiveSpots(boolean show) {
		showActiveSpots = show;
	}

	public void setShowArrows(boolean show) {
		showArrows = show;
	}

	public void setShowLegend(boolean show) {
		showLegend = show;
	}

	public void setShowMouseOver(boolean show) {
		showMouseOver = show;
	}

	public boolean showActiveSpots() {
		return showActiveSpots;
	}

	public boolean showArrows() {
		return showArrows;
	}

	public boolean showLegend() {
		return showLegend;
	}

	public boolean showMouseOver() {
		return showMouseOver;
	}

	public String toString() {
		return name;
	}

	protected void updatePropertiesFromSettings() {

	}

	protected void updateSettingsFromProperties() {

	}

	public boolean willPieceDragCenter() {
		return willPieceDragCenter;
	}

	public boolean willPieceSlide() {
		return willPieceSlide;
	}
}
