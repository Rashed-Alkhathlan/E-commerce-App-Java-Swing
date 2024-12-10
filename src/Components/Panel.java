package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class Panel extends JPanel {
    private Color firstColor = new Color(238, 238, 238);
    private Color secondColor = null;
    private Color thirdColor = null;
    private boolean borderPainted = false;
    private Color borderColor = new Color(175, 175, 175);

    private int arch = 0;

    public Panel() {
        initPanel();
    }

    public Panel(Color color) {
        this.firstColor = color;
        initPanel();
    }

    public Panel(String color) {
        this.firstColor = Color.decode(color);
        this.secondColor = Color.decode(color);
        initPanel();
    }

    public Panel(String firstColor, String secondColor) {
        this.firstColor = Color.decode(firstColor);
        this.secondColor = Color.decode(secondColor);
        initPanel();
    }

    public Panel(String firstColor, String secondColor, String thirdColor) {
        this.firstColor = Color.decode(firstColor);
        this.secondColor = Color.decode(secondColor);
        this.thirdColor = Color.decode(thirdColor);
        initPanel();
    }

    private void initPanel() {
        setOpaque(false);
        setFocusable(false);
    }

    public void setArch(int arch) {
        this.arch = arch;
    }

    @Override
    public void setBackground(Color color) {
        this.firstColor = color;
    }

    public void setBorderPainted(boolean flag) {
        borderPainted = flag;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public boolean isBorderPainted() {
        return borderPainted;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();

        Paint gradient;
        if (thirdColor == null && secondColor == null) {
            gradient = firstColor;
        } else if (thirdColor == null) {
            gradient = new GradientPaint(0, 0, firstColor, width, height, secondColor);
        } else {
            gradient = new LinearGradientPaint(new Point2D.Float(0, 0), new Point2D.Float(width, height), new float[]{0.0f, 0.5f, 1.0f}, new Color[]{firstColor, secondColor, thirdColor});
        }

        g2d.setPaint(gradient);
        if (arch != 0) {
            g2d.fillRoundRect(0, 0, width, height, arch, arch);
        } else {
            g2d.fillRect(0, 0, width, height);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (isBorderPainted()) {
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(1));
            if (arch != 0) {
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arch, arch);
            } else {
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        }
    }
}
