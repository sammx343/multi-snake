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
import javax.swing.JTable;

/**
 *
 * @author poodimoos
 */
public class Game implements Tickable {
    private final ArrayList<Player> players;
    private BoardCanvas bc;
    private JTable scoreBoard;
    private Timer timer;
    private TickTask tickTask;

    private static final int TICK_LENGTH = 150;

    public Game(final List<Player> players,
                BoardCanvas bc,
                JTable scoreBoard) {
        this.players = new ArrayList<Player>(players);

        this.bc = bc;
        this.scoreBoard = scoreBoard;

        timer = new Timer();
    }

    public void runGame() {
        tickTask = new TickTask(this);
        timer.scheduleAtFixedRate(tickTask, 0, TICK_LENGTH);
    }

    public void tick() {
        for(Player p : players) {
            p.tick();
        }
        checkCollisions();
        bc.repaint();
        scoreBoard.repaint();
    }

    public void checkCollisions() {
        // for every head
        for(Player p1 : players) {
            Snake snake = p1.getSnake();
            List<Location> locs = snake.getLocations();
            Location head = locs.get(0);

            // snake-on-snake: and every other snake
            for(Player p2 : players) {
                if (p2 == p1)
                    continue;

                Snake snake2 = p2.getSnake();
                List<Location> locs2 = snake2.getLocations();

                // see if the head collides with any part of the other snake
                for (Location loc : locs2) {
                    if (head.equals(loc)) {
                        p2.giveKill();
                        p1.kill();
                    }
                }
            }

            // snake-on-wall:
            if ((head.x < 0) || (head.x >= MultiSnake.BOARD_WIDTH)
                || (head.y < 0) || (head.y >= MultiSnake.BOARD_HEIGHT)) {
                p1.kill();
            }
        }
    }
}
