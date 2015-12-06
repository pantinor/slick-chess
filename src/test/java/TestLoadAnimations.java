import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import chess.core.PieceType;
import chess.media.Direction;
import chess.media.SpriteAnimationSet;


public class TestLoadAnimations extends BasicGame {
	
	SpriteAnimationSet animationSet;
	Direction dir = Direction.NORTH;
	
	public TestLoadAnimations() {
		super("TestLoadAnimations");
	}

	public static void main(String[] args) {
		try {
			TestLoadAnimations a = new TestLoadAnimations();
			AppGameContainer container = new AppGameContainer(a);
			container.setDisplayMode(900,900, false);
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void render(GameContainer container, Graphics g) throws SlickException {
				
		Image image = animationSet.getCurrentFrame(PieceType.BISHOP, true, dir);
		image.draw(500,  500);
		
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		animationSet = new SpriteAnimationSet();
		
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Animation anim = animationSet.getCurrentAnimation(PieceType.BISHOP, true, dir);
		anim.update(delta);
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		super.mouseMoved(oldx, oldy, newx, newy);
		
		dir = getDirectionRelativeMouse(newx, newy, 500, 500);

	}
	
	private Direction getDirectionRelativeMouse(float mouseX, float mouseY, float originX, float originY) {
		float ang = (float) Math.toDegrees(Math.atan2(mouseX - originX, originY - mouseY));
		if (ang < 0) {
			ang = 360 + ang;
		}
		ang = (ang + 90 + 22.5f) % 360;
		ang /= 45f;
		return Direction.forIndex((int) ang);
	}
	
	
}
