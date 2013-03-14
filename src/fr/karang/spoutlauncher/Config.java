package fr.karang.spoutlauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

public class Config {
	
	public int lastSpoutVersion = 0;
	public int lastVanillaVersion = 0;
	
	public String optRendermode = "GL20";
	public String optPlatform = "Client";
	public boolean optDebug = false;
	public boolean optCCoveride = false;
	
	private final File path;
	
	public Config() {
		path = new File(Util.getWorkingDirectory().getAbsolutePath()+"/launcher.txt");
		if (!path.exists()) {
			save();
		}
	}
	
	public void load() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			while ((line=reader.readLine()) != null) {
				if (line.startsWith("rendermode")) optRendermode = line.split(" ")[1];
				else if (line.startsWith("platform")) optPlatform = line.split(" ")[1];
				else if (line.startsWith("debug")) optDebug = Boolean.parseBoolean(line.split(" ")[1]);
				else if (line.startsWith("ccoveride")) optCCoveride = Boolean.parseBoolean(line.split(" ")[1]);
				else if (line.startsWith("spout")) lastSpoutVersion = Integer.parseInt(line.split(" ")[1]);
				else if (line.startsWith("vanilla")) lastVanillaVersion = Integer.parseInt(line.split(" ")[1]);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			FileOutputStream fout = new FileOutputStream(path);
			PrintStream ps = new PrintStream(fout);
			ps.println("rendermode "+optRendermode);
			ps.println("platform "+optPlatform);
			ps.println("debug "+optDebug);
			ps.println("ccoveride "+optCCoveride);
			ps.println("spout "+lastSpoutVersion);
			ps.println("vanilla "+lastVanillaVersion);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
