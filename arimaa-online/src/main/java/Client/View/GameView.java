package Client.View;

import Client.Controller.GameController;
import Client.Model.Board;
import Client.Model.GameModel;

import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel implements Runnable{
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 800;

    private GameController controller;
    final int FPS = 60;
    Thread gameThread;
    private final Board board = new Board();

    public GameView(GameController controller) {
        this.controller = controller;
    }

    public void init() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.ORANGE);
        JFrame window = new JFrame("Text to be displayed on top");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(this);
        window.pack();
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        this.launchGame();
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    //method run must create a game loop
    //the game loop is a sequence of processes that run continuously as long as the game is running
    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime)/drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    //Update the information such as piece's X and Y position, or the number of the pieces left on board
    private void update(){
        //TODO
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        board.drawBoard(g2);
    }

}
