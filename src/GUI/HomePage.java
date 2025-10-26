package GUI;

import Components.Button;
import Components.CardPanel;
import Enums.StoreStatus;
import Enums.UserRole;
import Objects.Main;
import Services.ProductsService;
import Services.StoresService;
import Utils.Images;

import javax.swing.*;
import java.awt.*;

public class HomePage extends Page {
    private final Button allProductsButton = new Button();
    private final Button allStoresButton = new Button();
    private final Button accountButton = new Button();

    HomePage() {
        initPage();
    }

    @Override
    protected void initPage() {
        setupBackground();
        setupMenu();
        setupLayout();
        actionListener();

        CardPanel panel = new CardPanel(Images.getImage("StoresImg", 30, 30), "Browse Stores", "Over "+ StoresService.getStoreCount(StoreStatus.OPEN) +"+ Stores available...", "<html><div style='text-align: center;'>"+"Discover All Types of Stores, and Categories"+"</div></html>");
        allStoresButton.setPreferredSize(new Dimension(300, 200));
        allStoresButton.setMargin(new Insets(0, 0, 0, 0));
        allStoresButton.add(panel);

        CardPanel panel1 = new CardPanel(Images.getImage("ProductsImg", 30, 30), "Browse Products", "Over " + ProductsService.getProductsCount(StoreStatus.OPEN) + "+ Products across all Stores...", "<html><div style='text-align: center;'>"+"Discover Products of All Types, and Categories"+"</div></html>");
        allProductsButton.setPreferredSize(new Dimension(300, 200));
        allProductsButton.setMargin(new Insets(0, 0, 0, 0));
        allProductsButton.add(panel1);

        String value = Main.isSignedIn() ? Main.getCurrentUser().getRole() == UserRole.MANAGER ? "Manage your Store and products" : Main.getCurrentUser().getRole() == UserRole.ADMIN ? "Manage and look over everything in the app" : "Manage your account" : "Login to be able to Checkout";
        CardPanel panel2 = new CardPanel(Images.getImage("ManImg", 30, 30), "Account", value, "");
        accountButton.setPreferredSize(new Dimension(300, 200));
        accountButton.setMargin(new Insets(0, 0, 0, 0));
        accountButton.add(panel2);
    }

    private void setupLayout() {
        GroupLayout layout = new GroupLayout(contentPanel);
        contentPanel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Create horizontal group
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(100, Short.MAX_VALUE)
                        .addComponent(allStoresButton, 300, 300, 500)
                        .addGap(20)
                        .addComponent(allProductsButton, 300, 300, 500)
                        .addGap(20)
                        .addComponent(accountButton, 300, 300, 500)
                        .addContainerGap(100, Short.MAX_VALUE)
        );

        // Create vertical group
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addContainerGap(100, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                    .addComponent(allStoresButton, 150, 150, 300)
                                    .addComponent(allProductsButton, 150, 150, 300)
                                    .addComponent(accountButton, 150, 150, 300))
                        .addContainerGap(200, Short.MAX_VALUE)
        );
    }

    protected void actionListener() {
        allStoresButton.addActionListener(e -> MyFrame.showPage(StoresPage.class));
        allProductsButton.addActionListener(e -> MyFrame.showPage(ProductsPage.class));
        accountButton.addActionListener(e -> MyFrame.showPage(AccountPage.class));
    }
}
