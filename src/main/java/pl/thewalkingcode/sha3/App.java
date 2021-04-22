package pl.thewalkingcode.sha3;

import pl.thewalkingcode.sha3.gui.GUI;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::createAndShowUI);
    }
}
