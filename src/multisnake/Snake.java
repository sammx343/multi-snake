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
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;

/**
 *
 * @author poodimoos
 */
public class Snake implements Tickable, Externalizable {
    private static final int START_SEGMENTS = 6;

    private List<Location> segments;
    private Direction dir, tempDir;
    private int age;

    private static final long serialVersionUID = 2001;

    // Initializes snake without setting up segments, call reset for that
    public Snake() {
        segments = Collections.synchronizedList(new LinkedList<Location>());
    }

    // move snake one spot
    public synchronized void tick() {
        dir = tempDir;
        Location firstLoc = segments.get(0);
        Location newLoc = firstLoc.getAdjacentLocation(dir);

        synchronized(segments) {
            segments.remove(segments.size() - 1);
            segments.add(0, newLoc);
        }

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

    // return a shallow copy so caller can do what it wants
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
        synchronized(segments) {
            Location last = segments.get(segments.size() - 1);
            segments.add(last);
        }
    }

    public void writeExternal(ObjectOutput out) {
        try {
            byte[] serialSnake = new byte[segments.size() * 2];
            synchronized(segments) {
                Iterator<Location> it = segments.iterator();
                for(int i = 0; it.hasNext(); i++) {
                    Location loc = it.next();
                    serialSnake[2 * i] = (byte)(loc.x);
                    serialSnake[2 * i + 1] = (byte)(loc.y);
                }
            }

            out.write(segments.size());
            out.write(serialSnake);
            out.flush();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
     }

    public void readExternal(ObjectInput in) {
        try {
            int length = in.read();
            for(int i = 0; i < length; i++) {
                int x = in.read(), y = in.read();
                Location loc = new Location(x, y);
                segments.add(loc);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
