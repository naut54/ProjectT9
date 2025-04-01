package gui;

import utils.Styles;
import utils.UtilsGUI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import static utils.Styles.createStyledButton;

public class MainPanel extends JPanel {
    private final String[] columnNames = {
            "Código", "Titulo", "Precio Entrada", "Categoria"
    };
    private JPanel quickAccessPanel;
    private JPanel buttonsPanel;
    private JPanel moviesTablePanel;
    private JTable moviesTable;
    private DefaultTableModel model;
    private final Color MENU_COLOR = new Color(230, 230, 230);
    private final Color FONT_COLOR = new Color(52, 73, 94);
    private final Color BUTTON_COLOR = new Color(5, 189, 12);
    private final Color BUTTON_COLOR_HOVER = new Color(5, 165, 11);
    private final MainWindow mainWindow;

    public MainPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        UtilsGUI.initPanel(this);
        UtilsGUI.createComponents(
                this::createQuickAccessPanel,
                this::createMoviesTable
        );
        layoutPanels();
        UtilsGUI.setupStyles();
    }

    private void createQuickAccessPanel() {
        quickAccessPanel = new JPanel(new GridBagLayout());
        quickAccessPanel.setBorder(BorderFactory.createTitledBorder("Accesos Rapidos"));
        System.out.println("Creating QuickAccessPanel");

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

    private void createMoviesTable() {
        moviesTablePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        System.out.println("Creating MoviesTablePanel");
        Object[][] data = new Object[0][0];
        if (this.columnNames != null) {
            System.out.println("Data is not null");
        } else {
            System.out.println("Data is null");
            return;
        }

        if (this.MENU_COLOR == null || this.FONT_COLOR == null || this.BUTTON_COLOR == null || this.BUTTON_COLOR_HOVER == null) {
            System.out.println("Colors are null");
            return;
        } else {
            System.out.println("Colors are not null");
        }

        this.model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        moviesTable = new JTable(this.model);
        moviesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moviesTable.setRowSelectionAllowed(true);
        moviesTable.setColumnSelectionAllowed(false);
        JTableHeader header = moviesTable.getTableHeader();
        utils.Styles.setTableStyle(moviesTable, header,
                MENU_COLOR,
                FONT_COLOR,
                new Font("Arial", Font.BOLD, 14),
                30,
                SwingConstants.CENTER
        );

        utils.Styles.setRowStyle(moviesTable,
                Color.WHITE,
                new Color(245, 245, 245),
                new Color(40, 167, 69),
                new Color(220, 53, 69),
                Color.BLACK,
                40,
                new Color(230, 230, 230),
                6,
                SwingConstants.CENTER
        );

        moviesTable.getTableHeader().setReorderingAllowed(false);

        moviesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                getSelectedMovie();
            }
        });

        for (Object[] row : data) {
            this.model.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(moviesTable);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        moviesTablePanel.add(scrollPane, gbc);
    }

    private void updateTable() {
        Object[][] data = new Object[0][0];
        if (this.columnNames != null) {
            System.out.println("Data is not null");
        } else {
            System.out.println("Data is null");
        }

    }

    private void getSelectedMovie() {
        int row = moviesTable.getSelectedRow();
        if (row == -1) {
            return;
        }
        model.getValueAt(row, 0);
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

        gbc.gridx = 0;
        gbc.weightx = 1.0;
        add(moviesTablePanel, gbc);
    }
}
