package mars.venus;

import mars.Globals;
import mars.Settings;
import mars.venus.GuiAction;
import mars.venus.VenusUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action class for the Phobos menu item to determine whether to show register usage on assembly.
 */
public class PhobosShowRegisterUsageAction extends GuiAction {
    public PhobosShowRegisterUsageAction(String name, Icon icon, String descrip,
                                        Integer mnemonic, KeyStroke accel, VenusUI gui) {
        super(name, icon, descrip, mnemonic, accel, gui);
    }

    public void actionPerformed(ActionEvent e) {
        Globals.getSettings().setBooleanSetting(
                Settings.POPUP_REGISTER_USAGE, ((JCheckBoxMenuItem)e.getSource()).isSelected());
    }
}
