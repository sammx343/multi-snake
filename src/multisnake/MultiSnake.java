/*
 *  Copyright (C) 2010 Patrick Hulin
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package multisnake;

import java.util.LinkedList;
import java.awt.event.KeyEvent;
import java.awt.*;
import javax.swing.*;

/**
 * Main class of MultiSnake
 * 
 * @author Patrick Hulin
 */
public class MultiSnake implements Runnable {
    public static final int BOARD_WIDTH = 21;
    public static final int BOARD_HEIGHT = 21;

    public void run() {
        boolean clientMode = false;
        String host = "";
        int port = -1;
        int tick = 75;

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
                clientMode = true;
                break;
            case 0:
            default:
                clientMode = false;
                break;
        }

        if(!clientMode)
            serverRun(port, tick);
        else
            clientRun(host, port);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MultiSnake());
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

        KeyboardPlayer player1 = new KeyboardPlayer("Player 1");
        NetworkPlayer player2 = new NetworkPlayer("Player 2", 10000);
        //NetworkPlayer player3 = new NetworkPlayer("Player 3", 10001);
        LinkedList<Player> players = new LinkedList<Player>();
        players.add(player1);
        players.add(player2);
        //players.add(player3);

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
