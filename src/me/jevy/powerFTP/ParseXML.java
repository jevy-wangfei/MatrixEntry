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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseXML {
	public List<Server> DomParseXML(File file) {
		List<Server> list = new ArrayList<Server>();;
		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();

		DocumentBuilder dombuilder = null;
		InputStream is = null;
		Document doc = null;
		try {
			dombuilder = domfac.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			doc = dombuilder.parse(is);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = doc.getDocumentElement();
		NodeList xmlServers = root.getChildNodes();
System.out.println("Before XMLServers.length: "+xmlServers.getLength());
		
		if (xmlServers != null) {
			
			for (int i = 0; i < xmlServers.getLength(); i++) {
				Node server = xmlServers.item(i);
System.out.println("NodeServer.nodeName"+server.getNodeName()+
							"NodeServer.nodevalue"+server.getNodeValue());
				Server serverInst= null;
				if(server.getNodeType()==Node.ELEMENT_NODE){
					serverInst = new Server();
				}
				 
				for (Node node = server.getFirstChild(); node != null&&"server".equals(server.getNodeName()); node = node
						.getNextSibling()) {
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						
						char ca = node.getNodeName().toCharArray()[0];
						switch (ca) {
						case 'i':
							serverInst.setIp(node.getFirstChild().getNodeValue());
							break;
						case 'u':
							serverInst.setUser(node.getFirstChild().getNodeValue());
							break;
						case 'p':
							serverInst.setPass(node.getFirstChild().getNodeValue());
							break;
						case 'r':
							serverInst.setRemoteDir(node.getFirstChild().getNodeValue());
							break;
						case 'l':
							serverInst.setLocalDir(node.getFirstChild().getNodeValue());
							break;
						case 'f':
							serverInst.setFile(node.getFirstChild().getNodeValue());
							break;
						case 't':
							serverInst.setTransfer(node.getFirstChild().getNodeValue());
							break;
						case 'm':
							serverInst.setMode(node.getFirstChild().getNodeValue());
							break;
						}
					
					}
				}
				if(serverInst != null){
System.out.println(serverInst.getIp()+serverInst.user+serverInst.pass+
							serverInst.remoteDir+serverInst.file+serverInst.transfer+serverInst.mode);

					list.add(serverInst);	
				}
				
			}
		}

		return list;

	}
	
}
