package Components;

import GUI.MyFrame;
import Utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PopupMessage extends Panel {

    private static int popupNumber = 0;
    private int popupNum = popupNumber;

    public enum Type {
        ERROR, WARNING, SUCCESS
    }

    int width = 300;
    int height = 40;

    public PopupMessage(String message, Type type) {
        popupNumber++;
        setLayout(new BorderLayout(5, 0));
        setArch(10);
        setBorderPainted(true);
        setBorder(new EmptyBorder(5, 5, 5, 5));

        // Message Label
        JLabel messageLabel = new JLabel("<html>" + message + "</html>", SwingConstants.LEFT);
        messageLabel.setFont(new Font(UIManager.getFont("Label.font").getFontName(), Font.BOLD, 14));
        messageLabel.setOpaque(false);

        JLabel icon;
        if (type == Type.ERROR) {
            setBackground(new Color(255, 182, 193));
            setBorderColor(new Color(255, 0 , 0));
            icon = new JLabel(Images.getImage("ErrorImg", 25, 25));
            messageLabel.setForeground(new Color(150, 0, 0));
        } else if (type == Type.WARNING) {
            setBackground(new Color(173, 216, 230));
            setBorderColor(new Color(0, 100, 139));
            icon = new JLabel(Images.getImage("InfoImg", 25, 25));
            messageLabel.setForeground(new Color(0, 0, 150));
        } else {
            setBackground( new Color(230, 255, 230));
            setBorderColor(new Color(0, 255 , 0));
            icon = new JLabel(Images.getImage("SuccessImg", 25, 25));
            messageLabel.setForeground(new Color(0, 150, 0));
        }


        add(messageLabel, BorderLayout.CENTER);
        add(icon, BorderLayout.WEST);
        showOn(MyFrame.getFrame());
    }

    public void showOn(JFrame parentFrame) {
        JLayeredPane layeredPane = parentFrame.getLayeredPane();

        // Add to layered pane for overlay effect
        layeredPane.add(this, JLayeredPane.POPUP_LAYER);

        // Set bounds at the top center
        setBounds((parentFrame.getWidth() - width) / 2, -height, width, height);
        layeredPane.revalidate();
        layeredPane.repaint();

        // Animate the panel sliding down
        slideDown(layeredPane);
    }

    private void slideDown(JLayeredPane layeredPane) {
        Timer timer = new Timer(10, null); // Timer for animation
        timer.addActionListener(e -> {
            Point currentLocation = getLocation();
            if (currentLocation.y < 5 + (popupNum * (height + 5))) {
                setLocation(currentLocation.x, Math.min(5 + (popupNum * (height + 5)), currentLocation.y + 5));
            } else {
                timer.stop();
                Delay(layeredPane);
            }
        });
        timer.start();
    }

    private void slideUp(JLayeredPane layeredPane) {
        Timer timer = new Timer(10, null); // Timer for animation
        timer.addActionListener(e -> {
            Point currentLocation = getLocation();
            if (currentLocation.y > -height - 5) {
                setLocation(currentLocation.x, Math.max(-height - 5, currentLocation.y - 5));
            } else {
                timer.stop();
                popupNumber--;
                layeredPane.remove(this);
                for (Component component : layeredPane.getComponents()) {
                    if (component instanceof PopupMessage) {
                        ((PopupMessage) component).popupNum--;
                        ((PopupMessage) component).updateLocation();
                    }
                }
                layeredPane.repaint();
            }
        });
        timer.start();
    }

    private void updateLocation() {
        Timer timer = new Timer(10, null); // Timer for animation
        timer.addActionListener(e -> {
            Point currentLocation = getLocation();
            if (currentLocation.y > 5 + (popupNum * (height + 5))) {
                setLocation(currentLocation.x, Math.max(5, currentLocation.y - 5));
            } else {
                timer.stop();
            }
        });
        timer.start();
    }

    private void Delay(JLayeredPane layeredPane) {
        Timer disposeTimer = new Timer(5000, e -> {
            slideUp(layeredPane);
        });
        disposeTimer.setRepeats(false);
        disposeTimer.start();
    }
}