package Components;

import Utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Header extends JPanel {

    public final Button menuButton = new Button(Images.getImage("MenuImg"));
    private final Button accountButton = new Button(Images.getImage("UserImg", 30, 30));
    private final Button cartButton = new Button(Images.getImage("CartImg", 30, 30));
    private final Button backButton = new Button(Images.getImage("BackImg", 20, 20));
    private final JPanel buttonPanel = new JPanel();
    private final JPanel buttonPanel2 = new JPanel();

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

        for (Button button : new Button[]{accountButton, cartButton, menuButton, backButton}) {
            button.setPreferredSize(new Dimension(40, 40));
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setOpaque(false);
        }

        buttonPanel.setLayout(new BorderLayout(5, 5));
        buttonPanel.setOpaque(false);

        buttonPanel2.setLayout(new BorderLayout(5, 5));
        buttonPanel2.setOpaque(false);

        add(buttonPanel, BorderLayout.EAST);
        add(buttonPanel2, BorderLayout.WEST);
    }

    public void addAccountButtonAction(ActionListener action) {
        buttonPanel.add(accountButton, BorderLayout.EAST);
        accountButton.addActionListener(action);
    }

    public void addCartButtonAction(ActionListener action) {
        buttonPanel.add(cartButton, BorderLayout.WEST);
        cartButton.addActionListener(action);
    }

    public void addMenuButtonAction(ActionListener action) {
        buttonPanel2.add(menuButton, BorderLayout.WEST);
        menuButton.addActionListener(action);
    }

    public void addBackButtonAction(ActionListener action) {
        buttonPanel2.add(backButton, BorderLayout.EAST);
        backButton.addActionListener(action);
    }
}