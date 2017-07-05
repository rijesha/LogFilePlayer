import java.awt.BorderLayout;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.JFrame;

import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

public class LogFilePlayer {
	private static String logFilePath = "tempPath";
	private static boolean debug = false;
	
	private static TreeMap<Long, Integer> timeMap = new TreeMap<Long, Integer>();
	private static ArrayList<DataPoint> data = new  ArrayList<DataPoint>();

	
	public static void main(String[] args) {
		createAndShowGUI();
		parseCLI(args);
		
		File file = new File(logFilePath);
		BufferedReader reader = null;
		
		try {
			boolean first = true;
			Long baseTime = (long) 0;
			Long lastTime = (long) 0;
			reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    String lastData = null;
		    int index = 0;
		    
		    Long absoluteTime;
	    	Long relativeTime;
		    
		    while ((text = reader.readLine()) != null) {
		    	Long timeStamp = Long.valueOf(text.split("\\s+")[0]);
		    	if (first) {
		    		baseTime = timeStamp;
		    		lastTime = timeStamp;
		    		first = false;
		    	}
		    	absoluteTime = timeStamp - baseTime;
		    	relativeTime = timeStamp - lastTime;
		    	if (debug) {
					System.out.println(absoluteTime);
		    		System.out.println(relativeTime);
				}
		    	
		    	timeMap.put(absoluteTime, index);
		    	data.add(new DataPoint(relativeTime, lastData));
		    	
		    	lastTime = timeStamp;
		    	lastData = text;
		    	index++;
		    }
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {
		    }
		}
		
		createAndShowGUI();
	}
	
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SliderDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UI ui = new UI(timeMap, data);
        
        Thread controlTH = new Thread(ui);
        controlTH.start();

        //Add content to the window.
        frame.add(ui, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	private static void parseCLI(String[] args) {
		Options options = new Options();

        Option logFilePathOption = new Option("f", "log_file", true, "path to log file");
        options.addOption(logFilePathOption);

        Option debugOption = new Option("d", "debug", false, "print debug");
        options.addOption(debugOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Real Time FFT Grapher", options);

            System.exit(1);
            return;
        }

        logFilePath = cmd.getOptionValue("log_file");
		debug = cmd.hasOption("debug");
	}
	

}
