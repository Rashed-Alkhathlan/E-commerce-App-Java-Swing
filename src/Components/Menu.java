package Components;

import GUI.MyFrame;
import Objects.Main;
import Utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Menu extends Panel {

    private Timer slideTimer;
    private final int extendedWidth = 250;
    private final int collapsedWidth = 50;
    private boolean isExpanded = false;
    private int numberOfComponents = 0;
    private final ArrayList<JSeparator> dividers = new ArrayList<>();
    private final HashMap<JLabel, JLabel> labels = new HashMap<>();

    private final JLabel extendedLogoLabel = new JLabel(Images.getImage("LargeLogoImg", 215, 75), JLabel.CENTER);;
    private final JLabel collapsedLogoLabel = new JLabel(Images.getImage("SmallLogoImg", 60, 60), JLabel.CENTER);

    public Menu() {
        super(Main.getThemeColor1(), Main.getThemeColor2());
        initMenu();
    }

    private void initMenu() {
        setLayout(null);
        setPreferredSize(new Dimension(isExpanded ? extendedWidth : collapsedWidth, 0));

        slideTimer = new Timer(10, e -> sliding());

        extendedLogoLabel.setBounds(15, 10, 215, 75);
        collapsedLogoLabel.setBounds(0, 25, 50, 50);

        extendedLogoLabel.setVisible(false);
        add(extendedLogoLabel);
        add(collapsedLogoLabel);

        setDoubleBuffered(true);
    }

    private void sliding() {
        int width = this.getWidth();

        if (width + 20 >= extendedWidth && isExpanded || width - 20 <= collapsedWidth && !isExpanded) {
            MyFrame.setInAnimation(false);
        }

        if (isExpanded && width < extendedWidth) {
            setPreferredSize(new Dimension(width + 20, 0));
        } else if (!isExpanded && width > collapsedWidth) {
            setPreferredSize(new Dimension(width - 20, 0));
        } else {
            if (isExpanded) {
                setPreferredSize(new Dimension(extendedWidth, 0));
            } else {
                setPreferredSize(new Dimension(collapsedWidth, 0));
            }
            slideTimer.stop();
            updateLogos();
        }

        revalidate();
    }

    private void updateLogos() {
        if (isExpanded) {
            extendedLogoLabel.setVisible(true);
            collapsedLogoLabel.setVisible(false);
            for (JSeparator divider : dividers) {
                divider.setBounds(15, divider.getY(), extendedWidth - 30, divider.getHeight());
            }
            for (Map.Entry<JLabel, JLabel> label : labels.entrySet()) {
                label.getKey().setVisible(true);
                label.getValue().setVisible(false);
            }
        } else {
            extendedLogoLabel.setVisible(false);
            collapsedLogoLabel.setVisible(true);
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
        MyFrame.setInAnimation(true);
        slideTimer.start();
    }

    public void setExpandedStatus(boolean isExpanded) {
        this.isExpanded = isExpanded;
        setPreferredSize(new Dimension(isExpanded ? extendedWidth : collapsedWidth, 0));
        updateLogos();
    }

    @Override
    public void removeAll() {
        int turn = 0;
        for (Component component : getComponents()) {
            if (component instanceof MenuButton || component instanceof JSeparator) {
                remove(component);
                numberOfComponents--;
            }
            else if (component instanceof JLabel && (component != extendedLogoLabel && component != collapsedLogoLabel)){
                remove(component);
                turn++;
                if (turn == 2) {
                    numberOfComponents--;
                    turn = 0;
                }
            }
        }
    }

    public void addButton(String name, String icon, ActionListener action) {
        MenuButton button = new MenuButton(name, icon);
        button.setBounds(10, 100 + (numberOfComponents * 50) - 25, extendedWidth - 20, 45);
        button.addActionListener(action);
        add(button);
        numberOfComponents++;
        updateLogos();
    }

    public void addDivider() {
        JSeparator divider = new JSeparator(SwingConstants.HORIZONTAL);
        divider.setBounds(15, 100 + (numberOfComponents * 50), extendedWidth - 30, 5);
        add(divider);
        dividers.add(divider);
        numberOfComponents++;
        updateLogos();
    }

    public void addLabel(String text) {
        addLabel(text, "LabelImg");
    }

    public void addLabel(String text, String icon) {
        JLabel label = new JLabel(text);
        label.setBounds(15, 75 + (numberOfComponents * 50), extendedWidth - 30, 30);

        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 17));
        JLabel iconLabel = new JLabel(Images.getImage(icon));
        iconLabel.setBounds(15, 80 + (numberOfComponents * 50), 20, 20);

        add(label);
        add(iconLabel);
        labels.put(label, iconLabel);
        numberOfComponents++;
        updateLogos();
    }

    private static class MenuButton extends Button {

        private final String name;
        private final String icon;

        public MenuButton(String name, String icon) {
            this.name = name;
            this.icon = icon;
            initButton();
        }

        private void initButton() {
            setBorder(new EmptyBorder(5, 5, 5, 5));
            setLayout(new BorderLayout(0, 0));

            setOpaque(false);

            JLabel imgLabel = new JLabel(Images.getImage(icon, 20, 20));
            JLabel label = new JLabel("    " + name);

            label.setFont(new Font("SansSerif", Font.BOLD, 15));
            label.setForeground(Color.WHITE);

            add(imgLabel, BorderLayout.WEST);
            add(label, BorderLayout.CENTER);
        }
    }
}
