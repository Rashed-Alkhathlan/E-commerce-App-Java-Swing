package GUI;

import Components.*;
import Components.Dialog;
import Components.Panel;
import Components.ScrollPane;
import Enums.StoreStatus;
import Enums.UserRole;
import Objects.Main;
import Objects.Manager;
import Objects.Store;
import Objects.User;
import Services.ProductsService;
import Services.StoresService;
import Services.UsersService;
import Utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminAccount extends AccountPage {

    AdminAccount() {
        super(true);
        initAdminPage();
    }

    private void initAdminPage() {
        setupAdminMenu();
    }

    private void setupAdminMenu() {
        menuPanel.addLabel("Admin Settings:", "SettingsImg");
        menuPanel.addButton("Dashboard", "DashImg", e -> switchToPanel(DashboardPanel::new));
        menuPanel.addButton("Manage managers", "ManImg", e -> switchToPanel(ManagersPanel::new));
        menuPanel.addButton("Manage stores", "ProductsImg", e -> switchToPanel(StoresPanel::new));
    }

    private class DashboardPanel extends Panel {

        private final Table table = new Table();
        private final ScrollPane tableScrollPane = new ScrollPane(table);
        private final Panel tablePanel = new Panel();
        private final JLabel tableLabel = new JLabel("Users data");

        private final CardPanel totalUsersPanel = new CardPanel(Images.getImage("ManImg", 30, 30));
        private final CardPanel totalStoresPanel = new CardPanel(Images.getImage("StoresImg", 30, 30));
        private final CardPanel totalOrdersPanel = new CardPanel(Images.getImage("FlagImg", 30, 30));

        DashboardPanel() {
            initPanel();
        }

        private void initPanel() {
            setupTable();
            setupStatsCards();
            setupLayout();
        }

        private void setupStatsCards() {
            totalUsersPanel.setAlignment(GroupLayout.Alignment.LEADING);
            totalUsersPanel.setArch(20);
            totalUsersPanel.setTitleLabel("Users");
            totalUsersPanel.setDescriptionLabel("Total: " + UsersService.getUsersCount());
            totalUsersPanel.setValuesLabel("Customers Managers and Admins");

            totalStoresPanel.setAlignment(GroupLayout.Alignment.LEADING);
            totalStoresPanel.setArch(20);
            totalStoresPanel.setTitleLabel("Stores");
            totalStoresPanel.setDescriptionLabel("Total: " + StoresService.getStoresCount());
            totalStoresPanel.setValuesLabel("Total Products: " + ProductsService.getProductCount());

            totalOrdersPanel.setAlignment(GroupLayout.Alignment.LEADING);
            totalOrdersPanel.setArch(20);
            totalOrdersPanel.setTitleLabel("Total Orders");
            totalOrdersPanel.setDescriptionLabel("Total Orders: 1231");
            totalOrdersPanel.setValuesLabel("Active Orders: 30");
        }

        private void setupTable() {
            tablePanel.setArch(20);
            tablePanel.setBackground(Color.WHITE);
            tablePanel.setLayout(new BorderLayout());
            tablePanel.add(tableScrollPane, BorderLayout.CENTER);
            tablePanel.add(tableLabel, BorderLayout.NORTH);
            tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            tableLabel.setFont(new Font("SanSerif", Font.BOLD, 20));
            tableLabel.setForeground(new Color(102, 102, 102));
            tableLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            tableLabel.setHorizontalAlignment(SwingConstants.LEFT);
            tableLabel.setOpaque(false);

            table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Name", "PhoneNumber", "Email", "Joined", "Type", "Action"}) {
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnIndex == 5;
                }
            });

            for (User user : UsersService.getUsers()) {
                if (!(user.getId().equals(Main.getCurrentUser().getId()))) {
                    table.addRow(new Object[]{user.getFirstName() + " " + user.getLastName(), user.getPhoneNumber(), user.getEmail() != null ? user.getEmail() : "not signed", user.getJoinedDate(), user.getRole(), (ActionListener) e -> {
                        Dialog dialog = new Dialog("Warning", "<html>Continuing means deleting the User and the corresponding Store(if they are a manager). Are sure you want to proceed?</html>");
                        dialog.setVisible(true);
                        dialog.addNoButtonAction(a -> dialog.dispose());
                        dialog.addYesButtonAction(a -> {
                            dialog.dispose();
                            UsersService.deleteUser(user.getId());
                            switchToPanel(DashboardPanel::new);
                        });
                    }});
                }
            }
        }

        private void setupLayout() {
            GroupLayout layout = new GroupLayout(this);
            setLayout(layout);

            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);

            // Create horizontal group
            layout.setHorizontalGroup(
                    layout.createSequentialGroup()
                            .addContainerGap(60, 100)
                            .addGap(40)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(tablePanel, 500, Short.MAX_VALUE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(totalOrdersPanel, 300, Short.MAX_VALUE, Short.MAX_VALUE)
                                            .addGap(20)
                                            .addComponent(totalStoresPanel, 300, Short.MAX_VALUE, Short.MAX_VALUE)
                                            .addGap(20)
                                            .addComponent(totalUsersPanel, 300, Short.MAX_VALUE, Short.MAX_VALUE)))
                            .addGap(40)
                            .addContainerGap(60, 100)
            );

            // Create vertical group
            layout.setVerticalGroup(
                    layout.createSequentialGroup()
                            .addContainerGap(100, Short.MAX_VALUE)
                            .addGap(20)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(totalUsersPanel, 150, 300, 300)
                                    .addComponent(totalStoresPanel, 150, 300, 300)
                                    .addComponent(totalOrdersPanel, 150, 300, 300))
                            .addGap(50)
                            .addComponent(tablePanel, 400, 500, Short.MAX_VALUE)
                            .addGap(20)
                            .addContainerGap(100, Short.MAX_VALUE)
            );
        }
    }

    private class ManagersPanel extends Panel {

        private final Table table = new Table();
        private final ScrollPane tableScrollPane = new ScrollPane(table);
        private final Panel tablePanel = new Panel();
        private final JLabel tableLabel = new JLabel("Managers data");

        private final CardPanel totalUsersPanel = new CardPanel(Images.getImage("ManImg", 30, 30));
        private final CardPanel totalStoresPanel = new CardPanel(Images.getImage("StoresImg", 30, 30));
        private final CardPanel totalOrdersPanel = new CardPanel(Images.getImage("FlagImg", 30, 30));

        ManagersPanel() {
            initPanel();
        }

        private void initPanel() {
            setupTable();
            setupStatsCards();
            setupLayout();
        }

        private void setupStatsCards() {
            totalUsersPanel.setAlignment(GroupLayout.Alignment.LEADING);
            totalUsersPanel.setArch(20);
            totalUsersPanel.setTitleLabel("Managers");
            totalUsersPanel.setDescriptionLabel("Total: " + UsersService.getUsersCount(UserRole.MANAGER));
            totalUsersPanel.setValuesLabel("");

            totalStoresPanel.setAlignment(GroupLayout.Alignment.LEADING);
            totalStoresPanel.setArch(20);
            totalStoresPanel.setTitleLabel("Stores");
            totalStoresPanel.setDescriptionLabel("Total: " + StoresService.getStoresCount());
            totalStoresPanel.setValuesLabel("Total Products: " + ProductsService.getProductCount());

            totalOrdersPanel.setAlignment(GroupLayout.Alignment.LEADING);
            totalOrdersPanel.setArch(20);
            totalOrdersPanel.setTitleLabel("Orders");
            totalOrdersPanel.setDescriptionLabel("Active Orders: 30");
            totalOrdersPanel.setValuesLabel("Total Orders: 1231");
        }

        private void setupTable() {
            tablePanel.setArch(20);
            tablePanel.setBackground(Color.WHITE);
            tablePanel.setLayout(new BorderLayout());
            tablePanel.add(tableScrollPane, BorderLayout.CENTER);
            tablePanel.add(tableLabel, BorderLayout.NORTH);
            tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            tableLabel.setFont(new Font("SanSerif", Font.BOLD, 20));
            tableLabel.setForeground(new Color(102, 102, 102));
            tableLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            tableLabel.setHorizontalAlignment(SwingConstants.LEFT);
            tableLabel.setOpaque(false);

            table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Name", "PhoneNumber", "Email", "Joined", "Type", "Action"}) {
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnIndex == 5;
                }
            });

            for (User user : UsersService.getUsers(UserRole.MANAGER)) {
                table.addRow(new Object[]{user.getFirstName() + " " + user.getLastName(), user.getPhoneNumber(), user.getEmail() != null ? user.getEmail() : "not signed", user.getJoinedDate(), user.getRole(), (ActionListener) e -> {
                    Dialog dialog = new Dialog("Warning", "<html>Continuing means deleting the Owner and the corresponding Store. Are sure you want to proceed?</html>");
                    dialog.setVisible(true);
                    dialog.addNoButtonAction(a -> dialog.dispose());
                    dialog.addYesButtonAction(a -> {
                        dialog.dispose();
                        UsersService.deleteUser(user.getId());
                        switchToPanel(ManagersPanel::new);
                    });
                }});
            }
        }

        private void setupLayout() {
            GroupLayout layout = new GroupLayout(this);
            setLayout(layout);

            // Create horizontal group
            layout.setHorizontalGroup(
                    layout.createSequentialGroup()
                            .addContainerGap(60, 100)
                            .addGap(40)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(tablePanel, 500, Short.MAX_VALUE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(totalOrdersPanel, 300, Short.MAX_VALUE, Short.MAX_VALUE)
                                            .addGap(20)
                                            .addComponent(totalStoresPanel, 300, Short.MAX_VALUE, Short.MAX_VALUE)
                                            .addGap(20)
                                            .addComponent(totalUsersPanel, 300, Short.MAX_VALUE, Short.MAX_VALUE)))
                            .addGap(40)
                            .addContainerGap(60, 100)
            );

            // Create vertical group
            layout.setVerticalGroup(
                    layout.createSequentialGroup()
                            .addContainerGap(100, Short.MAX_VALUE)
                            .addGap(20)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(totalUsersPanel, 150, 300, 300)
                                    .addComponent(totalStoresPanel, 150, 300, 300)
                                    .addComponent(totalOrdersPanel, 150, 300, 300))
                            .addGap(50)
                            .addComponent(tablePanel, 400, 500, Short.MAX_VALUE)
                            .addGap(20)
                            .addContainerGap(100, Short.MAX_VALUE)
            );
        }
    }

    private class StoresPanel extends Panel {

        private final Table table = new Table();
        private final ScrollPane tableScrollPane = new ScrollPane(table);
        private final Panel tablePanel = new Panel();
        private final JLabel tableLabel = new JLabel("Stores data");

        private final CardPanel totalUsersPanel = new CardPanel(Images.getImage("ManImg", 30, 30));
        private final CardPanel totalStoresPanel = new CardPanel(Images.getImage("StoresImg", 30, 30));
        private final CardPanel totalOrdersPanel = new CardPanel(Images.getImage("FlagImg", 30, 30));

        StoresPanel() {
            initPanel();
        }

        private void initPanel() {
            setupTable();
            setupStatsCards();
            setupLayout();
        }

        private void setupStatsCards() {
            totalUsersPanel.setAlignment(GroupLayout.Alignment.LEADING);
            totalUsersPanel.setArch(20);
            totalUsersPanel.setTitleLabel("Stores");
            totalUsersPanel.setDescriptionLabel("Total: " + StoresService.getStoresCount());
            totalUsersPanel.setValuesLabel("Total Products: " + ProductsService.getProductCount());

            totalStoresPanel.setAlignment(GroupLayout.Alignment.LEADING);
            totalStoresPanel.setArch(20);
            totalStoresPanel.setTitleLabel("Status");
            totalStoresPanel.setDescriptionLabel("Open Stores: " + StoresService.getStoresCount(StoreStatus.OPEN));
            totalStoresPanel.setValuesLabel("Closed Stores: " + StoresService.getStoresCount(StoreStatus.CLOSED));

            totalOrdersPanel.setAlignment(GroupLayout.Alignment.LEADING);
            totalOrdersPanel.setArch(20);
            totalOrdersPanel.setTitleLabel("Orders");
            totalOrdersPanel.setDescriptionLabel("Active Orders: 30");
            totalOrdersPanel.setValuesLabel("Total Orders: 1231");
        }

        private void setupTable() {
            tablePanel.setArch(20);
            tablePanel.setBackground(Color.WHITE);
            tablePanel.setLayout(new BorderLayout());
            tablePanel.add(tableScrollPane, BorderLayout.CENTER);
            tablePanel.add(tableLabel, BorderLayout.NORTH);
            tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            tableLabel.setFont(new Font("SanSerif", Font.BOLD, 20));
            tableLabel.setForeground(new Color(102, 102, 102));
            tableLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            tableLabel.setHorizontalAlignment(SwingConstants.LEFT);
            tableLabel.setOpaque(false);

            table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Name", "Products", "Owner", "Created", "Status", "Action"}){
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnIndex == 5;
                }
            });

            for (Store store : StoresService.getStores()) {
                Manager owner = StoresService.getStoreOwner(store.getId());
                table.addRow(new Object[]{store.getName().isEmpty() ? "Name not set" : store.getName(), StoresService.getStoreProductCount(store.getId()), owner.getFirstName() + " " + owner.getLastName(), store.getCreationDate(), store.getStatus(), (ActionListener) e -> {
                    Dialog dialog = new Dialog("Warning", "<html>Continuing means deleting the Store and the corresponding Products. Are sure you want to proceed?</html>");
                    dialog.setVisible(true);
                    dialog.addNoButtonAction(a -> dialog.dispose());
                    dialog.addYesButtonAction(a -> {
                        dialog.dispose();
                        StoresService.deleteStore(owner.getId());
                        switchToPanel(StoresPanel::new);
                    });
                }});
            }
        }

        private void setupLayout() {
            GroupLayout layout = new GroupLayout(this);
            setLayout(layout);

            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);

            // Create horizontal group
            layout.setHorizontalGroup(
                    layout.createSequentialGroup()
                            .addContainerGap(60, 100)
                            .addGap(40)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(tablePanel, 500, Short.MAX_VALUE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(totalOrdersPanel, 300, Short.MAX_VALUE, Short.MAX_VALUE)
                                            .addGap(20)
                                            .addComponent(totalStoresPanel, 300, Short.MAX_VALUE, Short.MAX_VALUE)
                                            .addGap(20)
                                            .addComponent(totalUsersPanel, 300, Short.MAX_VALUE, Short.MAX_VALUE)))
                            .addGap(40)
                            .addContainerGap(60, 100)
            );

            // Create vertical group
            layout.setVerticalGroup(
                    layout.createSequentialGroup()
                            .addContainerGap(100, Short.MAX_VALUE)
                            .addGap(20)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(totalUsersPanel, 150, 300, 300)
                                    .addComponent(totalStoresPanel, 150, 300, 300)
                                    .addComponent(totalOrdersPanel, 150, 300, 300))
                            .addGap(50)
                            .addComponent(tablePanel, 400, 500, Short.MAX_VALUE)
                            .addGap(20)
                            .addContainerGap(100, Short.MAX_VALUE)
            );
        }
    }
}
