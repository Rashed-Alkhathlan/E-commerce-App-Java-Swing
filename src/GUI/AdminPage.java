package GUI;

import javax.swing.*;
import java.awt.*;

public class AdminPage extends Page {
    private final JButton allManagers = new JButton("All Managers");
    private final JButton addManager = new JButton("Add Manager");
    private final JButton removeManager = new JButton("Remove Manager");
    private final JButton addStoreButton = new JButton("Add Store");
    private final JButton removeStoreButton = new JButton("Remove Store");
    private final JButton changeStoreManager = new JButton("Change Store Manager");
    private final JButton backButton = new JButton("Back");

    AdminPage() {
        initPage();
    }

    @Override
    protected void initPage() {
        defaultBackground();
        actionListener();

        setButton(allManagers, sidePanel, 0, 0, sidePanelWidth, 100);
        setButton(addManager, sidePanel, 0, 100, sidePanelWidth, 100);
        setButton(removeManager, sidePanel, 0, 200, sidePanelWidth, 100);
        setButton(addStoreButton, sidePanel, 0, 300, sidePanelWidth, 100);
        setButton(removeStoreButton, sidePanel, 0, 400, sidePanelWidth, 100);
        setButton(changeStoreManager, sidePanel, 0, 500, sidePanelWidth, 100);
        headerPanel.add(backButton, BorderLayout.WEST);
    }

    @Override
    protected void actionListener() {
        switchToPageWhenPressed(backButton, "PreviousPage");
    }
}
