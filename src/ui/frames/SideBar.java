package ui.frames;

import controller.StartDoingStuff;
import helper.Logger;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ui.setting.GraphicsSetting;

/**
 * A sidebar to hold all the items that control the simulation
 *
 * @author Kareem Horstink
 */
public class SideBar extends JFrame {

    private JLabel tickCounterL;
    private JLabel timeL;

    public SideBar(JFrame parent) {
        setTitle("Options");
        JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.NORTH);
        //Exits if escape key has been pressed
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    //Prints what in the logs
                    Logger.print();
                    Logger.printOther();

                    //Exit the application
                    System.exit(0);
                }
            }

        });
        JPanel general = new JPanel();
        //Set the layout
        general.setLayout(new GridLayout(0, 1));
        //Set it to hid when you close it
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //Gets the size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //Set the size of the window
        setSize((int) (GraphicsSetting.getInstance().getScale() * 0.3 * screenSize.getWidth()), parent.getHeight());
        //Set the location of the window to be next to the main screen
        setLocation(parent.getX() + parent.getWidth(), parent.getY());

        //Whether the OS decoration should be enabled
        if (!GraphicsSetting.getInstance().isDecorated()) {
            setUndecorated(true);
        }
        /*Add the buttons to the interface*/
        //Start button is added
        JButton button = new JButton("Start");
        button.addActionListener((ActionEvent e) -> {
            StartDoingStuff.start();
        });
        general.add(button);

        //The ticker counter is added
        JPanel tickCounterP = new JPanel();
        tickCounterL = new JLabel("0");
        tickCounterP.add(new JLabel("Tick counter:"));
        tickCounterP.add(tickCounterL);

        //The time elapsed is added
        JPanel timeP = new JPanel();
        timeP.add(new JLabel("Time elapsed (s):"));
        timeL = new JLabel("0");
        timeP.add(timeL);
        general.add(tickCounterP);
        general.add(timeP);

        //The speed of the simulation changer is added
        JPanel speedP = new JPanel();
        SpinnerModel spinnerModel = new SpinnerNumberModel(controller.Controller.getInstance().getTicker().getTickTimeInMS(), 1, 10000, 1);
        JSpinner speedSpinner = new JSpinner(spinnerModel);
        speedSpinner.addChangeListener(new ChangeListenerCustom());
        speedP.add(new JLabel("Speed of animation (ms)"));
        speedP.add(speedSpinner);
        general.add(speedP);
        jTabbedPane.add("General",general );
        
        JPanel stats = new JPanel();
        
        jTabbedPane.add("Statistics" , stats);
        
        JPanel mapControl = new JPanel();
        
        jTabbedPane.add("Map Controls", mapControl);
        
        JPanel vehicle = new JPanel();
        
        jTabbedPane.add("Vehicle Settings", vehicle);
        
        
        add(jTabbedPane);
        //Set it to visiable
        setVisible(true);

    }

    @Override
    public void repaint() {
        //Updates some elements
        int n = controller.Controller.getInstance().getTicker().getTickCount();
        tickCounterL.setText(Integer.toString(n));
        timeL.setText(Double.toString(Math.round(controller.Controller.getInstance().getTicker().getTimeElapsed() * 100) / 100d));
        super.repaint();
    }

    //The change listener for the spinner
    private class ChangeListenerCustom implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() instanceof JSpinner) {
                JSpinner s = (JSpinner) e.getSource();
                controller.Controller.getInstance().getTicker().setTickTimeInS(((Double) s.getModel().getValue()) / 1000d);
            }
        }

    }

}
