package ui;

import helper.Logger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/**
 * The canvas to draw everything
 *
 * @author Kareem
 */
public class Canvas extends JFrame {

    public Canvas() {
        setTitle("Traffic simulator");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth() / 2;
        int height = (int) screenSize.getHeight() / 2;
        setSize(width, height);
        
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    Logger.print();
                    Logger.printOther();
                    System.exit(0);
                }
            }

        });
        setDefaultCloseOperation(3);
        setUndecorated(true);
        setVisible(true);
        createBufferStrategy(2);
        setLocationRelativeTo(null);

    }

    private BufferStrategy bi = this.getBufferStrategy();

    /**
     * Get the item to draw everything
     *
     * @return The graphics
     */
    public Graphics getGraphic() {
        bi = this.getBufferStrategy();
        Graphics g = bi.getDrawGraphics();
        g.setColor(Color.gray.darker());
        g.fillRect(0, 0, getWidth(), getHeight());
        return g;
    }

    /**
     * Draws the image
     */
    public void getScene() {
        bi.show();
        Toolkit.getDefaultToolkit().sync();
    }

}
