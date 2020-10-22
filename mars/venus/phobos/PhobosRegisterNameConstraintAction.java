package mars.venus.phobos;

import mars.Globals;
import mars.Settings;
import mars.venus.GuiAction;
import mars.venus.VenusUI;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * Action class for the Phobos menu item to determine whether register names are required
 * in instructions instead of register numbers.
 */public class PhobosRegisterNameConstraintAction extends GuiAction {
    public PhobosRegisterNameConstraintAction(String name, Icon icon, String descrip,
                                       Integer mnemonic, KeyStroke accel, VenusUI gui) {
        super(name, icon, descrip, mnemonic, accel, gui);
    }

    public void actionPerformed(ActionEvent e) {
        Globals.getSettings().setBooleanSetting(
                Settings.REGISTER_NAME_CONSTRAINT, ((JCheckBoxMenuItem)e.getSource()).isSelected());
    }
}
