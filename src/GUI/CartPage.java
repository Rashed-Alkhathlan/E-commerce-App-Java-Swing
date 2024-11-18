package GUI;

import Objects.Product;
import Utils.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class CartPage extends Page {
    private final JButton checkoutButton = new JButton("Checkout");
    private final JButton backButton = new JButton("Back");
    private final JPanel productsButtonsPanel = new JPanel();
    private final JScrollPane productsScrollPane = new JScrollPane(productsButtonsPanel);

    private final Color buttonColor = Color.DARK_GRAY;

    CartPage() {
        initPage();
    }

    @Override
    protected void initPage() {
        actionListener();
        defaultBackground();

        setButton(checkoutButton, sidePanel, (sidePanelWidth / 2) - 100, sidePanelHeight - 250, 200, 25);
        setButton(backButton, sidePanel,(sidePanelWidth / 2) - 100, sidePanelHeight - 200, 200, 25);

        productsButtonsPanel.setBackground(Color.GRAY);
        productsButtonsPanel.setOpaque(false);

        productsScrollPane.setBounds(0, 40, frameWidth - sidePanelWidth, frameHeight - 40);
        productsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        productsScrollPane.setOpaque(false);
        productsScrollPane.getViewport().setOpaque(false);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateCartProductsPanel();
            }
        });

        centerPanel.add(productsScrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void actionListener() {
        switchToPageWhenPressed(checkoutButton, "CheckoutPage");
        switchToPageWhenPressed(backButton, "PreviousPage");
    }

    private void updateCartProductsPanel() {
        productsButtonsPanel.removeAll();
        int productPanelHeight = 20;

//        for (Product product : CartService.getCart()) {
//            productPanelHeight += 205;
//            JButton productButton = getProductButton(product);
//            productsButtonsPanel.add(productButton);
//        }

        productsButtonsPanel.setPreferredSize(new Dimension(0, productPanelHeight));
        productsButtonsPanel.revalidate();
        productsButtonsPanel.repaint();
    }

    private JButton getProductButton(Product product) {
        JButton productButton = new JButton();
        productButton.setPreferredSize(new Dimension(630, 200));
        productButton.setLayout(new BorderLayout());
        productButton.setMargin(new Insets(0, 0, 0, 0));

        productButton.addActionListener(e -> System.out.println("Product selected: " + product.getName()));

        JLabel imageLabel = new JLabel(Images.getImage("download", 200, 200));
        productButton.add(imageLabel, BorderLayout.WEST);

        JPanel textPanel = getProductButtonPanel(product);

        productButton.add(textPanel, BorderLayout.CENTER);

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
