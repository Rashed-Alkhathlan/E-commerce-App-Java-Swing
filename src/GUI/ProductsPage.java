package GUI;

import Components.TextField;
import Components.Button;
import Components.ScrollPane;
import Objects.*;
import Services.*;
import Utils.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.UUID;

public class ProductsPage extends Page {
    private final JPanel productsButtonsPanel = new JPanel();
    private final ScrollPane productsScrollPane = new ScrollPane(productsButtonsPanel);
    private final JPanel searchPanel = new JPanel();
    private final TextField searchField = new TextField("Search here...");
    private final Button searchButton = new Button(Images.getImage("SearchImg"));

    private final Color buttonColor = Color.WHITE;
    private final Store currentStore;
    private int totalProducts;
    ArrayList<Product> products;

    private final int productButtonWidth = 210;
    private final int productButtonHeight = 280;

    private final int iconWidth = 210;
    private final int iconHeight = 210;

    ProductsPage(UUID storeId) {
        currentStore = storeId != null ? StoresService.getStore(storeId) : null;
        initPage();
    }

    @Override
    protected void initPage() {
        setupBackground();
        setupMenu();
        actionListener();

        setupProductsPanel();
        setupSearchPanel();
    }

    public void actionListener() {
        productsScrollPane.getVerticalScrollBar().addAdjustmentListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                loadVisibleProducts(products);
                return null;
            }
        }.execute());
        productsScrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (!MyFrame.isInAnimation()) {
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            productsButtonsPanel.removeAll();
                            loadVisibleProducts(products);
                            return null;
                        }
                    }.execute();
                }
            }
        });
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().toLowerCase();
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    updateProducts(searchTerm);
                    loadVisibleProducts(products);
                    return null;
                }
            }.execute();
        });
    }

    private void setupProductsPanel() {
        productsButtonsPanel.setLayout(null);
        productsButtonsPanel.setOpaque(false);
        contentPanel.add(productsScrollPane, BorderLayout.CENTER);
        updateProducts("");
    }

    private void setupSearchPanel() {
        searchField.setPreferredSize(new Dimension(300, 30));
        searchButton.setPreferredSize(new Dimension(30, 30));
        searchButton.setArch(0);
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.setOpaque(false);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);
    }

    private void updateProducts(String searchTerm) {
        productsButtonsPanel.removeAll();
        if (currentStore != null) {
            products = ProductsService.getProducts(currentStore.getId(), searchTerm);
        } else {
            products = ProductsService.getProducts(searchTerm);
        }
        products.removeIf(product -> product.getQuantity() <= 0);
        totalProducts = products.size();
    }

    private void loadVisibleProducts(ArrayList<Product> products) {
        int panelWidth = productsScrollPane.getWidth();
        int horizontalGap = 6, verticalGap = 6;

        int productsPerRow = Math.max(1, (panelWidth + horizontalGap) / (productButtonWidth + horizontalGap));
        int rows = (int) Math.ceil((double) totalProducts / productsPerRow);

        int totalHeight = rows * (productButtonHeight + verticalGap) + 50;

        productsButtonsPanel.setPreferredSize(new Dimension(panelWidth, totalHeight));

        // Get the visible area of the JScrollPane
        Rectangle visibleRect = productsScrollPane.getViewport().getViewRect();

        int firstVisibleRow = Math.max(0, visibleRect.y / (productButtonHeight + verticalGap));
        int lastVisibleRow = Math.max(0, (visibleRect.y + visibleRect.height) / (productButtonHeight + verticalGap) + 2);

        if (firstVisibleRow > 0) {firstVisibleRow-= 2;}

        // Calculate product indices for visible rows
        int startIndex = firstVisibleRow * productsPerRow;
        int endIndex = Math.min((lastVisibleRow + 1) * productsPerRow, totalProducts);

        // Clear buttons outside the visible area
        for (Component component : productsButtonsPanel.getComponents()) {
            if (component instanceof JButton button) {
                int buttonIndex = (int) button.getClientProperty("index");
                if (buttonIndex < startIndex || buttonIndex >= endIndex) {
                    productsButtonsPanel.remove(button); // Remove buttons outside the visible area
                }
            }
        }

        // Add buttons in visible area
        for (int i = startIndex; i < endIndex; i++) {
            if (!isButtonVisible(i)) {
                JButton productButton = getProductButton(products.get(i));
                productButton.putClientProperty("index", i);

                int row = i / productsPerRow;
                int col = i % productsPerRow;
                int x = (col * (productButtonWidth + horizontalGap)) + ((panelWidth - (productsPerRow * (productButtonWidth + horizontalGap))) / 2);
                int y = row * (productButtonHeight + verticalGap);

                productButton.setBounds(x, y, productButtonWidth, productButtonHeight);
                productsButtonsPanel.add(productButton);
            }
        }

        productsButtonsPanel.revalidate();
        productsButtonsPanel.repaint();
    }

    private boolean isButtonVisible(int index) {
        for (Component component : productsButtonsPanel.getComponents()) {
            if (component instanceof JButton button) {
                Integer buttonIndex = (Integer) button.getClientProperty("index");
                if (buttonIndex != null && buttonIndex == index) {
                    return true;
                }
            }
        }
        return false;
    }

    //Buttons for Products
    private JButton getProductButton(Product product) {
        JButton productButton = new JButton();
        productButton.setContentAreaFilled(false);
        productButton.setPreferredSize(new Dimension(productButtonWidth, productButtonHeight));
        productButton.setLayout(new BorderLayout());
        productButton.setMargin(new Insets(0, 0, 0, 0));

        JLabel imageLabel;
        if (product.getMainImageIcon() != null) {
            imageLabel = new JLabel(Images.scaleImage(product.getMainImageIcon(), iconWidth, iconHeight));
        } else {
            imageLabel = new JLabel(Images.getImage("MissingImg", iconWidth, iconHeight));
        }
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