package GUI;

import Objects.Product;
import Objects.Store;
import Services.CartService;
import Services.ProductsService;
import Services.StoresService;
import Utils.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StoresPage extends Page {
    private final JPanel storesButtonsPanel = new JPanel();
    private final JPanel productsButtonsPanel = new JPanel();
    private final JScrollPane productsScrollPane = new JScrollPane(productsButtonsPanel);
    private final JScrollPane storesScrollPane = new JScrollPane(storesButtonsPanel);
    private final JPanel searchPanel = new JPanel();
    private final JTextField searchField = new JTextField(20);
    private final JButton searchButton = new JButton(Images.getImage("SearchImg"));
    private final JButton backButton =new JButton("Back");

    private final Color buttonColor = Color.WHITE;
    private Store currentStore = null;
    private int totalProducts = 0;

    StoresPage() {
        initPage();
    }

    @Override
    protected void initPage() {
        defaultBackground();
        actionListener();

        sidePanel.setLayout(new BorderLayout(0, 0));
        sidePanel.add(backButton, BorderLayout.NORTH);

        setupStoresPanel();
        setupProductsPanel();
        setupSearchBarPanel();
    }

    @Override
    public void actionListener() {
        switchToPageWhenPressed(backButton, "PreviousPage");
        productsButtonsPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateProductsPanelHeight(totalProducts);
            }
        });
    }

    private void setupStoresPanel() {
        storesButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        storesButtonsPanel.setOpaque(false);
        storesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        storesScrollPane.setOpaque(false);
        storesScrollPane.getViewport().setOpaque(false);
        sidePanel.add(storesScrollPane, BorderLayout.CENTER);
        updateStoresPanel("");
    }

    private void setupProductsPanel() {
        productsButtonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 6));
        productsButtonsPanel.setOpaque(false);
        productsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        productsScrollPane.setOpaque(false);
        productsScrollPane.getViewport().setOpaque(false);
        centerPanel.add(productsScrollPane, BorderLayout.CENTER);
    }

    private void setupSearchBarPanel() {
        searchField.setText("Search for stores...");
        searchField.setPreferredSize(new Dimension(600, 30));
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search for stores...")) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search for stores...");
                }
            }
        });
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().toLowerCase();
            if (searchTerm.equals("search for stores...")) {
                searchTerm = "";
            }
            updateStoresPanel(searchTerm);
        });
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.setOpaque(false);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);
    }

    private void updateStoresPanel(String searchTerm) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            int storesPanelHeight = 65;

            @Override
            protected Void doInBackground() {
                storesButtonsPanel.removeAll();
                storesButtonsPanel.setPreferredSize(new Dimension(storesScrollPane.getWidth(), storesPanelHeight));

                for (Store store : StoresService.getStores()) {
                    if (store.getName().toLowerCase().contains(searchTerm)) {
                        storesPanelHeight += 65;
                        JButton storeButton = getStoreButton(store);
                        storesButtonsPanel.add(storeButton);
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                storesButtonsPanel.setPreferredSize(new Dimension(storesScrollPane.getWidth(), storesPanelHeight));
                storesButtonsPanel.revalidate();
                storesButtonsPanel.repaint();
            }
        };
        worker.execute();
    }

    //Buttons for Stores:
    private JButton getStoreButton(Store store) {
        JButton storeButton = new JButton();
        storeButton.setPreferredSize(new Dimension(250, 60));
        storeButton.setLayout(new BorderLayout());
        storeButton.setMargin(new Insets(0, 0, 0, 0));

        JLabel imageLabel = new JLabel();
        JPanel textPanel = getStoreButtonPanel(store);

        storeButton.add(imageLabel, BorderLayout.WEST);
        storeButton.add(textPanel, BorderLayout.CENTER);

        storeButton.addActionListener(e -> {if (store != currentStore) {updateProductsPanel(store); currentStore = store;}});

        return storeButton;
    }

    private JPanel getStoreButtonPanel(Store store) {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.setBackground(buttonColor);
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel(store.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel descriptionLabel = new JLabel("<html><i>" + store.getDescription() + "</i></html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        textPanel.add(nameLabel, BorderLayout.NORTH);
        textPanel.add(descriptionLabel, BorderLayout.CENTER);
        return textPanel;
    }


    //Panel that displays products when clicking on a store:
    private void updateProductsPanel(Store store) {
        totalProducts = 0;
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                productsButtonsPanel.removeAll();
                productsButtonsPanel.setPreferredSize(new Dimension(productsScrollPane.getWidth(), 0));

                for (Product product : ProductsService.getProducts(store.getId())) {
                    JButton productButton = getProductButton(product);
                    productsButtonsPanel.add(productButton);
                    totalProducts++;
                }
                return null;
            }

            @Override
            protected void done() {
                updateProductsPanelHeight(totalProducts);
            }
        };
        worker.execute();
    }

    private void updateProductsPanelHeight(int totalProducts) {
        int productWidth = 165;  // Width of a product button
        int productHeight = 230; // Height of a product button
        int horizontalGap = 6;   // Horizontal gap between buttons
        int verticalGap = 6;     // Vertical gap between rows

        // Get the current width of the products panel
        int panelWidth = productsScrollPane.getWidth();

        // Calculate the number of products per row
        int productsPerRow = Math.max(1, (panelWidth + horizontalGap) / (productWidth + horizontalGap));

        // Calculate the number of rows required
        int rows = (int) Math.ceil((double) totalProducts / productsPerRow);

        // Calculate the total height needed for the panel
        int totalHeight = rows * (productHeight + verticalGap);

        // Set the preferred size of the productsButtonsPanel
        productsButtonsPanel.setPreferredSize(new Dimension(0, totalHeight + 50));
        productsButtonsPanel.revalidate();
        productsButtonsPanel.repaint();
    }


    //Buttons for Products
    private JButton getProductButton(Product product) {
        JButton productButton = new JButton();
        productButton.setPreferredSize(new Dimension(165, 230));
        productButton.setLayout(new BorderLayout());
        productButton.setMargin(new Insets(0, 0, 0, 0));

        JLabel imageLabel = new JLabel(Images.scaleImage(product.getMainImageIcon(), 165, 165));
        JPanel textPanel = getProductButtonPanel(product);

        productButton.add(imageLabel, BorderLayout.NORTH);
        productButton.add(textPanel, BorderLayout.CENTER);

        productButton.addActionListener(e -> CartService.addToCart(product, 1));
        return productButton;
    }

    private JPanel getProductButtonPanel(Product product) {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.setBackground(buttonColor);
        textPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 3));

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel descriptionLabel = new JLabel("<html><i>" + product.getDescription() + "</i></html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel priceLabel = new JLabel("$" + product.getPrice());
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        priceLabel.setForeground(Color.GREEN);

        textPanel.add(nameLabel, BorderLayout.NORTH);
        textPanel.add(descriptionLabel, BorderLayout.CENTER);
        textPanel.add(priceLabel, BorderLayout.SOUTH);
        return textPanel;
    }

}