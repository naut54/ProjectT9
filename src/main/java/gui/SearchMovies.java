package gui;

import data.PeliculaDAO;
import models.PeliculaRedesign;
import utils.UtilsGUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class SearchMovies extends JPanel {
    private final Color MENU_COLOR = new Color(52, 73, 94);
    private final Color BUTTON_COLOR = new Color(41, 128, 185);
    private final Color ERROR_COLOR = new Color(231, 76, 60);

    private JTextField searchField;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JButton searchButton;
    private JButton cancelButton;
    private final MainWindow mainWindow;

    private final String[] columnNames = {
            "Código", "Titulo", "Director", "Duración", "Precio Entrada"
    };

    public SearchMovies(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        createForm();
    }

    private void createForm() {
        JPanel searchPanel = createSearchPanel();

        JPanel resultsPanel = createResultsPanel();

        JPanel buttonPanel = createButtonPanel();

        add(searchPanel, BorderLayout.NORTH);
        add(resultsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MENU_COLOR, 1),
                "Búsqueda de Películas",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                MENU_COLOR
        ));
        panel.setBackground(Color.WHITE);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Título:");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(MENU_COLOR);

        searchField = new JTextField(30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });

        searchButton = new JButton("Buscar");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(BUTTON_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(e -> performSearch());

        inputPanel.add(titleLabel);
        inputPanel.add(searchField);
        inputPanel.add(searchButton);

        panel.add(inputPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MENU_COLOR, 1),
                "Resultados",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                MENU_COLOR
        ));
        panel.setBackground(Color.WHITE);

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (String columnName : columnNames) {
            tableModel.addColumn(columnName);
        }

        resultsTable = new JTable(tableModel);
        resultsTable.setRowHeight(25);
        resultsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.setGridColor(new Color(230, 230, 230));

        JTableHeader header = resultsTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(MENU_COLOR);
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);

        cancelButton = new JButton("Volver");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(ERROR_COLOR);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> cancelAction());

        panel.add(cancelButton);
        return panel;
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();

        if (searchTerm.isEmpty()) {
            UtilsGUI.showErrorMessage("Por favor, ingrese un término de búsqueda", "Error de búsqueda");
            return;
        }

        tableModel.setRowCount(0);

        PeliculaDAO dao = new PeliculaDAO();
        List<PeliculaRedesign> results = dao.searchByString(searchTerm);

        if (results.isEmpty()) {
            UtilsGUI.showInfoMessage("No se encontraron películas con ese criterio de búsqueda", "Sin resultados");
        } else {
            for (PeliculaRedesign movie : results) {
                Object[] row = {
                        movie.getId(),
                        movie.getTitulo(),
                        movie.getDirector(),
                        movie.getDuracion(),
                        movie.getPrecio()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void cancelAction() {
        mainWindow.showMainPanel();
    }
}