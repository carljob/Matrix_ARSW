package matrix.simulation;

import matrix.simulation.ui.MenuFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuFrame::new);
    }
}