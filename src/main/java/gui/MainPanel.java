package gui;

import utils.Styles;
import utils.UtilsGUI;
import javax.swing.*;
import java.awt.*;

import static utils.Styles.createStyledButton;

public class MainPanel extends JPanel {
    private JPanel quickAccessPanel;
    private JPanel buttonsPanel;
    private final Color MENU_COLOR = new Color(230, 230, 230);
    private final MainWindow mainWindow;

    public MainPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        UtilsGUI.initPanel(this);
        UtilsGUI.createComponents(
                this::createQuickAccessPanel
        );
    }

    private void createQuickAccessPanel() {
        quickAccessPanel = new JPanel(new GridBagLayout());
        quickAccessPanel.setBorder(BorderFactory.createTitledBorder("Accesos Rapidos"));

        Object[][] quickAccessButtons = {
                {"Alta Pelicula"},
                {"Baja Pelicula"},
                {"Consultar Peliculas"},
                {"Actualizar Pelicula"}
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        int row = 0;
        int col = 0;

        for (Object[] quickAccessButton : quickAccessButtons) {
            String buttonName = (String) quickAccessButton[0];

            JButton button = createStyledButton(buttonName, MENU_COLOR, 150, 60, MENU_COLOR);
            //button.addActionListener(e -> action.run());

            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            Styles.setSizeButton(button, 150, 60);

            gbc.gridx = col;
            gbc.gridy = row;
            quickAccessPanel.add(button, gbc);

            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }
}
