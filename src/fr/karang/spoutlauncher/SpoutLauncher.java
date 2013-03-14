package fr.karang.spoutlauncher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JPanel;

public class SpoutLauncher extends Frame {
	private static final long serialVersionUID = 1L;
	private final LogoPanel logo;
	private final OptionsPanel options;
	
	private final SpoutDownloader downloader;
	private final Config config;
	
	public SpoutLauncher() {
		super("Unofficial Spout Launcher");
		
		config = new Config();
		config.load();
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		logo = new LogoPanel();
		panel.add(logo, "North");
		
		options = new OptionsPanel(config);
		panel.add(options, "Center");
		
		panel.setPreferredSize(new Dimension(400, 600));
		
		add(panel, "Center");
		pack();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				options.save(config);
				config.save();
				System.exit(0);
			}
		});
		
		downloader = new SpoutDownloader(config);
		downloader.start();
	}
	
	public static void main(String [] args) {
		SpoutLauncher launcher = new SpoutLauncher();
		launcher.setVisible(true);
	}
}
