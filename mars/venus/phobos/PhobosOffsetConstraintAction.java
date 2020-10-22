package mars.venus.phobos;

import mars.Globals;
import mars.Settings;
import mars.venus.GuiAction;
import mars.venus.VenusUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Action class for the Phobos menu item to determine whether an offset is reuqired when
 * using the lw or sw instructions.
 */
public class PhobosOffsetConstraintAction  extends GuiAction {
    public PhobosOffsetConstraintAction(String name, Icon icon, String descrip,
                                              Integer mnemonic, KeyStroke accel, VenusUI gui) {
        super(name, icon, descrip, mnemonic, accel, gui);
    }

    public void actionPerformed(ActionEvent e) {
        Globals.getSettings().setBooleanSetting(
                Settings.OFFSET_CONSTRAINT, ((JCheckBoxMenuItem)e.getSource()).isSelected());
    }
}
