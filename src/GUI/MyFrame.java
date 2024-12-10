package GUI;

import Objects.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;
import java.util.function.Function;

public final class MyFrame {
    private static final JFrame frame = new JFrame("ShopSphere");
    private static final JPanel mainPanel = new JPanel();
    private static JPanel loadingPanel;
    private static final Stack<String> history = new Stack<>();
    private static boolean inAnimation = false;
    private static boolean loading = false;

    private static final HashMap<String, Function<Object, JPanel>> pages = new HashMap<>();

    private static final int width = 1300;
    private static final int height = 800;

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static boolean isInAnimation() {
        return inAnimation;
    }

    public static void setInAnimation(boolean animation) {
        inAnimation = animation;
    }

    public MyFrame() {
        initFrame();
    }

    private void initFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(width, height));

        setupLoadingPanel();
        loadPages();
        history.push("StartPage");
        showPage("StartPage");

        mainPanel.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    private void loadPages() {
        pages.put("StartPage", e -> new StartPage());
        pages.put("HomePage", e -> new HomePage());
        pages.put("CartPage", e -> new CartPage());
        pages.put("StoresPage", e -> new StoresPage());
        pages.put("ProductsPage", e -> new ProductsPage((UUID)e));
        pages.put("RegisterPage", e -> new RegisterPage());
        pages.put("LoginPage", e -> new LoginPage());
        pages.put("CheckoutPage", e -> new CheckoutPage());
        pages.put("AccountPage", e -> new AccountPage());
    }

    public static void showPage(String pageName) {
        if (pageName.equals("PreviousPage")) {history.pop(); pageName = history.peek();}
       showPage(pageName, null);
    }

    public static void showPage(String pageName, Object param) {
        if (pageName.equals("RegisterPage") && Main.isSignedIn()) {pageName = "HomePage";}
        if (pageName.equals("LoginPage") && Main.isSignedIn()) {pageName = "HomePage";}
        if (pageName.equals("CheckoutPage") && !Main.isSignedIn()) {pageName = "LoginPage";}
        if (pageName.equals("AccountPage") && !Main.isSignedIn()) {pageName = "LoginPage";}

        if (!pageName.equals(history.peek())) {history.push(pageName);}

        if (!loading) {
            String finalPageName = pageName;
            load(() -> {
                mainPanel.removeAll();
                mainPanel.add(pages.get(finalPageName).apply(param));
                System.out.println(finalPageName);
                mainPanel.revalidate();
                mainPanel.repaint();
            });
        }
    }

    public static void reloadPage() {
        showPage(history.peek());
    }

    public static void load(Runnable backgroundTask) {
        loading = true;
        loadingPanel.setVisible(true);
        loadingPanel.revalidate();
        loadingPanel.repaint();

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                backgroundTask.run();
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> loadingPanel.setVisible(false));
                loading = false;
            }
        }.execute();
    }

    private void setupLoadingPanel() {
        loadingPanel = new JPanel() {
            private Timer timer; // Timer for the loading animation
            private int angle = 0; // Rotation angle
            private final int radius = 50; // Circle radius
            private final int arcLength = 60; // Length of the arc

            {
                addHierarchyListener(e -> {
                    if (isShowing()) {
                        startAnimation();
                    } else {
                        stopAnimation();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // Semi-transparent background
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Enable anti-aliasing
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw rotating arc
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(4)); // Thickness of the arc
                g2d.drawArc(centerX - radius, centerY - radius, 2 * radius, 2 * radius, angle, arcLength);

                g2d.dispose();
            }

            private void startAnimation() {
                if (timer == null) {
                    timer = new Timer(10, e -> {
                        angle += 10; // Increment angle
                        if (angle >= 360) {
                            angle = 0;
                        }
                        loadingPanel.repaint(); // Repaint to show updated animation
                    });
                    timer.start();
                }
            }

            private void stopAnimation() {
                if (timer != null) {
                    timer.stop();
                    timer = null;
                }
            }
        };

        loadingPanel.setLayout(new GridBagLayout());
        loadingPanel.setOpaque(false);
        loadingPanel.setVisible(false);

        JLabel loadingLabel = new JLabel("Loading...");
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        loadingPanel.add(loadingLabel);

        frame.setGlassPane(loadingPanel);
    }

}
