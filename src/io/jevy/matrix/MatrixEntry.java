package io.jevy.matrix;

import io.jevy.matrix.ftp.PowerFTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class MatrixEntry {
	
	private Logger log = Logger.getLogger(PowerFTP.class.getName());
	
	public MatrixEntry() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		
		if(args == null){
			BufferedReader in = 
					new BufferedReader(new InputStreamReader(System.in));
			String[] commands = null;
			try {
				commands = in.readLine().replaceAll("\\s{1,}", " ").split(" ");
			} catch (IOException e) {
				Logger.getLogger(PowerFTP.class.getName()).error(e);
			}
			if(commands == null){
				new NullPointerException("The command you put is null.");
			}
			new MatrixEntry().callService(commands[0]);
			
		}else{
			new MatrixEntry().callService(args[0]);
		}
		
		
	}
	
	void callService(String command){
		command = command.toUpperCase();
		if("FTP".equals(command.toUpperCase())){
			new PowerFTP().createClient();
		}else if("TELNET".equals(command.toUpperCase())){
			createTelnetClient();
		}else if("SSH".equals(command.toUpperCase())){
			createSshClient();
		}else if("HELP".equals(command.toUpperCase()) || "?".equals(command.toUpperCase())){
			printHelp();
		}else{
			log.error("Ellegal command input.");
			printHelp();
		}
	}
	
	
	void createTelnetClient(){
		//Create related operation in the package of io.jevy.matrix.telnet.
	}
	
	void createSshClient(){
		//Create related operation in the package of io.jevy.matrix.ssh.
	}
	
	void printHelp(){
		
	}

}
