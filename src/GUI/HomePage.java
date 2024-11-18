package GUI;

import javax.swing.*;

public class HomePage extends Page {
    private final JButton AllProductsButton = new JButton();
    private final JButton AllStoresButton = new JButton();
    private final JButton AllBrowseButton = new JButton();

    HomePage() {
        initPage();
    }

    @Override
    protected void initPage() {
        defaultBackground(false);
    }

    @Override
    protected void actionListener() {

    }
}
