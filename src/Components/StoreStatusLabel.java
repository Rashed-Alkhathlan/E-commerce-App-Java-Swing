package Components;

import Enums.StoreStatus;

import java.awt.*;

public class StoreStatusLabel extends StatusLabel {

    @Override
    protected void paintComponent(Graphics g) {
        if (status != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Paint gradient;
            if (status == StoreStatus.OPEN){
                gradient = new GradientPaint(0, 0, new Color(100, 208, 62), 0, getHeight(), new Color(70, 184, 61));
            } else {
                gradient = new GradientPaint(0, 0, new Color(255, 60, 100), 0, getHeight(), new Color(240, 94, 70));
            }
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arch, arch);
        }
        super.paintComponent(g);
    }
}
