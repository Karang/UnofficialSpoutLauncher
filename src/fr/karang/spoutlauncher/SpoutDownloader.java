package fr.karang.spoutlauncher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class SpoutDownloader extends Thread {
	
	public Map<String, Integer> toDownload = new HashMap<String, Integer>();
	private Config config;
	
	public SpoutDownloader(Config config) {
		this.config = config;
		addJarToDownload("http://build.spout.org/job/Spout/", config.lastSpoutVersion);
		addJarToDownload("http://build.spout.org/job/Vanilla/", config.lastVanillaVersion);
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
	
	public void addJarToDownload(String urlPrefix, int version) {
		toDownload.put(urlPrefix, version);
	}
	
	public void run() {
		List<String> notUpToDate = new ArrayList<String>();
		for (String url : toDownload.keySet()) {
			if (!checkJenkinsVersion(url, toDownload.get(url))) {
				notUpToDate.add(url);
			}
		}
		
		boolean spout = true;
		for (String url : notUpToDate) {
			System.out.println(url+spout);
			spout = false;
		}
	}
}
