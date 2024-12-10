package Components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ScrollPane extends JScrollPane {

    public ScrollPane() {
        initScrollPane();
    }

    public ScrollPane(Component view) {
        super(view);
        initScrollPane();
    }

    private void initScrollPane() {
        setOpaque(false);
        getViewport().setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
        setVerticalScrollBar(new ScrollBar());
        getVerticalScrollBar().setBackground(Color.WHITE);
        getViewport().setBackground(Color.WHITE);
        JPanel corner = new JPanel();
        corner.setBackground(Color.WHITE);
        setCorner(JScrollPane.UPPER_RIGHT_CORNER, corner);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }


    public static class ScrollBar extends JScrollBar {

        public ScrollBar() {
            setUI(new ModernScrollBarUI());
            setPreferredSize(new Dimension(5, 5));
            setBackground(new Color(242, 242, 242));
            setUnitIncrement(20);
        }

        public static class ModernScrollBarUI extends BasicScrollBarUI {

            private static final int SCROLL_BAR_ALPHA_ROLLOVER = 100;
            private static final int SCROLL_BAR_ALPHA = 50;
            private static final int THUMB_SIZE = 8;
            private static final Color THUMB_COLOR = Color.BLACK;

            public ModernScrollBarUI() {
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new InvisibleScrollBarButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new InvisibleScrollBarButton();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                int alpha = isThumbRollover() ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
                int orientation = scrollbar.getOrientation();
                int x = thumbBounds.x;
                int y = thumbBounds.y;

                int width = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : thumbBounds.width;
                width = Math.max(width, THUMB_SIZE);

                int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height : THUMB_SIZE;
                height = Math.max(height, THUMB_SIZE);

                Graphics2D graphics2D = (Graphics2D) g.create();
                graphics2D.setColor(new Color(THUMB_COLOR.getRed(), THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));
                graphics2D.fillRect(x, y, width, height);
                graphics2D.dispose();
            }

            private static class InvisibleScrollBarButton extends JButton {

                private InvisibleScrollBarButton() {
                    setOpaque(false);
                    setFocusable(false);
                    setFocusPainted(false);
                    setBorderPainted(false);
                    setBorder(BorderFactory.createEmptyBorder());
                }
            }
        }
    }
}
