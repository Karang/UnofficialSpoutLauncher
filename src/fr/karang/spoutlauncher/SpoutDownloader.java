package fr.karang.spoutlauncher;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
	
	public void downloadJarFromJenkins(String urlPrefix, String localPath) {
		InputStream input = null;
        FileOutputStream writeFile = null;

        try {
        	URL url = new URL(urlPrefix+"lastStableBuild/artifact/target/spout-dev-SNAPSHOT.jar");
        	System.out.println(url);
            URLConnection connection = url.openConnection();
            int fileLength = connection.getContentLength();

            if (fileLength == -1) {
                System.out.println("Invalide URL or file.");
                return;
            }

            input = connection.getInputStream();
            String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
            writeFile = new FileOutputStream(localPath+'/'+fileName);
            byte[] buffer = new byte[1024];
            int read;

            while ((read = input.read(buffer)) > 0) {
                writeFile.write(buffer, 0, read);
            }
            writeFile.flush();
        } catch (IOException e) {
            System.out.println("Error while trying to download the file.");
            e.printStackTrace();
        } finally {
            try {
                writeFile.close();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
			if (spout) {
				downloadJarFromJenkins(url, Util.getWorkingDirectory().getAbsolutePath());
				spout = false;
			} else {
				downloadJarFromJenkins(url, Util.getPluginDirectory().getAbsolutePath());
			}
		}
	}
}
