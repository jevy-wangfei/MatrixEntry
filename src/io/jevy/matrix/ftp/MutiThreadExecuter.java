package io.jevy.matrix.ftp;

import io.jevy.matrix.conf.Server;
import io.jevy.matrix.util.ConsoleProgressBar;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

public class MutiThreadExecuter {

	private static Logger log = Logger.getLogger(MutiThreadExecuter.class.getName());
	
	void mutiRun(List<Server> serverList, FTPOperatorEnum operator, String file){
		
		log.info("The given operator is " + operator);
		
		int successNumber = 0;
		for(Iterator<Server> servers=serverList.iterator(); servers.hasNext();){
			Executer exe = new Executer(servers.next(), operator, file);
			new Thread(exe).run();
			
			if(exe.flag)
				successNumber++;
			new ConsoleProgressBar(successNumber, serverList.size(), 50);
		}
	}
	
}
