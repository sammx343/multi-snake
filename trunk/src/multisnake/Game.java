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

    private List<Pickup> pickups;

    private static final int TICK_LENGTH = 75;

    public Game(final List<Player> players,
                BoardCanvas bc,
                JTable scoreBoard) {
        this.players = new ArrayList<Player>(players);

        this.bc = bc;
        this.scoreBoard = scoreBoard;

        timer = new Timer();
        pickups = new LinkedList<Pickup>();
    }

    public void runGame() {
        for(Player p : players) {
            if (p instanceof KeyboardPlayer)
                bc.addKeyListener((KeyboardPlayer)p);

            p.beginGame(this);
        }

        bc.initForGame(players, pickups);

        tickTask = new TickTask(this);
        timer.scheduleAtFixedRate(tickTask, 0, TICK_LENGTH);

        makeNewFood();
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
        // Postpone killing players so it doesn't screw with
        // checking others' collisions, and use sets to ensure we don't give
        // duplicate kills.
        Set<Player> deadPlayers = new HashSet<Player>();
        Set<Player> killingPlayers = new HashSet<Player>();

        // for every head
        for(Player p1 : players) {
            Snake snake = p1.getSnake();
            List<Location> locs = snake.getLocations();
            Location head = locs.get(0);

            // snake-on-snake: and every other snake
            for(Player p2 : players) {
                Snake snake2 = p2.getSnake();
                List<Location> locs2 = snake2.getLocations();

                // see if the head collides with any part of the other snake
                Iterator<Location> it = locs2.iterator();
                Location loc = it.next();
                for(int i = 0; it.hasNext(); loc = it.next()) {
                    if(head.equals(loc)) {
                        if(p1 != p2)
                            killingPlayers.add(p2);
                        if(p1 != p2)
                            deadPlayers.add(p1);
                        else if((snake.getDirection() != Direction.NONE)
                                && (i != 0))
                            deadPlayers.add(p1);
                    }
                    i++;
                }
            }

            // snake-on-wall:
            if ((head.x < 0) || (head.x >= MultiSnake.BOARD_WIDTH)
                || (head.y < 0) || (head.y >= MultiSnake.BOARD_HEIGHT)) {
                deadPlayers.add(p1);
            }

            // snake-on-food:
            for(Pickup pu : pickups) {
                if (head.equals(pu.getLocation()))
                    pu.pickedUpBy(p1);
            }
        }

        // Kill dead players
        for(Player dp : deadPlayers) {
            dp.kill();
        }
        for(Player kp : killingPlayers) {
            kp.giveKill();
        }
    }

    public void makeNewFood() {
        Food food = new Food(randomValidLocation(), pickups);
        food.placeOnBoard();
    }

    public boolean isOccupied(Location loc) {
        for(Player p : players) {
            Snake s = p.getSnake();
            if(s == null)
                continue;
            List<Location> sLocs = s.getLocations();
            for(Location l : sLocs) {
                if(loc.equals(l)) {
                    return true;
                }
            }
        }

        for(Pickup p : pickups) {
            Location pLoc = p.getLocation();
            if(loc.equals(pLoc))
                return true;
        }

        return false;
    }

    public Location randomValidLocation() {
        Location loc;
        
        do {
            loc = Location.getRandomLocation();
        } while(isOccupied(loc));

        return loc;
    }
}
