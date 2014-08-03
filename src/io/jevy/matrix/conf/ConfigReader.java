package io.jevy.matrix.conf;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigReader {
	private String configFile = "./Servers.xml";
	private int countServer = 0;

	List<Server> list = new ArrayList<Server>();
	// private Log log = LogFactory.getLog(ConfigReader.class);
	private static Logger log = Logger.getLogger(ConfigReader.class.getName());

	public ConfigReader() {

	}

	public ConfigReader(String configFile) {
		this.configFile = configFile;
	}

	public List<Server> getServerList() {
		Document doc = getDocument();
		Element root = doc.getDocumentElement();
		NodeList xmlServers = root.getElementsByTagName("server");
		log.info("The server configure file have " + xmlServers.getLength()
				+ " server(s).");

		for (int i = 0; i < xmlServers.getLength(); i++) {
			Server serverInst = new Server();
			Element server = (Element) xmlServers.item(i);
			if (server.getNodeType() == Node.ELEMENT_NODE) {

				NodeList serverAttr = server.getChildNodes();

				for (int q = 0; q < serverAttr.getLength(); q++) {
					Node attr = serverAttr.item(q);
					if (attr.getNodeType() == Node.ELEMENT_NODE) {
						if ("ip".equals(attr.getNodeName())) {
							serverInst.setIp(attr.getTextContent().trim());
						} else if ("user".equals(attr.getNodeName().trim())) {
							serverInst.setUser(attr.getTextContent().trim());
						} else if ("password".equals(attr.getNodeName())) {
							serverInst.setPass(attr.getTextContent().trim());
						}

					}
				}
				if ((serverInst.getIp() != "") && ( serverInst.getUser() != "")) {
					list.add(serverInst);
					log.info(" Server: #" + i + " : " +serverInst.getIp()  + " : " + serverInst.getUser() + " added.");
				}
			}
		}
		return list;
	}
	
	private Document getDocument(){
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
			if (this.configFile.equals(null)) {
				log.info("configFile doesn't initalized. \n"
						+ "You should initalize this class by constractor ConfigReader(String configFile).\n"
						+ "Or, you can set the configureFile by the methord setConfigFile(String configFile).");
			}
			is = new FileInputStream(this.configFile);
		} catch (FileNotFoundException e) {
			log.error(e);
		}
		try {
			doc = dombuilder.parse(is);
		} catch (SAXException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		return doc;
	}
	
	public String getConfigFile() {
		return configFile;
	}

	public static void main(String[] args) {
		// System.out.println((new File("").getAbsolutePath())+"\\Servers.xml");
		// new ConfigReader((new
		// File("").getAbsolutePath())+"\\Servers.xml").getServerList();
		new ConfigReader("./Servers.xml").getServerList();
	}
}
