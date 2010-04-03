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

/**
 *
 * @author poodimoos
 */
public abstract class Player {
    private Snake snake;
    private int score;
    private int kills;

    public void tick() {
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
        // FIXME: RESET SNAKE
    }

    public void giveKill() {
        kills += 1;
        score += 30;
    }
}
