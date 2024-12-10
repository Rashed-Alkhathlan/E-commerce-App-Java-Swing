package Components;

import javax.swing.*;
import java.awt.*;

public class StatusLabel extends JLabel {
    protected Object status;
    protected int arch = 5;

    public StatusLabel() {
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void setArch(int arch) {
        this.arch = arch;
    }

    public void setStatus(Object status) {
        this.status = status;
        setText(status.toString());
        repaint();
    }
}
