package Components;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class TextField extends JTextField {

    private String hint = "";

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
        setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(175, 175, 175), 1), new EmptyBorder(5, 5, 5, 5)));
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
        super.setBorder(Objects.requireNonNullElseGet(border, () -> new EmptyBorder(5, 5, 5, 5)));
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
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (getText().isEmpty()) {
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int m = 0xfefefefe;
            int blendedColor = ((getBackground().getRGB() & m) >>> 1) + ((getForeground().getRGB() & m) >>> 1);
            g2d.setColor(new Color(blendedColor, true));
            g2d.drawString(hint, getInsets().left, ((getHeight() / 2) + (g2d.getFontMetrics().getAscent() / 2) - 2));
        }
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
