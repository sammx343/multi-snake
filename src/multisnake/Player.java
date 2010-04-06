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

import java.util.Timer;

/**
 *
 * @author poodimoos
 */
public abstract class Player {
    private Snake snake;

    private Timer timer;
    private TickTask tickTask;

    private int score;
    private int kills;
    // we want to wait a tick if the snake dies to make sure collision checks
    // work, so we queue it for destruction
    private boolean isDead = false;

    public Player() {
        // FIXME: REAL LOCATION
        snake = new Snake(new Location((MultiSnake.BOARD_WIDTH + 1) / 2,
                                       (MultiSnake.BOARD_HEIGHT + 1) / 2));
    }

    public void initTimer(Game game) {
        timer = new Timer();
        tickTask = new TickTask(this, game);
        timer.scheduleAtFixedRate(tickTask, 0, 300);
    }

    public void tick() {
        

        if (isDead) {
            snake.reset(new Location((MultiSnake.BOARD_WIDTH + 1) / 2,
                                     (MultiSnake.BOARD_HEIGHT + 1) / 2));
            isDead = false;
        }
        else
            snake.tick();
    }

    public Snake getSnake() {
        return snake;
    }

    public int getScore() {
        return score;
    }

    protected void setDirection(Direction dir) {
        snake.setDirection(dir);
    }

    // methods for game events, things that can happen to a player
    public void eat() {
        score += 5;
        snake.appendSegment();
    }

    public void kill() {
        score -= 20;
        isDead = true;
    }

    public void giveKill() {
        kills += 1;
        score += 30;
    }
}
