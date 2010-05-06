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


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import org.jdesktop.application.Application;

/**
 * Main class of MultiSnake
 * 
 * @author Patrick Hulin
 */
public class MultiSnake extends Application {
    public static final int BOARD_WIDTH = 21;
    public static final int BOARD_HEIGHT = 21;

    public static void main(String[] args) {
        Application.launch(MultiSnake.class, args);
    }

    public static MultiSnake getApplication() {
       return Application.getInstance(MultiSnake.class);
    }

    @Override
    protected void startup() {
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

        BoardCanvas bc = new BoardCanvas();
        JTable scoreBoard = new JTable(new ScoreBoardModel());

        setUpFrame(bc, scoreBoard);

        ClientGame clientGame = new ClientGame(bc, scoreBoard);

        clientGame.runGame(host, port);
    }

    public static void serverRun(int port, int tick) {
        GameSetup gameSetup = new GameSetup();
        gameSetup.setVisible(true);
    }

    public static JFrame setUpFrame(BoardCanvas bc, JTable scoreBoard) {
        scoreBoard.setFocusable(false);
        scoreBoard.setCellSelectionEnabled(false);
        scoreBoard.setColumnSelectionAllowed(false);
        scoreBoard.setEnabled(false);
        scoreBoard.setDragEnabled(false);

        TableColumn playerColumn = scoreBoard.getColumn("Player");
        playerColumn.setCellRenderer(new PlayerCellRenderer());

        JFrame mainFrame = new JFrame("MultiSnake");
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

        return mainFrame;
    }
}
