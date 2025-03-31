package gui;

import utils.Styles;
import utils.UtilsGUI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static utils.Styles.createStyledButton;

public class MainPanel extends JPanel {
    private final String[] columnNames = {
            "Código", "Nombre", "Descripcion", "Precio Entrada", "Categoria"
    };
    private JPanel quickAccessPanel;
    private JPanel buttonsPanel;
    private JPanel moviesTablePanel;
    private JTable moviesTable;
    private DefaultTableModel model;
    private final Color MENU_COLOR = new Color(230, 230, 230);
    private final Color BUTTON_COLOR = new Color(5, 189, 12);
    private final Color BUTTON_COLOR_HOVER = new Color(5, 165, 11);
    private final MainWindow mainWindow;

    public MainPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        UtilsGUI.initPanel(this);
        UtilsGUI.createComponents(
                this::createQuickAccessPanel
        );
        layoutPanels();
        UtilsGUI.setupStyles();
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

            JButton button = createStyledButton(buttonName, BUTTON_COLOR, 150, 60, BUTTON_COLOR_HOVER);
            //button.addActionListener(e -> action.run());

            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            Styles.setSizeButton(button, 300, 120);

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

    private void createMoviesTable(Object[][] data) {
        moviesTablePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        this.model = new DefaultTableModel();
    }

    private void deleteRow(int row) {
        quickAccessPanel.remove(row);
    }

    private void layoutPanels() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        add(quickAccessPanel, gbc);
    }
}
