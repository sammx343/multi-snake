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
        int clientMode = -1;
        String host = "";
        int port = -1;
        int tick = 75;

        for(String s : args) {
            if(s.startsWith("--host")) {
                clientMode = 0;
            }
            if(s.startsWith("--server=")) {
                clientMode = 1;
                host = s.substring(9);
            }
            if(s.startsWith("--port=")) {
                Integer portI = new Integer(s.substring(7));
                port = portI.intValue();
            }
            if(s.startsWith("--tick=")) {
                Integer tickI = new Integer(s.substring(7));
                tick = tickI.intValue();
            }
        }

        if(clientMode == -1) {
            String message = "Do you want to host the game "
                    + "or connect to another?";
            String[] options = {"Host", "Connect"};
            int response = JOptionPane.showOptionDialog(null,
                                                   message,
                                                   "Choose Mode",
                                                   JOptionPane.DEFAULT_OPTION,
                                                   JOptionPane.QUESTION_MESSAGE,
                                                   null,
                                                   options,
                                                   options[0]);
            switch(response) {
                case 1:
                    clientMode = 1;
                    break;
                case 0:
                default:
                    clientMode = 0;
                    break;
            }
        }

        if(clientMode == 0)
            serverRun(port, tick);
        else if(clientMode == 1) {
            clientRun(host, port);
        }
        else
            assert false;
    }
    
    public static void clientRun(String host, int port) {
        if(host.equals(""))
            host = JOptionPane.showInputDialog(null,
                                               "Connect where?",
                                               "Enter Host",
                                               JOptionPane.QUESTION_MESSAGE);

        if(port == -1) {
            String portS = JOptionPane.showInputDialog(null,
                                                  "On what port?",
                                                  "Enter Port",
                                                  JOptionPane.QUESTION_MESSAGE);
            Integer portI = Integer.valueOf(portS);
            port = portI.intValue();
        }

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

    public static void serverRun(int port, int tick) {
        /*if(port == -1) {
            String portS = JOptionPane.showInputDialog(null,
                                                  "Host on what port?",
                                                  "Enter Port",
                                                  JOptionPane.QUESTION_MESSAGE);
            Integer portI = Integer.valueOf(portS);
            port = portI.intValue();
        }*/

        JFrame mainFrame = new JFrame("MultiSnake");

        KeyboardPlayer player1 = new KeyboardPlayer("Player 1",
                                                    KeyEvent.VK_UP,
                                                    KeyEvent.VK_RIGHT,
                                                    KeyEvent.VK_DOWN,
                                                    KeyEvent.VK_LEFT);
        NetworkPlayer player2 = new NetworkPlayer("Player 2", 10000);
        NetworkPlayer player3 = new NetworkPlayer("Player 3", 10001);
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

        Game game = new Game(players, bc, scoreBoard, 75);

        game.runGame();
    }
}
