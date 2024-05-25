package Client.View;

import javax.swing.*;
import java.awt.*;

/**
 * CommonMethods is a utility class that provides common UI methods for creating and configuring buttons for main menu and sub menu.
 */
public class CommonMethods{

    /**
     * Creates a JButton with the specified text and centers its alignment.
     *
     * @param text The text to be displayed on the button.
     * @return A JButton with the specified text and centered alignment.
     */
    protected static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    /**
     * Sets the preferred, maximum, and minimum size of the specified button to the given dimension.
     *
     * @param button The button to be resized.
     * @param size   The dimension to set as the size for the button.
     */
    protected static void setButtonSize(JButton button, Dimension size) {
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
    }
}
