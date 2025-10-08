package GUI;

import javax.swing.*;

public class SplashScreen extends JFrame {
    private JPanel root;
    private JButton launchButton;

    public SplashScreen() {
        setTitle("Contact Manager");
        setContentPane(root);
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- KEY CHANGE ---
        // Attach the action listener directly in the constructor.
        launchButton.addActionListener(e -> {
            launchApp();
        });
    }

    public void launchApp() {
        SwingUtilities.invokeLater(() -> {
            GUI.ContactsApp app = new GUI.ContactsApp();
            app.setVisible(true);

            // Close the splash screen after launching the main app
            this.dispose();
        });
    }

    // The onClick() method is no longer needed.

    // Add a main method to run your application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.setVisible(true);
        });
    }
}