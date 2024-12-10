package GUI;

import Components.*;
import Components.Button;
import Components.Panel;
import Components.ScrollPane;
import Components.TextField;
import Enums.StoreStatus;
import Objects.Main;
import Objects.Product;
import Objects.Store;
import Services.ProductsService;
import Services.StoresService;
import Utils.Images;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class ManagerAccount extends AccountPage {

    private Store managerStore;

    ManagerAccount() {
        super(true);
        initManagerPage();
    }

    private void initManagerPage() {
        managerStore = StoresService.getStore(Main.getCurrentUser().getId());
        addManagerMenu();
    }

    private void addManagerMenu() {
        menuPanel.addLabel("Manager Settings:", "SettingsImg");
        menuPanel.addButton("Dashboard", "DashImg", e -> switchToPanel(DashboardPanel::new));
        menuPanel.addButton("Customize store", "StoresImg", e -> switchToPanel(CustomizeStorePanel::new));
        menuPanel.addButton("Add products", "ProductsImg", e -> switchToPanel(addProductsPanel::new));
    }

    private class DashboardPanel extends Panel {

        private final Table table = new Table();
        private final ScrollPane tableScrollPane = new ScrollPane(table);
        private final Panel tablePanel = new Panel();
        private final JLabel tableLabel = new JLabel("Products data");

        private final CardPanel totalProductsPanel = new CardPanel(Images.getImage("ProductsImg", 30, 30));
        private final CardPanel totalOrdersPanel = new CardPanel(Images.getImage("FlagImg", 30, 30));
        private final Panel storeStatusPanel = new Panel();

        DashboardPanel() {
            initPanel();
        }

        private void initPanel() {
            setupTable();
            setupStatsCards();
            setupLayout();
        }

        private void setupStatsCards() {
            totalProductsPanel.setAlignment(GroupLayout.Alignment.LEADING);
            totalProductsPanel.setArch(20);
            totalProductsPanel.setTitleLabel("Products");
            totalProductsPanel.setDescriptionLabel("Total: " + ProductsService.getProductCount(managerStore.getId()));
            totalProductsPanel.setValuesLabel("Stock: ");

            totalOrdersPanel.setAlignment(GroupLayout.Alignment.LEADING);
            totalOrdersPanel.setArch(20);
            totalOrdersPanel.setTitleLabel("Orders");
            totalOrdersPanel.setDescriptionLabel("Active: ");
            totalOrdersPanel.setValuesLabel("Total: ");

            storeStatusPanel.setArch(20);
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

            table.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Name", "Price", "Quantity", "Type"}) {
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }
            });

            for (Product product : ProductsService.getProducts(managerStore.getId())) {
                table.addRow(new Object[]{product.getId(), product.getName(), product.getPrice(), product.getQuantity(), StoreStatus.OPEN});
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
                                            .addComponent(storeStatusPanel, 300, Short.MAX_VALUE, Short.MAX_VALUE)
                                            .addGap(20)
                                            .addComponent(totalOrdersPanel, 300, Short.MAX_VALUE, Short.MAX_VALUE)
                                            .addGap(20)
                                            .addComponent(totalProductsPanel, 300, Short.MAX_VALUE, Short.MAX_VALUE)))
                            .addGap(40)
                            .addContainerGap(60, 100)
            );

            // Create vertical group
            layout.setVerticalGroup(
                    layout.createSequentialGroup()
                            .addContainerGap(100, Short.MAX_VALUE)
                            .addGap(20)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(totalProductsPanel, 150, 300, 300)
                                    .addComponent(totalOrdersPanel, 150, 300, 300)
                                    .addComponent(storeStatusPanel, 150, 300, 300))
                            .addGap(50)
                            .addComponent(tablePanel, 400, 500, Short.MAX_VALUE)
                            .addGap(20)
                            .addContainerGap(100, Short.MAX_VALUE)
            );
        }
    }

    private class CustomizeStorePanel extends JPanel {

        private final Panel customizePanel = new Panel();

        private final JPanel logoPanel = new JPanel();

        private final Button saveButton = new Button("Save");
        private final Button editButton = new Button("Edit");
        private final TextField storeNameField = new TextField("Enter a name for your store");
        private final JTextArea descriptionField = new JTextArea();
        private final JLabel storeNameLabel = new JLabel("Store name:");
        private final JLabel descriptionLabel = new JLabel("Description:");
        private final JLabel titleLabel = new JLabel("Customize Store");

        CustomizeStorePanel() {
            initPanel();
        }

        private void initPanel() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(40, 40, 40, 40));
            setupCustomizePanel();
        }

        private void setupCustomizePanel() {

            customizePanel.setArch(20);
            customizePanel.setBackground(Color.WHITE);

            Font font = new Font("SanSerif", Font.PLAIN, 14);
            for (JLabel label : new JLabel[]{storeNameLabel, descriptionLabel}) {
                label.setOpaque(false);
                label.setForeground(new Color(102, 102, 102));
                label.setFont(font);
            }

            enableDragAndDrop(logoPanel);

            titleLabel.setOpaque(false);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

            storeNameField.setText(managerStore.getName());
            descriptionField.setText(managerStore.getDescription());

            storeNameField.setEditable(false);
            storeNameField.setFocusable(false);
            storeNameField.setBorder(null);
            descriptionField.setEditable(false);
            descriptionField.setFocusable(false);
            descriptionField.setBackground(new Color(235, 235, 235));
            descriptionField.setBorder(new EmptyBorder(5, 5, 5, 5));
            DropTarget dropTarget = logoPanel.getDropTarget();
            if (dropTarget != null) {
                dropTarget.setActive(false); // Deactivate the DropTarget
            }

            final boolean[] firstChange = {true};
            descriptionField.setFont(new Font("SansSerif", Font.PLAIN, 13));
            descriptionField.setSelectionColor(new Color(220, 204, 182));
            descriptionField.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    String text = descriptionField.getText();
                    if (firstChange[0] && text.length() == 1) {
                        SwingUtilities.invokeLater(() -> descriptionField.setText(text.toUpperCase()));
                        firstChange[0] = false;
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {}

                @Override
                public void changedUpdate(DocumentEvent e) {}
            });

            descriptionField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    firstChange[0] = true;
                }
            });

            logoPanel.setBorder(new LineBorder(new Color(175, 175, 175)));
            logoPanel.add(new JLabel(managerStore.getMainImageIcon()));

            saveButton.addActionListener(e -> getInput());
            editButton.addActionListener(e -> {
                if (editButton.getText().equals("Edit")) {
                    if (dropTarget != null) {
                        dropTarget.setActive(true);
                    }
                    storeNameField.setEditable(true);
                    storeNameField.setFocusable(true);
                    storeNameField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(5, 5, 5, 5)));
                    descriptionField.setEditable(true);
                    descriptionField.setFocusable(true);
                    descriptionField.setBackground(Color.WHITE);
                    descriptionField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(5, 5, 5, 5)));
                    editButton.setText("Cancel");
                } else {
                    if (dropTarget != null) {
                        dropTarget.setActive(false);
                    }
                    storeNameField.setEditable(false);
                    storeNameField.setFocusable(false);
                    storeNameField.setBorder(null);
                    descriptionField.setEditable(false);
                    descriptionField.setFocusable(false);
                    descriptionField.setBackground(new Color(235, 235, 235));
                    descriptionField.setBorder(new EmptyBorder(5, 5, 5, 5));
                    storeNameField.setText(managerStore.getName());
                    descriptionField.setText(managerStore.getDescription());
                    logoPanel.removeAll();
                    logoPanel.add(new JLabel(managerStore.getMainImageIcon()));
                    logoPanel.revalidate();
                    logoPanel.repaint();
                    editButton.setText("Edit");
                }
            });

            add(customizePanel);
            setupCustomizePanelLayout();
        }

        private void getInput() {

            if (storeNameField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
                return;
            }

            managerStore.setName(storeNameField.getText());
            managerStore.setDescription(descriptionField.getText());

            if ((logoPanel.getClientProperty("Logo")) != null) {
                managerStore.setMainImageIcon((ImageIcon)((JLabel)logoPanel.getClientProperty("Logo")).getIcon());
            }

            StoresService.updateStore(managerStore.getId(), managerStore.getName(), managerStore.getDescription(), managerStore.getStatus(), managerStore.getMainImageIcon());
            MyFrame.load(() ->  {
                Timer timer = new Timer(250, null);
                timer.setRepeats(false);
                timer.start();
                DropTarget dropTarget = logoPanel.getDropTarget();
                if (dropTarget != null) {
                    dropTarget.setActive(false);
                }
                storeNameField.setEditable(false);
                storeNameField.setFocusable(false);
                storeNameField.setBorder(null);
                descriptionField.setEditable(false);
                descriptionField.setFocusable(false);
                descriptionField.setBackground(new Color(235, 235, 235));
                descriptionField.setBorder(new EmptyBorder(5, 5, 5, 5));
                storeNameField.setText(managerStore.getName());
                descriptionField.setText(managerStore.getDescription());
                logoPanel.removeAll();
                logoPanel.add(new JLabel(managerStore.getMainImageIcon()));
                logoPanel.revalidate();
                logoPanel.repaint();
                editButton.setText("Edit");
            });
        }

        private void setupCustomizePanelLayout() {
            GroupLayout layout = new GroupLayout(customizePanel);
            customizePanel.setLayout(layout);

            // Create horizontal group
            layout.setHorizontalGroup(
                    layout.createSequentialGroup()
                            .addContainerGap(40, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(titleLabel)
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(logoPanel, 200, 200, 200)
                                            .addGap(20)
                                            .addGroup(layout.createParallelGroup()
                                                    .addComponent(storeNameLabel, 250, 250, 250)
                                                    .addComponent(storeNameField, 250, 250, 250)))
                                    .addComponent(descriptionLabel, 470, 470, 470)
                                    .addComponent(descriptionField, 470, 470, 470)
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(saveButton, 50, 80, 200)
                                            .addGap(20)
                                            .addComponent(editButton, 50, 80, 200)))
                            .addContainerGap(40, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                    layout.createSequentialGroup()
                            .addContainerGap(100, 100)
                            .addGap(20)
                            .addComponent(titleLabel, 40, 40, 40)
                            .addGap(30)
                            .addGroup(layout.createParallelGroup()
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(storeNameLabel, 30, 30, 30)
                                            .addGap(5)
                                            .addComponent(storeNameField, 30, 30, 30))
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(logoPanel, 167, 167, 167)))
                            .addGap(25)
                            .addComponent(descriptionLabel, 30, 30, 30)
                            .addGap(5)
                            .addComponent(descriptionField, 120, 120, 120)
                            .addGap(30, 50, 60)
                            .addGroup(layout.createParallelGroup()
                                    .addComponent(saveButton, 30, 30, 30)
                                    .addComponent(editButton, 30, 30, 30))
                            .addContainerGap(100, 100)

            );
        }

        private void enableDragAndDrop(JPanel panel) {
            panel.setLayout(new BorderLayout());
            new DropTarget(panel, new DropTargetListener() {
                @Override
                public void dragEnter(DropTargetDragEvent dtde) {
                    panel.setBackground(Color.GRAY);
                }

                @Override
                public void dragOver(DropTargetDragEvent dtde) {}

                @Override
                public void dropActionChanged(DropTargetDragEvent dtde) {}

                @Override
                public void dragExit(DropTargetEvent dte) {
                    panel.setBackground(Color.LIGHT_GRAY);
                }

                @Override
                public void drop(DropTargetDropEvent dtde) {
                    try {
                        // Accept drop
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);

                        // Check for dropped files
                        Transferable transferable = dtde.getTransferable();
                        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            @SuppressWarnings("unchecked")
                            List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                            if (!files.isEmpty()) {
                                File logoFile = files.get(0);

                                // Ensure the file is an image
                                if (logoFile.getName().matches(".*\\.(png|jpg|jpeg|gif)")) {
                                    setLogo(logoFile);
                                } else {
                                    JOptionPane.showMessageDialog(panel, "Please drop a valid image file.", "Invalid File", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "Error handling the dropped file.", "Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        panel.setBackground(Color.LIGHT_GRAY);
                    }
                }
            });
        }

        private void setLogo(File logoFile) {
            try {
                // Display the logo in the panel
                ImageIcon logoIcon = Images.scaleImage(new ImageIcon(logoFile.getAbsolutePath()), logoPanel.getWidth(), logoPanel.getHeight());
                JLabel logoLabel = new JLabel(logoIcon);
                logoPanel.removeAll();
                logoPanel.putClientProperty("Logo", logoLabel);
                logoPanel.add(logoLabel);
                logoPanel.revalidate();
                logoPanel.repaint();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Could not load logo image.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class addProductsPanel extends JPanel {

        private final Panel addPanel = new Panel();

        private final JPanel logoPanel = new JPanel();

        private final Button addButton = new Button("Add");
        private final Button cancelButton = new Button("Cancel");
        private final TextField productNameField = new TextField("Enter a name for your Product");
        private final JTextArea descriptionField = new JTextArea();
        private final TextField productPriceField = new TextField("Enter a price");
        private final TextField productQuantityField = new TextField("Enter the quantity");
        private final JLabel productNameLabel = new JLabel("Product name:");
        private final JLabel descriptionLabel = new JLabel("Description:");
        private final JLabel productPriceLabel = new JLabel("Price:");
        private final JLabel productQuantityLabel = new JLabel("Quantity/Stock:");
        private final JLabel titleLabel = new JLabel("Add a Product");

        addProductsPanel() {
            initPanel();
        }

        private void initPanel() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(40, 40, 40, 40));
            setupAddingPanel();
        }

        private void setupAddingPanel() {

            addPanel.setArch(20);
            addPanel.setBackground(Color.WHITE);

            Font font = new Font("SanSerif", Font.PLAIN, 14);
            for (JLabel label : new JLabel[]{productNameLabel, descriptionLabel, productPriceLabel, productQuantityLabel}) {
                label.setOpaque(false);
                label.setForeground(new Color(102, 102, 102));
                label.setFont(font);
            }
            productPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);

            enableDragAndDrop(logoPanel);

            titleLabel.setOpaque(false);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

            final boolean[] firstChange = {true};
            descriptionField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(5, 5, 5, 5)));
            descriptionField.setFont(new Font("SansSerif", Font.PLAIN, 13));
            descriptionField.setSelectionColor(new Color(220, 204, 182));
            descriptionField.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    String text = descriptionField.getText();
                    if (firstChange[0] && text.length() == 1) {
                        SwingUtilities.invokeLater(() -> descriptionField.setText(text.toUpperCase()));
                        firstChange[0] = false;
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {}

                @Override
                public void changedUpdate(DocumentEvent e) {}
            });

            descriptionField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    firstChange[0] = true;
                }
            });

            logoPanel.setBorder(new LineBorder(new Color(175, 175, 175)));

            addButton.addActionListener(e -> getInput());
            cancelButton.addActionListener(e -> {
                productNameField.setText("");
                descriptionField.setText("");
                productPriceField.setText("");
                productQuantityField.setText("");
            });

            add(addPanel);
            setupAddingPanelLayout();
        }

        private void getInput() {
            String name = productNameField.getText();
            String description = descriptionField.getText();

            if (name.isEmpty() || description.isEmpty() || productPriceField.getText().isEmpty() || productQuantityField.getText().isEmpty()) {
                new PopupMessage("Please fill the blank fields", PopupMessage.Type.WARNING);
                return;
            }

            ImageIcon icon;
            double price = 0;
            int quantity = 0;

            if ((logoPanel.getClientProperty("Logo")) != null) {
                icon = (ImageIcon)((JLabel)logoPanel.getClientProperty("Logo")).getIcon();
            } else {
                icon = Images.getJPGImage("MissingImg");
            }

            try {price = Double.parseDouble(productPriceField.getText());} catch (NumberFormatException ignored) {};
            try {quantity = Integer.parseUnsignedInt(productQuantityField.getText());} catch (NumberFormatException ignored) {}

            ProductsService.registerProduct(managerStore.getId(), name, description, price, quantity, icon);
            MyFrame.load(() ->  {
                Timer timer = new Timer(1000, null);
                timer.setRepeats(false);
                timer.start();
                productNameField.setText("");
                descriptionField.setText("");
                productPriceField.setText("");
                productQuantityField.setText("");
                logoPanel.removeAll();
                logoPanel.setBackground(Color.LIGHT_GRAY);
                logoPanel.revalidate();
                logoPanel.repaint();
            });
            new PopupMessage("Added Product Successfully", PopupMessage.Type.SUCCESS);
        }

        private void setupAddingPanelLayout() {
            GroupLayout layout = new GroupLayout(addPanel);
            addPanel.setLayout(layout);

            // Create horizontal group
            layout.setHorizontalGroup(
                    layout.createSequentialGroup()
                            .addContainerGap(40, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(titleLabel)
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(logoPanel, 200, 200, 200)
                                            .addGap(20)
                                            .addGroup(layout.createParallelGroup()
                                                    .addComponent(productNameLabel, 250, 250, 250)
                                                    .addComponent(productNameField, 250, 250, 250)
                                                    .addGroup(layout.createSequentialGroup()
                                                            .addComponent(productPriceLabel, 100, 100, 100)
                                                            .addComponent(productPriceField, 150, 150, 150))
                                                    .addGroup(layout.createSequentialGroup()
                                                            .addComponent(productQuantityLabel, 100, 100, 100)
                                                            .addComponent(productQuantityField, 150, 150, 150))))
                                    .addComponent(descriptionLabel, 470, 470, 470)
                                    .addComponent(descriptionField, 470, 470, 470)
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(addButton, 50, 80, 200)
                                            .addGap(20)
                                            .addComponent(cancelButton, 50, 80, 200)))
                            .addContainerGap(40, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                    layout.createSequentialGroup()
                            .addContainerGap(100, 100)
                            .addGap(20)
                            .addComponent(titleLabel, 40, 40, 40)
                            .addGap(30)
                            .addGroup(layout.createParallelGroup()
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(productNameLabel, 30, 30, 30)
                                            .addGap(5)
                                            .addComponent(productNameField, 30, 30, 30)
                                            .addGap(25)
                                            .addGroup(layout.createParallelGroup()
                                                    .addComponent(productPriceLabel, 30, 30, 30)
                                                    .addComponent(productPriceField, 30, 30, 30))
                                            .addGap(25)
                                            .addGroup(layout.createParallelGroup()
                                                    .addComponent(productQuantityLabel, 30, 30, 30)
                                                    .addComponent(productQuantityField, 30, 30, 30)))
                                    .addComponent(logoPanel, 200, 200, 200))
                            .addGap(25)
                            .addComponent(descriptionLabel, 30, 30, 30)
                            .addGap(5)
                            .addComponent(descriptionField, 120, 120, 120)
                            .addGap(25, 40, 60)
                            .addGroup(layout.createParallelGroup()
                                    .addComponent(addButton, 30, 30, 30)
                                    .addComponent(cancelButton, 30, 30, 30))
                            .addContainerGap(100, 100)

            );
        }

        private void enableDragAndDrop(JPanel panel) {
            panel.setLayout(new BorderLayout());
            new DropTarget(panel, new DropTargetListener() {
                @Override
                public void dragEnter(DropTargetDragEvent dtde) {
                    panel.setBackground(Color.GRAY);
                }

                @Override
                public void dragOver(DropTargetDragEvent dtde) {}

                @Override
                public void dropActionChanged(DropTargetDragEvent dtde) {}

                @Override
                public void dragExit(DropTargetEvent dte) {
                    panel.setBackground(Color.LIGHT_GRAY);
                }

                @Override
                public void drop(DropTargetDropEvent dtde) {
                    try {
                        // Accept drop
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);

                        // Check for dropped files
                        Transferable transferable = dtde.getTransferable();
                        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            @SuppressWarnings("unchecked")
                            List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                            if (!files.isEmpty()) {
                                File logoFile = files.get(0);

                                // Ensure the file is an image
                                if (logoFile.getName().matches(".*\\.(png|jpg|jpeg|gif)")) {
                                    setLogo(logoFile);
                                } else {
                                    JOptionPane.showMessageDialog(panel, "Please drop a valid image file.", "Invalid File", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "Error handling the dropped file.", "Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        panel.setBackground(Color.LIGHT_GRAY);
                    }
                }
            });
        }

        private void setLogo(File logoFile) {
            try {
                // Display the logo in the panel
                ImageIcon logoIcon = Images.scaleImage(new ImageIcon(logoFile.getAbsolutePath()), logoPanel.getWidth(), logoPanel.getHeight());
                JLabel logoLabel = new JLabel(logoIcon);
                logoPanel.removeAll();
                logoPanel.putClientProperty("Logo", logoLabel);
                logoPanel.add(logoLabel);
                logoPanel.revalidate();
                logoPanel.repaint();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Could not load logo image.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

