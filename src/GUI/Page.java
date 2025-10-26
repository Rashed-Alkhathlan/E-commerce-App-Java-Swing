package GUI;

import Components.*;
import Components.Button;
import Components.Label;
import Components.Menu;
import Components.Panel;
import Components.TextField;
import Utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

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

        setupBackButton();

        add(backgroundPanel, BorderLayout.CENTER);
        setupSearch();
    }

    protected void setupBackButton() {
        if(!(this instanceof HomePage || this instanceof StartPage)) {
            headerPanel.addBackButtonAction(e -> MyFrame.goBack());
        }
    }

    private static final LinkedHashSet<String> searchHistory = new LinkedHashSet<>();
    private final JPopupMenu suggestionPopup = new JPopupMenu() {
        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(searchField.getWidth(), d.height);
        }
    };

    private void setupSearch() {
        searchField.setPreferredSize(new Dimension(400, 35));
        searchField.setMinimumSize(new Dimension(200, 35));
        searchButton.setPreferredSize(new Dimension(35, 35));
        searchButton.setArch(10);
        searchButton.setBorderPainted(false);
        searchField.setLayout(new BorderLayout());
        searchField.add(searchButton, BorderLayout.EAST);
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.setBorder(new EmptyBorder(0, 45, 0, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField);
        headerPanel.add(searchPanel, BorderLayout.CENTER);

        suggestionPopup.setBorder(new LineBorder(new Color(175, 175, 175)));
        suggestionPopup.setFocusable(false);
        suggestionPopup.setOpaque(false);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }
        });
        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { update(); }
        });

        searchField.addActionListener(e -> {
            String input = searchField.getText().trim();
            if (!input.isEmpty()) {
                addToHistory(input);
            }
            suggestionPopup.setVisible(false);
            search();
        });
        
        searchButton.addActionListener(e -> {
            String input = searchField.getText().trim();
            if (!input.isEmpty()) {
                addToHistory(input);
            }
            suggestionPopup.setVisible(false);
            search();
        });
    }

    private void update() {
        String input = searchField.getText().toLowerCase();
        suggestionPopup.setVisible(false);
        suggestionPopup.removeAll();

        for (String item : searchHistory) {
            if (item.toLowerCase().startsWith(input)) {
                suggestionPopup.insert(createSuggestionItem(item), 0);
            }
        }

        if (suggestionPopup.getComponentCount() > 0) {
            try {
                suggestionPopup.show(searchField, 0, searchField.getHeight());
                searchField.requestFocusInWindow();
            } catch (Exception e) {}
        }
    }

    private void addToHistory(String term) {
        searchHistory.remove(term);
        searchHistory.add(term);
        if (searchHistory.size() > 10) {
            Iterator<String> it = searchHistory.iterator();
            it.next();
            it.remove();
        }
    }

    private JPanel createSuggestionItem(String item) {
        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setFocusable(false);
        panel.setBackground(new Color(255, 255, 255, 120));
        panel.setBorder(new EmptyBorder(0, 5, 0, 0));

        JLabel label = new JLabel(item);
        label.setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 4));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Label removeButton = new Label("x");
        removeButton.setOpaque(false);
        removeButton.setBorder(new EmptyBorder(0, 6, 4, 6));
        removeButton.setFocusable(false);
        removeButton.setFont(new Font(UIManager.getFont("TextField.font").getFontName(), Font.PLAIN, 20));
        removeButton.setForeground(new Color(180, 180, 180));
        removeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        removeButton.setHoverForeground(Color.RED);

        removeButton.addClickableAction(() -> {
            searchHistory.remove(item);
            update();
        });

        label.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                searchField.setText(item);
                suggestionPopup.setVisible(false);
                addToHistory(item);
                search();
            }
        });

        panel.add(new JLabel(Images.getImage("searchImg", 15, 15)), BorderLayout.WEST);
        panel.add(label, BorderLayout.CENTER);
        panel.add(removeButton, BorderLayout.EAST);
        return panel;
    }

    private void search() {
        String searchTerm = searchField.getText();
        if (this instanceof ProductsPage && ((ProductsPage)this).getCurrentStoreId() != null) {
            MyFrame.showPage(ProductsPage.class, ((ProductsPage)this).getCurrentStoreId(), searchTerm);
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
        menuPanel.addLabel("Account & Settings");
        menuPanel.addButton("Account", "ManImg", e -> MyFrame.showPage(AccountPage.class));
        menuPanel.addButton("Orders", "DashImg", e -> MyFrame.showPage(AccountPage.class, 1));
        menuPanel.addButton("Addresses", "ElecImg", e -> MyFrame.showPage(AccountPage.class, 2));


        headerPanel.addMenuButtonAction(e -> menuPanel.slide());
        add(menuPanel, BorderLayout.WEST);
    }
}
