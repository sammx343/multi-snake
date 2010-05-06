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

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Patrick Hulin
 */
public class PlayerTableModel extends AbstractTableModel {
    private List<Player> players;

    private static final String[] columns = {"Name", "Port"};

    public PlayerTableModel(List<Player> players) {
        this.players = new ArrayList<Player>(players);
    }

    public int getRowCount() {
        return players.size();
    }

    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    @Override
    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Player player = getPlayer(rowIndex);

        if(columnIndex == 0) {
            return player;
        }
        else if(columnIndex == 1) {
            if(player instanceof NetworkPlayer) {
                NetworkPlayer nPlayer = (NetworkPlayer)player;
                return new Integer(nPlayer.getPort());
            }
            else {
                return "Local";
            }
        }

        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // only allow editing of names
        if(columnIndex == 0)
            return true;

        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);

        Player player = getPlayer(rowIndex);
        player.setName(aValue.toString());
        
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public List<Player> getPlayers() {
        return new ArrayList<Player>(players);
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public void addPlayer(Player player) {
        players.add(player);
        fireTableRowsInserted(players.size() - 1, players.size() - 1);
    }

    public void removePlayer(int playerIndex) {
        players.remove(playerIndex);
        fireTableRowsDeleted(playerIndex, playerIndex);
    }
}
