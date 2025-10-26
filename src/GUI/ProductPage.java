package GUI;

import Components.Button;
import Components.Panel;
import Components.PopupMessage;
import Components.ScrollPane;
import Components.Label;
import Enums.StoreStatus;
import Objects.Product;
import Objects.Store;
import Services.CartService;
import Services.ProductsService;
import Services.StoresService;
import Utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.UUID;

public class ProductPage extends Page {

    private final JPanel infoPanel = new JPanel();
    private final JPanel extraPanel = new JPanel();

    private final JPanel scrollPanel = new JPanel();
    private final ScrollPane scrollPane = new ScrollPane(scrollPanel);

    private final Panel sidePanel = new Panel();
    private final Panel productPanel = new Panel();

    private final Panel reviewsPanel = new Panel();

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

    private final Label soldLabel2 = new Label();
    private final Label shippingLabel2 = new Label();

    private final Panel quantityPanel = new Panel();
    JLabel amountLabel = new JLabel("1", SwingConstants.CENTER);

    private final Color buttonColor = Color.WHITE;
    private final Product currentProduct;
    private final Store productStore;

    private final int iconWidth = 300;
    private final int iconHeight = 300;

    public ProductPage(UUID productId) {
        if (productId == null) {
            currentProduct = null;
            productStore = null;
            MyFrame.goBack();
        } else {
            currentProduct = ProductsService.getProduct(productId);
            productStore = StoresService.getStore(currentProduct.getStoreId());
            if (productStore.getStatus() == StoreStatus.CLOSED) {
                MyFrame.goBack();
            } else {
                initPage();
            }
        }
    }

    @Override
    protected void initPage() {
        setupBackground();
        setupMenu();
        actionListener();

        setupProductPanel();
        setupSidePanel();
        setupReviewsPanel();
        setupLayout();
    }

    public void actionListener() {
        buyButton.addActionListener(e -> MyFrame.goBack());
        addToCartButton.addActionListener(e -> {
            int response = CartService.addToCart(currentProduct, Integer.parseUnsignedInt(amountLabel.getText()));
            if (response == 0) {
                new PopupMessage("Added To Cart Successfully", PopupMessage.Type.SUCCESS);
            } else if (response == 1) {
                new PopupMessage("Not enough stock MAX: " + currentProduct.getQuantity(), PopupMessage.Type.ERROR);
            }
        });
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            scrollPanel.revalidate();
            scrollPanel.repaint();
        });
    }

    private void setupLayout() {
        infoPanel.setLayout(new BorderLayout(40, 0));
        infoPanel.setBorder(new EmptyBorder(40, 40, 20, 40));
        infoPanel.add(sidePanel, BorderLayout.EAST);
        infoPanel.add(productPanel, BorderLayout.CENTER);

        extraPanel.setLayout(new BorderLayout(40, 0));
        extraPanel.setBorder(new EmptyBorder(40, 40, 20, 40));
        extraPanel.add(reviewsPanel);

        infoPanel.setPreferredSize(new Dimension(0 , 700));
        extraPanel.setPreferredSize(new Dimension(0 , 500));

        scrollPanel.setLayout(new BorderLayout());
        scrollPanel.add(infoPanel, BorderLayout.CENTER);
        scrollPanel.add(extraPanel, BorderLayout.SOUTH);

        contentPanel.add(scrollPane);
    }

    private void setupReviewsPanel() {
        reviewsPanel.setBackground(Color.WHITE);
        reviewsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        reviewsPanel.setArch(20);
    }

    private void setupProductPanel() {
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        productPanel.setArch(20);

        titleLabel.setText(currentProduct.getName());
        priceLabel1.setText("$" + currentProduct.getPrice());
        description.setText("<html>" + currentProduct.getDescription() + "</html>");

        titleLabel.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.BOLD, 24));
        priceLabel1.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.BOLD, 22));
        description.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.PLAIN, 16));

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
        sidePanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        priceLabel2.setText("$" + currentProduct.getPrice());
        soldLabel.setText("Sold By");
        shippingLabel.setText("Shipped By");
        soldLabel2.setText(productStore.getName());
        shippingLabel2.setText(productStore.getName());
        soldLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
        shippingLabel2.setHorizontalAlignment(SwingConstants.RIGHT);

        soldLabel2.addClickableAction(() -> MyFrame.showPage(ProductsPage.class, productStore.getId()));
        shippingLabel2.addClickableAction(() -> MyFrame.showPage(ProductsPage.class, productStore.getId()));

        priceLabel2.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.BOLD, 28));
        stockLabel.setFont(new Font("Verdana", Font.BOLD, 23));
        stockLabel.setForeground(new Color(0, 150, 50));

        buyButton.setBackground(Color.ORANGE);
        buyButton.setOpaque(true);
        buyButton.setBorderPainted(false);
        buyButton.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.PLAIN, 13));
        addToCartButton.setBackground(new Color(220, 150, 0));
        addToCartButton.setOpaque(true);
        addToCartButton.setBorderPainted(false);
        addToCartButton.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.PLAIN, 13));

        setupQuantityPanel();
        setupSideLayout();
    }

    private void setupQuantityPanel() {
        quantityPanel.setBorderPainted(true);
        quantityPanel.setArch(10);
        quantityPanel.setBackground(new Color(245, 245, 245));

        amountLabel.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.PLAIN, 14));
        amountLabel.setBorder(new EmptyBorder(4, 0, 0, 0));

        Button increaseButton = new Button("+");
        Button decreaseButton = new Button("-");

        increaseButton.setOpaque(false);
        increaseButton.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.PLAIN, 20));
        increaseButton.setHorizontalTextPosition(SwingConstants.CENTER);
        increaseButton.setBorder(new EmptyBorder(4, 0, 7, 0));
        decreaseButton.setOpaque(false);
        decreaseButton.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.PLAIN, 26));
        decreaseButton.setHorizontalTextPosition(SwingConstants.CENTER);
        decreaseButton.setBorder(new EmptyBorder(4, 5, 9, 5));

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
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(priceLabel2, 200, 200, 200)
                                        .addComponent(divider3, 200, 200, 200)
                                        .addComponent(deliveryLabel, 200, 200, 200)
                                        .addComponent(stockLabel, 200, 200, 200)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(soldLabel, 80, 80, 80)
                                                .addGap(0, Short.MAX_VALUE, Short.MAX_VALUE)
                                                .addComponent(soldLabel2, 100, 100, 100))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(shippingLabel, 80, 80, 80)
                                                .addGap(0, Short.MAX_VALUE, Short.MAX_VALUE)
                                                .addComponent(shippingLabel2, 100, 100, 100))
                                        .addComponent(quantityPanel, 200, 200, 200))
                                .addComponent(buyButton, 200, 200, 200)
                                .addComponent(addToCartButton, 200, 200 ,200))
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
                        .addGroup(layout.createParallelGroup()
                                .addComponent(soldLabel, 30, 30, 30)
                                .addComponent(soldLabel2, 30, 30, 30))
                        .addGap(10)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(shippingLabel, 30, 30, 30)
                                .addComponent(shippingLabel2, 30, 30, 30))
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
