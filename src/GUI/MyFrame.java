package GUI;

import Objects.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public final class MyFrame {
    private static final JFrame frame = new JFrame("ShopSphere");
    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel mainPanel = new JPanel(cardLayout);
    private static final Stack<String> history = new Stack<>();

    private static final int width = 1300;
    private static final int height = 800;

    public MyFrame() {
        initFrame();
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    private void initFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(width, height));
        //frame.setUndecorated(true);

        loadPanels();
        history.push("HomePage");
        switchToPage("HomePage");


        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private static void loadPanels() {
        mainPanel.setPreferredSize(new Dimension(width, height));
//        mainPanel.add(new StartPage(), "StartPage");
//        mainPanel.add(new LoginPage(), "LoginPage");
//        mainPanel.add(new StoresPage(), "StoresPage");
//        mainPanel.add(new CartPage(), "CartPage");
//        mainPanel.add(new AccountPage(), "AccountPage");
//        mainPanel.add(new AdminPage(), "AdminPage");
//        mainPanel.add(new RegisterPage(), "RegisterPage");
//        mainPanel.add(new HomePage(), "HomePage");
    }

    static void switchToPage(String pageName) {
        if (pageName.equals("CheckoutPage") && !Main.isSignedIn()) {pageName = "StartPage";}
        if (pageName.equals("AccountPage") && !Main.isSignedIn()) {pageName = "StartPage";}
        if (pageName.equals("PreviousPage")) {history.pop(); pageName = history.peek();}
        if (!pageName.equals(history.peek())) {history.push(pageName);}

        mainPanel.removeAll();

        switch(pageName) {
            case "HomePage":
                mainPanel.add(new HomePage());
                break;
            case "StoresPage":
                mainPanel.add(new StoresPage());
                break;
            case "ProductsPage":
                mainPanel.add(new ProductsPage());
                break;
            case "RegisterPage":
                mainPanel.add(new RegisterPage());
                break;
            case "LoginPage":
                mainPanel.add(new LoginPage());
                break;
            case "StartPage":
                mainPanel.add(new StartPage());
                break;
            case "AccountPage":
                mainPanel.add(new AccountPage());
                break;
            case "AdminPage":
                mainPanel.add(new AdminPage());
                break;
            case "ManagerPage":
                mainPanel.add(new ManagerPage());
                break;
            case "CheckoutPage":
                mainPanel.add(new CheckoutPage());
                break;
            case "CartPage":
                mainPanel.add(new CartPage());
                break;
            case "ProductPage":
                mainPanel.add(new ProductPage());
                break;
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }
}