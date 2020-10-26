package mars.venus;


import javax.swing.*;
import java.awt.event.ActionEvent;

public class EditAutoLayoutAction extends GuiAction {
    protected EditAutoLayoutAction(String name, Icon icon, String descrip,
                                   Integer mnemonic, KeyStroke accel, VenusUI gui) {
        super(name, icon, descrip, mnemonic, accel, gui);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String source = mainUI.getMainPane().getEditPane().getSource();

        mainUI.getMainPane().getEditPane().setSourceCode(AutoLayoutUtilities.autoLayout(source), true);
    }
}
