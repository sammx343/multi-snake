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
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author Patrick Hulin
 */
public abstract class Player implements Tickable, Externalizable {
    private Snake snake;
    transient private Game game;

    private int score;
    private int kills;

    private String name;
    private Color color;

    private static final Color[] colorArray = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.YELLOW};
    private static final Set<Color> colorSet;

    private static final long serialVersionUID = 1001;

    static {
        colorSet = Collections.synchronizedSet(new LinkedHashSet<Color>());
        for(Color c : colorArray) {
            colorSet.add(c.darker());
        }
    }

    public Player() {
        name = "";
        color = Color.BLACK;
    }

    public Player(String name) {
        this.name = name;

        synchronized(colorSet) {
            Iterator<Color> it = colorSet.iterator();
            color = it.next();
            it = null;
            colorSet.remove(color);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        synchronized(colorSet) {
            colorSet.add(color);
        }

        super.finalize();
    }

    // returns color to pool
    public void dispose() {
        synchronized(colorSet) {
            colorSet.add(color);
        }
    }

    public static int colorsLeft() {
        return colorSet.size();
    }

    @Override
    public String toString() {
        return getName();
    }

    public void beginGame(Game game) {
        this.game = game;
        snake = new Snake();
        snake.reset(game.randomValidLocation());
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }
    
    public void tick() {
        snake.tick();
    }

    // Some kinds of players may need to do things after ticking
    public void afterTick() { }

    public Snake getSnake() {
        return snake;
    }

    public int getScore() {
        return score;
    }

    public int getKills() {
        return kills;
    }

    // overridden by subclasses
    public boolean isReady() {
        return (snake != null);
    }

    protected void setDirection(Direction dir) {
        snake.setDirection(dir);
    }

    protected Game getGame() {
        return game;
    }

    // methods for game events, things that can happen to a player
    public void eat() {
        score += 10;
        snake.appendSegment();
        game.makeNewFood();
    }

    public void kill() {
        score -= 10;
        snake.reset(game.randomValidLocation());
    }

    public void giveKill() {
        kills += 1;
        score += 30;
    }

    // if for some reason the player needs to be dropped from the game
    // exists so can be overridden to perform final cleanup tasks if necessary
    protected void quit() {
        game.removePlayer(this);
    }

    // write in order snake, name, score, kills
     public void writeExternal(ObjectOutput out) {
         try {
             out.writeObject(snake);
             out.writeObject(name);
             out.writeObject(color);
             Integer scoreI = new Integer(score);
             out.writeObject(scoreI);
             Integer killsI = new Integer(kills);
             out.writeObject(killsI);
             out.flush();
         } catch(IOException ex) {
             ex.printStackTrace();
         }
     }

     public void readExternal(ObjectInput in) {
         try {
             snake = (Snake)(in.readObject());
             name = (String)(in.readObject());
             color = (Color)(in.readObject());
             Integer scoreI = (Integer)(in.readObject());
             score = scoreI.intValue();
             Integer killsI = (Integer)(in.readObject());
             kills = killsI.intValue();
         } catch(Exception ex) {
             ex.printStackTrace();
         }
     }
}
