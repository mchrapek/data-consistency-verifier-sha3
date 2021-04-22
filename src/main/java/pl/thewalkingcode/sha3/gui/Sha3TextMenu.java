package pl.thewalkingcode.sha3.gui;

import pl.thewalkingcode.sha3.Sha3;
import pl.thewalkingcode.sha3.Type;
import pl.thewalkingcode.sha3.utils.HexTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.nio.charset.StandardCharsets;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class Sha3TextMenu extends JPanel implements FocusListener {

    private final JTextArea inputArea;
    private final JTextArea outputArea;
    private final JProgressBar progressBar;
    private final JScrollPane inputTextAreaScrollPane;
    private final JScrollPane outputTextAreaScrollPane;
    private final ButtonGroup buttonGroup;
    private final JRadioButton radioButtonSha224;
    private final JRadioButton radioButtonSha256;
    private final JRadioButton radioButtonSha384;
    private final JRadioButton radioButtonSha512;
    private final JButton buttonConvert;

    private boolean untouched = Boolean.TRUE;

    public Sha3TextMenu() {
        setPreferredSize(new Dimension(400, 400));

        inputArea = new JTextArea("Put your text here...", 10, 34);
        inputArea.setWrapStyleWord(true);
        inputArea.setLineWrap(true);
        inputArea.setEditable(true);
        inputArea.setFocusable(true);
        inputArea.addFocusListener(this);
        inputTextAreaScrollPane = new JScrollPane(inputArea, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);

        outputArea = new JTextArea("", 4, 34);
        outputArea.setWrapStyleWord(true);
        outputArea.setLineWrap(true);
        outputArea.setEditable(false);
        outputArea.setFocusable(false);
        outputArea.setOpaque(false);
        outputTextAreaScrollPane = new JScrollPane(outputArea, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);

        radioButtonSha224 = new JRadioButton("SHA3-224", Boolean.TRUE);
        radioButtonSha256 = new JRadioButton("SHA3-256");
        radioButtonSha384 = new JRadioButton("SHA3-384");
        radioButtonSha512 = new JRadioButton("SHA3-512");

        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonSha224);
        buttonGroup.add(radioButtonSha256);
        buttonGroup.add(radioButtonSha384);
        buttonGroup.add(radioButtonSha512);

        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setValue(0);
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(378, 25));

        buttonConvert = new JButton("Hash");
        buttonConvert.addActionListener(e -> {
            if (!inputArea.getText().isEmpty()) {
                convert();
            }
        });
        buttonConvert.setPreferredSize(new Dimension(378, 25));

        add(inputTextAreaScrollPane);
        add(radioButtonSha224);
        add(radioButtonSha256);
        add(radioButtonSha384);
        add(radioButtonSha512);
        add(buttonConvert);
        add(progressBar);
        add(outputTextAreaScrollPane);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (untouched) {
            inputArea.setText("");
            untouched = Boolean.FALSE;
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        // Nothing to do
    }

    public void convert() {
        SwingWorker<String, String> worker = new SwingWorker<String, String>() {

            @Override
            protected String doInBackground() throws Exception {
                buttonConvert.setEnabled(false);
                progressBar.setVisible(true);

                String text = inputArea.getText();
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
                byte[] output = algorithm.encode(text.getBytes(StandardCharsets.UTF_8));

                return HexTools.convertToHex(output);
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

                buttonConvert.setEnabled(true);
                progressBar.setVisible(false);
            }
        };

        worker.execute();
    }
}
