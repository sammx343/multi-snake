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
public class Snake {
    private class Segment {
        private Location location;

        public Segment(Location loc) {
            location = loc;
        }

        public Location getLocation() {
            return location;
        }
    }

    private static final int START_SEGMENTS = 4;

    private LinkedList<Segment> segments;
    private Direction dir;

    public Snake(Location loc) {
        reset(loc);
    }

    // move snake one spot
    public void tick() {
        Segment firstSegment = segments.getFirst();
        Location firstLoc = firstSegment.getLocation();
        Location newLoc = firstLoc.getAdjacentLocation(dir);
        Segment newSegment = new Segment(newLoc);

        segments.removeLast();
        segments.addFirst(newSegment);
    }

    public void setDirection(Direction newDir) {
        dir = newDir;
    }

    public List<Location> getLocations() {
        LinkedList<Location> locs = new LinkedList<Location>();
        ListIterator<Segment> it = segments.listIterator();

        while(it.hasNext()) {
            Segment seg = it.next();

            locs.add(seg.getLocation());
        }

        return locs;
    }

    // if snake crashed into something or just to do initial construction
    public void reset(Location loc) {
        segments.clear();
        for(int i = 0; i < START_SEGMENTS; i++)
            segments.add(new Segment(loc));

        dir = Direction.NONE;
    }

    public void appendSegment() {
        Segment last = segments.getLast();
        Segment newSegment = new Segment(last.getLocation());
        segments.add(newSegment);
    }
}
