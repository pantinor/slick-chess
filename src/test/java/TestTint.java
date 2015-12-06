import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import chess.media.AnimationSet;

public class TestTint {

	public static void main(String[] args) {
		new TestTint();
	}

	public TestTint() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception ex) {
				}

				JFrame frame = new JFrame("Testing");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLayout(new BorderLayout());
				frame.add(new TestPane());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	public static GraphicsConfiguration getGraphicsConfiguration() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	}

	public static BufferedImage createCompatibleImage(int width, int height, int transparency) {
		BufferedImage image = getGraphicsConfiguration().createCompatibleImage(width, height, transparency);
		image.coerceData(true);
		return image;
	}

	public static void applyQualityRenderingHints(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}

	public static BufferedImage generateMask(BufferedImage imgSource, Color color, float alpha) {
		int imgWidth = imgSource.getWidth();
		int imgHeight = imgSource.getHeight();

		BufferedImage imgMask = createCompatibleImage(imgWidth, imgHeight, Transparency.TRANSLUCENT);
		Graphics2D g2 = imgMask.createGraphics();
		applyQualityRenderingHints(g2);

		g2.drawImage(imgSource, 0, 0, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
		g2.setColor(color);

		g2.fillRect(0, 0, imgSource.getWidth(), imgSource.getHeight());
		g2.dispose();

		return imgMask;
	}

	public BufferedImage tint(BufferedImage master, BufferedImage tint) {
		int imgWidth = master.getWidth();
		int imgHeight = master.getHeight();

		BufferedImage tinted = createCompatibleImage(imgWidth, imgHeight, Transparency.TRANSLUCENT);
		Graphics2D g2 = tinted.createGraphics();
		applyQualityRenderingHints(g2);
		g2.drawImage(master, 0, 0, null);
		g2.drawImage(tint, 0, 0, null);
		g2.dispose();

		return tinted;
	}

	public class TestPane extends JPanel {

		private BufferedImage master;
		private BufferedImage mask;
		private BufferedImage tinted;

		public TestPane() {
			try {
				master = ImageIO.read(TestTint.class.getResourceAsStream("/king-standing.png"));
				mask = generateMask(master, Color.RED, 0.5f);
				tinted = tint(master, mask);
			} catch (IOException exp) {
				exp.printStackTrace();
			}
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension size = super.getPreferredSize();
			if (master != null && mask != null) {
				size = new Dimension(master.getWidth() + mask.getWidth() + tinted.getWidth(), Math.max(Math.max(master.getHeight(), mask.getHeight()), tinted.getHeight()));
			}
			return size;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int x = (getWidth() - (master.getWidth() + mask.getWidth() + tinted.getWidth())) / 2;
			int y = (getHeight() - master.getHeight()) / 2;
			g.drawImage(master, x, y, this);

			x += mask.getWidth();
			y = (getHeight() - mask.getHeight()) / 2;
			g.drawImage(mask, x, y, this);

			x += tinted.getWidth();
			y = (getHeight() - tinted.getHeight()) / 2;
			g.drawImage(tinted, x, y, this);
		}

	}

}