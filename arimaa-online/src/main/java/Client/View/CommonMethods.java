package Client.View;

import javax.swing.*;
import java.awt.*;

public class CommonMethods extends JPanel {
    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    public static void setButtonSize(JButton button, Dimension size) {
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
    }
}
