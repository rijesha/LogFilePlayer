import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.*;
import javax.swing.event.*;


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
        }
    }


	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command == "Start" && slider.isEnabled()) {
			slider.setEnabled(false);
			System.out.println("Starting");
		} else if (command == "Stop"){

			slider.setEnabled(true);
			System.out.println("Stooping");
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
					Thread.sleep((int) dp.time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (dp.data != null)
					System.out.println(dp.data);
				
				i++;
		     }
		}
		
	}
	

}
