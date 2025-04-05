package utils;

import javax.swing.*;
import java.awt.*;

public class UtilsGUI {
    public static void initPanel(JPanel pnl) {
        if (pnl == null) {
            throw new NullPointerException("El panel no puede ser nulo.");
        }
        pnl.setLayout(new GridBagLayout());
    }

    public static void createComponents(Runnable... actions) {
        for (Runnable action : actions) {
            action.run();
        }
    }

    public static void setupStyles() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showInfoMessage(String s, String sinResultados) {
        JOptionPane.showMessageDialog(null, s, sinResultados, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorMessage(String s, String errorDeBusqueda) {
        JOptionPane.showMessageDialog(null, s, errorDeBusqueda, JOptionPane.ERROR_MESSAGE);
    }
}
