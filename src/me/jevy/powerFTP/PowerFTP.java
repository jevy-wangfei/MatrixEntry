package me.jevy.powerFTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;


public class PowerFTP {
	String configFile = "./Servers.xml";
	private Logger log = Logger.getLogger(PowerFTP.class.getName());
	private int num = 0;
	private List serverList = serverList = new ConfigReader(configFile).getServerList();
	public PowerFTP() {
	}

	/**
	 * @param args
	 */
	
	public static void main(String[] args) throws IOException {
		while (true) {
			BufferedReader in = 
					new BufferedReader(new InputStreamReader(System.in));
			String[] commands = in.readLine().replaceAll("\\s{1,}", " ").split(" ");
			String command = commands[0];
			List<String> files = new ArrayList<String>();
			for(int i=1; i<commands.length; i++){
				files.add(commands[i]);
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
	
	void setCoding(String coding){
		for(Iterator it=serverList.iterator(); it.hasNext();){
			((Server)it.next()).setCoding(coding);
			log.info("Set transfer coding to " + coding);
		}
		
	}
	void setPort(String port){
		for(Iterator it=serverList.iterator(); it.hasNext();){
			((Server)it.next()).setPort(port);
			log.info("Set transfer coding to " + port);
		}
	}
	void setLocalDir(String localDir){
		for(Iterator it=serverList.iterator(); it.hasNext();){
			((Server)it.next()).setLocalDir(localDir);
			log.info("Set transfer coding to " + localDir);
		}
	}
	void setRemoteDir(String remoteDir){
		for(Iterator it=serverList.iterator(); it.hasNext();){
			((Server)it.next()).setRemoteDir(remoteDir);
			log.info("Set transfer coding to " + remoteDir);
		}
	}
	void putfiles(List fileList){
		for(Iterator files=fileList.iterator(); files.hasNext();){
			String file = (String)files.next();
			for(Iterator servers=this.serverList.iterator(); servers.hasNext();){
				new Thread(new PutFile((Server)servers.next(), file)).run();
			}
		}
	}
	void removeFiles(List fileList){
		for(Iterator files=fileList.iterator(); files.hasNext();){
			String file = (String)files.next();
			for(Iterator servers=this.serverList.iterator(); servers.hasNext();){
				new Thread(new RemoveFiles((Server)servers.next(), file)).run();
			}
		}
	}
	void printHelp(){
		System.out.println("Usage: \n"
				+ "     put file [file2 file3 ...]  \n"
				+ "        --put the file(s) of current directory to the current remote directory\n"
				+ "        --use lcd command to change local directory."
				+ "        --use cd command to change remote directory."
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
