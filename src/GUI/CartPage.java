package GUI;

import Components.Button;
import Components.Panel;
import Components.ScrollPane;
import Objects.*;
import Services.CartService;
import Utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;


public class CartPage extends Page {

    private final Panel cartButtonsPanel = new Panel();
    private final Panel productsPanel = new Panel();

    private final JPanel productsButtonsPanel = new JPanel();
    private final ScrollPane productsScrollPane = new ScrollPane(productsButtonsPanel);
    private final Button checkoutButton = new Button("Checkout");
    private final Button BackButton = new Button("Back");

    private final JLabel subTotalLabel = new JLabel("SubTotal:");
    private final JLabel shippingLabel = new JLabel("Shipping and handling:");
    private final JLabel appliedDiscountLabel = new JLabel("Discount:");
    private final JLabel totalLabel = new JLabel("Total:");

    private final JLabel discountLabel = new JLabel("Apply a Discount:");
    private final TextField discountField = new TextField("Enter a discount");

    private final Color buttonColor = Color.WHITE;
    private int totalCart;
    private ArrayList<HashMap.Entry<Product, Integer>> cartProducts;

    private final int iconWidth = 200;
    private final int iconHeight = 200;

    private final int productButtonHeight = 200;


    CartPage() {
        initPage();
    }

    @Override
    protected void initPage() {
        setupBackground();
        setupMenu();
        actionListener();

        setupProductsPanel();
        setupLayout();
    }

