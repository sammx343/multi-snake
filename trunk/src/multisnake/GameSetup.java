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

import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Patrick Hulin
 */
public class GameSetup extends JFrame implements ActionListener {
    private DefaultListModel listModel;
    private JList list;

    public GameSetup() {
        super("Game Setup");

        listModel = new DefaultListModel();
        list = new JList(listModel);

        Player defaultPlayer = new KeyboardPlayer("Player 1");
        listModel.addElement(defaultPlayer);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");
        JButton doneButton = new JButton("Done");
        addButton.setActionCommand("add");
        removeButton.setActionCommand("remove");
        doneButton.setActionCommand("done");
        addButton.addActionListener(this);
        removeButton.addActionListener(this);
        doneButton.addActionListener(this);

        GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(200, 200));

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addComponent(scrollPane)
                .addGroup(layout.createParallelGroup()
                    .addComponent(addButton)
                    .addComponent(removeButton)
                    .addComponent(doneButton)));

        layout.linkSize(SwingConstants.HORIZONTAL, addButton, removeButton, doneButton);

        layout.setVerticalGroup(
                layout.createParallelGroup()
                .addComponent(scrollPane)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(addButton)
                    .addComponent(removeButton)
                    .addComponent(doneButton)));

        pack();
    }

    public List<Player> getPlayers() {
        Enumeration playersEnum = listModel.elements();
        List<Player> players = new LinkedList<Player>();

        while(playersEnum.hasMoreElements()) {
            players.add((Player)(playersEnum.nextElement()));
        }

        return players;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(command.equals("add")) {
            int response;
            String[] options = {"Keyboard", "Network"};
            response = JOptionPane.showOptionDialog(this,
                                                    "Keyboard Player or Network Player?",
                                                    "Player Type",
                                                    JOptionPane.DEFAULT_OPTION,
                                                    JOptionPane.QUESTION_MESSAGE,
                                                    null,
                                                    options,
                                                    options[1]);

            if(options[response].equals("Keyboard")) {
                System.out.println("keyboard");
            }
            else {
                String newName = JOptionPane.showInputDialog(this,
                                                             "Player's name:",
                                                             "Player Name",
                                                             JOptionPane.QUESTION_MESSAGE);
                NetworkPlayer newPlayer = new NetworkPlayer(newName);
                listModel.addElement(newPlayer);
            }
        }
        else if(command.equals("remove")) {
            int[] indices = list.getSelectedIndices();

            for(int i = indices.length - 1; i >= 0; i--) {
                listModel.remove(indices[i]);
            }
        }
        else if(command.equals("done")) {
            List<Player> players = getPlayers();

            BoardCanvas bc = new BoardCanvas();

            JTable scoreBoard = new JTable(new ScoreBoardModel(players));
            scoreBoard.setFocusable(false);

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

            Game game = new Game(players, bc, scoreBoard, 75);

            game.runGame();
        }
    }
}
