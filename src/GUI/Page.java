package GUI;

import Components.*;
import Components.Button;
import Components.Menu;
import Components.TextField;
import Utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class Page extends JPanel {

    protected abstract void initPage();

    protected JPanel backgroundPanel;
    protected JPanel contentPanel;

    protected Header headerPanel;
    protected int frameWidth = MyFrame.getWidth();
    protected int frameHeight = MyFrame.getHeight();

    protected final JPanel searchPanel = new JPanel();
    protected final TextField searchField = new TextField("Search here...");
    protected final Button searchButton = new Button(Images.getImage("SearchImg"));

    protected Page() {
        setLayout(new BorderLayout(0, 0));
        setBounds(0, 0, frameWidth, frameHeight);
    }

    protected void setupBackground() {
        backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new BorderLayout());

        headerPanel = new Header();
        headerPanel.addCartButtonAction(e -> MyFrame.showPage(CartPage.class));
        headerPanel.addAccountButtonAction(e -> MyFrame.showPage(AccountPage.class));

        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setLayout(new BorderLayout());

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        add(backgroundPanel, BorderLayout.CENTER);
        setupSearch();
    }

    private void setupSearch() {
        searchField.setPreferredSize(new Dimension(400, 35));
        searchField.setMinimumSize(new Dimension(200, 35));
        searchButton.setPreferredSize(new Dimension(40, 35));
        searchButton.setArch(10);
        searchButton.setBorderPainted(false);
        searchField.setLayout(new BorderLayout());
        searchField.add(searchButton, BorderLayout.EAST);
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.setBorder(new EmptyBorder(0, 45, 0, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        searchField.addActionListener(e -> search());
        searchButton.addActionListener(e -> search());
    }

    private void search() {
        String searchTerm = searchField.getText().toLowerCase();
        if (this instanceof ProductsPage && ((ProductsPage)this).getCurrentStore() != null) {
            MyFrame.showPage(ProductsPage.class, ((ProductsPage)this).getCurrentStore(), searchTerm);
        } else if (this instanceof StoresPage) {
            MyFrame.showPage(StoresPage.class, searchTerm);
        } else {
            MyFrame.showPage(ProductsPage.class, searchTerm);
        }
    }

    protected void setupMenu() {
        Menu menuPanel = new Menu();

        menuPanel.addDivider();
        menuPanel.addButton("Home", "HomeImg", e -> MyFrame.showPage(HomePage.class));
        menuPanel.addButton("Browse Stores", "StoresImg", e -> MyFrame.showPage(StoresPage.class));
        menuPanel.addButton("Browse Products", "ProductsImg", e -> MyFrame.showPage(ProductsPage.class));
        menuPanel.addDivider();

        headerPanel.addMenuButtonAction(e -> menuPanel.slide());
        add(menuPanel, BorderLayout.WEST);
    }

    public static void switchToPageWhenPressed(JButton button, Class<? extends Page> pageName) {
        button.addActionListener(e -> MyFrame.showPage(pageName));
    }

    protected void reload() {
        removeAll();
        initPage();
        revalidate();
        repaint();
    }
}
