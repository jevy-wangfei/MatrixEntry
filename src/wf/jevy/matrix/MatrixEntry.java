package wf.jevy.matrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import wf.jevy.matrix.ftp.FTP;

public class MatrixEntry {

	boolean run = true;

	public static void main(String[] args) {

		if (args.length == 0) {
			new MatrixEntry().getInput();

		} else {
			new MatrixEntry().callService(args[0].trim());
		}

	}

	void getInput() {
		while (run) {
			System.out.print("Matrix > ");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String[] commands = null;
			try {
				commands = in.readLine().replaceAll("\\s{1,}", " ").split(" ");
			} catch (IOException e) {
				Logger.getLogger(FTP.class.getName()).error(e);
			}
			if (commands.length == 0) {
				System.out.println("The command you put is null.");
			} else {
				callService(commands[0].trim());
			}
		}
		System.out.println("Matrix Engry is exited.");
	}

	void callService(String command) {
		if (command.length() > 0) {

			command = command.trim().toUpperCase();
			ToolsEnum tool = null;
			try {
				tool = ToolsEnum.valueOf(command);
			} catch (IllegalArgumentException e) {
				System.out.println("  Illegal Argument input: " + command);
			}

			if (tool != null) {
				switch (tool) {
				case FTP:
					new FTP().createClient();
					break;
				case TELNET:
					break;
				case SFTP:
					break;
				case SSH:
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
					System.out.println("  Individual input" + command);
					break;
				}
			}
		}
	}

	void createTelnetClient() {
		// Create related operation in the package of io.jevy.matrix.telnet.
	}

	void createSshClient() {
		// Create related operation in the package of io.jevy.matrix.ssh.
	}

	void printHelp() {

	}

}
