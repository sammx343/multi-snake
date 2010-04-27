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
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

/**
 *
 * @author Patrick Hulin
 */
public class TickPacket implements Externalizable {
    private List<Player> players;
    private List<Pickup> pickups;

    private static final long serialVersionUID = 9004;

    public TickPacket() {
        players = null;
        pickups = null;
    }

    public TickPacket(List<Player> players, List<Pickup> pickups) {
        this.players = players;
        this.pickups = pickups;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Pickup> getPickups() {
        return pickups;
    }

    public void writeExternal(ObjectOutput out) {
        try {
            synchronized(players) {
                out.write(players.size());
                for(Player p : players) {
                    out.writeObject(p);
                }
            }

            synchronized(pickups) {
                out.write(pickups.size());
                for(Pickup pu : pickups) {
                    out.writeObject(pu);
                }
            }

            out.flush();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void readExternal(ObjectInput in) {
        players = Collections.synchronizedList(new LinkedList<Player>());
        pickups = Collections.synchronizedList(new LinkedList<Pickup>());
        try {
            synchronized(players) {
                for(int i = in.read(); i > 0; i--) {
                    players.add((Player)(in.readObject()));
                }
            }

            synchronized(pickups) {
                for(int i = in.read(); i > 0; i--) {
                    pickups.add((Pickup)(in.readObject()));
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
