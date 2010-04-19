/*
 * MultiSnake.java
 */

package multisnake;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import java.util.LinkedList;
import java.awt.event.KeyEvent;
import java.awt.*;
import javax.swing.*;

/**
 * The main class of the application.
 */
public class MultiSnake extends SingleFrameApplication {
    public static final int BOARD_WIDTH = 11;
    public static final int BOARD_HEIGHT = 11;

    private Game game;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {        
        JFrame mainFrame = new JFrame("MultiSnake");

        KeyboardPlayer player1 = new KeyboardPlayer("Player 1",
                                                    KeyEvent.VK_UP,
                                                    KeyEvent.VK_RIGHT,
                                                    KeyEvent.VK_DOWN,
                                                    KeyEvent.VK_LEFT);
        KeyboardPlayer player2 = new KeyboardPlayer("Player 2",
                                                    KeyEvent.VK_W,
                                                    KeyEvent.VK_D,
                                                    KeyEvent.VK_S,
                                                    KeyEvent.VK_A);
        KeyboardPlayer player3 = new KeyboardPlayer("Player 3",
                                                    KeyEvent.VK_I,
                                                    KeyEvent.VK_L,
                                                    KeyEvent.VK_K,
                                                    KeyEvent.VK_J);

        LinkedList<Player> players = new LinkedList<Player>();
        players.add(player1);
        players.add(player2);
        players.add(player3);

        BoardCanvas bc = new BoardCanvas();

        JTable scoreBoard = new JTable(new ScoreBoardModel(players));
        scoreBoard.setFocusable(false);

        mainFrame.setLayout(new FlowLayout());
        mainFrame.add(bc);

        Container container = new Container();
        container.setLayout(new BorderLayout());
        container.add(scoreBoard.getTableHeader(), BorderLayout.PAGE_START);
        container.add(scoreBoard, BorderLayout.CENTER);
        mainFrame.add(container);

        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        bc.requestFocusInWindow();

        game = new Game(players, bc, scoreBoard);

        game.runGame();
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of MultiSnake
     */
    public static MultiSnake getApplication() {
        return Application.getInstance(MultiSnake.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(MultiSnake.class, args);
    }
}
