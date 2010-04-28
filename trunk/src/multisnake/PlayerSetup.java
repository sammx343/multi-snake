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

import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;

/**
 *
 * @author Patrick Hulin
 */
public class PlayerSetup extends JFrame {
    private DefaultListModel listModel;
    private JList list;

    public PlayerSetup() {
        listModel = new DefaultListModel();
        list = new JList(listModel);

        Player defaultPlayer = new KeyboardPlayer("Player 1");
        listModel.addElement(defaultPlayer);
    }

    public List<Player> getPlayers() {
        Player[] playerArray = (Player[])(listModel.toArray());
        List<Player> players = new ArrayList<Player>(playerArray.length);

        for(Player p : playerArray) {
            players.add(p);
        }

        return players;
    }
}
