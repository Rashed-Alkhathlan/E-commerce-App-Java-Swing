package GUI;

import Services.UsersService;

import javax.swing.*;

public class LoginPage extends Page {

    JButton loginButton = new JButton("Login");
    JButton backButton = new JButton("Back");
    JLabel emailLabel = new JLabel("Email:");
    JLabel passLabel = new JLabel("Password:");
    JTextField phoneOrEmailField = new JTextField("");
    JTextField passwordField = new JTextField("");
    JLabel warningLabel = new JLabel("", JLabel.CENTER);

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String PHONE_REGEX = "^\\+?\\d{10,15}$";


    LoginPage() {
        initPage();
    }

    @Override
    protected void initPage() {
        defaultBackground();
        actionListener();

        setButton(loginButton, sidePanel,(sidePanelWidth / 2) - 100, sidePanelHeight - 250, 200, 25);
        setButton(backButton, sidePanel, (sidePanelWidth / 2) - 100, sidePanelHeight - 200, 200, 25);

        setLabel(emailLabel, sidePanel, (sidePanelWidth / 2) - 100, (sidePanelHeight / 3) - 75, 200, 25);
        setLabel(passLabel, sidePanel, (sidePanelWidth / 2) - 100, (sidePanelHeight / 3) - 25, 200, 25);

        setTextField(phoneOrEmailField, sidePanel, (sidePanelWidth / 2) - 100, (sidePanelHeight / 3) - 50, 200, 25);
        setTextField(passwordField, sidePanel, (sidePanelWidth / 2) - 100, (sidePanelHeight / 3), 200, 25);

        setLabel(warningLabel, sidePanel, (sidePanelWidth / 2) - 100, sidePanelHeight - 275, 200, 25);
    }

    @Override
    public void actionListener() {
        switchToPageWhenPressed(backButton, "PreviousPage");
        loginButton.addActionListener(e -> getInput());
    }

    private void getInput() {
        if (phoneOrEmailField.getText().matches(PHONE_REGEX) || phoneOrEmailField.getText().matches(EMAIL_REGEX)) {
            if(UsersService.login(phoneOrEmailField.getText(), passwordField.getText())) {
                updateWarningLabel("");
                MyFrame.switchToPage("HomePage");
            } else {
                updateWarningLabel("Wrong credentials");
            }
        }
    }

    public void updateWarningLabel(String warning) {
        warningLabel.setText(warning);
        warningLabel.revalidate();
        warningLabel.repaint();
    }
}
