package fr.karang.spoutlauncher;

import java.io.File;

public class Util {
	private static File workDir = null;
	
	public static File getWorkingDirectory() {
		if (workDir==null)
			workDir = getWorkingDirectory("unofficialSpout");
		return workDir;
	}
	
	public static File getWorkingDirectory(String appName) {
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory;
		switch (getPlatform().ordinal()) {
			case 0:
			case 1:
				workingDirectory = new File(userHome, '.' + appName + '/');
				break;
			case 2:
				String appData = System.getenv("APPDATA");
				if (appData != null) {
					workingDirectory = new File(appData, '.' + appName + '/');
				} else {
					workingDirectory = new File(userHome, '.' + appName + '/');
				}
				break;
			case 3:
				workingDirectory = new File(userHome, "Library/Application Support/" + appName);
				break;
			default:
				workingDirectory = new File(userHome, appName + '/');
		}
		if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs()))
			throw new RuntimeException("The working directory could not be created: " + workingDirectory);
		return workingDirectory;
	}
	
	public static OS getPlatform() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) return OS.windows;
		if (osName.contains("mac")) return OS.macos;
		if (osName.contains("solaris")) return OS.solaris;
		if (osName.contains("sunos")) return OS.solaris;
		if (osName.contains("linux")) return OS.linux;
		if (osName.contains("unix")) return OS.linux;
		return OS.unknown;
	}
	
	public static enum OS {
		linux, solaris, windows, macos, unknown;
	}
}
