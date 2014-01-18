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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigReader {
	private String configFile = null;


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
		NodeList xmlServers = doc.getElementsByTagName("server");
		log.debug("ConfigReader Before XMLServers.length: " + xmlServers.getLength());
System.out.println("Before XMLServers.length: "+xmlServers.getLength());
		
		if (xmlServers != null) {
			
			for (int i = 0; i < xmlServers.getLength(); i++) {
				Node server = xmlServers.item(i);
				
				log.info("ConfigReader NodeServer.nodeName"+server.getNodeName()+
							"NodeServer.nodevalue"+server.getNodeValue());
System.out.println("NodeServer.nodeName"+server.getNodeName()+
							"NodeServer.nodevalue"+server.getNodeValue());
				
				Server serverInst= new Server();
				
				NodeList attributeList = server.getChildNodes();
				for(int j=0; j<attributeList.getLength(); j++){
					String attribute = attributeList.item(j).getNodeName().toUpperCase();
					String value = attributeList.item(j).getNodeValue();
					if(attribute.equals("IP"))
						serverInst.setIp(value);
					else if(attribute.equals("USER"))
						serverInst.setUser(value);
					else if(attribute.equals("PASS"))
						serverInst.setPass(value);
					else if(attribute.equals("CODING"))
						serverInst.setCoding(value);
					else if(attribute.equals("DIRECTION"))
						serverInst.setDirection(value);
					else if(attribute.equals("FILE"))
						serverInst.setFile(value);
					else if(attribute.equals("LOCALDIR"))
						serverInst.setLocalDir(value);
					else if(attribute.equals("REMOTEDIR"))
						serverInst.setRemoteDir(value);
					else if(attribute.equals("PORT"))
						serverInst.setPort(value);
						
					serverInst.setIp(attribute);
				}

				if(serverInst != null){
					log.debug("ConfigReader: " + serverInst.getIp()+serverInst.user+serverInst.pass+
							serverInst.remoteDir+serverInst.file+serverInst.coding+serverInst.direction);
System.out.println(serverInst.getIp()+serverInst.user+serverInst.pass+
							serverInst.remoteDir+serverInst.file+serverInst.coding+serverInst.direction);

					list.add(serverInst);	
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
		System.out.println((new File("").getAbsolutePath())+"\\Servers.xml");
		new ConfigReader((new File("").getAbsolutePath())+"\\Servers.xml").getServerList();
	}
}
