package me.jevy.powerFTP;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigReader {
	private String configFile = null;
	private int countServer = 0;

	List<Server> list = new ArrayList<Server>();
	//private Log log = LogFactory.getLog(ConfigReader.class);
	private static Logger log = Logger.getLogger(ConfigReader.class.getName());
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
			log.error(e);
			
			
		}

		try {
			if(this.configFile.equals(null)){
				log.info("configFile doesn't initalized. \n"
						+ "You should initalize this class by constractor ConfigReader(String configFile).\n"
						+ "Or, you can set the configureFile by the methord setConfigFile(String configFile).");
			}
			is = new FileInputStream(this.configFile);
		} catch (FileNotFoundException e) {
			log.error( e);
		}
		try {
			doc = dombuilder.parse(is);
		} catch (SAXException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		Element root = doc.getDocumentElement();
		NodeList xmlServers = root.getElementsByTagName("server");
		log.info("The configre file contains " + xmlServers.getLength() + " file(s).");
		
		if (xmlServers != null) {
			for (int i = 0; i < xmlServers.getLength(); i++) {
				Server serverInst= new Server();
				Element server = (Element)xmlServers.item(i);
				this.countServer++;
				
				serverInst.setIp(server.getElementsByTagName("ip").item(0).getTextContent());
				serverInst.setUser(server.getElementsByTagName("user").item(0).getTextContent());
				serverInst.setPass(server.getElementsByTagName("password").item(0).getTextContent());
				log.info("Reading server config : # "+ this.countServer+" : IP: "+serverInst.getIp() +" : "+ serverInst.getUser()+ " : " + serverInst.getPass());

				if(serverInst != null){
					log.debug(serverInst.getIp()+serverInst.user+serverInst.pass+
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
		//System.out.println((new File("").getAbsolutePath())+"\\Servers.xml");
		//new ConfigReader((new File("").getAbsolutePath())+"\\Servers.xml").getServerList();
		new ConfigReader("./Servers.xml").getServerList();
	}
}
