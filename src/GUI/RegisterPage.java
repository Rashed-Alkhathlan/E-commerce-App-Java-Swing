package GUI;

import Components.Button;
import Components.Panel;
import Components.TextField;
import Services.UsersService;

import javax.swing.*;
import java.awt.*;

public class RegisterPage extends Page {

    private final Panel registerPanel = new Panel();

    private final Button registerButton = new Button("Register");
    private final Button backButton = new Button("Back");
    private final TextField firstNameField = new TextField("Enter first name");
    private final TextField lastNameField = new TextField("Enter last name");
    private final TextField phoneNumberField = new TextField("Enter phone number");
    private final TextField emailField = new TextField("(optional) Enter email");
    private final TextField passwordField = new TextField("Enter password");
    private final TextField confirmPasswordField = new TextField("Confirm password");
    private final JLabel firstNameLabel = new JLabel("First Name:*");
    private final JLabel lastNameLabel = new JLabel("Last Name:*");
    private final JLabel phoneNumberLabel = new JLabel("Phone Number:*");
    private final JLabel emailLabel = new JLabel("(optional) Email Address:");
    private final JLabel passwordLabel = new JLabel("Password:*");
    private final JLabel confirmPasswordLabel = new JLabel("Confirm Password:*");
    private final JLabel warningLabel = new JLabel("", JLabel.CENTER);
    private final JLabel titleLabel = new JLabel("Create Account");

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String PHONE_REGEX = "^\\+?\\d{10,15}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";


    RegisterPage() {
        initPage();
    }

    @Override
    protected void initPage() {
        setupBackground();
        actionListener();
        setupMenu();
        setupRegisterPanel();
        setupLayout();
    }

    protected void actionListener() {
        switchToPageWhenPressed(backButton, "PreviousPage");
        registerButton.addActionListener(e -> getInput());
    }

    private void setupRegisterPanel() {
        registerPanel.setArch(20);
        registerPanel.setBackground(Color.WHITE);

        titleLabel.setOpaque(false);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        warningLabel.setForeground(Color.RED);
        warningLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));

        setupRegisterPanelLayout();
    }

    private void setupLayout() {
        GroupLayout layout = new GroupLayout(contentPanel);
        contentPanel.setLayout(layout);

        // Create horizontal group
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(Short.MAX_VALUE, Short.MAX_VALUE)
                        .addComponent(registerPanel, 400, 500, 600)
                        .addContainerGap(Short.MAX_VALUE, Short.MAX_VALUE)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(60, Short.MAX_VALUE)
                        .addComponent(registerPanel, 600, 700, 800)
                        .addContainerGap(60, Short.MAX_VALUE)
        );
    }
    
    private void setupRegisterPanelLayout() {
        GroupLayout layout = new GroupLayout(registerPanel);
        registerPanel.setLayout(layout);

        // Create horizontal group
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(40, 60)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(titleLabel, GroupLayout.Alignment.CENTER)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup()
                                                .addComponent(firstNameLabel)
                                                .addComponent(firstNameField))
                                        .addGap(40)
                                        .addGroup(layout.createParallelGroup()
                                                .addComponent(lastNameLabel)
                                                .addComponent(lastNameField)))
                                .addComponent(phoneNumberLabel)
                                .addComponent(phoneNumberField)
                                .addComponent(emailLabel)
                                .addComponent(emailField)
                                .addComponent(passwordLabel)
                                .addComponent(passwordField)
                                .addComponent(confirmPasswordLabel)
                                .addComponent(confirmPasswordField)
                                .addComponent(warningLabel, GroupLayout.Alignment.CENTER)
                                .addComponent(registerButton, GroupLayout.Alignment.CENTER, 100, 250, 250)
                                .addComponent(backButton, GroupLayout.Alignment.CENTER, 100, 250, 250))
                        .addContainerGap(40, 60)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(100, 100)
                        .addGap(30)
                        .addComponent(titleLabel, 40 ,40, 40)
                        .addGap(20)
                        .addGroup(layout.createParallelGroup()
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(firstNameLabel, 30, 30, 30)
                                        .addGap(5)
                                        .addComponent(firstNameField, 30, 30, 30))
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(lastNameLabel, 30, 30, 30)
                                        .addGap(5)
                                        .addComponent(lastNameField, 30, 30, 30)))
                        .addGap(25)
                        .addComponent(phoneNumberLabel, 30, 30, 30)
                        .addGap(5)
                        .addComponent(phoneNumberField, 30, 30, 30)
                        .addGap(15)
                        .addComponent(emailLabel, 30, 30, 30)
                        .addGap(5)
                        .addComponent(emailField, 30, 30, 30)
                        .addGap(20)
                        .addComponent(passwordLabel, 30, 30, 30)
                        .addGap(5)
                        .addComponent(passwordField, 30, 30, 30)
                        .addGap(10)
                        .addComponent(confirmPasswordLabel, 30, 30, 30)
                        .addGap(5)
                        .addComponent(confirmPasswordField, 30, 30, 30)
                        .addGap(30, 50, 60)
                        .addComponent(warningLabel, 30, 30, 30)
                        .addGap(10)
                        .addComponent(registerButton, 30, 30, 30)
                        .addGap(15)
                        .addComponent(backButton, 30, 30, 30)
                        .addGap(50)
                        .addContainerGap(100, 100)

        );
    }

    private void getInput() {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || phoneNumberField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            updateWarningLabel("Please fill mandatory fields.");
        } else if (!phoneNumberField.getText().matches(PHONE_REGEX)) {
            updateWarningLabel("Please enter a valid phone number.");
        } else if (!emailField.getText().isEmpty() && !emailField.getText().matches(EMAIL_REGEX)) {
            updateWarningLabel("Please enter a valid email address.");
        } else if (!passwordField.getText().matches(PASSWORD_REGEX)){
            updateWarningLabel("Please enter a valid password.");
        } else if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            updateWarningLabel("Password not the same.");
        } else {
             int response = UsersService.register(firstNameField.getText(), lastNameField.getText(), phoneNumberField.getText(), emailField.getText(), passwordField.getText());
            if (response == -1) {
                updateWarningLabel("Phone number already in use.");
            } else if (response == 1) {
                updateWarningLabel("email already in use.");
            } else {
                warningLabel.setForeground(Color.GRAY);
                updateWarningLabel("Account created successfully. Redirecting...");
            }
        }
    }

    public void updateWarningLabel(String warning) {
        warningLabel.setText(warning);
        warningLabel.revalidate();
        warningLabel.repaint();
    }

}
