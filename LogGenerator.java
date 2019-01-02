// imports
import java.io.*;
import java.util.*;
import java.util.logging.*;

public class LogGenerator
{
	//Standard logger data member
	private final static Logger LOGGER = Logger.getLogger(LogGenerator.class.getName());

	//File size in MB for log generation
	private int    FileSize;

	//Machine ID
	private int MachineID;

	private String FileName;
	File file;

	private FileHandler fileHandler;

	//Total log messages logged already
	private int TotalMessages = 0;

	//Flag ondicator for the logging should continue or not
	private boolean runStatus = true;


	//Init class members
	LogGenerator(String log_path, int FileSize, int MachineID)
	{
		this.MachineID = MachineID;
		this.FileName = log_path + "/machine." + MachineID + ".log";
		this.FileSize = FileSize;
		file = new File(FileName);
	}

	//Update the run Status
	private void updateRunStatus()
	{
		//Get the number of bytes in the file (bytes)
		long length = file.length();
		if(length > (FileSize * 1024 * 1024) )
			runStatus = false;
	}

	/*
	***Log generation
	Takes LogFormatter.java definition for generate random log
	in a determined machine
	*/
	private void generateLog()
	{
		int num1 = 0; int num2; int SomeMachineID1 = 0; int SomeMachineID2 = 0;
		//Information saved to be printed in the log
		Random gen1 = new Random(System.currentTimeMillis());
		Random gen2 = new Random(System.currentTimeMillis());

		while(runStatus)
		{
			//Proposeed algortih to generate the random log files
			num2 = gen2.nextInt(4) + 1;
			while(num2 == MachineID)
			{
				num2 = gen2.nextInt(4) + 1;
			}

			if(num2 > MachineID)
			{
				SomeMachineID1 = MachineID;
				SomeMachineID2 = num2;
			}
			else
			{
				SomeMachineID1 = num2;
				SomeMachineID2 = MachineID;
			}
			num1 = gen1.nextInt(100);
			if(num1 >= 30)
			{
				// Log frequent messages
				if(num1%2 == 0) //50%
					LOGGER.info("INFO log.  MachineID:" + MachineID + " SomeMachineIDs: " + SomeMachineID1 + "," + SomeMachineID2);
				else
					LOGGER.warning("WARNING log. MachineID: " + MachineID + "  SomeMachineIDs: " + SomeMachineID1 + "," + SomeMachineID2);
			}
			else if (num1 >= 5 && num1 < 30)
			{
				// Log somewhat frequent messages
				if(num1%2 == 0)
					LOGGER.fine("FINE.  MachineID: " + MachineID + "  SomeMachineIDs: " + SomeMachineID1 + "," + SomeMachineID2);
				else
					LOGGER.finest("FINEST. MachineID: "+  MachineID + "  SomeMachineIDs: " + SomeMachineID1 + "," + SomeMachineID2);
			}
			else
			{
				// Log rare message < 5%
				LOGGER.severe("SEVERE. MachineID: " + MachineID + "  SomeMachineIDs: " + SomeMachineID1 + "," + SomeMachineID2);
			}

			TotalMessages = TotalMessages + 1;
			if(TotalMessages % 5 == 0)
				updateRunStatus();
		}
	}

	//Set FileHandler attributes
	private void SetFileHandler()
	{
		try {
			fileHandler = new FileHandler(FileName);
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			LOGGER.log(Level.SEVERE, null, ex);
		}
		//Set the formatting
		fileHandler.setFormatter(new LogFormatter());
		LOGGER.addHandler(fileHandler);
		//Set all levels
		LOGGER.setLevel(Level.ALL);
		//Turn off console logging
		LOGGER.setUseParentHandlers(false);
	}

	//Common generate function
	private void generate()
	{
		SetFileHandler();
		generateLog();
	}

	// Main Funtion
	public static void main(String[] args)
	{
	 if (args.length < 3) {
      System.out.println("Usage: java logGenerator <log_path> <filesize> <MachineID>");
      System.exit(-1);
    }

		LogGenerator MyLogGenerator = new LogGenerator(args[0], Integer.parseInt(args[1]),Integer.parseInt(args[2]));
		MyLogGenerator.generate();
	}
}
