package wf.jevy.matrix.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wf.jevy.matrix.MatrixEntry;
import wf.jevy.matrix.ToolsEnum;
import wf.jevy.matrix.conf.ConfigReader;
import wf.jevy.matrix.conf.Server;

public class FTP {

	private List<Server> serverList = new ConfigReader().getServerList();
	String[] commands = null;
	String command = null;
	List<String> files = new ArrayList<String>();
	boolean run = true;
	
	public void createClient() {
		while (run) {
			System.out.print("Matrix FTP >> ");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			try {
				commands = in.readLine().replaceAll("\\s{1,}", " ").split(" ");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (commands.length == 0) {
				System.out.println("The command you put is null.");
			} else {
				command = commands[0].trim();
				for (int i = 1; i < commands.length; i++) {
					files.add(commands[i]);
				}
				if (command.length() > 0)
					callExecuter();
			}
		}
	}

	public void callExecuter() {

		FTPOperatorEnum comm = null;
		try {
			comm = FTPOperatorEnum.valueOf(command.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			System.out.println("  Illegal Argument input: " + command);
		}

		if (comm != null) {
			switch (comm) {
			case CD:
				new MutiThreadExecuter().mutiRun(serverList,
						FTPOperatorEnum.CD, null);
				break;
			case LCD:
				new MutiThreadExecuter().mutiRun(serverList,
						FTPOperatorEnum.LCD, null);
				break;
			case PUT:
				for (Iterator<String> file = files.iterator(); file.hasNext();) {
					new MutiThreadExecuter().mutiRun(serverList,
							FTPOperatorEnum.PUT, file.next());
				}
				break;
			case GET:
				for (Iterator<String> file = files.iterator(); file.hasNext();) {
					new MutiThreadExecuter().mutiRun(serverList,
							FTPOperatorEnum.GET, file.next());
				}
				break;
			case RM:
				for (Iterator<String> file = files.iterator(); file.hasNext();) {
					new MutiThreadExecuter().mutiRun(serverList,
							FTPOperatorEnum.RM, file.next());
				}
				break;
			case PWD:
				new MutiThreadExecuter().mutiRun(serverList,
						FTPOperatorEnum.PWD, null);
				break;
			case LPWD:
				new MutiThreadExecuter().mutiRun(serverList,
						FTPOperatorEnum.LPWD, null);
				break;
			case PORT:
				new MutiThreadExecuter().mutiRun(serverList,
						FTPOperatorEnum.PORT, null);
				break;
			case ASCII:
				new MutiThreadExecuter().mutiRun(serverList,
						FTPOperatorEnum.ASCII, null);
				break;
			case BIN:
				new MutiThreadExecuter().mutiRun(serverList,
						FTPOperatorEnum.BIN, null);
				break;
			case HELP:
				printHelp();
				break;
			case QUIT:
				this.run = false;
				break;
			case EXIT:
				this.run = false;
				break;
			case BYE:
				this.run = false;
				break;
			default:
				System.out.println("  Individual input: " + command);
				break;
			}
		}
	}

	public void printHelp() {
		System.out
				.println("Usage: \n"
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
						+ "     pwd (Show current remote directory)\n"
						+ "		lpwd (Show current local directory)\n"
						+ "     assii\n"
						+ "        --set transfer coding to assii.\n"
						+ "     bin\n"
						+ "        --set transfer coding to bin\n"
						+ "     port number\n"
						+ "        --set file transfer port to number, default port is 21.");
	}

}
