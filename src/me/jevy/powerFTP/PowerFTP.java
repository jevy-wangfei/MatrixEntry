package me.jevy.powerFTP;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.net.ftp.FTPClient;

import org.xml.sax.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.*;

public class PowerFTP {
	File file = null;
	int num = 0;
	
	//Test did

	List<Server> servers = null;

	Date date = new Date();

	String ran = "" + date.getYear() + date.getMonth() + date.getDay()
			+ date.getHours() + date.getMinutes() + date.getSeconds();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner c = new Scanner(System.in);
		String first = c.next();
		String second = c.next();
		PowerFTP jftp = new PowerFTP();
		if ("-f".equals(first)) {
			String file = second;
			jftp.jConfigFile(file);
		} else if ("-c".equals(first)) {
			String[] parms = second.split(":");
			String serverIP = parms[0];
			String user = parms[1];
			String passwd = parms[2];
			String transfer = parms[3];
			String dir = parms[4];
			String file = parms[5];
			String mode = null;
			if (parms.length == 7) {
				mode = parms[6];
			}
			if("get".equals(transfer.toLowerCase())){
				jftp.jFileGet(serverIP, user, passwd,dir, file, mode);
			}else if("put".equals(transfer.toLowerCase())){
				jftp.jFilePut(serverIP, user, passwd, dir, file, mode);
			}
			
		} else if("-d".equals(first)){
			String[] parms = second.split(":");
			String serverIP = parms[0];
			String user = parms[1];
			String passwd = parms[2];
			String transfer = parms[3];
			String dir = parms[4];
			if("get".equals(transfer.toLowerCase())){
				jftp.jDirGet(serverIP, user, passwd, dir);
			}else if("put".equals(transfer.toLowerCase())){
				jftp.jDirPut(serverIP, user, passwd, dir);
			}
		}
		else if (null == first || "-f".equals(c.next())|| "-c".equals(c.next())) {
			System.out
					.println("Usage: \n"
							+

							  "     -f xmlConfigureFile:[ascii/bin]  \n"+
							  "        --Use Servers configure file to control up/down load file\n"+
							  "     -c serverIP:user:passwd:getORput:dir:file:[ascii/bin]\n" +
							  "        --Get Or Put a File, Transfer Mode is Auto if No Identify\n" +
							  "     -d serverIP:user:passwd:getORput:dir\n" +
							  "        --Get Or Put a Dir, Transfer Mode is Auto");
		}

	}

	void jConfigFile(String file) {
		System.out.println("Total Files:" + this.servers.size());
		for (int i = 0; i < this.servers.size(); i++) {
			if ("get".equals(this.servers.get(i).getMode())) {
				this.jGet((Server) this.servers.get(i));
				this.num++;
				System.out.println("--" + (i + 1) + "/" + this.servers.size()
						+ " Files geted,  Transfer mode: "
						+ this.servers.get(i).getTransfer());
			} else {
				this.jPut((Server) this.servers.get(i));
				this.num++;
				System.out.println("--" + (i + 1) + "/" + this.servers.size()
						+ " Files puted,  Transfer mode: "
						+ this.servers.get(i).getTransfer());
			}
		}
		System.out.println("Success Transfer files: " + this.num + "/"
				+ this.servers.size());
		System.out.println("BYE");
	}

	public void jFileGet(String serverIP, String user, String passwd,
			 String dir, String file,  String mode) {

	}
	
	public void jFilePut(String serverIP, String user, String passwd,
			 String dir, String file, String mode) {

	}

	void jDirGet(String serverIP, String user, String passwd,
			 String dir) {

	}

	void jDirPut(String serverIP, String user, String passwd,
			 String dir) {

	}

	void jGet(Server ser) {
		FTPClient cli = this.conn(ser.getIp(),ser.getUser(),ser.getPass());
		try {
			if (ser.getRemoteDir().length() != 0) {
				cli.cd(ser.getRemoteDir());
			}
			TelnetInputStream is = cli.get(ser.getFile());

			File file_out = new File(ser.getLocalDir());
			// System.out.println(file_out.getPath()+file_out.getName());
			File[] files = file_out.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().toString().equals(
						ser.getFile().toString())) {
					// System.out.println(i+files[i].getName()+":"+ser.file);
					files[i].renameTo(new File(ser.getLocalDir()
							+ files[i].getName() + ".bak" + this.ran));
				}
			}
			file_out.delete();
			file_out = new File(ser.getLocalDir() + ser.getFile());
			FileOutputStream os = new FileOutputStream(file_out);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = is.read(bytes)) != -1) {
				os.write(bytes, 0, c);
			}
			is.close();
			os.close();
			cli.closeServer();
		} catch (IOException ex) {
			System.out.println("Get File Error:");
			ex.printStackTrace();
			this.num--;
		}
	}

	void jPut(Server ser) {
		FtpClient cli = this.conn(ser.getIp(),ser.getUser(),ser.getPass());
		try {
			if (ser.getRemoteDir().length() != 0) {
				cli.cd(ser.getRemoteDir());
			}
			List filesList = new ArrayList();
			DataInputStream dis = new DataInputStream(cli.nameList("./"));
			String filename = "";
			while ((filename = dis.readLine()) != null) {
				filesList.add(filename);
			}
			for (int n = 0; n < filesList.size(); n++) {
				if (ser.getFile().toString()
						.equals(filesList.get(n).toString())) {
					cli.rename(filesList.get(n).toString(), filesList.get(n)
							+ ".bak" + this.ran);
				}
			}

			TelnetOutputStream os = cli.put(ser.getFile());
			File file_in = new File(ser.getLocalDir() + ser.getFile());
			FileInputStream is = new FileInputStream(file_in);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = is.read(bytes)) != -1) {
				os.write(bytes, 0, c);
			}
			is.close();
			os.close();

			cli.closeServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.num--;
		}

	}

	FTPClient conn(String serverIP, String user, String passwd) {
		FTPClient cli = new FTPClient();
		try {
			cli.openServer(serverIP);
			// System.out.println(ser.user+ser.pass);
			cli.login(user, passwd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print("Connection error: ip=" + serverIP + " user="
					+ user + " Password=" + passwd);
			e.printStackTrace();
			cli = null;
		}
		return cli;
	}
}
