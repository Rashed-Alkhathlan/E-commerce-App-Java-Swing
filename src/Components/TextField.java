package Components;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class TextField extends JTextField {

    private String hint = "";

    private int arch = 10;

    private boolean capitalizeFirstLetter = true;
    private boolean firstChange = true;

    public TextField() {
        initTextField();
    }

    public TextField(String hint) {
        if (hint != null) {
            this.hint = hint;
        }
        initTextField();
    }

    private void initTextField() {
        //setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(175, 175, 175), 1), new EmptyBorder(5, 5, 5, 5)));
        setBorder(null);
        setOpaque(false);
        setFont(new Font("SansSerif", Font.PLAIN, 13));
        setSelectionColor(new Color(220, 204, 182));

        getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = getText();
                if (firstChange && text.length() == 1) {
                    SwingUtilities.invokeLater(() -> setText(text.toUpperCase()));
                    firstChange = false;
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {}

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                firstChange = capitalizeFirstLetter;
            }
        });
    }

    @Override
    public void setBorder(Border border) {
        super.setBorder(Objects.requireNonNullElseGet(border, () -> new EmptyBorder(5, 7, 5, 5)));
    }

    public void capitalizeFirstLetter(boolean capitalizeFirstLetter) {
        this.capitalizeFirstLetter = capitalizeFirstLetter;
        firstChange = capitalizeFirstLetter;
    }

    public void setHint(String hint) {
        if (hint != null) {
            this.hint = hint;
        }
    }

    public void setOnlyDouble() {
        ((AbstractDocument) getDocument()).setDocumentFilter(new DoubleDocumentFilter());
    }

    public void setOnlyInt() {
        ((AbstractDocument) getDocument()).setDocumentFilter(new NumericDocumentFilter());
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

    private static class DoubleDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) {
                return;
            }
            String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
            if (isValidDouble(newText)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) {
                return;
            }
            String oldText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = oldText.substring(0, offset) + string + oldText.substring(offset + length);
            if (isValidDouble(newText)) {
                super.replace(fb, offset, length, string, attr);
            }
        }

        @Override
        public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
            String oldText = fb.getDocument().getText(0, fb.getDocument().getLength());
            String newText = oldText.substring(0, offset) + oldText.substring(offset + length);
            if (isValidDouble(newText)) {
                super.remove(fb, offset, length);
            }
        }

        private boolean isValidDouble(String text) {
            // Allow empty text
            if (text.isEmpty()) {
                return true;
            }

            if (!text.matches("-?\\d*\\.?\\d*")) {
                return false;
            }

            // Check if the text is a valid double or integer
            try {
                Double.parseDouble(text);
                // Ensure only one decimal point is allowed
                return text.chars().filter(ch -> ch == '.').count() <= 1;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    private static class NumericDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) {
                return;
            }
            if (string.matches("\\d*")) { // Allow only digits
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
            if (string == null) {
                return;
            }
            if (string.matches("\\d*")) { // Allow only digits
                super.replace(fb, offset, length, string, attr);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }
    }
}
