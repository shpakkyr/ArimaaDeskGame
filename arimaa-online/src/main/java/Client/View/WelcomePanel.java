package Client.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePanel extends JPanel {

    public WelcomePanel() {
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

        JButton PvCButton = createButton("Versus Computer");
        JButton PvPButton = createButton("Local Multiplayer");
        JButton onlineButton = createButton("Online Multiplayer");
        JButton playHistoryButton = createButton("View Replays");
        JButton rulesButton = createButton("Rules and Devs");

        Dimension maxButtonSize = new Dimension(onlineButton.getMaximumSize().width+5, onlineButton.getMaximumSize().height);
        setButtonSize(PvCButton, maxButtonSize);
        setButtonSize(PvPButton, maxButtonSize);
        setButtonSize(onlineButton, maxButtonSize);
        setButtonSize(playHistoryButton, maxButtonSize);
        setButtonSize(rulesButton, maxButtonSize);

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
                GameView.changeCurrentPanel(new RulesPanel());
            }
        });

        PvCButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameView.changeCurrentPanel(new PvCChangePanel());
            }
        });

        PvPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameView.changeCurrentPanel(new PvPChangePanel());
            }
        });

    }
    private void setButtonSize(JButton button, Dimension size) {
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }
}
