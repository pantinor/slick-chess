package chess.core;

public enum PieceType {
	
	PAWN(1,"Pa",""), 
	ROOK(5,"Ro","R"), 
	KNIGHT(5,"Kn","N"), 
	BISHOP(10,"Bi","B"), 
	QUEEN(40,"Qu","Q"), 
	KING(100,"Ki","K");

	public int cost;
	public String name;
	public String type;

	private PieceType(int cost, String name, String type) {
		this.cost = cost;
		this.name = name;
		this.type = type;
	}

}
