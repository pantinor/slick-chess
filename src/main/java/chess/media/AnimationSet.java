package chess.media;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import javax.imageio.ImageIO;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

public class AnimationSet {
			
	private Animation east;
	private Animation west;
	private Animation north;
	private Animation south;
	
	private Animation southeast;
	private Animation southwest;
	private Animation northeast;
	private Animation northwest;
	
	public static final int DURATION =  150;
	
	public AnimationSet(String filename) throws Exception {
		this(filename, false);
	}
	
	public AnimationSet(String filename, boolean swapColor) throws Exception {
		
	    BufferedImage readImage = ImageIO.read(AnimationSet.class.getResourceAsStream("/" + filename));
	    
	    Image image = null;
		if (swapColor) {
			java.awt.Image swappedImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(readImage.getSource(), new RedBlueSwapFilter()));
			image = convertToSlickImage(imageToBufferedImage(swappedImage));
		} else {
			image = convertToSlickImage(readImage);
		}
		
	    int h = readImage.getHeight();
	    int max = h/96;
	    		
		SpriteSheet sheet = new SpriteSheet(image, 96, 96);
		

		this.east = new Animation();
		this.west = new Animation();
		this.north = new Animation();
		this.south = new Animation();
		this.southeast = new Animation();
		this.southwest = new Animation();
		this.northwest = new Animation();
		this.northeast = new Animation();

		for (int i=0;i<max;i++) east.addFrame(sheet.getSprite(0, i), DURATION);
		for (int i=0;i<max;i++) north.addFrame(sheet.getSprite(1, i), DURATION);
		for (int i=0;i<max;i++) northeast.addFrame(sheet.getSprite(2, i), DURATION);
		for (int i=0;i<max;i++) northwest.addFrame(sheet.getSprite(3, i), DURATION);
		for (int i=0;i<max;i++) south.addFrame(sheet.getSprite(4, i), DURATION);
		for (int i=0;i<max;i++) southeast.addFrame(sheet.getSprite(5, i), DURATION);
		for (int i=0;i<max;i++) southwest.addFrame(sheet.getSprite(6, i), DURATION);
		for (int i=0;i<max;i++) west.addFrame(sheet.getSprite(7, i), DURATION);
		
		
		this.east.setAutoUpdate(false);
		this.west.setAutoUpdate(false);
		this.north.setAutoUpdate(false);
		this.south.setAutoUpdate(false);
		this.southeast.setAutoUpdate(false);
		this.southwest.setAutoUpdate(false);
		this.northwest.setAutoUpdate(false);
		this.northeast.setAutoUpdate(false);
		
	}
	
	class RedBlueSwapFilter extends RGBImageFilter {
		public int filterRGB(int x, int y, int rgb) {
			return ((rgb & 0xff00ff00) | ((rgb & 0xff0000) >> 16) | ((rgb & 0xff) << 16));
		}
	}

	
	public Image getCurrentFrame(Direction direction) {
			
		Animation anim = southwest;
		switch(direction) {
		case NORTH:anim = north;break;
		case NORTHWEST:anim = northwest;break;
		case NORTHEAST:anim = northeast;break;
		case SOUTH:anim = south;break;
		case SOUTHEAST:anim = southeast;break;
		case SOUTHWEST:anim = southwest;break;
		case EAST:anim = east;break;
		case WEST:anim = west;break;
		}
		Image image = anim.getCurrentFrame();
		return image;
		
	}
	
	public Animation getAnimation(Direction direction) {
		
		Animation anim = south;
		switch(direction) {
		case NORTH:anim = north;break;
		case NORTHWEST:anim = northwest;break;
		case NORTHEAST:anim = northeast;break;
		case SOUTH:anim = south;break;
		case SOUTHEAST:anim = southeast;break;
		case SOUTHWEST:anim = southwest;break;
		case EAST:anim = east;break;
		case WEST:anim = west;break;
		}
		return anim;
		
	}
	
	public Image convertToSlickImage(BufferedImage image) throws Exception {
		Texture texture = BufferedImageUtil.getTexture("", image);
		Image slickImage = new Image(image.getWidth(), image.getHeight());
		slickImage.setTexture(texture);
		return slickImage;
	}
	
	public static BufferedImage imageToBufferedImage(java.awt.Image im) {
		BufferedImage bi = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(im, 0, 0, null);
		bg.dispose();
		return bi;
	}
	
	
	
}
