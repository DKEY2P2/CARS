/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javafx.scene.input.KeyCode;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
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
        setLocationRelativeTo(null);
        addKeyListener(new KeyAdapter() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
            
        });
        setDefaultCloseOperation(3);
//        setUndecorated(true);
        setVisible(true);
        createBufferStrategy(2);
    }
    
    BufferedImage Scene;

    /**
     * Get the item to draw everything
     *
     * @return The graphics
     */
    public Graphics getGraphic() {
        return this.getBufferStrategy().getDrawGraphics();
    }

    /**
     * Draws the image
     */
    public void getScene() {
        BufferStrategy bi = this.getBufferStrategy();
        bi.show();
        Toolkit.getDefaultToolkit().sync();
    }
    
    public static void main(String[] args) {
        new Canvas();
    }
    
}
