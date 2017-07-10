import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.*;
import javax.swing.event.*;

import java.net.HttpURLConnection;
import java.net.URL;

import java.io.OutputStreamWriter;


/*
 * SliderDemo.java requires all the files in the images/doggy
 * directory.
 */
public class UI extends JPanel implements ChangeListener, ActionListener, Runnable {
    static final int FPS_MAX = 30;
    static final int FPS_INIT = 1;    //initial frames per second
	
	private static JButton startButton = new JButton("Start");
	private static JButton stopButton = new JButton("Stop");
	
	private static TreeMap<Long, Integer> timeMap = new TreeMap<Long, Integer>();
	private static ArrayList<DataPoint> data = new  ArrayList<DataPoint>();
	
	private static JSlider slider;
	private static JLabel sliderLabel;
	
    
    public UI(TreeMap<Long, Integer> timeMap, ArrayList<DataPoint> data) {
    	this.timeMap =  timeMap;
    	this.data = data;
    	
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        //Create the label.
        sliderLabel = new JLabel("Time: ", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Create the slider.
        slider = new JSlider(JSlider.HORIZONTAL, 0, timeMap.size(), FPS_INIT);

        slider.addChangeListener(this);

        //Turn on labels at major tick marks.

        slider.setMajorTickSpacing(7680);
        slider.setMinorTickSpacing(1280);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        add(sliderLabel);
        add(slider);
        startButton.addActionListener(this);
		add(startButton);
		stopButton.addActionListener(this);
		add(stopButton);
		
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    }
    
 
    /** Listen to the slider. */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            int fps = (int)source.getValue();
			DataPoint dp = data.get(fps);
			sliderLabel.setText("Time: " + dp.absoluteTime/1000);
        }
    }

	public void sendHTTPRequest(double lat, double lon, double alt) {
		try {
			URL url = new URL("http", "127.0.0.1",1919, "null");
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod("PUT");
			OutputStreamWriter out = new OutputStreamWriter(
    		httpCon.getOutputStream());
			String msg = "{\"fillcolor\": \"orange\", \"status\": 0, \"linecolor\": \"yellow\", \"id\": 22, \"shape\": 0, \"msgname\": \"SHAPE\", \"msgclass\": \"ground\", \"radius\": 10, \"lonarr\": [" + (int) (lon*10000000) + ","+ (int) (lon*10000000) +"], \"opacity\": 2, \"latarr\": [" + (int) (lat*10000000) + ","+ (int) (lat*10000000) +"], \"text\": "  + alt + "} ";
			out.write(msg);
			out.close();
			httpCon.getInputStream();		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command == "Start" && slider.isEnabled()) {
			slider.setEnabled(false);
		} else if (command == "Stop"){
			slider.setEnabled(true);
		}
		
	}

	@Override
	public void run() {
		while (true) {

	    	try {
				Thread.sleep(125);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			int i = slider.getValue();
			while(i < data.size() && !slider.isEnabled()) {
		         DataPoint dp = data.get(i);
				 slider.setValue(dp.index);
				 sliderLabel.setText("Time: " + dp.absoluteTime/1000);
		         try {
					Thread.sleep((int) dp.time*4);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (dp.data != null) {
					System.out.println(dp.data);
					if (dp.hasGPS) {
						sendHTTPRequest(dp.lat, dp.lon, dp.alt);
					}						
				}
					
				i++;
		     }
		}
		
	}
	

}
