package librarysystem;

import javax.swing.SwingUtilities;
import models.LoginView;


public class LibrarySystemApp {
        public void run() {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.showLogin();
        });
        }}
