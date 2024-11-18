package GUI;

import GUI_Components.*;
import GUI_Components.Menu;

import javax.swing.*;
import java.awt.*;

public abstract class Page extends JPanel {

    protected JPanel centerPanel;
    protected JPanel sidePanel;
    protected Header headerPanel;
    protected Menu menuPanel;

    protected int frameWidth = MyFrame.getWidth();
    protected int frameHeight = MyFrame.getHeight();
    protected int sidePanelWidth = 300;
    protected int sidePanelHeight = frameHeight;

    protected abstract void initPage();
    protected abstract void actionListener();

    Page() {
        this.setLayout(new BorderLayout(0, 0));
        this.setBounds(0, 0, frameWidth, frameHeight);
    }

    protected void defaultBackground() {
        defaultBackground(true);
    }

    protected void defaultBackground(boolean makeSidePanel) {
        headerPanel = new Header("#ffffff", "#ffffff");
        headerPanel.addMenuButtonAction(e -> menuPanel.slide());

        centerPanel = new JPanelGradiant("#F2F2F2", "#EAEAEA");
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(headerPanel, BorderLayout.NORTH);

        if (makeSidePanel) {
            sidePanel = new JPanelGradiant("#0F2027", "#2C5364");
            sidePanel.setLayout(null);
            sidePanel.setPreferredSize(new Dimension(sidePanelWidth, 0));
            centerPanel.add(sidePanel, BorderLayout.EAST);
        }

        setupMenu();
        add(centerPanel, BorderLayout.CENTER);
    }

    private void setupMenu() {
        menuPanel = new Menu("#56CCF2", "#2F80ED");

        menuPanel.addDivider();
        menuPanel.addButton(1, "Browse Stores", "StoresImg", e -> MyFrame.switchToPage("StoresPage"));
        menuPanel.addButton(2, "Browse Products", "ProductsImg", e -> MyFrame.switchToPage("ProductsPage"));
        menuPanel.addDivider();
        menuPanel.addLabel("Shop by Department:");
        menuPanel.addButton(3, "Electronics", "ElecImg", e -> MyFrame.switchToPage("HomePage"));
        menuPanel.addButton(4, "Accessories", "AccsImg", e -> MyFrame.switchToPage("HomePage"));
        menuPanel.addButton(5, "Arts & Crafts", "ArtImg", e -> MyFrame.switchToPage("HomePage"));
        menuPanel.addButton(6, "Outdoor", "OutImg", e -> MyFrame.switchToPage("HomePage"));

        add(menuPanel, BorderLayout.WEST);
    }

    protected void setButton(JButton button, JPanel panel, int x , int y, int width, int height) {
        button.setBounds(x, y, width, height);
        button.setFocusable(false);
        panel.add(button);
    }

    protected void setLabel(JLabel label, JPanel panel, int x , int y, int width, int height) {
        label.setBounds(x, y, width, height);
        panel.add(label);
    }

    protected void setTextField(JTextField textField, JPanel panel, int x, int y, int width, int height) {
        textField.setBounds(x, y, width, height);
        panel.add(textField);
    }

    public static void switchToPageWhenPressed(JButton button, String pageName) {
        button.addActionListener(e -> MyFrame.switchToPage(pageName));
    }
}
