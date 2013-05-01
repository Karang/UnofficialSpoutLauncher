package fr.karang.spoutlauncher;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JProgressBar;

import fr.karang.spoutlauncher.Util.OS;

public class OptionsPanel extends JPanel implements ActionListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	
	private final JProgressBar progressbar;
	
	private final JComboBox platform;
	private final JComboBox rendermode;
	private final JCheckBox debug;
	private final JCheckBox ccoveride;
	
	private final JLabel pluginsDir;
	private final JButton launch;
	
	public OptionsPanel(Config config) {
		setLayout(new GridLayout(6, 2));
		
		add(new JLabel("Downloading..."));
		progressbar = new JProgressBar(0, 100);
		progressbar.setValue(0);
		progressbar.setStringPainted(true);
		add(progressbar);
		
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
		launch.addActionListener(this);
		add(launch);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		launch.setEnabled(false);
		
		SpoutDownloader downloader = new SpoutDownloader();
		downloader.addPropertyChangeListener(this);
		downloader.execute();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals("progress")) {
			int progress = (Integer) event.getNewValue();
			progressbar.setValue(progress);
		}
	}
	
	public void save(Config config) {
		config.optRendermode = (String) rendermode.getSelectedItem();
		config.optPlatform = (String) platform.getSelectedItem();
		config.optDebug = debug.isSelected();
		config.optCCoveride = ccoveride.isSelected();
	}
	
	public String getLaunchOptions() {
		StringBuilder sb = new StringBuilder();
		OS os = Util.getPlatform();
		if (os==OS.macos) {
			sb.append("#!/bin/bash\n");
			sb.append("cd \"${0%/*}\"; java -Xms1024M -Xmx1024M -jar spout*.jar ");
		} else if (os==OS.windows) {
			sb.append("@echo off\n");
			sb.append("java -server -XX:+UseG1GC -Xms1024M -Xmx1024M -jar spout*.jar ");
		} else if (os==OS.linux || os==OS.solaris) {
			sb.append("#/bin/sh\n");
			sb.append("java -server -XX:+UseG1GC -Xms1024M -Xmx1024M -jar spout*.jar ");
		}
		
		sb.append("--platform ");
		sb.append(((String) platform.getSelectedItem()).toUpperCase());
		sb.append(" ");
		
		if (debug.isSelected()) {
			sb.append("--debug ");
		}
		
		if (ccoveride.isSelected()) {
			sb.append("--ccoveride ");
		}
		
		sb.append("--rendermode ");
		sb.append(((String) rendermode.getSelectedItem()).toUpperCase());
		
		return sb.toString();
	}
}
