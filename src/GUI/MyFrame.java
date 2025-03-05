package GUI;

import Objects.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Function;

public final class MyFrame {
    private static final JFrame frame = new JFrame("ShopSphere");
    private static final JPanel mainPanel = new JPanel();
    private static JPanel loadingPanel;
    private static Stack<PageHistoryEntry> history = new Stack<>();
    private static boolean inAnimation = false;

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
        frame.setLocation(GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().x - (getWidth() / 2), GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().y - (getHeight() / 2));
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(width, height));

        setupLoadingPanel();
        showPage(StartPage.class);

        mainPanel.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    public static void showPage(Class<? extends Page> pageClass, Object... args) {
        load(() -> {
            Page newPage = createPage(pageClass, args);
            if (newPage == null) return;

            history.push(new PageHistoryEntry(pageClass, args));

            mainPanel.removeAll();
            mainPanel.add(newPage);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
    }

    public static void goBack() {
        if (history.size() > 1) {
            history.pop();
            PageHistoryEntry previousEntry = history.pop();
            showPage(previousEntry.pageClass, previousEntry.args); // Restore previous page with args
        }
    }

    public static void reloadPage() {
        if (!history.isEmpty()) {
            PageHistoryEntry previousEntry = history.pop();
            showPage(previousEntry.pageClass, previousEntry.args); // Restore previous page with args
        }
    }

    private static Page createPage(Class<? extends Page> pageClass, Object... args) {
        if (pageClass == AccountPage.class && !Main.isSignedIn()) pageClass = LoginPage.class;
        if (pageClass == CheckoutPage.class && !Main.isSignedIn()) pageClass = LoginPage.class;
        if (pageClass == RegisterPage.class && Main.isSignedIn()) pageClass = HomePage.class;
        if (pageClass == LoginPage.class && Main.isSignedIn()) pageClass = HomePage.class;

        try {
            if (args.length == 0) {
                return pageClass.getDeclaredConstructor().newInstance();
            } else {
                for (Constructor<?> constructor : pageClass.getDeclaredConstructors()) {
                    Class<?>[] paramTypes = constructor.getParameterTypes();

                    if (paramTypes.length == args.length) {
                        boolean matches = true;
                        for (int i = 0; i < paramTypes.length; i++) {
                            if (!paramTypes[i].isInstance(args[i])) {
                                matches = false;
                                break;
                            }
                        }
                        if (matches) {
                            return (Page) constructor.newInstance(args);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void load(Runnable backgroundTask) {
        loadingPanel.setVisible(true);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                backgroundTask.run();
                return null;
            }

            @Override
            protected void done() {
                loadingPanel.setVisible(false);
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
                    timer = new Timer(15, e -> {
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

    private static class PageHistoryEntry {
        Class<? extends Page> pageClass;
        Object[] args;

        PageHistoryEntry(Class<? extends Page> pageClass, Object[] args) {
            this.pageClass = pageClass;
            this.args = args;
        }
    }

}
