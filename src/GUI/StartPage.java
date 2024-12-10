package GUI;

import Components.Button;
import Components.Panel;
import Utils.Images;

import javax.swing.*;
import java.awt.*;

public class StartPage extends Page {

    private final Panel StartPanel = new Panel();

    private final Button loginButton = new Button("Login");
    private final Button registerButton = new Button("Register");
    private final Button guestButton = new Button("Continue as guest");
    private final JLabel titleLabel = new JLabel("Welcome");
    private final JLabel logoLabel = new JLabel(Images.getImage("SmallLogoImg", 200, 200));

    StartPage() {
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
        loginButton.addActionListener(e -> MyFrame.showPage("LoginPage"));
        registerButton.addActionListener(e -> MyFrame.showPage("RegisterPage"));
        guestButton.addActionListener(e -> MyFrame.showPage("HomePage"));
    }

    private void setupLoginPanel() {
        StartPanel.setArch(20);
        StartPanel.setBackground(Color.WHITE);

        titleLabel.setOpaque(false);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        setupStartPanelLayout();
    }

    private void setupLayout() {
        GroupLayout layout = new GroupLayout(contentPanel);
        contentPanel.setLayout(layout);

        // Create horizontal group
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(Short.MAX_VALUE, Short.MAX_VALUE)
                        .addComponent(StartPanel, 400, 500, 600)
                        .addContainerGap(Short.MAX_VALUE, Short.MAX_VALUE)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(60, Short.MAX_VALUE)
                        .addComponent(StartPanel, 600, 700, 800)
                        .addContainerGap(60, Short.MAX_VALUE)
        );
    }

    private void setupStartPanelLayout() {
        GroupLayout layout = new GroupLayout(StartPanel);
        StartPanel.setLayout(layout);

        // Create horizontal group
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(40, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(titleLabel, GroupLayout.Alignment.CENTER)
                                .addComponent(logoLabel, GroupLayout.Alignment.CENTER)
                                .addComponent(loginButton, GroupLayout.Alignment.CENTER, 100, 250, 250)
                                .addComponent(registerButton, GroupLayout.Alignment.CENTER, 100, 250, 250)
                                .addComponent(guestButton, GroupLayout.Alignment.CENTER, 100, 250, 250))
                        .addContainerGap(40, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(100, 100)
                        .addComponent(logoLabel)
                        .addGap(10)
                        .addComponent(titleLabel, 40 ,40, 40)
                        .addGap(50, 80, Short.MAX_VALUE)
                        .addComponent(loginButton, 30, 30, 30)
                        .addGap(15)
                        .addComponent(registerButton, 30, 30, 30)
                        .addGap(15)
                        .addComponent(guestButton, 30, 30, 30)
                        .addGap(50)
                        .addContainerGap(100, 100)

        );
    }
}
