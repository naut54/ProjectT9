package gui;

import data.PeliculaDAO;
import models.PeliculaRedesign;
import utils.UtilsGUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class UpdateMovie extends JPanel {
    private final Color MENU_COLOR = new Color(52, 73, 94);
    private final Color BUTTON_COLOR = new Color(41, 128, 185);
    private final Color ERROR_COLOR = new Color(231, 76, 60);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);

    private JTextField idField;
    private JTextField titleField;
    private JTextField directorField;
    private JTextField durationField;
    private JComboBox<String> categoryCombo;
    private JTextField priceField;
    private JButton updateButton;
    private JButton cancelButton;
    private final MainWindow mainWindow;
    private PeliculaRedesign currentMovie;

    private final String[] categories = {"COMEDIA", "TERROR", "ACCION", "ROMANCE", "AVENTURA"};

    public UpdateMovie(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        createForm();
    }

    public void setMovieToUpdate(int movieId) {
        PeliculaDAO dao = new PeliculaDAO();
        currentMovie = dao.searchById(movieId);

        if (currentMovie != null) {
            populateFields();
        } else {
            UtilsGUI.showErrorMessage("No se pudo encontrar la película con ID: " + movieId, "Error");
            mainWindow.showMainPanel();
        }
    }

    private void populateFields() {
        idField.setText(String.valueOf(currentMovie.getId()));
        titleField.setText(currentMovie.getTitulo());
        directorField.setText(currentMovie.getDirector());
        durationField.setText(String.valueOf((int)currentMovie.getDuracion()));
        priceField.setText(String.valueOf(currentMovie.getPrecio()));

        categoryCombo.setSelectedIndex(0);
    }

    private void createForm() {
        JPanel basicInfoPanel = createBasicInfoPanel();

        JPanel commercialInfoPanel = createCommercialInfoPanel();

        JPanel buttonPanel = createButtonPanel();

        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(basicInfoPanel);
        contentPanel.add(commercialInfoPanel);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createBasicInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MENU_COLOR, 1),
                "Información Básica",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                MENU_COLOR
        ));
        panel.setBackground(Color.WHITE);

        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        idLabel.setForeground(MENU_COLOR);

        idField = new JTextField();
        idField.setFont(new Font("Arial", Font.PLAIN, 14));
        idField.setEditable(false);
        idField.setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel("Título:");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(MENU_COLOR);

        titleField = new JTextField();
        titleField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel directorLabel = new JLabel("Director:");
        directorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        directorLabel.setForeground(MENU_COLOR);

        directorField = new JTextField();
        directorField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel durationLabel = new JLabel("Duración (min):");
        durationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        durationLabel.setForeground(MENU_COLOR);

        durationField = new JTextField();
        durationField.setFont(new Font("Arial", Font.PLAIN, 14));
        durationField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });

        panel.add(idLabel);
        panel.add(idField);
        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(directorLabel);
        panel.add(directorField);
        panel.add(durationLabel);
        panel.add(durationField);

        return panel;
    }

    private JPanel createCommercialInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MENU_COLOR, 1),
                "Información Comercial",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                MENU_COLOR
        ));
        panel.setBackground(Color.WHITE);

        JLabel categoryLabel = new JLabel("Categoría:");
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryLabel.setForeground(MENU_COLOR);

        categoryCombo = new JComboBox<>(categories);
        categoryCombo.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel priceLabel = new JLabel("Precio:");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        priceLabel.setForeground(MENU_COLOR);

        priceField = new JTextField();
        priceField.setFont(new Font("Arial", Font.PLAIN, 14));
        priceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });

        panel.add(categoryLabel);
        panel.add(categoryCombo);
        panel.add(priceLabel);
        panel.add(priceField);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);

        updateButton = new JButton("Actualizar");
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setBackground(SUCCESS_COLOR);
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(e -> updateMovie());

        cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(ERROR_COLOR);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> cancelAction());

        panel.add(updateButton);
        panel.add(cancelButton);
        return panel;
    }

    private void updateMovie() {
        if (titleField.getText().trim().isEmpty()) {
            UtilsGUI.showErrorMessage("El título no puede estar vacío", "Error de validación");
            titleField.requestFocus();
            return;
        }

        if (directorField.getText().trim().isEmpty()) {
            UtilsGUI.showErrorMessage("El director no puede estar vacío", "Error de validación");
            directorField.requestFocus();
            return;
        }

        if (durationField.getText().trim().isEmpty()) {
            UtilsGUI.showErrorMessage("La duración no puede estar vacía", "Error de validación");
            durationField.requestFocus();
            return;
        }

        if (priceField.getText().trim().isEmpty()) {
            UtilsGUI.showErrorMessage("El precio no puede estar vacío", "Error de validación");
            priceField.requestFocus();
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText().trim();
            String director = directorField.getText().trim();
            int duration = Integer.parseInt(durationField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());

            PeliculaRedesign updatedMovie = new PeliculaRedesign(id, title, director, duration, price);

            PeliculaDAO dao = new PeliculaDAO();
            boolean success = dao.updateMovie(updatedMovie);

            if (success) {
                UtilsGUI.showInfoMessage("La película se ha actualizado correctamente", "Actualización exitosa");
                mainWindow.showMainPanel();
            } else {
                UtilsGUI.showErrorMessage("No se pudo actualizar la película", "Error de actualización");
            }

        } catch (NumberFormatException e) {
            UtilsGUI.showErrorMessage("Por favor, ingrese valores numéricos válidos para duración y precio", "Error de formato");
        }
    }

    private void cancelAction() {
        mainWindow.showMainPanel();
    }
}