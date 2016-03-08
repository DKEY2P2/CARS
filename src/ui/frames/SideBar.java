package ui.frames;

import controller.StartDoingStuff;
import helper.Logger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ui.setting.GraphicsSetting;

/**
 *
 * @author Kareem Horstink
 */
public class SideBar extends JFrame {

    JLabel tickCounterL;

    public SideBar(JFrame parent) throws HeadlessException {
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
        setLayout(new GridLayout(0, 1));
        setIconImage(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (GraphicsSetting.getInstance().getScale() * 0.3 * screenSize.getWidth()), parent.getHeight());
        setLocation(parent.getX() + parent.getWidth(), parent.getY());
        if (!GraphicsSetting.getInstance().isDecorated()) {
            setUndecorated(true);
        }
        setVisible(true);
        JButton button = new JButton("Start");
        button.addActionListener((ActionEvent e) -> {
            StartDoingStuff.start();
        });
        add(button);
        JPanel tickCounterP = new JPanel();
        tickCounterL = new JLabel("0");
        tickCounterP.add(new JLabel("Tick counter:"));
        tickCounterP.add(tickCounterL);
        add(tickCounterP);
        JPanel speedP = new JPanel();
        SpinnerModel spinnerModel = new SpinnerNumberModel(1000, 1, 10000, 1);
        JSpinner speedSpinner = new JSpinner(spinnerModel);
        speedSpinner.addChangeListener(new ChangeListenerCustom());
        speedP.add(new JLabel("Speed of animation"));
        speedP.add(speedSpinner);
        add(speedP);

    }

    @Override
    public void repaint() {
        super.repaint();
        int n = controller.Controller.getInstance().getTicker().getTickCount();
        tickCounterL.setText(Integer.toString(n));
    }

    private class ChangeListenerCustom implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() instanceof JSpinner) {
                JSpinner s = (JSpinner) e.getSource();
                controller.Controller.getInstance().getTicker().setTickTimeInS(((Integer) s.getModel().getValue()) / 1000d);
            }
        }

    }

}
