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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Patrick Hulin
 */
public class KeyboardPlayer extends Player implements KeyListener {
    transient private int north, east, south, west;

    private static final long serialVersionUID = 4002;

    // for externalization, only trivial initialization
    public KeyboardPlayer() {
        super();

        this.north = 0;
        this.east = 0;
        this.south = 0;
        this.west = 0;
    }

    public KeyboardPlayer(String name) {
        this(name,
             KeyEvent.VK_UP,
             KeyEvent.VK_RIGHT,
             KeyEvent.VK_DOWN,
             KeyEvent.VK_LEFT);
    }

    public KeyboardPlayer(String name,
                          int north, int east, int south, int west) {
        super(name);
        
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

    // unused methods
    public void keyPressed(KeyEvent e) { }
    public void keyTyped(KeyEvent e) { }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == north)
            setDirection(Direction.NORTH);
        else if(key == east)
            setDirection(Direction.EAST);
        else if(key == south)
            setDirection(Direction.SOUTH);
        else if(key == west)
            setDirection(Direction.WEST);
    }
}
