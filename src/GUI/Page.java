package GUI;

import Components.*;
import Components.Button;
import Components.Menu;
import Components.TextField;
import Utils.Images;

import javax.swing.*;
import java.awt.*;

public abstract class Page extends JPanel {

    protected abstract void initPage();

    protected JPanel backgroundPanel;
    protected JPanel contentPanel;

    protected Header headerPanel;
    protected int frameWidth = MyFrame.getWidth();
    protected int frameHeight = MyFrame.getHeight();

    protected Page() {
        setLayout(new BorderLayout(0, 0));
        setBounds(0, 0, frameWidth, frameHeight);
    }

    protected void setupBackground() {
        backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new BorderLayout());

        headerPanel = new Header();
        headerPanel.addCartButtonAction(e -> MyFrame.showPage("CartPage"));
        headerPanel.addAccountButtonAction(e -> MyFrame.showPage("AccountPage"));

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        add(backgroundPanel, BorderLayout.CENTER);
        setupSearch();
    }

    protected void setupSearch() {
        final JPanel searchPanel = new JPanel();
        final TextField searchField = new TextField("Search here...");
        final Button searchButton = new Button(Images.getImage("SearchImg"));

        searchField.setPreferredSize(new Dimension(400, 35));
        searchButton.setPreferredSize(new Dimension(40, 35));
        searchButton.setArch(0);
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.setOpaque(false);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().toLowerCase();
            MyFrame.showPage("ProductsPage", searchTerm);
        });
    }

    protected void setupMenu() {
        Menu menuPanel = new Menu();

        menuPanel.addDivider();
        menuPanel.addButton("Home", "HomeImg", e -> MyFrame.showPage("HomePage"));
        menuPanel.addButton("Browse Stores", "StoresImg", e -> MyFrame.showPage("StoresPage"));
        menuPanel.addButton("Browse Products", "ProductsImg", e -> MyFrame.showPage("ProductsPage"));
        menuPanel.addDivider();

        headerPanel.addMenuButtonAction(e -> menuPanel.slide());
        add(menuPanel, BorderLayout.WEST);
    }

    public static void switchToPageWhenPressed(JButton button, String pageName) {
        button.addActionListener(e -> MyFrame.showPage(pageName));
    }
}
