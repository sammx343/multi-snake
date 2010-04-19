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
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Queue;

/**
 *
 * @author poodimoos
 */
public class BoardCanvas extends Canvas {
    List<Player> players;
    List<Pickup> pickups;

    private int scaling = 50;

    private static Color[] snakeColors = {Color.RED, Color.BLUE, Color.GREEN};

    public BoardCanvas() {
        players = new LinkedList<Player>();
        pickups = new LinkedList<Pickup>();

        setSize(scaling * MultiSnake.BOARD_WIDTH,
                scaling * MultiSnake.BOARD_HEIGHT);
    }

    public void initForGame(List<Player> players,
                            List<Pickup> pickups) {
        this.players = players;
        this.pickups = pickups;
    }

    @Override
    public void update(Graphics gf) {
        int height = getHeight(), width = getWidth();

        System.out.println("paint");

        // double buffer
        Image img = new BufferedImage(scaling * MultiSnake.BOARD_WIDTH,
                                      scaling * MultiSnake.BOARD_HEIGHT,
                                      BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)img.getGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, img.getWidth(null) - 1, img.getHeight(null) - 1);

        // draw everyone's snakes
        Iterator<Player> it = players.iterator();
        for(int i = 0; it.hasNext(); i++) {
            Player p = it.next();
            drawSnake(g, p.getSnake(), snakeColors[i]);
        }

        // draw all the pickups
        for(Pickup pu : pickups) {
            drawPickup(g, pu);
        }

        gf.drawImage(img, 0, 0, null);
    }

    private void drawSnake(Graphics g, Snake snake, Color c) {
        List<Location> locs = snake.getLocations();
        Iterator<Location> it = locs.iterator();
        Location loc, prev;
        loc = it.next();

        g.setColor(c);

        while(it.hasNext()) {
            prev = loc;
            loc = it.next();
            Direction dir = loc.getDirectionTo(prev);

            g.fillRect(scaling * loc.x + 1, scaling * loc.y + 1,
                       scaling - 1, scaling - 1);

            switch(dir) {
                case NORTH:
                    g.fillRect(scaling * loc.x + 1, scaling * loc.y - 1,
                               scaling - 2, 2);
                    break;
                case EAST:
                    g.drawLine(scaling * loc.x, scaling * loc.y + 1,
                               scaling * loc.x, scaling * (loc.y + 1) - 1);
                    break;
                case WEST:
                    break;
                case SOUTH:
                    break;
                default:
                    assert(false);
            }
        }
    }

    private void drawPickup(Graphics g, Pickup pickup) {
        Location loc = pickup.getLocation();

        g.drawImage(pickup.getImage(), scaling * loc.x, scaling * loc.y, null);
    }
}
