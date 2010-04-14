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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 *
 * @author poodimoos
 */
public class BoardCanvas extends Canvas {
    LinkedList<Player> players;

    private int scaling = 20;

    private static Color[] snakeColors = {Color.RED, Color.BLUE, Color.GREEN};

    public BoardCanvas(java.util.List<Player> players) {
        this.players = new LinkedList<Player>(players);

        setSize(scaling * MultiSnake.BOARD_WIDTH,
                scaling * MultiSnake.BOARD_HEIGHT);
    }

    @Override
    public void paint(Graphics gf) {
        int height = getHeight(), width = getWidth();

        System.out.println("paint");

        // double buffer
        Image img = new BufferedImage(scaling * MultiSnake.BOARD_WIDTH,
                                      scaling * MultiSnake.BOARD_HEIGHT,
                                      BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        //g.setColor(Color.WHITE);
        //g.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, img.getWidth(null) - 1, img.getHeight(null) - 1);

        // draw everyone's snakes
        Iterator<Player> it = players.iterator();
        for(int i = 0; it.hasNext(); i++) {
            Player p = it.next();
            drawSnake(g, p.getSnake(), snakeColors[i]);
        }

        gf.drawImage(img, 0, 0, null);
    }

    private void drawSnake(Graphics g, Snake snake, Color c) {
        java.util.List<Location> locs = snake.getLocations();

        g.setColor(c);

        for (Location loc : locs) {
            g.fillRect(scaling * loc.x + 1, scaling * loc.y + 1, scaling - 1, scaling - 1);
        }
    }
}
