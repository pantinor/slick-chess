import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.imageio.ImageIO;
import javax.swing.*;


public class ColorSwap {
	public static void main(String[] args) {

		try {
			BufferedImage mapImage = ImageIO.read(ColorSwap.class.getResourceAsStream("/king-standing.png"));
			Image newMapImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(mapImage.getSource(), new XorFilter()));

			Image grayImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(mapImage.getSource(), new RedBlueSwapFilter()));

			Image grayToColorImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(grayImage.getSource(), new GrayToColorFilter(Color.red)));

			ImageIcon mapIcon = new ImageIcon(mapImage);
			ImageIcon newMapIcon = new ImageIcon(newMapImage);
			ImageIcon newGrayIcon = new ImageIcon(grayImage);

			ImageIcon grayToColorIcon = new ImageIcon(grayToColorImage);

			JPanel imagePanel = new JPanel(new GridLayout(2, 2));
			imagePanel.add(new JLabel(mapIcon));
			imagePanel.add(new JLabel(newMapIcon));
			imagePanel.add(new JLabel(newGrayIcon));
			imagePanel.add(new JLabel(grayToColorIcon));

			JOptionPane.showMessageDialog(null, imagePanel, "Image Color Swap", JOptionPane.PLAIN_MESSAGE);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class RedBlueSwapFilter extends RGBImageFilter {
	public int filterRGB(int x, int y, int rgb) {
		return ((rgb & 0xff00ff00) | ((rgb & 0xff0000) >> 16) | ((rgb & 0xff) << 16));
	}
}

class XorFilter extends RGBImageFilter {
	public int filterRGB(int x, int y, int argb) {
		return ((argb & 0xff000000) | (argb ^ 0x00ffffff));
	}
}

class MyGrayFilter extends RGBImageFilter {
	public int filterRGB(int x, int y, int argb) {
		int r = (argb & 0x00ff0000) >> 0x10;
		int g = (argb & 0x0000ff00) >> 0x08;
		int b = (argb & 0x000000ff);
		int ave = (r + g + b) / 3;

		return ((argb & 0xff000000) | (ave << 0x10 | ave << 0x08 | ave));
	}
}

class GrayToColorFilter extends RGBImageFilter {
	private Color c;

	public GrayToColorFilter(Color c) {
		this.c = c;
	}

	public int filterRGB(int x, int y, int argb) {
		return (argb | c.getRGB());
	}

}