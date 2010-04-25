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
import java.awt.event.*;

/**
 *
 * @author poodimoos
 */
public class KeyboardPlayer extends Player implements KeyListener {
    private int north, east, south, west;

    public KeyboardPlayer() {
        super();

        north = KeyEvent.VK_UP;
        east = KeyEvent.VK_RIGHT;
        south = KeyEvent.VK_DOWN;
        west = KeyEvent.VK_LEFT;
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
