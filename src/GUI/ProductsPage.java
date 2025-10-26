package GUI;

import Components.Button;
import Components.ScrollPane;
import Enums.StoreStatus;
import Objects.*;
import Services.*;
import Utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class ProductsPage extends Page {
    private final JPanel productsButtonsPanel = new JPanel();
    private final ScrollPane productsScrollPane = new ScrollPane(productsButtonsPanel);

    private final Color buttonColor = Color.WHITE;
    private final Store currentStore;
    private int totalProducts;
    ArrayList<Product> products;

    private String searchTerm = "";

    private final int productButtonWidth = 260;
    private final int productButtonHeight = 370;

    private final int iconWidth = productButtonWidth;
    private final int iconHeight = productButtonWidth;

    ProductsPage() {
        currentStore = null;
        initPage();
    }

    ProductsPage(String searchTerm) {
        currentStore = null;
        this.searchTerm = searchTerm;
        initPage();
    }

    ProductsPage(UUID storeId) {
        currentStore = StoresService.getStore(storeId);
        initPage();
    }

    ProductsPage(UUID storeId, String searchTerm) {
        currentStore = StoresService.getStore(storeId);
        this.searchTerm = searchTerm;
        initPage();
    }

    protected UUID getCurrentStoreId() {
        return currentStore == null ? null : currentStore.getId();
    }

    @Override
    protected void initPage() {
        setupBackground();
        setupMenu();
        actionListener();

        searchField.setText(searchTerm);

        setupProductsPanel();
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
                            productsButtonsPanel.revalidate();
                            productsButtonsPanel.repaint();
                            return null;
                        }
                    }.execute();
                }
            }
        });
    }

    private void setupProductsPanel() {
        productsButtonsPanel.setLayout(null);
        productsButtonsPanel.setOpaque(false);
        contentPanel.add(productsScrollPane, BorderLayout.CENTER);
        updateProducts(searchTerm);
    }

    private void updateProducts(String searchTerm) {
        productsButtonsPanel.removeAll();
        if (currentStore != null && (currentStore.getStatus() == StoreStatus.OPEN)) {
            products = ProductsService.getProducts(searchTerm.toLowerCase(), currentStore.getId(), StoreStatus.OPEN);
        } else {
            products = ProductsService.getProducts(searchTerm.toLowerCase(), null, StoreStatus.OPEN);
        }
        products.removeIf(product -> product.getQuantity() <= 0);
        Collections.shuffle(products);
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

        if (firstVisibleRow > 1) {firstVisibleRow -= 2;}

        // Calculate product indices for visible rows
        int startIndex = firstVisibleRow * productsPerRow;
        int endIndex = Math.min(lastVisibleRow * productsPerRow, totalProducts);

        // Clear buttons outside the visible area
        for (Component component : productsButtonsPanel.getComponents()) {
            if (component instanceof JButton button) {
                int buttonIndex = (int) button.getClientProperty("index");
                if ((buttonIndex < startIndex || buttonIndex > endIndex) && button.isVisible()) {
                    button.setVisible(false); // Remove buttons outside the visible area
                    //System.out.println(buttonIndex);
                }
            }
        }

        // Add buttons in visible area
        for (int i = startIndex; i < endIndex; i++) {
            if (!isButtonExist(i)) {
                JButton productButton = getProductButton(products.get(i));
                productButton.putClientProperty("index", i);

                int row = i / productsPerRow;
                int col = (i + productsPerRow) % productsPerRow;
                int x = (col * (productButtonWidth + horizontalGap)) + ((panelWidth - (productsPerRow * (productButtonWidth + horizontalGap))) / 2);
                int y = row * (productButtonHeight + verticalGap);

                productButton.setBounds(x, y + 20, productButtonWidth, productButtonHeight);
                productsButtonsPanel.add(productButton);
            }
        }
    }

    private boolean isButtonExist(int index) {
        for (Component component : productsButtonsPanel.getComponents()) {
            if (component instanceof JButton button) {
                int buttonIndex = (int) button.getClientProperty("index");
                if (buttonIndex == index) {
                    button.setVisible(true);
                    return true;
                }
            }
        }
        return false;
    }

    //Buttons for Products
    private JButton getProductButton(Product product) {
        Button productButton = new Button();
        productButton.setBorderPainted(true);
        productButton.setArch(0);
        productButton.setBackground(new Color(240, 240, 240));
        productButton.setBorderColor(new Color(240, 240, 240));
        productButton.setPreferredSize(new Dimension(productButtonWidth, productButtonHeight));
        productButton.setLayout(new BorderLayout());
        productButton.setBorder(new EmptyBorder(1, 1, 1, 1));

        JLabel imageLabel = new JLabel(Images.scaleImage(product.getMainImageIcon(), iconWidth, iconHeight));
        JPanel textPanel = getProductButtonPanel(product);

        productButton.add(imageLabel, BorderLayout.NORTH);
        productButton.add(textPanel, BorderLayout.CENTER);

        productButton.addActionListener(e -> MyFrame.showPage(ProductPage.class, product.getId()));

        return productButton;
    }

    private JPanel getProductButtonPanel(Product product) {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.setBackground(buttonColor);
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 3, 3));

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.PLAIN, 20));

        JLabel descriptionLabel = new JLabel("<html><i>" + product.getDescription() + "</i></html>");
        descriptionLabel.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.PLAIN, 15));

        JLabel priceLabel = new JLabel("$" + product.getPrice());
        priceLabel.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.BOLD, 23));
        priceLabel.setForeground(new Color(5,5, 5));

        textPanel.add(nameLabel, BorderLayout.NORTH);
        textPanel.add(descriptionLabel, BorderLayout.CENTER);
        textPanel.add(priceLabel, BorderLayout.SOUTH);
        return textPanel;
    }

}