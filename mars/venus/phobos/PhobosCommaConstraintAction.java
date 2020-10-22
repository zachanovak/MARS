package mars.venus.phobos;

import mars.Globals;
import mars.Settings;
import mars.venus.GuiAction;
import mars.venus.VenusUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action class for the Phobos menu item to determine whether commas are required
 * in instructions.
 */
public class PhobosCommaConstraintAction extends GuiAction {
    public PhobosCommaConstraintAction(String name, Icon icon, String descrip,
                                       Integer mnemonic, KeyStroke accel, VenusUI gui) {
        super(name, icon, descrip, mnemonic, accel, gui);
    }

    public void actionPerformed(ActionEvent e) {
        Globals.getSettings().setBooleanSetting(
                Settings.COMMA_CONSTRAINT, ((JCheckBoxMenuItem)e.getSource()).isSelected());
    }
}
