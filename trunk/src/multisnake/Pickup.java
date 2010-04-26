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

import java.awt.Image;
import java.util.List;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;

/**
 *
 * @author poodimoos
 */
public abstract class Pickup implements Externalizable {
    private Location loc;
    private List<Pickup> pickups;

    private static final long serialVersionUID = 5001;

    // this seems to be needed for serialization... i could be very profane here
    public Pickup() { }

    public Pickup(Location loc, List<Pickup> pickups) {
        this.loc = loc;
        this.pickups = pickups;
    }

    public void placeOnBoard() {
        pickups.add(this);
    }

    public void pickedUpBy(Player player) {
        if (pickups != null)
            pickups.remove(this);
    }

    public Location getLocation() {
        return loc;
    }

    public abstract Image getImage(int size);

    public void writeExternal(ObjectOutput out) {
        try {
            out.writeObject(loc);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void readExternal(ObjectInput in) {
        try {
            loc = (Location)(in.readObject());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
