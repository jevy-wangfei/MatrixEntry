package io.jevy.matrix.ftp;

import io.jevy.matrix.conf.ConfigReader;
import io.jevy.matrix.conf.Server;
import io.jevy.matrix.util.ConsoleProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;


public class PowerFTP {
	
	private Logger log = Logger.getLogger(PowerFTP.class.getName());
	private List<Server> serverList = new ConfigReader().getServerList();
	int totalServers = this.serverList.size();
	public PowerFTP() {
	}

	
	public void createClient(){
		PowerFTP powerFTP = new PowerFTP();
		while (true) {
			BufferedReader in = 
					new BufferedReader(new InputStreamReader(System.in));
			String[] commands = null;
			try {
				commands = in.readLine().replaceAll("\\s{1,}", " ").split(" ");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(commands == null){
				new NullPointerException("The command you put is null.");
			}
			String command = commands[0];
			List<String> files = new ArrayList<String>();
			for(int i=1; i<commands.length; i++){
				files.add(commands[i]);
			}
			
			if ("ASCII".equals(command.toUpperCase())) {
				powerFTP.setCoding(command.toLowerCase());
			} else if ("BIN".equals(command.toUpperCase())) {
				powerFTP.setCoding(command.toLowerCase());
			} else if ("LCD".equals(command.toUpperCase())){
				powerFTP.setLocalDir(files.get(0));
			} else if ("CD".equals(command.toUpperCase())){
				powerFTP.setRemoteDir(files.get(0));
			} else if ("PUT".equals(command.toUpperCase())){
				powerFTP.putfiles(files);
			} else if("RM".equals(command.toUpperCase())||"REMOVE".equals(command.toUpperCase())){
			} else if("RM".equals(command.toUpperCase())){
				powerFTP.removeFiles(files);
			} else if ("HELP".equals(command.toUpperCase())){
				powerFTP.printHelp();
			} else if ("QUIT".equals(command.toUpperCase())||"EXIT".equals(command.toUpperCase())||"BYE".equals(command.toUpperCase())){
				System.exit(0);
			} else if("PORT".equals(command.toUpperCase())){
				powerFTP.setPort(files.get(0));
			} else {
				powerFTP.printHelp();
				System.exit(0);
			}
		}
	}
	
	public void setCoding(String coding){
		for(Iterator<Server> it=serverList.iterator(); it.hasNext();){
			((Server)it.next()).setCoding(coding);
			log.info("Set Transfer Code to " + coding);
		}
		
	}
	public void setPort(String port){
		for(Iterator<Server> it=serverList.iterator(); it.hasNext();){
			((Server)it.next()).setPort(port);
			log.info("Set Transfer Port to " + port);
		}
	}
	public void setLocalDir(String localDir){
		for(Iterator<Server> it=serverList.iterator(); it.hasNext();){
			((Server)it.next()).setLocalDir(localDir);
			log.info("Set Local Directory to " + localDir);
		}
	}
	public void setRemoteDir(String remoteDir){
		for(Iterator<Server> it=serverList.iterator(); it.hasNext();){
			((Server)it.next()).setRemoteDir(remoteDir);
			log.info("Set Remote Directory to " + remoteDir);
		}
	}
	public void putfiles(List<String> fileList){
		for(Iterator<String> files=fileList.iterator(); files.hasNext();){
			String file = (String)files.next();
			log.info("Uploading file " + file);
			int successNumber = 0;
			for(Iterator<Server> servers=this.serverList.iterator(); servers.hasNext();){
				PutFile putfile = new PutFile((Server)servers.next(), file);
				new Thread(putfile).run();
				if(putfile.flag)
					successNumber++;
				new ConsoleProgressBar(successNumber, totalServers, 50);
			}
		}
	}
	public void removeFiles(List<String> fileList){
		for(Iterator<String> files=fileList.iterator(); files.hasNext();){
			String file = (String)files.next();
			log.info("Uploading file " + file);
			int successNumber = 0;
			for(Iterator<Server> servers=this.serverList.iterator(); servers.hasNext();){
				RemoveFile removeFile = new RemoveFile((Server)servers.next(), file);
				new Thread(removeFile).run();
				if(removeFile.flag)
					successNumber++;
				new ConsoleProgressBar(successNumber, totalServers, 50);
			}
		}
	}
	public void printHelp(){
		System.out.println("Usage: \n"
				+ "     put file [file2 file3 ...]  \n"
				+ "        --put the file(s) of current directory to the current remote directory\n"
				+ "        --use lcd command to change local directory."
				+ "        --use cd command to change remote directory."
				+ "		rm file [file2 file3 ...]\n"
				+ "		   --remove remote server file(s) \n"
				+ "     lcd directory (Shoule be full directory)\n"
				+ "        --change local directory. \n"
				+ "          Example: lcd D:\\hadoop\\hadoop.zip \n"
				+ "     cd directory (Should be full directory)\n"
				+ "        --change remote directory.\n"
				+ "          Example: cd /home/hadoop\n"
				+ "     assii\n"
				+ "        --set transfer coding to assii.\n"
				+ "     bin\n"
				+ "        --set transfer coding to bin\n"
				+ "     port number\n"
				+ "        --set file transfer port to number, default port is 21.");
	}
		
}
