package GUI;

import Components.Button;
import Components.Panel;
import Components.TextField;
import Services.UsersService;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends Page {

    private final Panel loginPanel = new Panel();

    private final Button registerButton = new Button("New customer? start here.");
    private final Button loginButton = new Button("Login");
    private final Button backButton = new Button("Back");
    private final JLabel phoneOrEmailLabel = new JLabel("Phone number or email:");
    private final JLabel passwordLabel = new JLabel("Password:");
    private final TextField phoneOrEmailField = new TextField("Enter phone number or email");
    private final TextField passwordField = new TextField("Enter password");
    private final JLabel warningLabel = new JLabel("", JLabel.CENTER);
    private final JLabel titleLabel = new JLabel("Login");

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String PHONE_REGEX = "^\\+?\\d{10,15}$";


    LoginPage() {
        initPage();
    }

    @Override
    protected void initPage() {
        setupBackground();
        setupMenu();
        actionListener();
        setupLoginPanel();
        setupLayout();
    }

    public void actionListener() {
        switchToPageWhenPressed(backButton, "PreviousPage");
        switchToPageWhenPressed(registerButton, "RegisterPage");
        loginButton.addActionListener(e -> getInput());
    }

    private void setupLoginPanel() {
        loginPanel.setArch(20);
        loginPanel.setBackground(Color.WHITE);

        registerButton.setOpaque(false);
        registerButton.setForeground(new Color(10, 100, 255));

        titleLabel.setOpaque(false);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        warningLabel.setForeground(Color.RED);
        warningLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        setupLoginPanelLayout();
    }

    private void setupLayout() {
        GroupLayout layout = new GroupLayout(contentPanel);
        contentPanel.setLayout(layout);

        // Create horizontal group
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(Short.MAX_VALUE, Short.MAX_VALUE)
                        .addComponent(loginPanel, 400, 500, 600)
                        .addContainerGap(Short.MAX_VALUE, Short.MAX_VALUE)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(60, Short.MAX_VALUE)
                        .addComponent(loginPanel, 600, 700, 800)
                        .addContainerGap(60, Short.MAX_VALUE)
        );
    }

    private void setupLoginPanelLayout() {
        GroupLayout layout = new GroupLayout(loginPanel);
        loginPanel.setLayout(layout);

        // Create horizontal group
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(40, 60)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(titleLabel, GroupLayout.Alignment.CENTER)
                                .addComponent(phoneOrEmailLabel)
                                .addComponent(phoneOrEmailField)
                                .addComponent(passwordLabel)
                                .addComponent(passwordField)
                                .addComponent(warningLabel, GroupLayout.Alignment.CENTER)
                                .addComponent(loginButton, GroupLayout.Alignment.CENTER, 100, 250, 250)
                                .addComponent(backButton, GroupLayout.Alignment.CENTER, 100, 250, 250)
                                .addComponent(registerButton, GroupLayout.Alignment.CENTER, 100, 200, 200))
                        .addContainerGap(40, 60)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(100, 100)
                        .addGap(30)
                        .addComponent(titleLabel, 40 ,40, 40)
                        .addGap(20)
                        .addComponent(phoneOrEmailLabel, 30, 30, 30)
                        .addGap(5)
                        .addComponent(phoneOrEmailField, 30, 30, 30)
                        .addGap(20)
                        .addComponent(passwordLabel, 30, 30, 30)
                        .addGap(5)
                        .addComponent(passwordField, 30, 30, 30)
                        .addGap(30, 50, 60)
                        .addComponent(warningLabel, 30, 30, 30)
                        .addGap(10)
                        .addComponent(loginButton, 30, 30, 30)
                        .addGap(15)
                        .addComponent(backButton, 30, 30, 30)
                        .addGap(15)
                        .addComponent(registerButton, 15, 15, 15)
                        .addGap(50)
                        .addContainerGap(100, 100)

        );
    }

    private void getInput() {
        if (phoneOrEmailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            updateWarningLabel("Please fill mandatory fields");
        } else if (!phoneOrEmailField.getText().matches(PHONE_REGEX) && !phoneOrEmailField.getText().matches(EMAIL_REGEX)) {
            updateWarningLabel("Please enter a valid phone number / email");
        } else if (UsersService.login(phoneOrEmailField.getText(), passwordField.getText())) {
            warningLabel.setForeground(Color.GRAY);
            updateWarningLabel("Logged in successfully. Redirecting...");
        } else if (phoneOrEmailField.getText().matches(PHONE_REGEX)){
            updateWarningLabel("Wrong phone number / password");
        } else if (phoneOrEmailField.getText().matches(EMAIL_REGEX)) {
            updateWarningLabel("Wrong email / password");
        }
    }

    public void updateWarningLabel(String warning) {
        warningLabel.setText(warning);
        warningLabel.revalidate();
        warningLabel.repaint();
    }
}
