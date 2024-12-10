package GUI;

import Components.*;
import Components.Button;
import Components.Menu;
import Components.Panel;
import Components.ScrollPane;
import Components.TextField;
import Objects.*;
import Services.UsersService;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
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
        menuPanel.addButton("Addresses", "StoresImg", e -> switchToPanel(AddressesPanel::new));
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

    private static class UserInfoPanel extends JPanel {

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
            setupInfoPanel();
            setupInfoLayout();
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

            setupInfoPanelLayout();
        }

        private void setupInfoLayout() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(40, 40, 40, 40));
            add(infoPanel);
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

    private class AddressesPanel extends JPanel {

        private final Panel addAddressPanel = new Panel();
        private final Panel addressesPanel = new Panel();

        private final JPanel addressesPanelsPanel = new JPanel();
        private final ScrollPane addressesScrollPane = new ScrollPane(addressesPanelsPanel);

        private final Button addButton = new Button("Add");
        private final Button cancelButton = new Button("Cancel");

        private final JLabel titleLabel = new JLabel("Addresses");
        private final JLabel countryLabel = new JLabel("Country:");
        private final JLabel cityLabel = new JLabel("City:");
        private final JLabel postalCodeLabel = new JLabel("Postal code:");
        private final JLabel additionalInfoLabel = new JLabel("Additional Information:");

        private final TextField countryField = new TextField("Enter your country");
        private final TextField cityField = new TextField("Enter your city");
        private final TextField postalCodeField = new TextField("Enter your postalCode");
        private final TextField additionalInfoField = new TextField("(e.g street number, neighbourhood name, house number)");

        private final Color buttonColor = Color.WHITE;
        private ArrayList<Address> addresses;
        private int totalAddresses = 0;

        private final int addressPanelHeight = 150;

        AddressesPanel() {
            initPanel();
        }

        private void initPanel() {
            actionListener();
            setupAddressesPanel();
            setupAddressesLayout();
        }

        private void actionListener() {
            addressesScrollPane.getVerticalScrollBar().addAdjustmentListener(e -> new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    loadVisibleAddresses();
                    return null;
                }
            }.execute());
            addressesScrollPane.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if (!MyFrame.isInAnimation()) {
                        new SwingWorker<Void, Void>() {
                            @Override
                            protected Void doInBackground() {
                                addressesPanelsPanel.removeAll();
                                loadVisibleAddresses();
                                return null;
                            }
                        }.execute();
                    }
                }
            });
        }

        private void setupAddressesPanel() {
            addAddressPanel.setArch(20);
            addAddressPanel.setBackground(Color.WHITE);
            addressesPanel.setArch(20);
            addressesPanel.setBackground(Color.WHITE);

            Font font = new Font("SanSerif", Font.PLAIN, 14);
            for (JLabel label : new JLabel[]{countryLabel, cityLabel, postalCodeLabel, additionalInfoLabel}) {
                label.setOpaque(false);
                label.setForeground(new Color(102, 102, 102));
                label.setFont(font);
            }

            titleLabel.setOpaque(false);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

            addressesPanelsPanel.setBackground(Color.WHITE);
            addressesPanelsPanel.setLayout(null);
            addressesPanel.setPreferredSize(new Dimension(350, 0));
            addressesPanel.setLayout(new BorderLayout());
            addressesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            addressesPanel.add(addressesScrollPane);

            addButton.addActionListener(e -> getInput());
            cancelButton.addActionListener(e -> {
                countryField.setText("");
                cityField.setText("");
                postalCodeField.setText("");
                additionalInfoField.setText("");
            });


            updateAddressesPanel();
            setupAddressesPanelLayout();
        }

        private void getInput() {
            String country = countryField.getText();
            String city = cityField.getText();
            String postalCode = postalCodeField.getText();
            String additionalInfo = additionalInfoField.getText();

            if (country.isEmpty() || city.isEmpty() || postalCode.isEmpty() || additionalInfo.isEmpty()) {
                new PopupMessage("Please fill the blank fields", PopupMessage.Type.WARNING);
                return;
            }

            UsersService.registerAddress(country, city, postalCode, additionalInfo);
            switchToPanel(AddressesPanel::new);
            new PopupMessage("Added Address Successfully", PopupMessage.Type.SUCCESS);
        }

        private void setupAddressesLayout() {
            setLayout(new BorderLayout(30, 0));
            setBorder(new EmptyBorder(40, 40, 40, 40));
            add(addressesPanel, BorderLayout.EAST);
            add(addAddressPanel, BorderLayout.CENTER);
        }

        private void setupAddressesPanelLayout() {
            GroupLayout layout = new GroupLayout(addAddressPanel);
            addAddressPanel.setLayout(layout);

            // Create horizontal group
            layout.setHorizontalGroup(
                    layout.createSequentialGroup()
                            .addContainerGap(100, Short.MAX_VALUE)
                            .addGap(20)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(countryLabel, 400, 400, 400)
                                    .addComponent(countryField, 400, 400, 400)
                                    .addComponent(cityLabel, 400, 400, 400)
                                    .addComponent(cityField, 400, 400, 400)
                                    .addComponent(postalCodeLabel, 400, 400, 400)
                                    .addComponent(postalCodeField, 400, 400, 400)
                                    .addComponent(additionalInfoLabel, 400, 400, 400)
                                    .addComponent(additionalInfoField, 400, 400, 400)
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(addButton, 50, 80, 180)
                                            .addGap(20)
                                            .addComponent(cancelButton, 50, 80, 180)))
                            .addGap(20)
                            .addContainerGap(100, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup()
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap(60, Short.MAX_VALUE)
                                    .addComponent(countryLabel, 30, 30, 30)
                                    .addGap(5)
                                    .addComponent(countryField, 30, 30, 30)
                                    .addGap(50)
                                    .addComponent(cityLabel, 30, 30, 30)
                                    .addGap(5)
                                    .addComponent(cityField, 30, 30, 30)
                                    .addGap(50)
                                    .addComponent(postalCodeLabel, 30, 30, 30)
                                    .addGap(5)
                                    .addComponent(postalCodeField, 30, 30, 30)
                                    .addGap(50)
                                    .addComponent(additionalInfoLabel, 30, 30, 30)
                                    .addGap(5)
                                    .addComponent(additionalInfoField, 30, 30, 30)
                                    .addGap(25, 40, 60)
                                    .addGroup(layout.createParallelGroup()
                                            .addComponent(addButton, 30, 30, 30)
                                            .addComponent(cancelButton, 30 ,30, 30))
                                    .addContainerGap(60, Short.MAX_VALUE))

            );
        }

        private void updateAddressesPanel() {
            addressesPanelsPanel.removeAll();
            addresses = UsersService.getAddresses(Main.getCurrentUser().getId());
            totalAddresses = addresses.size();
            loadVisibleAddresses();
        }

        private void loadVisibleAddresses() {

            int panelWidth = addressesScrollPane.getWidth();
            int verticalGap = 6;

            int productsPerRow = 1;
            int rows = totalAddresses;
            int totalHeight = rows * (addressPanelHeight + verticalGap) + 50;

            addressesPanelsPanel.setPreferredSize(new Dimension(panelWidth, totalHeight));

            // Get the visible area of the JScrollPane
            Rectangle visibleRect = addressesScrollPane.getViewport().getViewRect();

            int firstVisibleRow = Math.max(0, visibleRect.y / (addressPanelHeight + verticalGap));
            int lastVisibleRow = Math.max(0, ((visibleRect.y + visibleRect.height) / (addressPanelHeight + verticalGap)) + 2);

            if (firstVisibleRow > 0) {firstVisibleRow-= 2;}

            // Calculate product indices for visible rows
            int startIndex = firstVisibleRow * productsPerRow;
            int endIndex = Math.min(lastVisibleRow * productsPerRow, totalAddresses);

            // Clear buttons outside the visible area
            for (Component component : addressesPanelsPanel.getComponents()) {
                if (component instanceof Panel panel) {
                    int panelIndex = (int) panel.getClientProperty("index");
                    if (panelIndex < startIndex || panelIndex >= endIndex) {
                        addressesPanelsPanel.remove(panel); // Remove buttons outside the visible area
                    }
                }
            }

            // Add buttons in visible area
            for (int i = startIndex; i < endIndex; i++) {
                if (!isPanelVisible(i)) {
                    Address address = addresses.get(i);
                    JPanel addressPanel = getAddressPanel(address);
                    addressPanel.putClientProperty("index", i);

                    int x = 2;
                    int y = i * (addressPanelHeight + verticalGap);

                    addressPanel.setBounds(x, y, panelWidth - 4, addressPanelHeight);
                    addressesPanelsPanel.add(addressPanel);
                }
            }
            addressesPanelsPanel.revalidate();
            addressesPanelsPanel.repaint();
        }

        private boolean isPanelVisible(int index) {
            for (Component component : addressesPanelsPanel.getComponents()) {
                if (component instanceof Panel panel) {
                    Integer PanelIndex = (Integer) panel.getClientProperty("index");
                    if (PanelIndex != null && PanelIndex == index) {
                        return true;
                    }
                }
            }
            return false;
        }

        //Buttons for Stores
        private JPanel getAddressPanel(Address address) {
            Panel addressPanel = new Panel(Color.WHITE);

            addressPanel.setArch(20);
            addressPanel.setBorderPainted(true);
            addressPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            addressPanel.setLayout(new BorderLayout());
            JPanel textPanel = getAddressInfoPanel(address);

            addressPanel.add(textPanel, BorderLayout.CENTER);

            return addressPanel;
        }

        private JPanel getAddressInfoPanel(Address address) {
            JPanel textPanel = new JPanel();
            JPanel extraPanel = new JPanel();
            textPanel.setLayout(new BorderLayout());
            textPanel.setBackground(buttonColor);
            textPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JLabel countryButtonLabel = new JLabel(address.getCountry());
            countryButtonLabel.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel cityButtonLabel = new JLabel(address.getCity());
            cityButtonLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            extraPanel.setBackground(buttonColor);
            extraPanel.setLayout(new BorderLayout());

            JLabel postalCodeButtonLabel = new JLabel(address.getPostalCode());
            postalCodeButtonLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel additionalInfoButtonLabel = new JLabel(address.getAdditionalInfo());
            additionalInfoButtonLabel.setFont(new Font("Arial", Font.PLAIN, 12));

            extraPanel.add(postalCodeButtonLabel, BorderLayout.CENTER);
            extraPanel.add(additionalInfoButtonLabel, BorderLayout.SOUTH);

            textPanel.add(countryButtonLabel, BorderLayout.NORTH);
            textPanel.add(cityButtonLabel, BorderLayout.CENTER);
            textPanel.add(extraPanel, BorderLayout.SOUTH);
            return textPanel;
        }
    }
}
