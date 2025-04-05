package gui;

import data.PeliculaDAO;
import models.PeliculaRedesign;
import utils.UtilsGUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AddMovie extends JPanel {
    private final Color MENU_COLOR = new Color(52, 73, 94);
    private final Color BUTTON_COLOR = new Color(41, 128, 185);
    private final Color ERROR_COLOR = new Color(231, 76, 60);

    private JTextField titleField;
    private JTextField directorField;
    private JTextField durationField;
    private JComboBox<String> categoryCombo;
    private JTextField priceField;
    private JButton saveButton;
    private JButton cancelButton;
    private final MainWindow mainWindow;

    public AddMovie(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        UtilsGUI.initPanel(this);
        UtilsGUI.createComponents(
                this::createForm
        );
        UtilsGUI.setupStyles();
    }

    private void createForm() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MENU_COLOR),
                "Datos de la Película",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 14),
                MENU_COLOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.weightx = 0.1;

        JPanel basicInfoPanel = createBasicInfoPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(basicInfoPanel, gbc);

        JPanel commercialInfoPanel = createCommercialInfoPanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(commercialInfoPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createBasicInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Información Básica",
                TitledBorder.LEFT,
                TitledBorder.TOP));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Título:");
        titleLabel.setDisplayedMnemonic('T');
        titleField = new JTextField(20);
        titleLabel.setLabelFor(titleField);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(titleLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(titleField, gbc);

        JLabel directorLabel = new JLabel("Director:");
        directorLabel.setDisplayedMnemonic('D');
        directorField = new JTextField(20);
        directorLabel.setLabelFor(directorField);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(directorLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(directorField, gbc);

        JLabel durationLabel = new JLabel("Duración (min):");
        durationLabel.setDisplayedMnemonic('u');
        durationField = new JTextField(5);
        durationLabel.setLabelFor(durationField);
        durationField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                    durationField.setBorder(BorderFactory.createLineBorder(ERROR_COLOR));
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    durationField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(durationLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        panel.add(durationField, gbc);

        JLabel categoryLabel = new JLabel("Categoría:");
        categoryLabel.setDisplayedMnemonic('C');
        categoryCombo = new JComboBox<>(new String[] {"Acción", "Aventura", "Animación", "Comedia", "Drama", "Terror", "Ciencia Ficción", "Romántica", "Documental"});
        categoryLabel.setLabelFor(categoryCombo);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(categoryLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(categoryCombo, gbc);

        return panel;
    }

    private JPanel createCommercialInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Información Comercial",
                TitledBorder.LEFT,
                TitledBorder.TOP));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel priceLabel = new JLabel("Precio (€):");
        priceLabel.setDisplayedMnemonic('P');
        priceField = new JTextField(10);
        priceLabel.setLabelFor(priceField);
        priceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                    priceField.setBorder(BorderFactory.createLineBorder(ERROR_COLOR));
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    priceField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(priceLabel, gbc);
        gbc.gridx = 1;
        panel.add(priceField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        saveButton = new JButton("Guardar");
        saveButton.setMnemonic('G');
        saveButton.setBackground(BUTTON_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveMovie());

        cancelButton = new JButton("Cancelar");
        cancelButton.setMnemonic('C');
        cancelButton.addActionListener(e -> cancelAction());

        panel.add(saveButton);
        panel.add(cancelButton);

        return panel;
    }

    private void saveMovie() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El título es obligatorio",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            titleField.requestFocus();
            return;
        }

        if (directorField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El director es obligatorio",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            directorField.requestFocus();
            return;
        }

        try {
            if (!durationField.getText().trim().isEmpty()) {
                Integer.parseInt(durationField.getText().trim());
            } else {
                JOptionPane.showMessageDialog(this,
                        "La duración es obligatoria",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                durationField.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "La duración debe ser un número entero",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            durationField.requestFocus();
            return;
        }

        try {
            if (!priceField.getText().trim().isEmpty()) {
                Double.parseDouble(priceField.getText().trim());
            } else {
                JOptionPane.showMessageDialog(this,
                        "El precio es obligatorio",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                priceField.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El precio debe ser un número decimal",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            priceField.requestFocus();
            return;
        }

        PeliculaDAO.storeMovie(new PeliculaRedesign(2, titleField.getText(), directorField.getText(), Integer.parseInt(durationField.getText().trim()), Double.parseDouble(priceField.getText())));

        JOptionPane.showMessageDialog(this,
                "Película guardada correctamente",
                "Operación exitosa",
                JOptionPane.INFORMATION_MESSAGE);

        clearForm();
    }

    private void cancelAction() {
        int option = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea cancelar la operación?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            clearForm();
            mainWindow.showMainPanel();
        }
    }

    private void clearForm() {
        titleField.setText("");
        directorField.setText("");
        durationField.setText("");
        categoryCombo.setSelectedIndex(0);
        priceField.setText("");
        titleField.requestFocus();
    }
}