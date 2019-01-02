import java.io.*;
import java.net.*;
import java.util.*;

class LogQueryClient {
	private Socket sock;

	//Set of information <IP, Port> values
	private ArrayList< ServerProperties > serverList = new ArrayList< ServerProperties >();

	public void addServer(ServerProperties server) {
		//Add <IP, Port> to the system list
		serverList.add(server);
	}

	public void connect(String[] args) {

		//One thread per each worker server connection
		ArrayList< LogQueryClientThread > worker_list = new ArrayList< LogQueryClientThread >();

		//Initialize the worker
		for (ServerProperties server : serverList) {
			LogQueryClientThread worker = new LogQueryClientThread(server, Arrays.asList(args), this);
			worker_list.add(worker);
			worker.start();
		}

		//Socket logic for fetching the results of each server
		for (LogQueryClientThread worker : worker_list) {
			try {
				worker.join();
				printResult(worker.getServer(), worker.getResultFile());
			} catch(InterruptedException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	//Formatted printing result
	private void printResult(ServerProperties server, File resultFile) {
		if (resultFile == null) {
			return;
		}
		resultFile.deleteOnExit();
		System.out.println("Result from the Server " + server.ip + ":" + server.port + ":");
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

/*
	public void read(File file){
		try {
			Scanner scanner = new Scanner(file);
	    while(scanner.hasNext()){
				System.out.println("line");
	      String[] tokens = scanner.nextLine().split(" ");
	      String last = tokens[tokens.length - 1];
	      System.out.println(last);
	    }
		} catch(FileNotFoundException ex) {
			System.out.println(ex.getMessage());
			System.exit(-1);
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
*/

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java LogQueryClient <grep_arg1> <grep_arg2> ... <grep_argN>");
			//System.out.println("Connecting to <IP, Port> format on serverList.txt");
		}
		System.exit(-1);
		LogQueryClient client = new LogQueryClient();

		//Fetching the <IP, Port> values defined in serverList.txt
		//File serverListFile = new File(System.getProperty("user.home"), "serverList.txt");
		//client.read(serverListFile);

		client.addServer(new ServerProperties("127.0.0.1", 1024));
		client.addServer(new ServerProperties("192.168.1.101", 1024));
		client.addServer(new ServerProperties("192.168.1.106", 1024));
		client.connect(args);
	}
}
