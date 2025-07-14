package Client.View;

import Client.Model.GameModel;
import Client.Model.GameState;
import Server.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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

    /**
     * Method is used to send the gameState to server when the step is made by the player, so that another player can update his view
     *
     * @param game GameModel object presenting the model part
     * @param client Client that is sending the gameState
     */
    protected static void sendGameStateToServer(GameModel game, Client client) {
        if (client != null) {
            GameState gameState = game.saveState(1, false);
            try {
                client.sendObjectToEnemy(gameState);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
