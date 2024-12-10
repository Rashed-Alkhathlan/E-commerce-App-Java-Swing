package GUI;

import Components.*;
import Components.Menu;
import Components.Panel;

import javax.swing.*;
import java.awt.*;

public abstract class Page extends JPanel {

    protected abstract void initPage();

    protected JPanel backgroundPanel;
    protected JPanel contentPanel;
    protected JPanel sidePanel;

    protected Header headerPanel;
    protected int frameWidth = MyFrame.getWidth();
    protected int frameHeight = MyFrame.getHeight();
    protected int sidePanelWidth = 300;

    protected int sidePanelHeight = frameHeight;

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
    }

    protected void addSidePanel() {
        sidePanel = new Panel("#ad5389", "#3c1053");
        sidePanel.setLayout(null);
        sidePanel.setPreferredSize(new Dimension(sidePanelWidth, 0));
        contentPanel.add(sidePanel, BorderLayout.EAST);
    }

    protected void setupMenu() {
        Menu menuPanel = new Menu();

        menuPanel.addDivider();
        menuPanel.addButton("Home", "HomeImg", e -> MyFrame.showPage("HomePage"));
        menuPanel.addButton("Browse Stores", "StoresImg", e -> MyFrame.showPage("StoresPage"));
        menuPanel.addButton("Browse Products", "ProductsImg", e -> MyFrame.showPage("ProductsPage"));
        menuPanel.addDivider();
        menuPanel.addLabel("Shop by Category:");
        menuPanel.addButton("Electronics", "ElecImg", e -> MyFrame.showPage("HomePage"));
        menuPanel.addButton("Accessories", "AccsImg", e -> MyFrame.showPage("HomePage"));
        menuPanel.addButton("Arts & Crafts", "ArtImg", e -> MyFrame.showPage("HomePage"));
        menuPanel.addButton("Outdoor", "OutImg", e -> MyFrame.showPage("HomePage"));

        headerPanel.addMenuButtonAction(e -> menuPanel.slide());
        add(menuPanel, BorderLayout.WEST);
    }

    protected void setButton(JButton button, JPanel panel, int x , int y, int width, int height) {
        button.setBounds(x, y, width, height);
        button.setFocusable(false);
        panel.add(button);
    }

    public static void switchToPageWhenPressed(JButton button, String pageName) {
        button.addActionListener(e -> MyFrame.showPage(pageName));
    }
}
