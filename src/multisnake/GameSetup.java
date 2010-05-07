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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

/**
 *
 * @author Patrick Hulin
 */
public class GameSetup extends JFrame implements ActionListener, TableModelListener {
    private PlayerTableModel tableModel;
    private JTable table;

    public GameSetup() {
        super("Game Setup");

        ArrayList<Player> players = new ArrayList<Player>();
        players.add(new KeyboardPlayer("Player 1"));
        players.add(new NetworkPlayer("Player 2"));

        tableModel = new PlayerTableModel(players);
        table = new JTable(tableModel);
        table.setShowGrid(false);
        table.setDragEnabled(false);
        table.setColumnSelectionAllowed(false);
        tableModel.addTableModelListener(this);

        TableColumn playerColumn = table.getColumn("Name");
        playerColumn.setCellRenderer(new PlayerCellRenderer());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        JScrollPane scrollPane = new JScrollPane(table);
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

    private List<Player> getPlayers() {
        return tableModel.getPlayers();
    }

    public String nextName() {
        return "Player " + (tableModel.getRowCount() + 1);
    }

    public void addPlayer(Player player) {
        tableModel.addPlayer(player);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(command.equals("add")) {
            if(Player.colorsLeft() > 0) {
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

                if (options[response].equals("Keyboard")) {
                    KPSetup kpSetup = new KPSetup(this, true);
                    kpSetup.setVisible(true);
                } else {
                    NetworkPlayer newPlayer = new NetworkPlayer(nextName());
                    tableModel.addPlayer(newPlayer);
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Too many players already.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(command.equals("remove")) {
            int[] indices = table.getSelectedRows();

            for(int i = indices.length - 1; i >= 0; i--) {
                Player p = tableModel.getPlayer(indices[i]);
                p.dispose();
                tableModel.removePlayer(indices[i]);
            }
        }
        else if(command.equals("done")) {
            List<Player> players = getPlayers();

            BoardCanvas bc = new BoardCanvas();
            JTable scoreBoard = new JTable(new ScoreBoardModel(players));

            MultiSnake.setUpFrame(bc, scoreBoard);
            setVisible(false);

            Game game = new Game(players, bc, scoreBoard, 75);

            dispose();
            game.runGame();
        }
    }

    public void tableChanged(TableModelEvent e) {
        table.repaint();
    }

    @Override
    public void dispose() {
        tableModel = null;
        table = null;
    }
}
