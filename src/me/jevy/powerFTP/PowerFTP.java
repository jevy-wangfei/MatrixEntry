package me.jevy.powerFTP;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.xml.sax.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.*;

public class PowerFTP {
	String configFile = "./Servers.xml";
	private Log log = LogFactory.getLog(PowerFTP.class);
	private int num = 0;
	private List serverList = null;

	public PowerFTP() {
		this.serverList = new ConfigReader(configFile).getServerList();
	}

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		while (true) {
			Scanner c = new Scanner(System.in);
			String command = null;

			if(c.hasNext()){
				command = c.next();
			}
			List<String> files = new ArrayList();
			while(c.hasNext()){
				files.add(c.next());
			}
			
			PowerFTP powerFTP = new PowerFTP();
			
			if ("ASCII".equals(command.toUpperCase())) {
				powerFTP.setCoding(command.toLowerCase());
			} else if ("BIN".equals(command.toUpperCase())) {
				powerFTP.setCoding(command.toLowerCase());
			} else if ("LCD".equals(command.toUpperCase())){
				powerFTP.setLocalDir(command.toLowerCase());
			} else if ("CD".equals(command.toUpperCase())){
				powerFTP.setRemoteDir(command.toLowerCase());
			} else if ("PUT".equals(command.toUpperCase())){
				powerFTP.putfiles(files);
			} else if ("HELP".equals(command.toUpperCase())){
				powerFTP.printHelp();
			} else if ("QUIT".equals(command.toUpperCase())||"EXIT".equals(command.toUpperCase())||"BYE".equals(command.toUpperCase())){
				System.exit(0);
			} else if("PORT".equals(command.toUpperCase())){
				powerFTP.setPort(files.get(0));
			} else {
			}
				powerFTP.printHelp();
				System.exit(0);
			}
		}
	
	void setCoding(String coding){
		
	}
	void setPort(String port){
		
	}
	void setLocalDir(String localDir){
		
	}
	void setRemoteDir(String remoteDir){
		
	}
	void putfiles(List fileList){
		
	}
	void printHelp(){
		System.out.println("Usage: \n"
				+ "     put file [file2 file3 ...]  \n"
				+ "        --put the file(s) of current directory to the current remote directory\n"
				+ "        --use lcd command to change local directory."
				+ "        --use cd command to change remote directory."
				+ "     lcd directory\n"
				+ "        --change local directory.\n"
				+ "     cd directory\n"
				+ "        --change remote directory.\n"
				+ "     assii\n"
				+ "        --set transfer coding to assii.\n"
				+ "     bin\n"
				+ "        --set transfer coding to bin\n"
				+ "     port number\n"
				+ "        --set file transfer port to number, default port is 21.");
	}
		
}
