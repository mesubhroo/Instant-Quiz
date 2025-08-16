package Instant_Quiz.com;

import java.awt.*;
import javax.swing.*;

public class Frame extends JFrame {
    Frame(){
        this.setSize(800,600);
        this.setTitle("Kaun Banega Crorepati");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon image = new ImageIcon("src/corepati.jpg");
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(new Color(19, 31, 217));
    }
}

