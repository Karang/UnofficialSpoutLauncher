package fr.karang.spoutlauncher;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;

public class OptionsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JComboBox platform;
	private final JComboBox rendermode;
	private final JCheckBox debug;
	private final JCheckBox ccoveride;
	
	private final JLabel pluginsDir;
	private final JButton launch;
	
	public OptionsPanel(Config config) {
		setLayout(new GridLayout(5, 2));
		
		add(new JLabel("Platform:"));
		platform = new JComboBox(new String[] {"Client", "Server", "Proxy"});
		platform.setSelectedItem(config.optPlatform);
		add(platform);
		
		add(new JLabel("Rendermode:"));
		rendermode = new JComboBox(new String[] {"GL20", "GL30", "GL40"});
		rendermode.setSelectedItem(config.optRendermode);
		add(rendermode);
		
		add(new JLabel("Debug mode:"));
		debug = new JCheckBox();
		debug.setSelected(config.optDebug);
		add(debug);
		
		add(new JLabel("CCoveride:"));
		ccoveride = new JCheckBox();
		ccoveride.setSelected(config.optCCoveride);
		add(ccoveride);
		
		pluginsDir = new JLabel("Open plugin directory...");
		pluginsDir.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				try {
					Desktop.getDesktop().open(Util.getPluginDirectory());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		add(pluginsDir);
		
		launch = new JButton("Launch Spout !");
		add(launch);
	}
	
	public void save(Config config) {
		config.optRendermode = (String) rendermode.getSelectedItem();
		config.optPlatform = (String) platform.getSelectedItem();
		config.optDebug = debug.isSelected();
		config.optCCoveride = ccoveride.isSelected();
	}
	
	public String getLaunchOptions() {
		return "";
	}
}
