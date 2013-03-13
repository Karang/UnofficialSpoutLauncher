package fr.karang.spoutlauncher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class SpoutDownloader extends Thread {
	
	public Map<String, File> toDownload = new HashMap<String, File>();
	
	public SpoutDownloader() {
		addJarToDownload("http://build.spout.org/job/Spout/", false);
		addJarToDownload("http://build.spout.org/job/Vanilla/", true);
	}
	
	public boolean checkJenkinsVersion(String urlPrefix, int currentVersion) {
		try {
			URL url = new URL(urlPrefix+"api/xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(url.openStream());
			
			NodeList jobs = doc.getFirstChild().getChildNodes();
			for (int i=0 ; i<jobs.getLength() ; i++) {
				if (jobs.item(i).getNodeName().equalsIgnoreCase("lastStableBuild")) {
					String v = jobs.item(i).getChildNodes().item(0).getTextContent();
					return (currentVersion==Integer.parseInt(v));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void downloadJarFromJenkins(String urlPrefix) {
		try {
			URL url = new URL(urlPrefix+"lastStableBuild/");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addJarToDownload(String urlPrefix, boolean isPlugin) {
		File dir = Util.getWorkingDirectory();
		if (isPlugin)
			dir = Util.getPluginDirectory();
		toDownload.put(urlPrefix, dir);
	}
	
	public void run() {
		for (String url : toDownload.keySet()) {
			checkJenkinsVersion(url, 0);
		}
	}
}
