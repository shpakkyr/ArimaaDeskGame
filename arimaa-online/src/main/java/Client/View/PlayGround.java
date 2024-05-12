package Client.View;

import Client.Model.Board;
import Client.Model.Offset2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlayGround extends JPanel {

    private GameBoardPanel gameBoardPanel;
    private JTextArea moveConsole;
    private JButton giveUpButton;

    public PlayGround() {
        setLayout(new BorderLayout());

        gameBoardPanel = new GameBoardPanel();
        moveConsole = new JTextArea();
        giveUpButton = new JButton("Give Up");
        add(gameBoardPanel, BorderLayout.CENTER);


        moveConsole.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveConsole);
        scrollPane.setPreferredSize(new Dimension(200, 400));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.add(giveUpButton, BorderLayout.SOUTH);
        add(leftPanel, BorderLayout.WEST);


        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(200, getHeight()));
        add(controlPanel, BorderLayout.EAST);

        giveUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameView.changeCurrentPanel(new WelcomePanel());
            }
        });
    }

    class GameBoardPanel extends JPanel {
        private final JPanel[][] tiles;

        public GameBoardPanel() {
            tiles = new JPanel[Board.DIMENSION][Board.DIMENSION];
            setLayout(new GridLayout(Board.DIMENSION, Board.DIMENSION));
            initializeBoard();
        }

        private void initializeBoard() {
            int tileSize = 70;
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JPanel clickedSquare = (JPanel) e.getSource();
                    Point point = clickedSquare.getLocation();
                    int row = point.y / tileSize;
                    int col = point.x / tileSize;
                    updateMoveConsole(row, col);
                }
            };

            for (int i = 0; i < Board.DIMENSION * Board.DIMENSION; i++) {
                JPanel square = new JPanel(new BorderLayout());
                square.setPreferredSize(new Dimension(tileSize, tileSize));
                int row = i / Board.DIMENSION;
                int col = i % Board.DIMENSION;
                tiles[row][col] = square;

                if(Offset2D.TRAP_OFFSET.contains(new Offset2D(row, col))){
                    square.setBackground(Color.BLACK);
                }else {
                    square.setBackground(Color.DARK_GRAY);
                }
                square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                square.addMouseListener(mouseAdapter);
                add(square);
            }
        }

        private void updateMoveConsole(int row, int col) {
            String message = String.format("Row %d, col %d\n", row, col);
            moveConsole.append(message);
            moveConsole.setCaretPosition(moveConsole.getDocument().getLength());
        }
    }
}