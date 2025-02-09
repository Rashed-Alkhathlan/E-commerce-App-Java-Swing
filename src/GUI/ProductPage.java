package GUI;

import Components.Button;
import Components.Panel;
import Components.PopupMessage;
import Objects.Product;
import Services.CartService;
import Services.ProductsService;
import Utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.UUID;

public class ProductPage extends Page {

    private final Panel sidePanel = new Panel();
    private final Panel productPanel = new Panel();

    private final Button buyButton = new Button("Buy Now");
    private final Button addToCartButton = new Button("Add To Cart");

    JSeparator divider1 = new JSeparator(SwingConstants.HORIZONTAL);
    JSeparator divider2 = new JSeparator(SwingConstants.HORIZONTAL);
    JSeparator divider3 = new JSeparator(SwingConstants.HORIZONTAL);

    private final JLabel titleLabel = new JLabel();
    private final JLabel priceLabel1 = new JLabel();
    private final JLabel description = new JLabel();

    private final JPanel mainImgPanel = new JPanel();

    private final JLabel priceLabel2 = new JLabel();
    private final JLabel deliveryLabel = new JLabel("FREE Delivery February 4 - 5");
    private final JLabel stockLabel = new JLabel("In Stock");
    private final JLabel soldLabel = new JLabel();
    private final JLabel shippingLabel = new JLabel();

    private final Panel quantityPanel = new Panel();
    JLabel amountLabel = new JLabel("1", SwingConstants.CENTER);

    private final Color buttonColor = Color.WHITE;
    private final Product currentProduct;

    private final int iconWidth = 300;
    private final int iconHeight = 300;

    private final int productButtonHeight = 200;

    public ProductPage(UUID productId) {
        if (productId == null) {
            currentProduct = null;
            MyFrame.showPage("PreviousPage");
        } else {
            currentProduct = ProductsService.getProduct(productId);
            initPage();
        }
    }

    @Override
    protected void initPage() {
        setupBackground();
        setupMenu();
        actionListener();

        setupProductPanel();
        setupSidePanel();
        setupLayout();
    }

    public void actionListener() {
        buyButton.addActionListener(e -> MyFrame.showPage("PreviousPage"));
        addToCartButton.addActionListener(e -> {
            CartService.addToCart(currentProduct, Integer.parseUnsignedInt(amountLabel.getText()));
            new PopupMessage("Added To Cart Successfully", PopupMessage.Type.SUCCESS);
        });
    }

    private void setupLayout() {
        contentPanel.setLayout(new BorderLayout(40, 0));
        contentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        contentPanel.add(sidePanel, BorderLayout.EAST);
        contentPanel.add(productPanel, BorderLayout.CENTER);
    }

    private void setupProductPanel() {
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        productPanel.setArch(20);

        titleLabel.setText(currentProduct.getName());
        priceLabel1.setText("$" + currentProduct.getPrice());
        description.setText("<html>" + currentProduct.getDescription() + "</html>");

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        priceLabel1.setFont(new Font("SansSerif", Font.BOLD, 22));
        description.setFont(new Font("SansSerif", Font.PLAIN, 16));

        mainImgPanel.setLayout(new GridLayout());
        mainImgPanel.setBorder(new LineBorder(new Color(175, 175, 175), 1));
        mainImgPanel.add(new JLabel(Images.scaleImage(currentProduct.getMainImageIcon(), iconWidth - 1, iconHeight - 1)));

        setupProductLayout();
    }

