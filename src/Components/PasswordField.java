package Components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class PasswordField extends JPasswordField {

    private String hint = "";

    private int arch = 10;

    public PasswordField() {
        initPasswordField();
    }

    public PasswordField(String hint) {
        if (hint != null) {
            this.hint = hint;
        }
        initPasswordField();
    }

    private void initPasswordField() {
        setBorder(null);
        setOpaque(false);
        setFont(new Font(UIManager.getFont("TextField.font").getFontName(), Font.PLAIN, 13));
        setSelectionColor(new Color(220, 204, 182));

        Button showButton = new Button("Show");
        showButton.setBorder(new EmptyBorder(0, 0, 1, 2));
        showButton.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.PLAIN, 13));
        showButton.setOpaque(false);
        showButton.setHoverable(false);
        showButton.addActionListener(new ActionListener() {
            private boolean showing = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (showing) {
                    setEchoChar('•'); // Mask password
                    showButton.setText("Show");
                } else {
                    setEchoChar((char) 0); // Unmask password
                    showButton.setText("Hide");
                }
                showing = !showing;
            }
        });
        setLayout(new BorderLayout());
        add(showButton, BorderLayout.EAST);
    }

    @Override
    public void setBorder(Border border) {
        super.setBorder(Objects.requireNonNullElseGet(border, () -> new EmptyBorder(5, 7, 5, 5)));
    }

    public void setHint(String hint) {
        if (hint != null) {
            this.hint = hint;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arch, arch);

        // Call super to draw text field content
        super.paintComponent(g);

        // Draw hint if the field is empty
        if (getText().isEmpty() && hint != null) {
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(Color.GRAY); // Hint color
            g2.drawString(hint, getInsets().left, (getHeight() / 2) + (g2.getFontMetrics().getAscent() / 2) - 2);
        }

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(175, 175, 175)); // Border color
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arch, arch);
        g2.dispose();
    }
}
