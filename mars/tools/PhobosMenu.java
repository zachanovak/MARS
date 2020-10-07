package mars.tools;

import mars.Globals;
import mars.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PhobosMenu implements MarsTool {
    private static final int PREFRERRED_WIDTH = 712;
    private static final int PREFERRED_HEIGHT = 652;

    @Override
    public String getName() {
        return "Phobos Menu";
    }

    @Override
    public void action() {
        PhobosRunnable pr = new PhobosRunnable();
        Thread t1 = new Thread(pr);
        t1.start();
    }

    private class PhobosRunnable implements Runnable {
        JPanel panel;

        public PhobosRunnable() {
            final JDialog frame = new JDialog(Globals.getGui(),"Phobos Menu");
            panel = new JPanel(new BorderLayout());
            JPanel optionsPanel = initOptionsPanel();

            panel.add(optionsPanel, BorderLayout.CENTER);

            // Snippet by Pete Sanderson, 2 Nov. 2006, to be a window-closing sequence
            frame.addWindowListener(
                    new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                            frame.setVisible(false);
                            frame.dispose();
                        }
                    });

            frame.getContentPane().add(panel);
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);
            frame.setTitle("Phobos Menu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(PREFRERRED_WIDTH, PREFERRED_HEIGHT));
            frame.setVisible(true);
        }

        private JPanel initOptionsPanel() {
            JPanel optionsPanel = new JPanel();
            optionsPanel.setLayout(new BorderLayout());
            optionsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            JPanel constraintsPanel = new JPanel();
            TitledBorder cTitledBorder = new TitledBorder("Constraints");
            //cTitledBorder.setTitleJustification(TitledBorder.CENTER);
            constraintsPanel.setBorder(cTitledBorder);
            GridLayout cGridLayout = new GridLayout(3, 1);
            constraintsPanel.setLayout(cGridLayout);

            JCheckBox option1CheckBox = new JCheckBox("Comma Constraint");
            option1CheckBox.setToolTipText("Requires the user to enter commas in instructions.");
            option1CheckBox.setSelected(Globals.getSettings().getBooleanSetting(Settings.COMMA_CONSTRAINT));
            option1CheckBox.addChangeListener(changeEvent ->
                    Globals.getSettings().setBooleanSetting(Settings.COMMA_CONSTRAINT, option1CheckBox.isSelected()));
            constraintsPanel.add(option1CheckBox);

            JCheckBox option2CheckBox = new JCheckBox("Register Name Constraint");
            option2CheckBox.setToolTipText("Restricts the user to only use register names rather than register numbers.");
            option2CheckBox.setSelected(Globals.getSettings().getBooleanSetting(Settings.REGISTER_NAME_CONSTRAINT));
            option2CheckBox.addChangeListener(changeEvent ->
                    Globals.getSettings().setBooleanSetting(Settings.REGISTER_NAME_CONSTRAINT, option2CheckBox.isSelected()));
            constraintsPanel.add(option2CheckBox);

            JPanel option3Panel = new JPanel();
            TitledBorder isTitledBorder = new TitledBorder("Instruction Subset");
            //isTitledBorder.setTitleJustification(TitledBorder.CENTER);
            option3Panel.setBorder(isTitledBorder);
            JCheckBox option3CheckBox = new JCheckBox("Enabled");
            option3CheckBox.setToolTipText("Check to allow/disallow certain instructions from being used.");
            option3CheckBox.setSelected(Globals.getSettings().getBooleanSetting(Settings.INSTRUCTION_SUBSET));
            option3CheckBox.addChangeListener(changeEvent ->
                    Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET, option3CheckBox.isSelected()));
            option3Panel.add(option3CheckBox);
            JButton configureButton = new JButton("Configure Subset");
            configureButton.setToolTipText("Configure what instructions should be allowed/disallowed.");
            option3Panel.add(configureButton);
            constraintsPanel.add(option3Panel);

            optionsPanel.add(constraintsPanel, BorderLayout.CENTER);

            JCheckBox option4CheckBox = new JCheckBox("Show Register Usage On Assemble");
            option4CheckBox.setToolTipText("After assembled, window with register usage will appear.");
            option4CheckBox.setSelected(Globals.getSettings().getBooleanSetting(Settings.POPUP_REGISTER_USAGE));
            option4CheckBox.addChangeListener(changeEvent ->
                    Globals.getSettings().setBooleanSetting(Settings.POPUP_REGISTER_USAGE, option4CheckBox.isSelected()));
            optionsPanel.add(option4CheckBox, BorderLayout.SOUTH);

            return optionsPanel;
        }

        @Override
        public void run() {

        }
    }
}
