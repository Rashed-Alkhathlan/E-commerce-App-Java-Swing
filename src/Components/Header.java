package Components;

import Utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class Header extends JPanel {

    public final Button menuButton = new Button(Images.getImage("MenuImg"));
    private final Button accountButton = new Button(Images.getImage("UserImg", 30, 30));
    private final Button cartButton = new Button(Images.getImage("CartImg", 30, 30));
    private final JPanel buttonPanel = new JPanel();

    public Header() {
        setBackground(Color.WHITE);
        initPanel();
    }

    private void initPanel() {
        setLayout(new BorderLayout(0, 5));
        setPreferredSize(new Dimension(0, 50));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        Buttons();
    }

    private void Buttons() {

        for (Button button : new Button[]{accountButton, cartButton, menuButton}) {
            button.setPreferredSize(new Dimension(40, 40));
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setOpaque(false);
        }

        buttonPanel.setLayout(new GridLayout(1 , 2, 5, 5));
        buttonPanel.setOpaque(false);

        add(buttonPanel, BorderLayout.EAST);
    }

    public void addAccountButtonAction(ActionListener action) {
        buttonPanel.add(accountButton);
        accountButton.addActionListener(action);
    }

    public void addCartButtonAction(ActionListener action) {
        buttonPanel.add(cartButton);
        cartButton.addActionListener(action);
    }

    public void addMenuButtonAction(ActionListener action) {
        add(menuButton, BorderLayout.WEST);
        menuButton.addActionListener(action);
    }
}