package Client.View;

import Client.Model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PvCChangePanel extends JPanel {
    private GameModel game;
    private GameView view;
    public PvCChangePanel(GameModel game, GameView view){
        this.view = view;
        this.game = game;
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Versus Computer");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeLabel.setVerticalAlignment(JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(50,10,10,10));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel PvCButtonMenu = new JPanel();
        PvCButtonMenu.setLayout(new BoxLayout(PvCButtonMenu,BoxLayout.Y_AXIS));
        PvCButtonMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        PvCButtonMenu.setBorder(BorderFactory.createEmptyBorder(80, 10, 10, 10));

        JButton NGButton = CommonMethods.createButton("New Game");
        JButton S1Button = CommonMethods.createButton("Load Save 1");
        JButton S2Button = CommonMethods.createButton("Load Save 2");
        JButton S3Button = CommonMethods.createButton("Load Save 3");
        JButton ReturnButton = CommonMethods.createButton("Return");

        Dimension maxButtonSize = new Dimension(S1Button.getMaximumSize().width+5, S1Button.getMaximumSize().height);
        CommonMethods.setButtonSize(NGButton, maxButtonSize);
        CommonMethods.setButtonSize(S1Button, maxButtonSize);
        CommonMethods.setButtonSize(S2Button, maxButtonSize);
        CommonMethods.setButtonSize(S3Button, maxButtonSize);
        CommonMethods.setButtonSize(ReturnButton, maxButtonSize);

        PvCButtonMenu.add(NGButton);
        PvCButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        PvCButtonMenu.add(S1Button);
        PvCButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        PvCButtonMenu.add(S2Button);
        PvCButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        PvCButtonMenu.add(S3Button);
        PvCButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        PvCButtonMenu.add(ReturnButton);

        add(PvCButtonMenu, BorderLayout.CENTER);

        ReturnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameView.changeCurrentPanel(new WelcomePanel(game, view));
            }
        });
        NGButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameView.changeCurrentPanel(view.getBoardPanel());
            }
        });
    }
}
