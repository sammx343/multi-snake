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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.RenderingHints;
import java.util.List;
import java.io.Serializable;

/**
 *
 * @author poodimoos
 */
public class Food extends Pickup {
    private static final long serialVersionUID = 6001;

    public Food() { super(); }

    public Food(Location loc, List<Pickup> pickups) {
        super(loc, pickups);
    }

    public Image getImage(int size) {
        BufferedImage icon = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = icon.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.setColor(Color.BLACK);
        g.fillOval(0, 0, size, size);

        return icon;
    }

    @Override
    public void pickedUpBy(Player player) {
        super.pickedUpBy(player);
        player.eat();
    }
}
