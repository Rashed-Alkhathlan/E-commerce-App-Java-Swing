package GUI_Components;

import Utils.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static GUI.Page.switchToPageWhenPressed;

public class Header extends JPanelGradiant {

    public final JButton menuButton = new JButton(Images.getImage("MenuImg"));
    private final JButton accountButton = new JButton(Images.getImage("UserImg", 30, 30));
    private final JButton cartButton = new JButton(Images.getImage("CartImg", 30, 30));
    private final JPanel buttonPanel = new JPanel();

    public Header(String firstColor, String secondColor) {
        super(firstColor, secondColor);
        initPanel();
    }

    private void initPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.decode("#ffffff"));
        this.setPreferredSize(new Dimension(0, 50));
        Buttons();
    }

    private void Buttons() {

        buttonPanel.setLayout(new GridLayout(1 , 2, 5, 5));
        buttonPanel.setOpaque(false);

        for (JButton button : new JButton[]{accountButton, cartButton, menuButton}) {
            button.setPreferredSize(new Dimension(40, 40));
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setContentAreaFilled(false);
        }

        switchToPageWhenPressed(cartButton, "CartPage");
        switchToPageWhenPressed(accountButton, "AccountPage");

        buttonPanel.add(cartButton);
        buttonPanel.add(accountButton);

        this.add(menuButton, BorderLayout.WEST);
        this.add(buttonPanel, BorderLayout.EAST);
    }

    public void addMenuButtonAction(ActionListener action) {
        menuButton.addActionListener(action);
    }
}