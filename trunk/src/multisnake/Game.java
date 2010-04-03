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

import java.util.*;

/**
 *
 * @author poodimoos
 */
public class Game {
    ArrayList<Player> players;
    BoardCanvas bc;

    public Game(List<Player> players) {
        this.players = new ArrayList<Player>(players);

        bc = new BoardCanvas(this.players);
    }

    public void runGame() {

    }

    public void checkCollisions() {
        // for every head
        for(Player p1 : players) {
            Snake snake = p1.getSnake();
            List<Location> locs = snake.getLocations();
            Location head = locs.get(0);

            // and every other snake
            for(Player p2 : players) {
                Snake snake2 = p2.getSnake();
                List<Location> locs2 = snake.getLocations();

                // see if the head collides with any part of the other snake
                for (Location loc : locs2) {
                    if (head.equals(loc)) {
                        p2.giveKill();
                        p1.kill();
                    }
                }
            }
        }
    }
}
