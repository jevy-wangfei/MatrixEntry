package io.jevy.matrix.ftp;

import io.jevy.matrix.conf.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;


public class PutFile implements Runnable {
	private Server server;
	private static Logger log;
	private String file;
	public boolean flag = false;

	public PutFile(Server server, String file) {
		// TODO Auto-generated constructor stub
		this.server = server;
		this.file = file;
		log = Logger.getLogger(PutFile.class.getName());
	}
	
	
	
	void put(){
		//System.out.println("File put....");
		
		FTPClient cli = new FTPClient();
		int reply = 0;
		try {
			cli.connect(this.server.ip);
			reply = cli.getReplyCode();
			if(!FTPReply.isPositiveCompletion(reply)) {
		        cli.disconnect();
		        log.error("PowerFTP: FTP server refused connection.");
		        System.exit(1);
		      }
			cli.login(this.server.user, this.server.pass);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("PutFile.put(): " + e);
			cli = null;
		}
		
		
		try {
			//cli.setFileTransferMode(mode)
			cli.changeWorkingDirectory(server.getRemoteDir());
			FileInputStream fileInput=new FileInputStream(new File(server.getLocalDir() + "/" + this.file));
			this.flag = cli.storeFile(file, fileInput);
			fileInput.close();
			cli.logout();
			cli.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("PutFile.put(): " + e);
		}finally {   
            if (cli.isConnected()) {   
                try {   
                    cli.disconnect();   
                } catch (IOException ioe) {   
                	log.error("PutFile.put(): " + ioe);
                }   
            }   
        }   
        
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.put();

	}


	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
