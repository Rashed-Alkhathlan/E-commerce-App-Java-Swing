package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Label extends JLabel {
    private Color fg = UIManager.getColor("Label.foreground");
    private Color hoverFg = new Color(10, 100, 255);
    private String text;

    public Label(String text) {
        setText(text);
        this.text = text;
    }

    public Label() {
        super();
    }

    public void addClickableAction(Runnable action) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Label.super.setForeground(hoverFg);
                Label.super.setText("<html><u>" + text + "</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Label.super.setForeground(fg);
                Label.super.setText("<html>" + text + "</html>");
            }
        });
    }

    @Override
    public void setForeground(Color fg) {
        this.fg = fg;
        super.setForeground(fg);
    }

    @Override
    public void setText(String text) {
        super.setText("<html>" + text + "</html>");
        this.text = text;
    }

    public void setHoverForeground(Color hoverFg) {
        this.hoverFg = hoverFg;
    }
}
