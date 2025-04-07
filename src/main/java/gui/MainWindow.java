package gui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainWindow() {
        initFrame();
        innitPanels();
    }

    private void initFrame() {
        setTitle("T9");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setResizable(false);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        setContentPane(contentPanel);

        setLocationRelativeTo(null);
    }

    private void innitPanels() {
        contentPanel.add(new MainPanel(this), "mainPanel");
        contentPanel.add(new AddMovie(this), "addMovie");
        contentPanel.add(new SearchMovies(this), "searchMovies");
        contentPanel.add(new UpdateMovie(this), "updateMovie");

        cardLayout.show(contentPanel, "mainPanel");
        pack();
    }

    public void showPanel(String panelName, Object... args) {
        if (args.length > 0) {
            for (Component component : contentPanel.getComponents()) {
                if (panelName.equals(component.getName())) {
                    cardLayout.show(contentPanel, panelName);
                } else if (panelName.equals("updateMovie") && component instanceof UpdateMovie updateMovie) {
                    int id = 2;
                    if (args.length == 1) {
                        id = (int) args[0];
                    }
                    updateMovie.setMovieToUpdate(id);
                }
            }
        }
        cardLayout.show(contentPanel, panelName);
        pack();
    }

    public void showMainPanel() {
        showPanel("mainPanel");
    }
}
