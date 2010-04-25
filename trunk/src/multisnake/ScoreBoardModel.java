/*
 *  Copyright (C) 2010 phulin10
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

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * @author phulin10
 */
public class ScoreBoardModel extends AbstractTableModel {
    private List<Player> players;

    private static final String[] COLUMN_NAMES = {"Player", "Score", "Kills"};

    public ScoreBoardModel() {
        this.players = new LinkedList<Player>();
    }

    public ScoreBoardModel(List<Player> players) {
        this.players = players;
    }

    public Object getValueAt(int row, int col) {
        Player p = players.get(row);

        if(col == 0) {
            return p.getName();
        }
        else if(col == 1) {
            return String.valueOf(p.getScore());
        }
        else if(col == 2) {
            return String.valueOf(p.getKills());
        }

        assert false : "Too many columns.";
        return null;
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    public int getRowCount() {
        return players.size();
    }

    @Override
    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }
}