    private void setupProductLayout(){
        GroupLayout layout = new GroupLayout(productPanel);
        productPanel.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(50, 50)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(mainImgPanel, 300, 300, 300))
                        .addGap(40)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(titleLabel)
                                .addComponent(divider1)
                                .addComponent(priceLabel1)
                                .addComponent(description)
                                .addComponent(divider2))
                        .addContainerGap(50, 50)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(50, 50)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(mainImgPanel, 300, 300, 300)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(titleLabel)
                                        .addGap(15)
                                        .addComponent(divider1)
                                        .addGap(15)
                                        .addComponent(priceLabel1)
                                        .addGap(15)
                                        .addComponent(description)
                                        .addGap(15)
                                        .addComponent(divider2)))
                        .addContainerGap(50, 50)
        );
    }

    private void setupSidePanel() {
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setArch(20);
        sidePanel.setPreferredSize(new Dimension(280, 0));

        priceLabel2.setText("$" + currentProduct.getPrice());
        soldLabel.setText("Sold By     ");
        shippingLabel.setText("Shipped By  ");

        priceLabel2.setFont(new Font("SansSerif", Font.BOLD, 28));
        stockLabel.setFont(new Font("Verdana", Font.BOLD, 23));
        stockLabel.setForeground(new Color(0, 150, 50));

        buyButton.setBackground(Color.ORANGE);
        buyButton.setOpaque(true);
        buyButton.setBorderPainted(false);
        buyButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        addToCartButton.setBackground(new Color(220, 150, 0));
        addToCartButton.setOpaque(true);
        addToCartButton.setBorderPainted(false);
        addToCartButton.setFont(new Font("SansSerif", Font.PLAIN, 13));

        setupQuantityPanel();
        setupSideLayout();
    }

    private void setupQuantityPanel() {
        quantityPanel.setBorderPainted(true);
        quantityPanel.setArch(10);
        quantityPanel.setBackground(new Color(245, 245, 245));

        amountLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        Button increaseButton = new Button("+");
        Button decreaseButton = new Button("-");

        increaseButton.setOpaque(false);
        increaseButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
        increaseButton.setHorizontalTextPosition(SwingConstants.CENTER);
        decreaseButton.setOpaque(false);
        decreaseButton.setFont(new Font("SansSerif", Font.PLAIN, 26));
        decreaseButton.setHorizontalTextPosition(SwingConstants.CENTER);
        decreaseButton.setBorder(new EmptyBorder(0, 5, 5, 6));

        GroupLayout layout = new GroupLayout(quantityPanel);
        quantityPanel.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(5, 5)
                        .addComponent(increaseButton, 30, 30, 30)
                        .addComponent(amountLabel, 30, 100, 200)
                        .addComponent(decreaseButton, 30, 30, 30)
                        .addContainerGap(5, 5)
        );

        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addComponent(increaseButton, 30, 30, 30)
                        .addComponent(amountLabel, 30, 30, 30)
                        .addComponent(decreaseButton, 30, 30, 30)
        );

        increaseButton.addActionListener(e -> {
            amountLabel.setText("" + (Integer.parseUnsignedInt(amountLabel.getText()) + 1));
        });
        decreaseButton.addActionListener(e -> {
            if (((Integer.parseUnsignedInt(amountLabel.getText()) - 1) != 0)) {
                amountLabel.setText("" + (Integer.parseUnsignedInt(amountLabel.getText()) - 1));
            }
        });
    }

    private void setupSideLayout() {
        GroupLayout layout = new GroupLayout(sidePanel);
        sidePanel.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(50, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(priceLabel2, 200, 200, 200)
                                        .addComponent(divider3, 200, 200, 200)
                                        .addComponent(deliveryLabel, 200, 200, 200)
                                        .addComponent(stockLabel, 200, 200, 200)
                                        .addComponent(soldLabel, 200, 200, 200)
                                        .addComponent(shippingLabel, 200, 200, 200)
                                        .addComponent(quantityPanel, 200, 200, 200))
                                .addComponent(buyButton, 200, 200, 200)
                                .addComponent(addToCartButton, 200, 200 ,200))
                        .addContainerGap(50, Short.MAX_VALUE)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(50, 50)
                        .addComponent(priceLabel2, 30, 30, 30)
                        .addGap(15)
                        .addComponent(divider3, 30, 30, 30)
                        .addGap(5)
                        .addComponent(deliveryLabel, 30, 30, 30)
                        .addGap(45, 60, 100)
                        .addComponent(stockLabel, 30, 30, 30)
                        .addGap(45, 60, 100)
                        .addComponent(soldLabel, 30, 30, 30)
                        .addGap(10)
                        .addComponent(shippingLabel, 30, 30, 30)
                        .addGap(25)
                        .addComponent(quantityPanel, 30, 30, 30)
                        .addGap(15)
                        .addComponent(buyButton, 30, 30, 30)
                        .addGap(10)
                        .addComponent(addToCartButton, 30, 30 ,30)
                        .addContainerGap(50, Short.MAX_VALUE)
        );
    }
}
