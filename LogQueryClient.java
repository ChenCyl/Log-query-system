import java.io.*;
import java.net.*;
import java.util.*;

class LogQueryClient {
	private Socket sock;
	private ArrayList< ServerProperties > serverList =
		new ArrayList< ServerProperties >();

	public void addServer(ServerProperties server) {
		serverList.add(server);
	}

	public void connect(String[] args) {
		ArrayList< LogQueryClientThread > worker_list =
			new ArrayList< LogQueryClientThread >();
		for (ServerProperties server : serverList) {
			LogQueryClientThread worker =
				new LogQueryClientThread(server, Arrays.asList(args), this);
			worker_list.add(worker);
			worker.start();
		}
		for (LogQueryClientThread worker : worker_list) {
			try {
				worker.join();
				printResult(worker.getServer(), worker.getResultFile());
			} catch(InterruptedException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	private void printResult(ServerProperties server, File resultFile) {
		if (resultFile == null) {
			return;
		}
		resultFile.deleteOnExit();
		System.out.println(
			"Result from the Server " + server.ip + ":" + server.port + ":");
		try {
			BufferedReader result = new BufferedReader(new FileReader(resultFile));
			String line;
			while ((line = result.readLine()) != null) {
				System.out.println(line);
			}
			result.close();
		} catch(FileNotFoundException ex) {
			System.out.println(ex.getMessage());
			System.exit(-1);
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java logQueryClient <grep_arg1> <grep_arg2> ... <grep_argN>");
			System.exit(-1);
		}
		LogQueryClient client = new LogQueryClient();
		client.addServer(new ServerProperties("127.0.0.1", 1024));
		client.connect(args);
	}
}
