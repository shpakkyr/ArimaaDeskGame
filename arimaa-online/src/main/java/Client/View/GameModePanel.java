package Client.View;

import Client.Model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GameModePanel is the panel displayed when selecting any of game modes.
 * It provides options to start a new game, load a saved game, or return to the welcome screen.
 */
public class GameModePanel extends JPanel {

    /**
     * Constructs a new PvCChangePanel with the specified game model and view.
     *
     * @param game The game model.
     * @param view The game view.
     */
    public GameModePanel(GameModel game, GameView view, boolean vsComputer){
        setLayout(new BorderLayout());

        // Title label
        JLabel welcomeLabel = new JLabel("Versus Computer");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setVerticalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(50,10,10,10));
        add(welcomeLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel PvCButtonMenu = new JPanel();
        PvCButtonMenu.setLayout(new BoxLayout(PvCButtonMenu,BoxLayout.Y_AXIS));
        PvCButtonMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        PvCButtonMenu.setBorder(BorderFactory.createEmptyBorder(80, 10, 10, 10));

        JButton NGButton = CommonMethods.createButton("New Game");
        JButton SButton = CommonMethods.createButton("Load Save");
        JButton ReturnButton = CommonMethods.createButton("Return");

        Dimension maxButtonSize = new Dimension(SButton.getMaximumSize().width+5, SButton.getMaximumSize().height);
        CommonMethods.setButtonSize(NGButton, maxButtonSize);
        CommonMethods.setButtonSize(SButton, maxButtonSize);
        CommonMethods.setButtonSize(ReturnButton, maxButtonSize);

        PvCButtonMenu.add(NGButton);
        PvCButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        PvCButtonMenu.add(SButton);
        PvCButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        PvCButtonMenu.add(ReturnButton);

        add(PvCButtonMenu, BorderLayout.CENTER);

        // Action listener for the Return button
        ReturnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameView.changeCurrentPanel(new WelcomePanel(game, view));
            }
        });

        // Action listener for the New Game button
        NGButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showNewGameDialog(GameView.getWindow(), vsComputer);
            }
        });

        // Action listener for the Load Save button
        SButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.loadSave(vsComputer);
            }
        });
    }
}
