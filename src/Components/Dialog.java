package Components;

import GUI.MyFrame;
import Utils.Images;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class Dialog extends JDialog {
    private final Button yesButton = new Button("Yes");
    private final Button noButton = new Button("No");

    public Dialog(String name, String text) {
        setSize(400, 200);
        setLayout(new BorderLayout());
        setLocationRelativeTo(MyFrame.getFrame());
        setResizable(false);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        setAlwaysOnTop(true);
        setTitle(name);

        noButton.setPreferredSize(new Dimension(70, 30));
        yesButton.setPreferredSize(new Dimension(70, 30));

        // Add components to the dialog
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel messageLabel = new JLabel(text, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        textPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        add(textPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void addYesButtonAction(ActionListener action) {
        yesButton.addActionListener(action);
    }

    public void addNoButtonAction(ActionListener action) {
        noButton.addActionListener(action);
    }
}
