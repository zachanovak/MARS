package mars.tools;

import mars.Globals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InstructionSubsetWindow implements Runnable {
    private final JDialog frame;
    private JPanel panel;

    public InstructionSubsetWindow() {
        frame = new JDialog(Globals.getGui(),"Instruction Subset Window");

        // Snippet by Pete Sanderson, 2 Nov. 2006, to be a window-closing sequence
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        frame.setVisible(false);
                        frame.dispose();
                    }
                });

        panel = new JPanel(new GridLayout(1, 2));
        frame.getContentPane().add(panel);
        initLayout();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("Register Usage Window");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setPreferredSize(new Dimension(PREFRERRED_WIDTH, PREFERRED_HEIGHT));
        frame.setVisible(true);
    }

    private void initLayout() {
        JTable instructionsTable = new JTable();
        panel.add(instructionsTable);

        JPanel rightSidePanel = new JPanel(new GridLayout(4, 1));
        panel.add(rightSidePanel);

        JButton addInstructionButton = new JButton("Add Instruction");
        rightSidePanel.add(addInstructionButton);

        JRadioButton whitelistButton = new JRadioButton("Whitelist");
        JRadioButton blacklistButton = new JRadioButton("Blacklist");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(whitelistButton);
        buttonGroup.add(blacklistButton);
        rightSidePanel.add(whitelistButton);
        rightSidePanel.add(blacklistButton);

        JButton importButton = new JButton("Import from text file");
        rightSidePanel.add(importButton);
    }

    @Override
    public void run() {

    }
}
