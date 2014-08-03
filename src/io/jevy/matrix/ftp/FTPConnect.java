package io.jevy.matrix.ftp;

import io.jevy.matrix.conf.Server;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class FTPConnect {
	private static Logger log = Logger.getLogger(FTPConnect.class.getName());
	FTPClient cli = new FTPClient();
	
	FTPClient getConnect(Server server) {
		int reply = 0;
		try {
			cli.connect(server.ip);
			reply = cli.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				cli.disconnect();
				log.error("PowerFTP: FTP server refused connection.");
			}
			cli.login(server.user, server.pass);
		} catch (IOException e) {
			log.error(e);
			cli = null;
		}
		return cli;
	}

	void closeConnect() {
		try {
			cli.logout();
			cli.disconnect();
		} catch (IOException e) {
			log.error( e);
		} finally {
			if (cli.isConnected()) {
				try {
					cli.disconnect();
				} catch (IOException ioe) {
					log.error(ioe);
				}
			}
		}
	}
}
