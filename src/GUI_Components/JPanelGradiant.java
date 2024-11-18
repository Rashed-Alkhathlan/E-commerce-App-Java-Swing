package GUI_Components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class JPanelGradiant extends JPanel {
    private final String firstColor;
    private final String secondColor;
    private final String thirdColor;

    public JPanelGradiant(String firstColor, String secondColor) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.thirdColor = null;
    }

    public JPanelGradiant(String firstColor, String secondColor, String thirdColor) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.thirdColor = thirdColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (thirdColor == null) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            Color startColor = Color.decode(firstColor);
            Color endColor = Color.decode(secondColor);
            int width = getWidth();
            int height = getHeight();
            GradientPaint gradient = new GradientPaint(0, 0, startColor, width, 0, endColor);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, width, height);
        } else {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Decode colors from the provided hex strings
            Color color1 = Color.decode(firstColor); // Start color
            Color color2 = Color.decode(secondColor); // Middle color
            Color color3 = Color.decode(thirdColor); // End color

            int width = getWidth();
            int height = getHeight();

            // Define gradient positions: 0.0f (start), 0.5f (middle), 1.0f (end)
            float[] fractions = {0.0f, 0.5f, 1.0f};
            Color[] colors = {color1, color2, color3};

            // Create a horizontal LinearGradientPaint that spans across the component
            LinearGradientPaint gradient = new LinearGradientPaint(
                    new Point2D.Float(0, 0),
                    new Point2D.Float(width, 0),
                    fractions,
                    colors
            );

            // Apply the gradient and fill the component
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, width, height);
        }

    }

}
