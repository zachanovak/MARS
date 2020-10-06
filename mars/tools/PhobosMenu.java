package mars.tools;

import mars.Globals;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
            JPanel buttonPanel = new JPanel();


            panel.add(optionsPanel, BorderLayout.CENTER);
            panel.add(buttonPanel, BorderLayout.SOUTH);

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
            optionsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            GridLayout gridLayout = new GridLayout(4, 2);
            optionsPanel.setLayout(gridLayout);

            JCheckBox option1CheckBox = new JCheckBox("Comma Constraint");
            option1CheckBox.setToolTipText("Requires the user to enter commas in instructions.");
            optionsPanel.add(option1CheckBox);
            optionsPanel.add(new Label());
            JCheckBox option2CheckBox = new JCheckBox("Register Name Constraint");
            option2CheckBox.setToolTipText("Restricts the user to only use register names rather than register numbers.");
            optionsPanel.add(option2CheckBox);
            optionsPanel.add(new Label());
            JCheckBox option3CheckBox = new JCheckBox("Show Register Usage On Assemble");
            option3CheckBox.setToolTipText("After assembled, window with register usage will appear.");
            optionsPanel.add(option3CheckBox);
            optionsPanel.add(new Label());
            JCheckBox option4CheckBox = new JCheckBox("Instruction Subset");
            option4CheckBox.setToolTipText("Allow/disallow certain instructions from being used.");
            optionsPanel.add(option4CheckBox);
            JButton configureButton = new JButton("Configure Subset");
            configureButton.setToolTipText("Configure what instructions should be allowed/disallowed.");
            optionsPanel.add(configureButton);

            return optionsPanel;
        }

        @Override
        public void run() {

        }
    }
}
