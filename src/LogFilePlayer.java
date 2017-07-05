import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.TreeMap;

public class LogFilePlayer {
	private static String logFilePath = "tempPath";
	private static boolean debug = false;
	
	public static void main(String[] args) {
		parseCLI(args);
		
		File file = new File(logFilePath);
		BufferedReader reader = null;
		
		TreeMap<Long, Integer> timeMap = new TreeMap<Long, Integer>();
		ArrayList<DataPoint> data = new  ArrayList<DataPoint>();
		
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
		
		Iterator<DataPoint> iter = data.iterator();
		
		while(iter.hasNext()) {
	         DataPoint dp = iter.next();
	         try {
				Thread.sleep((int) dp.time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dp.data != null)
				System.out.println(dp.data);
	     }
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
	
	public static class DataPoint {
		public float time;
		public String data;
		public DataPoint(float time, String data){
			this.time = time;
			this.data = data;
		}		
	}

	
	private static void newFrame(){
		JFrame frame = new JFrame("Log File Player -- " + logFilePath);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = frame.getContentPane();
		
        frame.setSize(50, 75);
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.ipady = 25;      //make this component tall
		c.ipadx = 25;      //make this component tall
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.weighty = 1;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 1;       //reset to default
		c.weighty = 1;   //request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; //bottom of space
		c.gridx = 0;
		
		frame.pack();
		frame.setVisible(true);
	}
	


}
