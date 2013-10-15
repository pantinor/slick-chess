

//http://www.reinerstilesets.de/2d-grafiken/2d-humans/

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;


public class ExpandSprites {
	
	public static String dir = "C:\\Users\\Paul\\Desktop\\sprites\\char\\";
	
	private static void expand() throws Exception {
		
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> east_files = new ArrayList<String>();

		
		String prefix = "paused";
		String f1 = "./sprites-people.png";
		String f2 = "./src/main/resources/red-sword-standing.png";
		
		int animLen = 8;
		int directions = 8;
		int tileDim = 96;

		boolean flip_east_for_west = false;
		
		for(int i=0;i<animLen;i++) files.add(prefix + " e"+StringUtils.leftPad(""+i, 4, '0')+".bmp");
		for(int i=0;i<animLen;i++) files.add(prefix + " n"+StringUtils.leftPad(""+i, 4, '0')+".bmp");
		for(int i=0;i<animLen;i++) files.add(prefix + " ne"+StringUtils.leftPad(""+i, 4, '0')+".bmp");
		for(int i=0;i<animLen;i++) files.add(prefix + " nw"+StringUtils.leftPad(""+i, 4, '0')+".bmp");
		for(int i=0;i<animLen;i++) files.add(prefix + " s"+StringUtils.leftPad(""+i, 4, '0')+".bmp");
		for(int i=0;i<animLen;i++) files.add(prefix + " se"+StringUtils.leftPad(""+i, 4, '0')+".bmp");
		for(int i=0;i<animLen;i++) files.add(prefix + " sw"+StringUtils.leftPad(""+i, 4, '0')+".bmp");
		//will be skipped of max = 7
		for(int i=0;i<animLen;i++) files.add(prefix + " w"+StringUtils.leftPad(""+i, 4, '0')+".bmp");
		
		for(int i=0;i<animLen;i++) east_files.add(prefix + " e"+StringUtils.leftPad(""+i, 4, '0')+".bmp");

	
				
		BufferedImage output = new BufferedImage(tileDim*directions, tileDim*animLen, BufferedImage.TYPE_INT_ARGB);
							
		int index = 0;
		for (int i=0;i<directions;i++) {
			for (int j=0;j<animLen;j++) {
				BufferedImage input = ImageIO.read(new File(dir + files.get(index)));
				System.out.println("i="+i+" j="+j +" index="+ index+" file=" +dir + files.get(index));
				output.getGraphics().drawImage(input,i*tileDim, j*tileDim, null);
				index ++;
			}
		}
		
		if (flip_east_for_west) {
			for (int i=0;i<animLen;i++) {
				BufferedImage input = ImageIO.read(new File(dir + files.get(i)));
				System.out.println("flipping east to west i="+i+" file=" +dir + files.get(i));
				output.getGraphics().drawImage(getFlippedImage(input),7*tileDim, i*tileDim, null);
			}
		}
		

		
		System.out.println("Writing: "+f1);
		ImageIO.write(output, "PNG", new File(f1));
		
		ImageTransparency.convert(f1,f2);
		
		
	}
			
	/**
	 * Flip horizontally
	 */
	public static BufferedImage getFlippedImage(BufferedImage bi) {
		BufferedImage flipped = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
		AffineTransform tran = AffineTransform.getTranslateInstance(bi.getWidth(), 0);
		AffineTransform flip = AffineTransform.getScaleInstance(-1d, 1d);
		tran.concatenate(flip);

		Graphics2D g = flipped.createGraphics();
		g.setTransform(tran);
		g.drawImage(bi, 0, 0, null);
		g.dispose();

		return flipped;
	}
	
	public static void main(String[] argv) throws Exception {
		expand();
	}

}
