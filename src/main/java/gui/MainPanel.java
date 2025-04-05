package gui;

import data.PeliculaDAO;
import models.PeliculaRedesign;
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
                this::createMoviesTable,
                this::createMenuBar
        );
        layoutPanels();
        UtilsGUI.setupStyles();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(MENU_COLOR);
        menuBar.setOpaque(true);

        JMenu configMenu = new JMenu("Configuración");
        JMenu viewMenu = new JMenu("Ver");
        JMenu helpMenu = new JMenu("Ayuda");

        configMenu.setForeground(Color.WHITE);
        viewMenu.setForeground(Color.WHITE);
        helpMenu.setForeground(Color.WHITE);

        configMenu.add(createMenuItem("Cargar Datos"));
        configMenu.add(createMenuItem("Guardar Datos"));
        configMenu.add(createMenuItem("Borrar Datos"));

        viewMenu.add(createMenuItem("Consulta Rápida"));

        helpMenu.add(createMenuItem("Acerca de"));

        menuBar.add(configMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        mainWindow.setJMenuBar(menuBar);
    }

    private JMenuItem createMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        Styles.setItemStyle(item);
        return item;
    }

    private void createQuickAccessPanel() {
        quickAccessPanel = new JPanel(new GridBagLayout());
        quickAccessPanel.setBorder(BorderFactory.createTitledBorder("Accesos Rapidos"));
        System.out.println("Creating QuickAccessPanel");

        Object[][] quickAccessButtons = {
                {"Alta Pelicula", (Runnable) () -> mainWindow.showPanel("addMovie")},
                {"Baja Pelicula", (Runnable) () -> mainWindow.showPanel("deleteMovie")},
                {"Consultar Peliculas", (Runnable) () -> mainWindow.showPanel("searchMovie")},
                {"Actualizar Pelicula", (Runnable) () -> mainWindow.showPanel("updateMovie")},
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        int row = 0;
        int col = 0;

        for (Object[] quickAccessButton : quickAccessButtons) {
            String buttonName = (String) quickAccessButton[0];
            Runnable action = (Runnable) quickAccessButton[1];

            JButton button = createStyledButton(buttonName, BUTTON_COLOR, 150, 60, BUTTON_COLOR_HOVER);
            button.addActionListener(e -> action.run());

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
        updateTable();
    }

    private void updateTable() {
        try {
            this.model.setRowCount(0);

            PeliculaDAO peliculaDAO = new PeliculaDAO();
            List peliculas = (List) peliculaDAO.getAllMovies();

            if (peliculas == null || ((java.util.List<?>) peliculas).isEmpty()) {
                System.out.println("No se encontraron películas en la base de datos");
                return;
            }

            // Arreglar, peliculas.size() devuelve un Dimension en vez de un int

            Object[][] data = new Object[peliculas.size()][];
            for (int i = 0; i < peliculas.size(); i++) {
                PeliculaRedesign pelicula = (PeliculaRedesign) ((java.util.List<?>) peliculas).get(i);
                data[i] = new Object[]{
                        pelicula.getId(),
                        pelicula.getTitulo(),
                        pelicula.getPrecio()
                };
            }

            for (Object[] row : data) {
                this.model.addRow(row);
            }

            System.out.println("Se cargaron " + peliculas.size() + " películas en la tabla");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar películas: " + e.getMessage(),
                    "Error de carga",
                    JOptionPane.ERROR_MESSAGE);
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
