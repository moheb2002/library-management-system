package models;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;

public class LoginView {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";

    public void showLogin() {
        JFrame frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        frame.add(panel);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JLabel typeLabel = new JLabel("Type:");

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Admin", "Regular"});

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(typeLabel);
        panel.add(typeBox);
        panel.add(loginButton);
        panel.add(registerButton);

        loginButton.addActionListener((ActionEvent e) -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String type = (String) typeBox.getSelectedItem();

            if (UserFactory.authenticateUser(username, password, type)) {
                JOptionPane.showMessageDialog(frame, "Login successful!");
                if (type.equals("Admin")) {
                    AdminDashboardView adminView = new AdminDashboardView(username);
                    adminView.showAdminDashboard();
                } else {
                    RegularDashboardView regularView = new RegularDashboardView(username);
                    regularView.showRegularDashboard();
                }
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials!");
            }
        });

        registerButton.addActionListener((ActionEvent e) -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String type = (String) typeBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username and Password cannot be empty!");
            } else if (!Pattern.matches(PASSWORD_REGEX, password)) {
                JOptionPane.showMessageDialog(frame, "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 8 characters long.");
            } else {
                if (UserFactory.registerUser(username, password, type)) {
                    JOptionPane.showMessageDialog(frame, "Registration successful!");
                } else {
                    JOptionPane.showMessageDialog(frame, "User already exists! Please choose another username.");
                }
            }
        });

        frame.setVisible(true);
    }
}
