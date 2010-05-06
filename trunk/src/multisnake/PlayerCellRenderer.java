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

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Patrick Hulin
 */
public class PlayerCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {
        Component comp = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, col);

        Object o = table.getModel().getValueAt(row, col);

        if (o instanceof Player) {
            Player p = (Player)o;
            Color color = p.getColor();
            if(table.isCellSelected(row, col))
                comp.setBackground(color.darker());
            else
                comp.setBackground(color);

            comp.setForeground(Color.WHITE);
        }

        return comp;
    }
}
