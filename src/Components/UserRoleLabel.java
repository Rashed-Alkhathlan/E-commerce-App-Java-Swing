package Components;

import Enums.UserRole;

import java.awt.*;

public class UserRoleLabel extends StatusLabel {

    @Override
    protected void paintComponent(Graphics g) {
        if (status != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Paint gradient;
            if (status == UserRole.CUSTOMER) {
                gradient = new GradientPaint(0, 0, new Color(186, 123, 247), 0, getHeight(), new Color(167, 94, 236));
            } else if (status == UserRole.MANAGER) {
                gradient = new GradientPaint(0, 0, new Color(142, 142, 250), 0, getHeight(), new Color(123, 123, 245));
            } else {
                gradient = new GradientPaint(0, 0, new Color(241, 208, 62), 0, getHeight(), new Color(211, 184, 61));
            }
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arch, arch);
        }
        super.paintComponent(g);
    }
}
