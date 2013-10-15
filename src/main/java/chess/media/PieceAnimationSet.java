package chess.media;

import org.apache.log4j.Logger;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import chess.core.PieceType;
import chess.media.BamAnimationStore.MapDirection;

public class PieceAnimationSet implements Images {
	
	private static final Logger logger = Logger.getLogger(PieceAnimationSet.class);


	private BamAnimationStore whitePawn;
	private BamAnimationStore whiteKnight;
	private BamAnimationStore whiteBishop;
	private BamAnimationStore whiteRook;
	private BamAnimationStore whiteQueen;
	private BamAnimationStore whiteKing;

	private BamAnimationStore blackPawn;
	private BamAnimationStore blackKnight;
	private BamAnimationStore blackBishop;
	private BamAnimationStore blackRook;
	private BamAnimationStore blackQueen;
	private BamAnimationStore blackKing;
		
	public static final String BAMDIR = "C:\\Users\\Paul\\Desktop\\BAMS";
	
	public static final String[] prefixes = {// "MAIR","MAIS","MAKH","MASG","MASL","MBAS","MBEG",
	// "MBEH","MBER","MBES","MCAR","MDJI","MDJL","MDKN","MDKS","MDLI","MDOG",
			// "MFIG", "MEAE", "MEAS", "METN", "METT", "MEYE", "MFDR", "MFIE", "MDRO", "MFIS",
			"MGHL","MGIB","MGIT","MGLC","MGNL","MGO1","MGO2","MGO3","MGO4","MGWE",
			// "MHOB","MIGO","MIMP","MINO","MKOB","MLER","MLIC","MLIZ","MMAG","MMEL","MMIN","MMIS",
			// "MMST","MMUM","MMY2","MMYC","MNO1","MNO2","MNO3","MOGH","MOGM","MOGN","MOGR","MOR1",
			// "MOR2","MOR3","MOR4","MOR5","MOTY","MOVE","MRAK","MRAV","MSA2","MSAH","MSAI","MSAL",
			// "MSAT","MSHD","MSHR","MSIR","MSKA","MSKB","MSKL","MSKT","MSLI","MSLY","MSNK","MSOG",
			// "MMUM","MSOL","MSPI","MSPL","MSPS","MTAN","MTAS","MTRO","MTRS","MUMB","MVAF","MVAM","MWER",
			"MWFM", "MWLF", "MWLS", "MWYV", "MXVT", "MYU1", "MYU2", "MYU3", "MZOM" };


	public PieceAnimationSet() throws SlickException {
		setAnimations();
	}

	public void setAnimations() {
		
		for (PieceType type : PieceType.values()) {
			logger.debug("setAnimations: " + type.toString());
			setStore(initStore(type, true), type, true);
			setStore(initStore(type, false), type, false);
		}

	}


	public BamAnimationStore initStore(PieceType type, boolean white) {

		String animName = "";

		switch (type) {
		case PAWN:
			if (white)
				animName = "MGO3";
			else
				animName = "MGO4";
			break;
		case KNIGHT:
			if (white)
				animName = "MMUM";
			else
				animName = "MGLC";
			break;
		case BISHOP:
			if (white)
				animName = "MDJI";
			else
				animName = "MDJL";
			break;
		case ROOK:
			if (white)
				animName = "MGIT";
			else
				animName = "MGIT";
			break;
		case QUEEN:
			if (white)
				animName = "MFIS";
			else
				animName = "MFIS";
			break;
		case KING:
			if (white)
				animName = "MDKN";
			else
				animName = "MDKN";
			break;
		}
		
		BamAnimationStore store = new BamAnimationStore(BAMDIR, animName);
		
		logger.debug(animName + " animations size=" + store.animations.size());

		for (String key : store.animations.keySet()) {
			Animation anim = store.animations.get(key);
			anim.setAutoUpdate(false);
		}

		return store;
	}

	public void setStore(BamAnimationStore store, PieceType type, boolean white) {
		switch (type) {
		case PAWN:
			if (white)
				whitePawn = store;
			else
				blackPawn = store;
			break;
		case KNIGHT:
			if (white)
				whiteKnight = store;
			else
				blackKnight = store;
			break;
		case BISHOP:
			if (white)
				whiteBishop = store;
			else
				blackBishop = store;
			break;
		case ROOK:
			if (white)
				whiteRook = store;
			else
				blackRook = store;
			break;
		case QUEEN:
			if (white)
				whiteQueen = store;
			else
				blackQueen = store;
			break;
		case KING:
			if (white)
				whiteKing = store;
			else
				blackKing = store;
			break;
		}
	}

	public BamAnimationStore getStore(PieceType type, boolean white) {
		switch (type) {
		case PAWN:
			if (white)
				return whitePawn;
			else
				return blackPawn;
		case KNIGHT:
			if (white)
				return whiteKnight;
			else
				return blackKnight;
		case BISHOP:
			if (white)
				return whiteBishop;
			else
				return blackBishop;
		case ROOK:
			if (white)
				return whiteRook;
			else
				return blackRook;
		case QUEEN:
			if (white)
				return whiteQueen;
			else
				return blackQueen;
		case KING:
			if (white)
				return whiteKing;
			else
				return blackKing;
		}
		return null;
	}
	
	public Image getCurrentFrame(PieceType type, boolean white) {
		BamAnimationStore store = getStore(type, white);
		if (store == null) return null;
		Animation anim = store.getAnimation("STAND1", white?MapDirection.NORTH.getDegrees():MapDirection.SOUTH.getDegrees());
		if (anim == null) return null;
		Image frame = anim.getCurrentFrame();
		return frame;
	}
	
	public Animation getCurrentAnimation(PieceType type, boolean white) {
		BamAnimationStore store = getStore(type, white);
		if (store == null) return null;
		Animation anim = store.getAnimation("STAND1", white? MapDirection.NORTH.getDegrees() : MapDirection.SOUTH.getDegrees());
		return anim;
	}

	public Image getCurrentFrame(PieceType type, boolean white, Direction dir) {
		// TODO Auto-generated method stub
		return null;
	}

	public Animation getCurrentAnimation(PieceType type, boolean white, Direction dir) {
		// TODO Auto-generated method stub
		return null;
	}


}
