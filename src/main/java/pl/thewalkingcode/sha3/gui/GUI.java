package pl.thewalkingcode.sha3.gui;

import javax.swing.*;
import java.awt.*;

public class GUI {

    public static void createAndShowUI() {
        JFrame frame = new JFrame("SHA3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel sha3TextMenuCard = new Sha3TextMenu();
        JPanel sha3FileMenuCard = new Sha3FileMenu();

        tabbedPane.addTab("SHA3 - Text", sha3TextMenuCard);
        tabbedPane.addTab("SHA3 - File", sha3FileMenuCard);

        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }
}
