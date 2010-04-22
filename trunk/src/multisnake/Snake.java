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
import java.io.Serializable;

/**
 *
 * @author poodimoos
 */
public class Snake implements Tickable, Serializable {
    private static final int START_SEGMENTS = 6;

    private LinkedList<Location> segments;
    private Direction dir, tempDir;
    private int age;

    // Initializes snake without setting up segments, call reset for that
    public Snake() {
        segments = new LinkedList<Location>();
    }

    // move snake one spot
    public synchronized void tick() {
        dir = tempDir;
        Location firstLoc = segments.getFirst();
        Location newLoc = firstLoc.getAdjacentLocation(dir);

        segments.removeLast();
        segments.addFirst(newLoc);

        age++;
    }

    public synchronized void setDirection(Direction newDir) {
        // passing null should not change direction
        if(newDir == null)
            return;

        // don't allow straight direction reversal
        // and wait a bit before snakes can move
        if ((age <= 2)
            || ((dir == Direction.SOUTH) && (newDir == Direction.NORTH))
            || ((dir == Direction.NORTH) && (newDir == Direction.SOUTH))
            || ((dir == Direction.EAST) && (newDir == Direction.WEST))
            || ((dir == Direction.WEST) && (newDir == Direction.EAST)))
            return;

        // delay changing direction until tick; this means can't turn in on self
        tempDir = newDir;
    }

    public synchronized Direction getDirection() {
        return tempDir;
    }

    public final List<Location> getLocations() {
        return new LinkedList<Location>(segments);
    }

    // if snake crashed into something or just to do initial construction
    public synchronized void reset(Location startLoc) {
        segments.clear();
        for(int i = 0; i < START_SEGMENTS; i++)
            segments.add(startLoc);

        dir = Direction.NONE;
        tempDir = Direction.NONE;

        age = 0;
    }

    public synchronized void appendSegment() {
        Location last = segments.getLast();
        segments.add(last);
    }
}
