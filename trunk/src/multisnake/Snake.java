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
    private class Segment {
        private Location location;

        public Segment(Location loc) {
            location = loc;
        }

        public Location getLocation() {
            return location;
        }
    }

    private static final int START_SEGMENTS = 6;

    private LinkedList<Segment> segments;
    private Direction dir, tempDir;
    private boolean isDead = false;

    // Initializes snake without setting up segments, call reset for that
    public Snake() {
        segments = new LinkedList<Segment>();
    }

    // move snake one spot
    public void tick() {
        System.out.println("tick");

        dir = tempDir;
        Segment firstSegment = segments.getFirst();
        Location firstLoc = firstSegment.getLocation();
        Location newLoc = firstLoc.getAdjacentLocation(dir);
        Segment newSegment = new Segment(newLoc);

        segments.removeLast();
        segments.addFirst(newSegment);
    }

    public void setDirection(Direction newDir) {
        // don't allow straight direction reversal
        if (((dir == Direction.SOUTH) && (newDir == Direction.NORTH))
             || ((dir == Direction.NORTH) && (newDir == Direction.SOUTH))
             || ((dir == Direction.EAST) && (newDir == Direction.WEST))
             || ((dir == Direction.WEST) && (newDir == Direction.EAST)))
            return;
        tempDir = newDir;
        System.out.println("set direction to " + dir);
    }

    public List<Location> getLocations() {
        LinkedList<Location> locs = new LinkedList<Location>();
        Iterator<Segment> it = segments.iterator();

        while(it.hasNext()) {
            Segment seg = it.next();

            locs.add(seg.getLocation());
        }

        return locs;
    }

    // if snake crashed into something or just to do initial construction
    public void reset(Location startLoc) {
        segments.clear();
        for(int i = 0; i < START_SEGMENTS; i++)
            segments.add(new Segment(startLoc));

        dir = Direction.NONE;
        tempDir = Direction.NONE;

        isDead = false;
    }

    public void appendSegment() {
        Segment last = segments.getLast();
        Segment newSegment = new Segment(last.getLocation());
        segments.add(newSegment);
    }
}
