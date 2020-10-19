package mars.tools;

import mars.Globals;
import mars.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class ConfigureSubsetWindow implements Runnable {

    private final JDialog frame;
    private JPanel panel;

    private JTextField addInstructionTextField;
    private DefaultListModel<String> subsetModel;

    public ConfigureSubsetWindow() {
        frame = new JDialog(Globals.getGui(),"Configure Subset Window");

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
        frame.setTitle("Configure Subset Window");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    private void initLayout() {
        initLeftSidePanel();
        initRightSidePanel();
    }

    private void initLeftSidePanel() {
        JPanel leftSidePanel = new JPanel(new BorderLayout());
        leftSidePanel.setBorder(new EmptyBorder(10, 10, 10, 5));
        panel.add(leftSidePanel);

        JLabel isLabel = new JLabel("Instruction Subset");
        isLabel.setHorizontalAlignment(JLabel.CENTER);
        isLabel.setToolTipText("The instructions that are in the subset");
        leftSidePanel.add(isLabel, BorderLayout.NORTH);

        subsetModel = new DefaultListModel<>();
        JList<String> instructionSubset = new JList<>(subsetModel);
        instructionSubset.setToolTipText("The instructions that are in the subset");
        JScrollPane scrollPane = new JScrollPane(instructionSubset);
        leftSidePanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void initRightSidePanel() {
        JPanel rightSidePanel = new JPanel(new GridLayout(6, 1, 10, 10));
        rightSidePanel.setBorder(new EmptyBorder(10, 5, 10, 10));
        panel.add(rightSidePanel);

        JPanel addPanel = new JPanel(new BorderLayout(10, 10));
        addInstructionTextField = new JTextField();
        addInstructionTextField.setToolTipText("The instruction that gets added");
        addPanel.add(addInstructionTextField, BorderLayout.CENTER);
        JButton addInstructionButton = new JButton("+");
        addInstructionButton.setToolTipText("Adds an instruction into the subset");
        addInstructionButton.addActionListener(actionEvent -> addButtonPressed());
        addPanel.add(addInstructionButton, BorderLayout.EAST);

        rightSidePanel.add(addPanel);

        JButton removeInstructionButton = new JButton("Remove Selected");
        removeInstructionButton.setToolTipText("Removes the selected instruction from the subset");
        rightSidePanel.add(removeInstructionButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setToolTipText("Clears all of the instructions in the subset");
        rightSidePanel.add(clearButton);

        JRadioButton blacklistButton = new JRadioButton("Blacklist");
        blacklistButton.setToolTipText("Disallows the instructions entered in the subset from being used within the program");
        blacklistButton.addChangeListener(changeEvent ->
                Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET_WHITELIST, !blacklistButton.isSelected()));
        blacklistButton.setSelected(!Globals.getSettings().getBooleanSetting(Settings.INSTRUCTION_SUBSET_WHITELIST));
        JRadioButton whitelistButton = new JRadioButton("Whitelist");
        whitelistButton.setToolTipText("Only allows the instructions entered into the subset to be used within the program");
        whitelistButton.addChangeListener(changeEvent ->
                Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET_WHITELIST, whitelistButton.isSelected()));
        whitelistButton.setSelected(Globals.getSettings().getBooleanSetting(Settings.INSTRUCTION_SUBSET_WHITELIST));
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(blacklistButton);
        buttonGroup.add(whitelistButton);
        rightSidePanel.add(blacklistButton);
        rightSidePanel.add(whitelistButton);

        JButton importButton = new JButton("Import Subset");
        importButton.setToolTipText("Imports a subset of instructions from a text file");
        rightSidePanel.add(importButton);
    }

    private void addButtonPressed() {
        String instruction = addInstructionTextField.getText();
        if (instruction.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No instruction was entered!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        instruction = instruction.trim();
        if (subsetModel.contains(instruction)) {
            JOptionPane.showMessageDialog(frame, "Instruction already exists in subset.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ArrayList instructions = Globals.instructionSet.matchOperator(instruction);
        if (instructions == null) {
            JOptionPane.showMessageDialog(frame, "The instruction \"" + instruction + "\" does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        subsetModel.addElement(instruction);
        addInstructionTextField.setText("");
    }

    @Override
    public void run() {

    }
}
