package mars.venus.phobos;

import mars.Globals;
import mars.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConfigureSubsetDialog extends JDialog {
    // Used to set the size of the menu
    private static final int PREFRERRED_WIDTH = 470;
    private static final int PREFERRED_HEIGHT = 290;

    private JPanel panel;

    private JPanel leftSidePanel;
    private JPanel rightSidePanel;
    private JCheckBox enableCheckBox;
    private JPanel addPanel;

    private JTextField addInstructionTextField;
    private DefaultListModel<String> subsetModel;
    private JList<String> instructionSubset;
    private JFileChooser fileChooser;

    public ConfigureSubsetDialog() {
        super(Globals.getGui(),"Instruction Subset Window", true);

        panel = new JPanel(new GridLayout(1, 2));
        getContentPane().add(panel);
        initLayout();
        setPreferredSize(new Dimension(PREFRERRED_WIDTH, PREFERRED_HEIGHT));
        pack();
        setLocationRelativeTo(null);
    }

    private void initLayout() {
        initLeftSidePanel();
        initRightSidePanel();
        enabledChanged(enableCheckBox.isSelected());
    }

    private void initLeftSidePanel() {
        leftSidePanel = new JPanel(new BorderLayout());
        leftSidePanel.setBorder(new EmptyBorder(10, 10, 10, 5));
        panel.add(leftSidePanel);

        JLabel isLabel = new JLabel("Instruction Subset");
        isLabel.setHorizontalAlignment(JLabel.CENTER);
        isLabel.setToolTipText("The instructions that are in the subset");
        leftSidePanel.add(isLabel, BorderLayout.NORTH);

        subsetModel = new DefaultListModel<>();
        // Load and add all instructions to subsetModel
        for (String instruction : Settings.instructionSubset) {
            subsetModel.addElement(instruction);
        }
        instructionSubset = new JList<>(subsetModel);
        instructionSubset.setToolTipText("The instructions that are in the subset");
        JScrollPane scrollPane = new JScrollPane(instructionSubset);
        leftSidePanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void initRightSidePanel() {
        rightSidePanel = new JPanel(new GridLayout(7, 1, 10, 10));
        rightSidePanel.setBorder(new EmptyBorder(10, 5, 10, 10));
        panel.add(rightSidePanel);

        enableCheckBox = new JCheckBox("Enable Instruction Subset");
        enableCheckBox.setToolTipText("Check to allow/disallow certain instructions from being used.");
        enableCheckBox.setSelected(Globals.getSettings().getBooleanSetting(Settings.INSTRUCTION_SUBSET));
        enableCheckBox.addChangeListener(changeEvent -> enabledChanged(enableCheckBox.isSelected()));
        rightSidePanel.add(enableCheckBox);

        addPanel = new JPanel(new BorderLayout(10, 10));
        addInstructionTextField = new JTextField();
        addInstructionTextField.setToolTipText("The instruction that gets added");
        addPanel.add(addInstructionTextField, BorderLayout.CENTER);
        JButton addInstructionButton = new JButton("+");
        addInstructionButton.setToolTipText("Adds an instruction into the subset");
        addInstructionButton.addActionListener(actionEvent -> addButtonPressed());
        addPanel.add(addInstructionButton, BorderLayout.EAST);

        rightSidePanel.add(addPanel);

        JButton removeSelectedButton = new JButton("Remove Selected");
        removeSelectedButton.setToolTipText("Removes the selected instruction(s) from the subset");
        removeSelectedButton.addActionListener(actionEvent -> removeSelectedButtonPressed());
        rightSidePanel.add(removeSelectedButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setToolTipText("Clears all of the instructions in the subset");
        clearButton.addActionListener(actionEvent -> clearButtonPressed());
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

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".txt") || file.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File("."));
        fileChooser.setMultiSelectionEnabled(false);
        JButton importButton = new JButton("Import Subset");
        importButton.setToolTipText("Imports a subset of instructions from a text file");
        importButton.addActionListener(actionEvent -> importButtonPressed());
        rightSidePanel.add(importButton);
    }

    private void enabledChanged(boolean enabled) {
        Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET, enableCheckBox.isSelected());
        for (Component comp : leftSidePanel.getComponents()) {
            comp.setEnabled(enabled);
        }
        for (Component comp : rightSidePanel.getComponents()) {
            if (comp != enableCheckBox) comp.setEnabled(enabled);
        }
        for (Component comp : addPanel.getComponents()) {
            comp.setEnabled(enabled);
        }
    }

    private void addButtonPressed() {
        String instruction = addInstructionTextField.getText();
        instruction = instruction.trim();
        if (instruction.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No instruction was entered!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (subsetModel.contains(instruction)) {
            JOptionPane.showMessageDialog(this, "Instruction already exists in subset.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (Globals.instructionSet.matchOperator(instruction) == null) {
            JOptionPane.showMessageDialog(this, "The instruction \"" + instruction + "\" does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        subsetModel.addElement(instruction);
        addInstructionTextField.setText("");
        composeAndSaveSubsetString();
        Settings.instructionSubset.add(instruction);
    }

    private void removeSelectedButtonPressed() {
        if (instructionSubset.getSelectedValue() == null) {
            JOptionPane.showMessageDialog(this, "No instruction was selected in the subset!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        List<String> removeInstructions = instructionSubset.getSelectedValuesList();
        Settings.instructionSubset.removeAll(removeInstructions);
        for (String instruction : removeInstructions) {
            subsetModel.removeElement(instruction);
        }
        composeAndSaveSubsetString();
    }

    private void clearButtonPressed() {
        int input = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear the subset?", "Select an Option", JOptionPane.YES_NO_OPTION);
        if (input == JOptionPane.YES_OPTION) {
            subsetModel.clear();
            composeAndSaveSubsetString();
            Settings.instructionSubset.clear();
        }
    }

    private void importButtonPressed() {
        int input = fileChooser.showOpenDialog(this);
        if (input == JFileChooser.APPROVE_OPTION) {
            String[] options = {"Overwrite", "Add", "Cancel"};
            int option = JOptionPane.showOptionDialog(this, "Do you want to overwrite the existing subset\nwith the subset from the text file?", "Select an Option",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (option == 2) // Cancel
                return;
            ArrayList<String> instructions = parseInImportFile();
            if (option == 0) { // Overwrite
                subsetModel.clear();
                composeAndSaveSubsetString();
                Settings.instructionSubset.clear();
            }
            for (String instruction : instructions) {
                if ((option == 0 || !subsetModel.contains(instruction)) &&
                        Globals.instructionSet.matchOperator(instruction) != null) {
                    subsetModel.addElement(instruction);
                    Settings.instructionSubset.add(instruction);
                }
            }
            composeAndSaveSubsetString();
        }
    }

    private ArrayList<String> parseInImportFile() {
        ArrayList<String> instructions = new ArrayList<>();
        File importFile = fileChooser.getSelectedFile();

        try {
            Scanner scanner = new Scanner(importFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty())
                    instructions.add(line);
            }
        } catch (FileNotFoundException ex) {
            // If file not found then method will just return empty list
        }

        return instructions;
    }

    private void composeAndSaveSubsetString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < subsetModel.size(); i++) {
            sb.append(subsetModel.get(i));
            sb.append('~');
        }
        Globals.getSettings().setInstructionSubsetString(sb.toString());
    }
}
