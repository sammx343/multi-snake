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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author poodimoos
 */
public class NetworkPlayer extends Player implements Runnable {
    private ServerSocket serverSocket;
    private Socket socket;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private Thread waitingThread;

    public NetworkPlayer(String name, int port) {
        super(name);

        try {
            serverSocket = new ServerSocket(port);
        } catch(IOException ex) {
            System.out.println(ex);
        }
    }
    
    public void waitForConnection() {
        try {
            socket = serverSocket.accept();

            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println(ex);
        }

        waitingThread = new Thread(this);
    }

    @Override
    public void tick() {
        super.tick();

        Game game = getGame();
        List<Player> players = game.getPlayers();
        List<Pickup> pickups = game.getPickups();

        TickPacket tp = new TickPacket(players, pickups);

        try {
            outputStream.writeObject(tp);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void run() {
        while(!socket.isClosed()) {
            Direction dir = null;

            try {
                dir = (Direction) (inputStream.readObject());
            } catch (Exception ex) {
                System.out.println(ex);
            }

            setDirection(dir);
        }
    }
}
