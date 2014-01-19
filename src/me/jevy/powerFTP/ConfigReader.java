package me.jevy.powerFTP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigReader {
	private String configFile = null;
	private int countServer = 0;

	List<Server> list = new ArrayList<Server>();
	private Log log = LogFactory.getLog(ConfigReader.class);
	public ConfigReader(){
		
	}
	public ConfigReader(String configFile){
		this.configFile = configFile;
	}
	
	
	public List<Server> getServerList() {
		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();

		DocumentBuilder dombuilder = null;
		InputStream is = null;
		Document doc = null;
		try {
			dombuilder = domfac.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			log.error("ConfigReader: " + e);
		}

		try {
			is = new FileInputStream(this.configFile);
		} catch (FileNotFoundException e) {
			log.error("ConfigReader: " + e);
		}
		try {
			doc = dombuilder.parse(is);
		} catch (SAXException e) {
			log.error("ConfigReader: " + e);
		} catch (IOException e) {
			log.error("ConfigReader: " + e);
		}
		Element root = doc.getDocumentElement();
		NodeList xmlServers = root.getElementsByTagName("server");
		log.info("ConfigReader: The servers configre file have(has) " + xmlServers.getLength() + " file(s).");
		
		if (xmlServers != null) {
			for (int i = 0; i < xmlServers.getLength(); i++) {
				Server serverInst= new Server();
				Element server = (Element)xmlServers.item(i);
				this.countServer++;
				log.info("ConfigReader: Reading the "+ this.countServer+" server configureation : "+server.getElementsByTagName("ip").item(0).getTextContent() + ".");

				serverInst.setCoding(server.getElementsByTagName("coding").item(0).getTextContent());
				serverInst.setIp(server.getElementsByTagName("ip").item(0).getTextContent());
				serverInst.setUser(server.getElementsByTagName("user").item(0).getTextContent());
				serverInst.setPass(server.getElementsByTagName("password").item(0).getTextContent());
				serverInst.setDirection(server.getElementsByTagName("direction").item(0).getTextContent());
				serverInst.setLocalDir(server.getElementsByTagName("localDir").item(0).getTextContent());
				serverInst.setRemoteDir(server.getElementsByTagName("remoteDir").item(0).getTextContent());
				serverInst.setPort(server.getElementsByTagName("port").item(0).getTextContent());
				
				if(serverInst != null){
					log.debug("ConfigReader: " + serverInst.getIp()+serverInst.user+serverInst.pass+
							serverInst.remoteDir+serverInst.file+serverInst.coding+serverInst.direction);
					list.add(serverInst);
					log.info("ConfigReader: Server " + serverInst.getIp() + " has been readed.");
				}
			}
		} 
		return list;
	}
	
	public String getConfigFile() {
		return configFile;
	}
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
	public static void main(String[] args){
		//System.out.println((new File("").getAbsolutePath())+"\\Servers.xml");
		new ConfigReader((new File("").getAbsolutePath())+"\\Servers.xml").getServerList();
	}
}
