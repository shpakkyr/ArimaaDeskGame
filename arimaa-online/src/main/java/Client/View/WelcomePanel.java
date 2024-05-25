package Client.View;

import Client.Model.GameModel;
import Server.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * The WelcomePanel class represents the initial welcome screen for the game.
 * It provides buttons for various game modes, including versus computer, local multiplayer,
 * online multiplayer, viewing replays, and accessing rules and developer information.
 */
public class WelcomePanel extends JPanel {

    /**
     * Constructs a new WelcomePanel with the specified game model and game view.
     * This panel includes buttons for selecting different game modes and viewing game information.
     *
     * @param game The game model containing the game's state and logic.
     * @param view The game view managing the display and user interactions.
     */
    public WelcomePanel(GameModel game, GameView view) {
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Arimaa");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setVerticalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(50,10,10,10));
        add(welcomeLabel, BorderLayout.NORTH);


        JPanel welcomeButtonMenu = new JPanel();
        welcomeButtonMenu.setLayout(new BoxLayout(welcomeButtonMenu,BoxLayout.Y_AXIS));
        welcomeButtonMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeButtonMenu.setBorder(BorderFactory.createEmptyBorder(80, 10, 10, 10));

        JButton PvCButton = CommonMethods.createButton("Versus Computer");
        JButton PvPButton = CommonMethods.createButton("Local Multiplayer");
        JButton onlineButton = CommonMethods.createButton("Online Multiplayer");
        JButton playHistoryButton = CommonMethods.createButton("View Replay");
        JButton rulesButton = CommonMethods.createButton("Rules and Devs");

        Dimension maxButtonSize = new Dimension(onlineButton.getMaximumSize().width+5, onlineButton.getMaximumSize().height);
        CommonMethods.setButtonSize(PvCButton, maxButtonSize);
        CommonMethods.setButtonSize(PvPButton, maxButtonSize);
        CommonMethods.setButtonSize(onlineButton, maxButtonSize);
        CommonMethods.setButtonSize(playHistoryButton, maxButtonSize);
        CommonMethods.setButtonSize(rulesButton, maxButtonSize);

        welcomeButtonMenu.add(PvCButton);
        welcomeButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        welcomeButtonMenu.add(PvPButton);
        welcomeButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        welcomeButtonMenu.add(onlineButton);
        welcomeButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        welcomeButtonMenu.add(playHistoryButton);
        welcomeButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        welcomeButtonMenu.add(rulesButton);

        add(welcomeButtonMenu, BorderLayout.CENTER);

        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.changeCurrentPanel(new RulesPanel(game, view));
            }
        });

        PvCButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.changeCurrentPanel(new GameModePanel(game, view, true));
            }
        });

        PvPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.changeCurrentPanel(new GameModePanel(game, view, false));
            }
        });

        playHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.loadReplay();
            }
        });

        onlineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = JOptionPane.showInputDialog(view.getWindow(), "Enter your name:");
                // Show the new game dialog immediately
                new Thread(() -> {
                    try {
                        Client.main(new String[]{playerName}, view);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }).start();
            }
        });

    }
}
