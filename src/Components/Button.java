package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JButton {

    private boolean opaque = true;

    private final Color hoverColor = new Color(0, 0, 0, 20);
    private boolean isHovered = false;

    private int arch = 20;

    public Button() {
        initButton();
    }

    public Button(ImageIcon icon) {
        super(icon);
        initButton();
    }

    public Button(String text) {
        super(text);
        initButton();
    }

    private void initButton() {
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(true);
        setBorder(null);

        // Add mouse hover effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    //Overload
    public void add(Panel panel) {
        setOpaque(false);
        panel.setArch(arch);
        super.add(panel);
    }

    public void setArch(int arch) {
        this.arch = arch;
    }

    @Override
    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
        setBorderPainted(opaque);
    }

    public boolean isOpaque() {
        return opaque;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (isOpaque()) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            if (arch != 0) {
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arch, arch);
            } else {
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (isBorderPainted()) {
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(175, 175, 175));
            g2d.setStroke(new BasicStroke(1));
            if (arch != 0) {
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arch, arch);
            } else {
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        }
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);

        Graphics2D g2d = (Graphics2D) g;
        if (isHovered) {
            g2d.setColor(hoverColor);
            if (arch != 0) {
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arch, arch);
            } else {
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}