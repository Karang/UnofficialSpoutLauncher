package fr.karang.spoutlauncher;

import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JComboBox;

public class OptionsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JComboBox platform;
	private final JComboBox rendermode;
	private final JCheckBox debug;
	private final JCheckBox ccoveride;
	
	
	public OptionsPanel() {
		setLayout(new GridLayout(4, 2));
		
		add(new Label("Platform:"));
		platform = new JComboBox(new String[] {"Client", "Server", "Proxy"});
		add(platform);
		
		add(new Label("Rendermode:"));
		rendermode = new JComboBox(new String[] {"GL20", "GL30", "GL40"});
		add(rendermode);
		
		add(new Label("Debug mode:"));
		debug = new JCheckBox();
		add(debug);
		
		add(new Label("CCoveride:"));
		ccoveride = new JCheckBox();
		add(ccoveride);
	}
}
