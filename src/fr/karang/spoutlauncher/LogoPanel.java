package fr.karang.spoutlauncher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class LogoPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image logo;
	
	public LogoPanel() {
		setOpaque(true);
		try {
			BufferedImage src = ImageIO.read(SpoutLauncher.class.getResource("logo.png"));
			int w = src.getWidth();
			int h = src.getHeight();
			logo = src.getScaledInstance(w, h, 16);
			setPreferredSize(new Dimension(w+32, h+32));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setBackground(Color.BLACK);
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(logo, 24, 24, null);
	}
}
