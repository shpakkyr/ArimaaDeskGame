package Client.View;

import Client.Model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PvPChangePanel extends JPanel {
    private GameModel game;
    private GameView view;
    public PvPChangePanel(GameModel game, GameView view){
        this.view = view;
        this.game = game;
        setLayout(new BorderLayout());

        JLabel PvPLabel = new JLabel("Local Multiplayer");
        PvPLabel.setHorizontalAlignment(JLabel.CENTER);
        PvPLabel.setVerticalAlignment(JLabel.CENTER);
        PvPLabel.setFont(new Font("Arial", Font.BOLD, 24));
        PvPLabel.setBorder(BorderFactory.createEmptyBorder(50,10,10,10));
        add(PvPLabel, BorderLayout.NORTH);

        JPanel PvPButtonMenu = new JPanel();
        PvPButtonMenu.setLayout(new BoxLayout(PvPButtonMenu,BoxLayout.Y_AXIS));
        PvPButtonMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        PvPButtonMenu.setBorder(BorderFactory.createEmptyBorder(80, 10, 10, 10));

        JButton NGButton = CommonMethods.createButton("New Game");
        JButton SButton = CommonMethods.createButton("Load Save");
        JButton ReturnButton = CommonMethods.createButton("Return");

        Dimension maxButtonSize = new Dimension(SButton.getMaximumSize().width+5, SButton.getMaximumSize().height);
        CommonMethods.setButtonSize(NGButton, maxButtonSize);
        CommonMethods.setButtonSize(SButton, maxButtonSize);
        CommonMethods.setButtonSize(ReturnButton, maxButtonSize);

        PvPButtonMenu.add(NGButton);
        PvPButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        PvPButtonMenu.add(SButton);
        PvPButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        PvPButtonMenu.add(ReturnButton);

        add(PvPButtonMenu, BorderLayout.CENTER);

        ReturnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameView.changeCurrentPanel(new WelcomePanel(game, view));
            }
        });
        NGButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showNewGameDialog(GameView.getWindow(), false);
            }
        });
        SButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.loadSave();
            }
        });

    }
}
