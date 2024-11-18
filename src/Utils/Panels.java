package Utils;

import javax.swing.*;
import java.awt.*;

public final class Panels {
    private Panels() {}

    public static JPanel JPanelWithGradiant(String firstColor, String secondColor) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color startColor = Color.decode(firstColor);
                Color endColor = Color.decode(secondColor);
                int width = getWidth();
                int height = getHeight();
                GradientPaint gradient = new GradientPaint(0, 0, startColor, width, height, endColor);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
            }
        };
        panel.repaint();
        return panel;
    }
}
