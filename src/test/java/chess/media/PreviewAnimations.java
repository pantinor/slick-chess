package chess.media;


import java.awt.Font;
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

import chess.media.BamAnimationStore;


public class PreviewAnimations extends BasicGame {

	BamAnimationStore store;
	TrueTypeFont myFont;
	int displaySectionIndex = 1;

	public static final String BAMDIR = "C:\\Users\\Paul\\Desktop\\BAMS";

	public static final String[] prefixes = {// "MAIR","MAIS","MAKH","MASG","MASL","MBAS","MBEG",
	"MBEH","MBER","MBES","MCAR","MDJI","MDJL","MDKN","MDKS","MDLI","MDOG",
			"MFIG", "MEAE", "MEAS", "METN", "METT", "MEYE", "MFDR", "MFIE", "MDRO", "MFIS"};
			//"MGCL","MGCP","MGHL","MGIB","MGIT","MGLC","MGNL","MGO1","MGO2","MGO3","MGO4","MGWE",
			// "MHOB","MIGO","MIMP","MINO","MKOB","MLER","MLIC","MLIZ","MMAG","MMEL","MMIN","MMIS",
			// "MMST","MMUM","MMY2","MMYC","MNO1","MNO2","MNO3","MOGH","MOGM","MOGN","MOGR","MOR1",
			// "MOR2","MOR3","MOR4","MOR5","MOTY","MOVE","MRAK","MRAV","MSA2","MSAH","MSAI","MSAL",
			// "MSAT","MSHD","MSHR","MSIR","MSKA","MSKB","MSKL","MSKT","MSLI","MSLY","MSNK","MSOG",
			// "MMUM","MSOL","MSPI","MSPL","MSPS","MTAN","MTAS","MTRO","MTRS","MUMB","MVAF","MVAM","MWER",
			//"MWFM", "MWLF", "MWLS", "MWYV", "MXVT", "MYU1", "MYU2", "MYU3", "MZOM" };

	public static boolean initializingStore = true;
	public static int currentPrefixIndex = 0;

	public static void main(String[] args) {
		try {
			PreviewAnimations as = new PreviewAnimations();
			AppGameContainer container = new AppGameContainer(as);
			container.setDisplayMode(container.getScreenWidth(), container.getScreenHeight(), false);
			container.setShowFPS(false);
			container.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PreviewAnimations() {
		super("PreviewAnimations");
	}

	public void init(GameContainer container) throws SlickException {

		Font awtFont = new Font("Lucida Sans", Font.PLAIN, 10);
		myFont = new TrueTypeFont(awtFont, false);

		initStore(prefixes[currentPrefixIndex]);

	}

	public void initStore(String prefix) throws SlickException {

		initializingStore = true;

		store = new BamAnimationStore(BAMDIR, prefix);

		System.out.println("animations size=" + store.animations.size());

		for (String key : store.animations.keySet()) {
			Animation anim = store.animations.get(key);
			anim.setAutoUpdate(false);
		}

		initializingStore = false;

	}

	public void render(GameContainer container, Graphics g) {

		if (initializingStore || store == null)
			return;

		g.setFont(myFont);
		g.drawString(store.sheetName, 100, 10);

		int dimX = store.maxWidth;
		int dimY = store.maxHeight;

		int y = 1;
		int count = 0;
		int index = 0;

		for (String key : store.animations.keySet()) {
			Animation anim = store.animations.get(key);

			index++;
			if (displaySectionIndex == 1 && index > 30) continue;
			if (displaySectionIndex == 2 && (index <= 30 || index > 60)) continue;
			if (displaySectionIndex == 3 && (index <= 60 || index > 90)) continue;
			if (displaySectionIndex == 4 && (index <= 90 || index > 120)) continue;
			if (displaySectionIndex == 5 && (index <= 120 || index > 150)) continue;


			count++;

			int rX = (count * dimX) - (dimX / 2);
			int rY = (y * dimY) - (dimY / 2);
			int centerRectX = rX + (dimX / 2);
			int centerRectY = rY + (dimY / 2);

			Rectangle rect = new Rectangle(rX, rY, dimX, dimY);
			g.setColor(Color.green);
			g.draw(rect);

			// centerize the image on the rectangle
			Image frame = anim.getCurrentFrame();
			int width = frame.getWidth();
			int height = frame.getHeight();
			frame.draw(centerRectX - width / 2, centerRectY - height / 2, width, height);

			g.setColor(Color.pink);
			g.drawString(key, rX, rY);

			if (count > 7)
				y++;
			if (count > 7)
				count = 0;
		}

	}

	public void update(GameContainer container, int delta) {
		for (String key : store.animations.keySet()) {
			Animation anim = store.animations.get(key);
			anim.update(delta);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_SPACE) {
			displaySectionIndex++;
			if (displaySectionIndex > 5) displaySectionIndex = 1;
		}

		if (key == Input.KEY_A) {
			currentPrefixIndex++;
			if (currentPrefixIndex == prefixes.length)
				currentPrefixIndex = 0;
			try {
				initStore(prefixes[currentPrefixIndex]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}