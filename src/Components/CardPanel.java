package Components;

import Objects.Main;

import javax.swing.*;
import java.awt.*;

public class CardPanel extends Panel {

    private final JLabel iconLabel = new JLabel();
    private final JLabel titleLabel = new JLabel();
    private final JLabel descriptionLabel = new JLabel();
    private final JLabel valuesLabel = new JLabel();

    private GroupLayout.Alignment alignment = GroupLayout.Alignment.CENTER;

    public CardPanel(ImageIcon icon, String text, String value, String description) {
        super(Main.getThemeColor1(), Main.getThemeColor2());
        iconLabel.setIcon(icon);
        titleLabel.setText(text);
        descriptionLabel.setText(description);
        valuesLabel.setText(value);
        initComponents();
    }

    public CardPanel(ImageIcon icon) {
        super(Main.getThemeColor1(), Main.getThemeColor2());
        iconLabel.setIcon(icon);
        initComponents();
    }

    public CardPanel() {
        super(Main.getThemeColor1(), Main.getThemeColor2());
        initComponents();
    }

    public void setIconLabel(ImageIcon icon) {
        iconLabel.setIcon(icon);
    }

    public void setTitleLabel(String text) {
        titleLabel.setText(text);
    }

    public void setDescriptionLabel(String text) {
        descriptionLabel.setText(text);
    }

    public void setValuesLabel(String text) {
        valuesLabel.setText(text);
    }

    public void setAlignment(GroupLayout.Alignment alignment) {
        this.alignment = alignment;
        setupLayout();
    }

    private void initComponents() {

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28)); // NOI18N
        titleLabel.setForeground(new Color(255, 255, 255));

        descriptionLabel.setFont(new Font("SansSerif", Font.BOLD, 12)); // NOI18N
        descriptionLabel.setForeground(new Color(220, 220, 220));

        valuesLabel.setFont(new Font("SansSerif", Font.ITALIC, 10)); // NOI18N
        valuesLabel.setForeground(new Color(220, 220, 220));

        setupLayout();
    }

    private void setupLayout() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        if (alignment == GroupLayout.Alignment.CENTER) {
            layout.setHorizontalGroup(
                    layout.createParallelGroup(alignment)
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap(100, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(alignment)
                                            .addComponent(valuesLabel)
                                            .addComponent(descriptionLabel)
                                            .addComponent(titleLabel)
                                            .addComponent(iconLabel))
                                    .addContainerGap(100, Short.MAX_VALUE))
            );
        } else {
            layout.setHorizontalGroup(
                    layout.createParallelGroup(alignment)
                            .addGroup(layout.createSequentialGroup()
                                    .addGap(20)
                                    .addGroup(layout.createParallelGroup(alignment)
                                            .addComponent(valuesLabel)
                                            .addComponent(descriptionLabel)
                                            .addComponent(titleLabel)
                                            .addComponent(iconLabel))
                                    .addContainerGap(100, Short.MAX_VALUE))
            );
        }
        layout.setVerticalGroup(
                layout.createParallelGroup(alignment)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(iconLabel)
                                .addGap(18, 18, 18)
                                .addComponent(titleLabel)
                                .addGap(18, 18, 18)
                                .addComponent(descriptionLabel)
                                .addGap(24, 24, 24)
                                .addComponent(valuesLabel)
                                .addContainerGap(25, Short.MAX_VALUE))
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.fillOval(getWidth() - (getHeight() / 2), 10, getHeight(), getHeight());
        g2d.fillOval(getWidth() - (getHeight() / 2) - 20, getHeight() / 2 + 20, getHeight(), getHeight());
    }
}
