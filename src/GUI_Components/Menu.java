package GUI_Components;

import Utils.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Menu extends JPanelGradiant {

    private Timer slideTimer;
    private final int menuWidth = 250;
    private static int currentWidth = 50;
    private static boolean isExpanded = false;
    private int numberOfComponents = 0;
    private final ArrayList<JSeparator> dividers = new ArrayList<>();
    private final HashMap<JLabel, JLabel> labels = new HashMap<>();

    JLabel openLogoLabel = new JLabel(Images.getImage("LargeLogoImg", 240, 80));;
    JLabel closeLogoLabel= new JLabel(Images.getImage("SmallLogoImg", 80, 80));

    public Menu(String firstColor, String secondColor, String thirdColor) {
        super(firstColor, secondColor, thirdColor);
        initPanel();
    }

    public Menu(String firstColor, String secondColor) {
        super(firstColor, secondColor);
        initPanel();
    }

    private void initPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(currentWidth, 0));

        slideTimer = new Timer(10, e -> sliding());

        openLogoLabel.setBounds(5, 5, 240, 80);
        closeLogoLabel.setBounds(0, 25, 50, 50);
        openLogoLabel.setVisible(false);
        add(openLogoLabel);
        add(closeLogoLabel);

    }

    private void sliding() {
        int width = this.getWidth();

        if (isExpanded && width < menuWidth) {
            setPreferredSize(new Dimension(width + 20, 0));
        } else if (!isExpanded && width > 50) {
            setPreferredSize(new Dimension(width - 20, 0));
        } else {
            currentWidth = getWidth();
            updateLogos();
            slideTimer.stop();
        }
        revalidate();
        repaint();
    }

    private void updateLogos() {
        if (isExpanded) {
            openLogoLabel.setVisible(true);
            closeLogoLabel.setVisible(false);
            for (JSeparator divider : dividers) {
                divider.setBounds(15, divider.getY(), menuWidth - 30, divider.getHeight());
            }
            for (Map.Entry<JLabel, JLabel> label : labels.entrySet()) {
                label.getKey().setVisible(true);
                label.getValue().setVisible(false);
            }
        } else {
            openLogoLabel.setVisible(false);
            closeLogoLabel.setVisible(true);
            for (JSeparator divider : dividers) {
                divider.setBounds(10, divider.getY(), 30, divider.getHeight());
            }
            for (Map.Entry<JLabel, JLabel> label : labels.entrySet()) {
                label.getKey().setVisible(false);
                label.getValue().setVisible(true);
            }
        }
    }

    public void slide() {
        isExpanded = !isExpanded;
        if (isExpanded) {updateLogos();}
        slideTimer.start();
    }

    public void addButton(int index, String name, String icon, ActionListener action) {
        MenuButton button = new MenuButton(name, icon);
        button.setBounds(10, 100 + (numberOfComponents * 50) - 25, menuWidth - 20, 45);
        button.addActionListener(action);
        add(button);
        numberOfComponents++;
        updateLogos();
    }

    public void addDivider() {
        JSeparator divider = new JSeparator(SwingConstants.HORIZONTAL);
        divider.setBounds(15, 100 + (numberOfComponents * 50), menuWidth - 30, 5);
        add(divider);
        dividers.add(divider);
        numberOfComponents++;
        updateLogos();
    }

    public void addLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBounds(15, 75 + (numberOfComponents * 50), menuWidth - 30, 30);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 17));

        JLabel icon = new JLabel(Images.getImage("LabelImg"));
        icon.setBounds(15, 80 + (numberOfComponents * 50), 20, 20);

        add(label);
        add(icon);
        labels.put(label, icon);
        numberOfComponents++;
        updateLogos();
    }
}
