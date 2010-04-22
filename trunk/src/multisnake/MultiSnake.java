/*
 * MultiSnake.java
 */

package multisnake;

import java.util.LinkedList;
import java.awt.event.KeyEvent;
import java.awt.*;
import javax.swing.*;

/**
 * The main class of the application.
 */
public class MultiSnake{
    public static final int BOARD_WIDTH = 21;
    public static final int BOARD_HEIGHT = 21;

    public static void main(String[] args) {
        boolean clientMode = false;
        String host = "";
        int port = -1;

        for(String s : args) {
            if(s.startsWith("--server=")) {
                clientMode = true;
                host = s.substring(9);
            }
            if(s.startsWith("--port=")) {
                Integer portI = new Integer(s.substring(7));
                port = portI.intValue();
            }
        }

        if(!clientMode)
            serverRun();
        else
            clientRun(host, port);
    }
    
    public static void clientRun(String host, int port) {
        if((host.equals("")) || (port == -1)) {
            System.out.println("Must include host and port in client mode.");
            System.exit(1);
        }

        System.out.println("connecting to " + host + " at port " + port);

        JFrame mainFrame = new JFrame("MultiSnake");

        BoardCanvas bc = new BoardCanvas();

        JTable scoreBoard = new JTable(new ScoreBoardModel());
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

        ClientGame clientGame = new ClientGame(bc, scoreBoard);

        clientGame.runGame(host, port);
    }

    public static void serverRun() {
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
        NetworkPlayer player3 = new NetworkPlayer("Player 3",
                                                  10000);
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

        Game game = new Game(players, bc, scoreBoard);

        game.runGame();
    }
}
