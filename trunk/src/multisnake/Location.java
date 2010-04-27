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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 *
 * @author Patrick Hulin
 */
public class Location implements Externalizable {
    public int x;
    public int y;

    private static final long serialVersionUID = 8002;

    public Location() {
        this(-1, -1);
    }

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location getAdjacentLocation(Direction dir) {
        switch(dir) {
            case NORTH:
                return new Location(x, y - 1);
            case EAST:
                return new Location(x + 1, y);
            case SOUTH:
                return new Location(x, y + 1);
            case WEST:
                return new Location(x - 1, y);
            case NONE:
            default:
                return this;
        }
    }

    public Direction getDirectionTo(Location loc) {
        if((loc == null) || equals(loc))
            return Direction.NONE;

        double angle = Math.atan2(loc.y - y, loc.x - x);

        if((angle > 3 * Math.PI / 4) || (angle <= -3 * Math.PI / 4))
            return Direction.WEST;
        else if ((angle > -3 * Math.PI / 4) && (angle <= -1 * Math.PI / 4))
            return Direction.SOUTH;
        else if ((angle > -1 * Math.PI / 4) && (angle <= 1 * Math.PI / 4))
            return Direction.EAST;
        else if ((angle > 1 * Math.PI / 4) && (angle <= 3 * Math.PI / 4))
            return Direction.NORTH;
        else {
            assert false : "Getting direction failed.";
            return null;
        }
    }

    public boolean equals(Location loc2) {
        return ((loc2.x == x) && (loc2.y == y));
    }

    public static Location getRandomLocation() {
        int x = (int)(Math.random() * MultiSnake.BOARD_WIDTH);
        int y = (int)(Math.random() * MultiSnake.BOARD_HEIGHT);
        return new Location(x, y);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.write(x);
        out.write(y);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        x = in.read();
        y = in.read();
    }
}
