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
    private Player[] players;
    private Pickup[] pickups;

    private static final long serialVersionUID = 9005;

    public TickPacket() {
        players = null;
        pickups = null;
    }

    public TickPacket(Player[] players, Pickup[] pickups) {
        this.players = players;
        this.pickups = pickups;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Pickup[] getPickups() {
        return pickups;
    }

    public void writeExternal(ObjectOutput out) {
        try {
            synchronized(players) {
                out.write(players.length);
                for(Player p : players) {
                    out.writeObject(p);
                }
            }

            synchronized(pickups) {
                out.write(pickups.length);
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
        int i = -1;
        try {
            int numPlayers = in.read();
            players = new Player[numPlayers];
            synchronized(players) {
                for(i = 0; i < numPlayers; i++) {
                    players[i] = (Player)(in.readObject());
                }
            }

            int numPickups = in.read();
            pickups = new Pickup[numPickups];
            synchronized(pickups) {
                for(i = 0; i < numPickups; i++) {
                    pickups[i] = (Pickup)(in.readObject());
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println(i);
        }
    }
}
