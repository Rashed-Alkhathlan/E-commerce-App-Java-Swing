package GUI;

import Components.Menu;
import Components.Panel;
import Components.TextField;
import Components.UserRoleLabel;
import Objects.*;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Supplier;

public class AccountPage extends Page {
    protected final Menu menuPanel = new Menu();

    AccountPage() {
        initPage();
    }

    AccountPage(boolean subClass) {
        initSubPage();
    }

    @Override
    protected void initPage() {
        if (Main.getCurrentUser() instanceof Customer) {
            add(new CustomerAccount());
        } else if (Main.getCurrentUser() instanceof Manager) {
            add(new ManagerAccount());
        } else if (Main.getCurrentUser() instanceof Admin) {
            add(new AdminAccount());
        }
    }

    private void initSubPage() {
        setupBackground();
        setupMenu();
        switchToPanel(UserInfoPanel::new);
    }

    @Override
    protected void setupMenu() {
        menuPanel.setExpandedStatus(true);
        menuPanel.addDivider();
        menuPanel.addButton("Home", "HomeImg", e -> MyFrame.showPage("HomePage"));
        menuPanel.addButton("User information", "ManImg", e -> switchToPanel(UserInfoPanel::new));
        menuPanel.addButton("Orders", "ProductsImg", e -> MyFrame.showPage("HomePage"));
        menuPanel.addButton("Addresses", "StoresImg", e -> MyFrame.showPage("HomePage"));
        menuPanel.addDivider();

        headerPanel.addMenuButtonAction(e -> menuPanel.slide());
        add(menuPanel, BorderLayout.WEST);
    }

    protected void switchToPanel(Supplier<JPanel> panelSupplier) {
        MyFrame.load(() -> {
            contentPanel.removeAll();
            contentPanel.add(panelSupplier.get());
            contentPanel.revalidate();
            contentPanel.repaint();
        });
    }

    private static class UserInfoPanel extends Panel {

        private final Panel infoPanel = new Panel();

        private final JLabel firstNameLabel = new JLabel("First name:");
        private final JLabel lastNameLabel = new JLabel("Last name:");
        private final JLabel phoneLabel = new JLabel("Phone Number:");
        private final JLabel emailLabel = new JLabel("Email: (optional)");
        private final JLabel passwordLabel = new JLabel("Password:");
        private final JLabel joinedDateLabel = new JLabel("Date joined:", JLabel.CENTER);

        private final JLabel userRoleLabel = new JLabel("User Type:");
        private final UserRoleLabel userRole = new UserRoleLabel();
        private final User user = Main.getCurrentUser();

        private final TextField firstNameField = new TextField(user.getFirstName());
        private final TextField lastNameField = new TextField(user.getLastName());
        private final TextField phoneField = new TextField(user.getPhoneNumber());
        private final TextField emailField = new TextField(user.getEmail());
        private final TextField passwordField = new TextField(user.getPassword());
        private final JLabel joinedDate = new JLabel(user.getJoinedDate(), JLabel.CENTER);

        UserInfoPanel() {
            initPanel();
        }

        private void initPanel() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(40, 40, 40, 40));
            setupInfoPanel();
        }

        private void setupInfoPanel() {
            infoPanel.setArch(20);
            infoPanel.setBackground(Color.WHITE);

            for (TextField textField : new TextField[]{firstNameField, lastNameField, phoneField, emailField, passwordField}) {
                textField.setEditable(false);
                textField.setFocusable(false);
                textField.setBorder(null);
            }

            Font font = new Font("SanSerif", Font.PLAIN, 14);
            for (JLabel label : new JLabel[]{firstNameLabel, lastNameLabel, phoneLabel, emailLabel, passwordLabel, joinedDateLabel, joinedDate}) {
                label.setOpaque(false);
                label.setForeground(new Color(102, 102, 102));
                label.setFont(font);
            }

            joinedDateLabel.setFont(new Font("SanSerif", Font.BOLD, 16));

            userRoleLabel.setOpaque(false);
            userRoleLabel.setFont(new Font("SanSerif", Font.BOLD, 18));
            userRoleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            userRoleLabel.setForeground(new Color(102, 102, 102));

            userRole.setStatus(Main.getCurrentUser().getRole());
            userRole.setFont(new Font("SanSerif", Font.BOLD, 24));
            userRole.setArch(120);


            add(infoPanel);
            setupInfoPanelLayout();
        }

        private void setupInfoPanelLayout() {
            GroupLayout layout = new GroupLayout(infoPanel);
            infoPanel.setLayout(layout);

            // Create horizontal group
            layout.setHorizontalGroup(
                    layout.createSequentialGroup()
                            .addContainerGap(100, Short.MAX_VALUE)
                            .addGap(20)
                            .addGroup(layout.createParallelGroup()
                                    .addComponent(firstNameLabel, 400, 400, 400)
                                    .addComponent(firstNameField, 400, 400, 400)
                                    .addComponent(lastNameLabel, 400, 400, 400)
                                    .addComponent(lastNameField, 400, 400, 400)
                                    .addComponent(phoneLabel, 400, 400, 400)
                                    .addComponent(phoneField, 400, 400, 400)
                                    .addComponent(emailLabel, 400, 400, 400)
                                    .addComponent(emailField, 400, 400, 400)
                                    .addComponent(passwordLabel, 400, 400, 400)
                                    .addComponent(passwordField, 400, 400, 400))
                            .addGap(50, 200, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup()
                                    .addComponent(userRoleLabel, GroupLayout.Alignment.CENTER, 300, 300, 300)
                                    .addComponent(userRole, GroupLayout.Alignment.CENTER, 300, 300, 300)
                                    .addComponent(joinedDateLabel, GroupLayout.Alignment.CENTER, 300, 300, 300)
                                    .addComponent(joinedDate, GroupLayout.Alignment.CENTER, 300, 300, 300))
                            .addGap(20)
                            .addContainerGap(100, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup()
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap(60, Short.MAX_VALUE)
                                    .addComponent(firstNameLabel, 30, 30, 30)
                                    .addGap(5)
                                    .addComponent(firstNameField, 30, 30, 30)
                                    .addGap(50)
                                    .addComponent(lastNameLabel, 30, 30, 30)
                                    .addGap(5)
                                    .addComponent(lastNameField, 30, 30, 30)
                                    .addGap(50)
                                    .addComponent(phoneLabel, 30, 30, 30)
                                    .addGap(5)
                                    .addComponent(phoneField, 30, 30, 30)
                                    .addGap(50)
                                    .addComponent(emailLabel, 30, 30, 30)
                                    .addGap(5)
                                    .addComponent(emailField, 30, 30, 30)
                                    .addGap(50)
                                    .addComponent(passwordLabel, 30, 30, 30)
                                    .addGap(5)
                                    .addComponent(passwordField, 30, 30, 30)
                                    .addContainerGap(60, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap(60, Short.MAX_VALUE)
                                    .addComponent(userRoleLabel)
                                    .addGap(30)
                                    .addComponent(userRole, 150, 150, 150)
                                    .addGap(25)
                                    .addComponent(joinedDateLabel, 30, 30, 30)
                                    .addGap(3)
                                    .addComponent(joinedDate, 30, 30, 30)
                                    .addContainerGap(60, Short.MAX_VALUE))

            );
        }
    }
}
