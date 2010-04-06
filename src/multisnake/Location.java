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
public class Location {
    public int x;
    public int y;

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
        double angle = Math.atan2(loc.x - x, loc.y - y);

        if((angle > 7 * Math.PI / 4) || (angle <= Math.PI / 4))
            return Direction.EAST;
        else if ((angle > Math.PI / 4) && (angle <= 3 * Math.PI / 4))
            return Direction.NORTH;
        else if ((angle > 3 * Math.PI / 4) && (angle <= 5 * Math.PI / 4))
            return Direction.WEST;
        else if ((angle > 5 * Math.PI / 4) && (angle <= 7 * Math.PI / 4))
            return Direction.SOUTH;
        else {
            assert false : "Getting direction failed.";
            return Direction.NONE;
        }
    }

    public boolean equals(Location loc2) {
        return ((loc2.x == x) && (loc2.y == y));
    }
}
