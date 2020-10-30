package mars.venus;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action class for the Phobos menu item for instruction subset configuration.
 */
public class PhobosInstructionSubsetAction extends GuiAction {

    JDialog configureSubsetDialog;

    public PhobosInstructionSubsetAction(String name, Icon icon, String descrip,
                                        Integer mnemonic, KeyStroke accel, VenusUI gui) {
        super(name, icon, descrip, mnemonic, accel, gui);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        configureSubsetDialog = new ConfigureSubsetDialog();
        configureSubsetDialog.setVisible(true);
    }
}
