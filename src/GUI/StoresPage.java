package GUI;

import Components.TextField;
import Components.Button;
import Components.ScrollPane;
import Enums.StoreStatus;
import Objects.*;
import Services.*;
import Utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class StoresPage extends Page {
    private final JPanel storesButtonsPanel = new JPanel();
    private final ScrollPane storesScrollPane = new ScrollPane(storesButtonsPanel);
    private final JPanel searchPanel = new JPanel();
    private final TextField searchField = new TextField("Search here...");
    private final Button searchButton = new Button(Images.getImage("SearchImg"));

    private final Color buttonColor = Color.WHITE;
    private int totalStores;
    ArrayList<Store> stores;

    private final int storeButtonWidth = 300;
    private final int storeButtonHeight = 280;

    private final int iconWidth = 300;
    private final int iconHeight = 250;


    StoresPage() {
        initPage();
    }

    @Override
    protected void initPage() {
        setupBackground();
        setupMenu();
        actionListener();

        setupStoresPanel();
        setupSearchPanel();
    }

    public void actionListener() {
        storesScrollPane.getVerticalScrollBar().addAdjustmentListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                loadVisibleStores();
                return null;
            }
        }.execute());
        storesScrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (!MyFrame.isInAnimation()) {
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            storesButtonsPanel.removeAll();
                            loadVisibleStores();
                            return null;
                        }
                    }.execute();
                }
            }
        });
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().toLowerCase();
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    updateStoresPanel(searchTerm);
                    loadVisibleStores();
                    return null;
                }
            }.execute();
        });
    }

    private void setupStoresPanel() {
        storesButtonsPanel.setLayout(null);
        storesButtonsPanel.setOpaque(false);
        contentPanel.add(storesScrollPane, BorderLayout.CENTER);
        updateStoresPanel("");
    }

    private void setupSearchPanel() {
        searchField.setPreferredSize(new Dimension(300, 30));
        searchButton.setPreferredSize(new Dimension(30, 30));
        searchButton.setArch(0);
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.setOpaque(false);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.CENTER);
    }

    private void updateStoresPanel(String searchTerm) {
        storesButtonsPanel.removeAll();
        stores = StoresService.getStores(searchTerm);
        stores.removeIf(store -> store.getStatus() == StoreStatus.CLOSED);
        totalStores = stores.size();
    }

    private void loadVisibleStores() {
        int panelWidth = storesScrollPane.getWidth();
        int horizontalGap = 6, verticalGap = 6;

        int storesPerRow = Math.max(1, (panelWidth + horizontalGap) / (storeButtonWidth + horizontalGap));
        int rows = (int) Math.ceil((double) totalStores / storesPerRow);

        int totalHeight = rows * (storeButtonHeight + verticalGap) + 50;

        storesButtonsPanel.setPreferredSize(new Dimension(panelWidth, totalHeight));

        // Get the visible area of the JScrollPane
        Rectangle visibleRect = storesScrollPane.getViewport().getViewRect();

        int firstVisibleRow = Math.max(0, visibleRect.y / (storeButtonHeight + verticalGap));
        int lastVisibleRow = Math.max(0, ((visibleRect.y + visibleRect.height) / (storeButtonHeight + verticalGap)) + 2);

        if (firstVisibleRow > 0) {firstVisibleRow-= 2;}

        // Calculate product indices for visible rows
        int startIndex = firstVisibleRow * storesPerRow;
        int endIndex = Math.min(lastVisibleRow * storesPerRow, totalStores);

        // Clear buttons outside the visible area
        for (Component component : storesButtonsPanel.getComponents()) {
            if (component instanceof JButton button) {
                int buttonIndex = (int) button.getClientProperty("index");
                if (buttonIndex < startIndex || buttonIndex >= endIndex) {
                    storesButtonsPanel.remove(button); // Remove buttons outside the visible area
                }
            }
        }

        // Add buttons in visible area
        for (int i = startIndex; i < endIndex; i++) {
            if (!isButtonVisible(i)) {
                JButton storeButton = getStoreButton(stores.get(i));
                storeButton.putClientProperty("index", i);

                int row = i / storesPerRow;
                int col = i % storesPerRow;
                int x = (col * (storeButtonWidth + horizontalGap)) + ((panelWidth - (storesPerRow * (storeButtonWidth + horizontalGap))) / 2);
                int y = row * (storeButtonHeight + verticalGap);

                storeButton.setBounds(x, y, storeButtonWidth, storeButtonHeight);
                storesButtonsPanel.add(storeButton);
            }
        }
        storesButtonsPanel.revalidate();
        storesButtonsPanel.repaint();
    }

    private boolean isButtonVisible(int index) {
        for (Component component : storesButtonsPanel.getComponents()) {
            if (component instanceof JButton button) {
                Integer buttonIndex = (Integer) button.getClientProperty("index");
                if (buttonIndex != null && buttonIndex == index) {
                    return true;
                }
            }
        }
        return false;
    }

    //Buttons for Stores
    private JButton getStoreButton(Store store) {
        Button storeButton = new Button();
        storeButton.setArch(0);
        storeButton.setPreferredSize(new Dimension(storeButtonWidth, storeButtonHeight));
        storeButton.setBorder(new EmptyBorder(1, 1, 1, 1));
        storeButton.setLayout(new BorderLayout());

        JLabel imageLabel = new JLabel(Images.scaleImage(store.getMainImageIcon(), iconWidth, iconHeight));
        JPanel textPanel = getStoreButtonPanel(store);

        storeButton.add(imageLabel, BorderLayout.NORTH);
        storeButton.add(textPanel, BorderLayout.CENTER);

        storeButton.addActionListener(e -> MyFrame.showPage("ProductsPage", store.getId()));
        return storeButton;
    }

    private JPanel getStoreButtonPanel(Store store) {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.setBackground(buttonColor);
        textPanel.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 3));

        JLabel nameLabel = new JLabel(store.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel descriptionLabel = new JLabel("<html><i>" + store.getDescription() + "</i></html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        textPanel.add(nameLabel, BorderLayout.NORTH);
        textPanel.add(descriptionLabel, BorderLayout.CENTER);
        return textPanel;
    }

}