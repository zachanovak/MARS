package mars.tools;

import mars.Globals;
import mars.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Window that pops up when "Show Register Usage on Assemble" is enabled on the
 * Phobos Menu. Displays bar graphs in four different tabs of how many of each
 * register is written in the program.
 */
public class RegisterUsageWindow implements Runnable {
    // Each array in this 2D array is a tab of the window
    private static final String[][] allRegisterNames = {
            {"$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9"},
            {"$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7"},
            {"$v0", "$v1", "$a0", "$a1", "$a2", "$a3", "$sp", "$fp", "$ra"},
            {"$zero", "$at", "$k0", "$k1", "$gp"}
    };

    private final JDialog frame;
    private JPanel panel;

    /**
     * Constructor that initiates the GUI of the window
     */
    public RegisterUsageWindow() {
        frame = new JDialog(Globals.getGui(),"Register Usage Window");

        // Snippet by Pete Sanderson, 2 Nov. 2006, to be a window-closing sequence
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        frame.setVisible(false);
                        frame.dispose();
                    }
                });

        panel = new JPanel(new GridLayout(1, 1));
        frame.getContentPane().add(panel);
        initLayout();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("Register Usage Window");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Creates the tabbed pane containing the bar charts
     */
    private void initLayout() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Temporary Registers", new RegisterUsageBarChart(0));
        tabbedPane.addTab("Saved Registers", new RegisterUsageBarChart(1));
        tabbedPane.addTab("Function Registers", new RegisterUsageBarChart(2));
        tabbedPane.addTab("Miscellaneous Registers", new RegisterUsageBarChart(3));

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        panel.add(tabbedPane);
    }

    @Override
    public void run() {

    }

    /**
     * JPanel that displays a bar chart of certain registers
     */
    private class RegisterUsageBarChart extends JPanel {
        // The index in the above 2D array of registers to use
        private int registerIndex;
        // The array from the above 2D array of registers
        private String[] registers;

        /**
         * Constructor that gets the registers it needs to display
         * @param registerIndex the index in the above 2D array of registers
         */
        public RegisterUsageBarChart(int registerIndex) {
            this.registerIndex = registerIndex;
            this.registers = allRegisterNames[registerIndex];
        }

        /**
         * Displays the amount of registers in the form of a bar chart
         * @param g used to create the display
         */
        @Override
        protected void paintComponent(Graphics g) {
            // Find longest bar
            int max = Integer.MIN_VALUE;
            for (String key : registers) {
                if (Settings.registerUsageMap.containsKey(key)) {
                    max = Math.max(max, Settings.registerUsageMap.get(key));
                }
            }

            // Paint bars
            int width = (getWidth() / registers.length) - 12;
            int x = 10;
            for (int i = 0; i < registers.length; i++) {
                int num = Settings.registerUsageMap.get(registers[i]) == null
                        ? 0 : Settings.registerUsageMap.get(registers[i]);

                int height = (int)((getHeight() - 70) * ((double)num / max));
                if (height != 0) {
                    // I add 30 to the height here because I want the register name text to fit in the bar
                    // if there is a bar. I also only want to do this when the bar isn't 0
                    height += 30;
                }
                g.setColor(getColor());
                g.fillRect(x, getHeight() - height, width, height);
                g.setColor(Color.BLACK);
                g.drawRect(x, getHeight() - height, width, height);

                if (num == 0)
                    g.setColor(Color.BLACK);
                else
                    g.setColor(Color.WHITE);
                g.drawString(registers[i], x + width / 2 - registers[i].length() * 4,
                        getHeight() - 10);

                g.setColor(Color.BLACK);
                // Numbers get extra height if num is 0, otherwise the number will be on top of register name
                int extraHeight = num == 0 ? 23 : 10;
                g.drawString(num + "", x + width / 2 - (num + "").length() * 4,
                        getHeight() - height - extraHeight);

                x += (width + 10);
            }
        }

        /**
         * Utility method that gets the color of the bars depending on which
         * registers are currently being shown
         * @return Color of the bar
         */
        private Color getColor() {
            Color color;

            switch (registerIndex) {
                case 0:
                    color = Color.RED;
                    break;
                case 1:
                    color = Color.BLUE;
                    break;
                case 2:
                    color = Color.MAGENTA;
                    break;
                case 3:
                    color = Color.GRAY;
                    break;
                default:
                    color = Color.WHITE;
                    break;
            }

            return color;
        }

        /**
         * Gets the size of the panel
         * @return Dimension of JPanel
         */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(registers.length * 70 + 20, 500);
        }
    }
}