    public void actionListener() {
        productsScrollPane.getVerticalScrollBar().addAdjustmentListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                loadVisibleProducts();
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
                            loadVisibleProducts();
                            return null;
                        }
                    }.execute();
                }
            }
        });
    }

    private void setupLayout() {
        cartButtonsPanel.setBackground(Color.WHITE);
        cartButtonsPanel.setArch(20);
        cartButtonsPanel.setPreferredSize(new Dimension(350, 0));

        contentPanel.setLayout(new BorderLayout(40, 0));
        contentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        contentPanel.add(cartButtonsPanel, BorderLayout.EAST);
        contentPanel.add(productsPanel, BorderLayout.CENTER);

        GroupLayout layout = new GroupLayout(cartButtonsPanel);
        cartButtonsPanel.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(50, 100)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(subTotalLabel)
                                .addComponent(shippingLabel)
                                .addComponent(appliedDiscountLabel)
                                .addComponent(discountLabel)
                                .addComponent(discountField)
                                .addComponent(totalLabel))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(subTotalLabel)
                        .addComponent(shippingLabel)
                        .addComponent(appliedDiscountLabel)
                        .addComponent(discountLabel)
                        .addComponent(discountField)
                        .addComponent(totalLabel)
        );
    }

    private void setupProductsPanel() {
        productsButtonsPanel.setLayout(null);
        productsButtonsPanel.setOpaque(false);
        productsPanel.setBackground(Color.WHITE);
        productsPanel.setArch(20);
        productsPanel.setLayout(new BorderLayout());
        productsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        productsPanel.add(productsScrollPane, BorderLayout.CENTER);
        updateProductsPanel();
    }

    private void updateProductsPanel() {
        productsButtonsPanel.removeAll();
        cartProducts = Main.getCurrentUser().getCart().getProducts();
        totalCart = cartProducts.size();
    }

    private void loadVisibleProducts() {

        int panelWidth = productsScrollPane.getWidth();
        int horizontalGap = 6, verticalGap = 6;

        int productsPerRow = 1;
        int rows = totalCart;

        int totalHeight = rows * (productButtonHeight + verticalGap) + 50;

        productsButtonsPanel.setPreferredSize(new Dimension(panelWidth, totalHeight));

        // Get the visible area of the JScrollPane
        Rectangle visibleRect = productsScrollPane.getViewport().getViewRect();

        int firstVisibleRow = Math.max(0, visibleRect.y / (productButtonHeight + verticalGap));
        int lastVisibleRow = Math.max(0, ((visibleRect.y + visibleRect.height) / (productButtonHeight + verticalGap)) + 2);

        if (firstVisibleRow > 0) {firstVisibleRow-= 2;}

        // Calculate product indices for visible rows
        int startIndex = firstVisibleRow * productsPerRow;
        int endIndex = Math.min(lastVisibleRow * productsPerRow, totalCart);

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
                HashMap.Entry<Product, Integer> entry = cartProducts.get(i);
                Product product = entry.getKey();
                int quantity = entry.getValue();
                JButton productButton = getProductButton(product, quantity);
                productButton.putClientProperty("index", i);

                int row = i / productsPerRow;
                int x = (panelWidth - (productsPerRow * (panelWidth + horizontalGap))) / 2;
                int y = row * (productButtonHeight + verticalGap);

                productButton.setBounds(x, y, panelWidth, productButtonHeight);
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

    //Buttons for Stores
    private JButton getProductButton(Product product, int quantity) {
        Button productButton = new Button();

        productButton.setArch(0);
        productButton.setPreferredSize(new Dimension(productsScrollPane.getWidth(), productButtonHeight));
        productButton.setBorder(new EmptyBorder(1, 1, 1, 1));
        productButton.setLayout(new BorderLayout());

        JLabel imageLabel = new JLabel(Images.scaleImage(product.getMainImageIcon(), iconWidth, iconHeight));
        JPanel textQuantityPanel = new JPanel();
        JPanel textPanel = getProductButtonPanel(product);
        JPanel quantityPanel = getProductQuantityPanel(product, quantity);
        textQuantityPanel.setLayout(new BorderLayout());
        textQuantityPanel.setOpaque(false);
        textQuantityPanel.add(textPanel, BorderLayout.CENTER);
        textQuantityPanel.add(quantityPanel, BorderLayout.SOUTH);

        productButton.add(imageLabel, BorderLayout.WEST);
        productButton.add(textQuantityPanel, BorderLayout.CENTER);

        return productButton;
    }

    private JPanel getProductButtonPanel(Product product) {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.setBackground(buttonColor);
        textPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel descriptionLabel = new JLabel(product.getDescription());
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JLabel priceLabel = new JLabel("$" + product.getPrice());
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        priceLabel.setForeground(Color.GREEN);

        textPanel.add(nameLabel, BorderLayout.NORTH);
        textPanel.add(descriptionLabel, BorderLayout.CENTER);
        textPanel.add(priceLabel, BorderLayout.SOUTH);
        return textPanel;
    }

    private JPanel getProductQuantityPanel(Product product, int quantity) {
        JPanel panel = new JPanel();
        Button increaseButton = new Button("+");
        Button decreaseButton = new Button("-");
        JLabel amountLabel = new JLabel(String.valueOf(quantity), SwingConstants.CENTER);

        panel.setBackground(buttonColor);
        increaseButton.setFont(new Font("Arial", Font.PLAIN, 20));
        increaseButton.setHorizontalTextPosition(SwingConstants.CENTER);
        decreaseButton.setFont(new Font("Arial", Font.PLAIN, 26));
        decreaseButton.setHorizontalTextPosition(SwingConstants.CENTER);
        decreaseButton.setBorder(new EmptyBorder(2, 5, 5, 6));

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setHorizontalGroup(
               layout.createSequentialGroup()
                        .addContainerGap(5, Short.MAX_VALUE)
                        .addComponent(increaseButton, 30, 30, 30)
                        .addComponent(amountLabel, 30, 30, 30)
                        .addComponent(decreaseButton, 30, 30, 30)
                       .addGap(5)
        );

        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addComponent(increaseButton, 30, 30, 30)
                        .addComponent(amountLabel, 30, 30, 30)
                        .addComponent(decreaseButton, 30, 30, 30)
        );

        increaseButton.addActionListener(e -> {
            CartService.addToCart(product, 1);
            amountLabel.setText(String.valueOf((Integer.parseUnsignedInt(amountLabel.getText()) + 1)));
        });
        decreaseButton.addActionListener(e -> {
            CartService.removeFromCart(product, 1);
            amountLabel.setText(String.valueOf((Integer.parseUnsignedInt(amountLabel.getText()) - 1)));
            if (Integer.parseUnsignedInt(amountLabel.getText()) <= 0) {
                MyFrame.reloadPage();
            }
        });

        return panel;
    }

}