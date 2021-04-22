package pl.thewalkingcode.sha3.gui;

import pl.thewalkingcode.sha3.Sha3;
import pl.thewalkingcode.sha3.Type;
import pl.thewalkingcode.sha3.utils.HexTools;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.FileInputStream;
import java.io.InputStream;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class Sha3FileMenu extends JPanel {

    private final JTextArea outputArea;
    private final JProgressBar progressBar;
    private final JScrollPane outputTextAreaScrollPane;
    private final JButton fileButton;
    private final JLabel selectedFileLabel;
    private final JButton buttonConvert;
    private final ButtonGroup buttonGroup;
    private final JRadioButton radioButtonSha224;
    private final JRadioButton radioButtonSha256;
    private final JRadioButton radioButtonSha384;
    private final JRadioButton radioButtonSha512;

    private String selectedFilePath;
    private final String prefixFilePath = "File : ";

    public Sha3FileMenu() {
        setPreferredSize(new Dimension(400, 400));

        selectedFileLabel = new JLabel(prefixFilePath);
        selectedFileLabel.setPreferredSize(new Dimension(378, 25));

        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setValue(0);
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(378, 25));

        fileButton = new JButton("Choose file");
        fileButton.setPreferredSize(new Dimension(378, 25));
        fileButton.addActionListener(e -> {
            chooseFile();
        });

        radioButtonSha224 = new JRadioButton("SHA3-224", Boolean.TRUE);
        radioButtonSha256 = new JRadioButton("SHA3-256");
        radioButtonSha384 = new JRadioButton("SHA3-384");
        radioButtonSha512 = new JRadioButton("SHA3-512");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonSha224);
        buttonGroup.add(radioButtonSha256);
        buttonGroup.add(radioButtonSha384);
        buttonGroup.add(radioButtonSha512);

        buttonConvert = new JButton("Hash");
        buttonConvert.addActionListener(e -> {
            if (selectedFilePath != null) {
                convert();
            }
        });
        buttonConvert.setPreferredSize(new Dimension(378, 25));

        outputArea = new JTextArea("", 4, 34);
        outputArea.setWrapStyleWord(true);
        outputArea.setLineWrap(true);
        outputArea.setEditable(false);
        outputArea.setFocusable(false);
        outputArea.setOpaque(false);
        outputTextAreaScrollPane = new JScrollPane(outputArea, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);

        add(fileButton);
        add(selectedFileLabel);
        add(radioButtonSha224);
        add(radioButtonSha256);
        add(radioButtonSha384);
        add(radioButtonSha512);
        add(buttonConvert);
        add(progressBar);
        add(outputTextAreaScrollPane);
    }

    public void convert() {
        SwingWorker<String, String> worker = new SwingWorker<String, String>() {

            @Override
            protected String doInBackground() throws Exception {
                fileButton.setEnabled(false);
                buttonConvert.setEnabled(false);
                progressBar.setVisible(true);

                Type type;

                if (radioButtonSha224.isSelected()) {
                    type = Type.SHA3_224;
                } else if (radioButtonSha256.isSelected()) {
                    type = Type.SHA3_256;
                } else if (radioButtonSha384.isSelected()) {
                    type = Type.SHA3_384;
                } else {
                    type = Type.SHA3_512;
                }

                Sha3 algorithm = new Sha3(type);

                try (InputStream fileInputStream = new FileInputStream(selectedFilePath)) {
                    byte[] output = algorithm.encode(fileInputStream);
                    return HexTools.convertToHex(output);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }

            @Override
            protected void done() {
                try {
                    String result = get();
                    outputArea.setText(result);
                } catch (Exception e) {
                    outputArea.setText("Something goes wrong");
                    e.printStackTrace();
                }

                fileButton.setEnabled(true);
                buttonConvert.setEnabled(true);
                progressBar.setVisible(false);
            }
        };

        worker.execute();
    }

    public void chooseFile() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
            selectedFileLabel.setText(prefixFilePath + getLast35Characters(selectedFilePath));
        }
    }

    public String getLast35Characters(String text) {
        return text.length() <= 35 ? text : "..." + text.substring(text.length() - 35);
    }
}
