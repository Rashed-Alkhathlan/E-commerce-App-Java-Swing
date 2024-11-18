package GUI_Components;

import Utils.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuButton extends JButton {
    private final String name;
    private final String icon;

    private final Color transparentColor = new Color(0, 0, 0, 0);
    private final Color hoverColor = new Color(0, 0, 0, 75);

    public MenuButton(String name, String icon) {
        this.name = name;
        this.icon = icon;
        initButton();
    }

    private void initButton() {
        setMargin(new Insets(5, 5, 5, 5));
        setLayout(new BorderLayout(0, 0));

        JLabel imgLabel = new JLabel(Images.getImage(icon, 20, 20));
        JLabel label = new JLabel("    " + name);

        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setForeground(Color.WHITE);

        setBackground(transparentColor);
        setFocusPainted(false);
        setFocusable(false);
        setBorderPainted(false);

        add(imgLabel, BorderLayout.WEST);
        add(label, BorderLayout.CENTER);
        hoverAdapter();
    }

    private void hoverAdapter() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(transparentColor);
            }
        });
    }
}
