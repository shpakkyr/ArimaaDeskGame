package Client.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PvPChangePanel extends JPanel {
    public PvPChangePanel(){
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

        PvPButtonMenu.add(NGButton);
        PvPButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        PvPButtonMenu.add(S1Button);
        PvPButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        PvPButtonMenu.add(S2Button);
        PvPButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        PvPButtonMenu.add(S3Button);
        PvPButtonMenu.add(Box.createRigidArea(new Dimension(0, 15)));
        PvPButtonMenu.add(ReturnButton);

        add(PvPButtonMenu, BorderLayout.CENTER);

        ReturnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameView.changeCurrentPanel(new WelcomePanel());
            }
        });
        NGButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameView.changeCurrentPanel(new PlayGround());
            }
        });
    }
}
