package wf.jevy.matrix.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import wf.jevy.matrix.conf.Server;

public class Executer implements Runnable{
	private Server server;
	private String file;
	private FTPOperatorEnum oper;
	public boolean flag = false;
	private static Logger log = Logger.getLogger(Executer.class.getName());
	
	public Executer(Server server, FTPOperatorEnum operator, String file ) {
		this.server = server;
		this.file = file;
		oper = operator;
	}
	
	public void execute(){
		FTPConnect conn = new FTPConnect();
		FTPClient cli = conn.getConnect(this.server);
		try {
			//cli.setFileTransferMode(mode)
			switch(oper){
			case CD: 
				cli.changeWorkingDirectory(server.getRemoteDir());
					break;
			case LCD: 
				break;
			case PUT: 
				FileInputStream fileInput=new FileInputStream(new File(server.getLocalDir() + "/" + this.file));
				this.flag = cli.storeFile(file, fileInput);
				fileInput.close();
				break;
			case GET: 
				File newFile = new File(server.getLocalDir() + "/" + this.server.getIp()+"/"+this.file);
				if(!newFile.exists()){
					newFile.mkdir();
				}
				FileOutputStream fileOutput=new FileOutputStream(server.getLocalDir() + "/" + this.server.getIp()+"/"+this.file);
				cli.setBufferSize(1024);
				cli.setFileType(FTPClient.BINARY_FILE_TYPE);
				this.flag = cli.retrieveFile(this.server.remoteDir + "/" + this.file, fileOutput);
				if(!this.flag){
					newFile.delete();
					log.error("The file downloading is not exist.");
				}
				fileOutput.close();
				break;
			case RM:
				flag = cli.deleteFile(server.getRemoteDir() + "/" + this.file);
				break;
			case PWD:
				break;
			case LPWD:
				break;
			case PORT:
				break;
			case ASCII:
				break;
			case BIN:
				break;
			
			}
			
		} catch (IOException e) {
			log.error("PutFile.put(): " + e);
		}
		conn.closeConnect();
        
    }

	@Override
	public void run() {
		this.execute();
	}
}
