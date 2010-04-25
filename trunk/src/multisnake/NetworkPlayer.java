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
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

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

    private int port;

    public NetworkPlayer() {
        super();
    }

    public NetworkPlayer(String name, int port) {
        super(name);

        this.port = port;

        try {
            serverSocket = new ServerSocket(port);
        } catch(IOException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public boolean isReady() {
        return (outputStream != null);
    }
    
    public void waitForConnection() {
        try {
            System.out.println("waiting for connection at port " + port);

            socket = serverSocket.accept();

            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            System.out.println(getName() + " connected");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void beginGame(Game game) {
        super.beginGame(game);

        waitingThread = new Thread(this);
        waitingThread.start();
    }

    @Override
    public void afterTick() {
        Game game = getGame();
        List<Player> players = game.getPlayers();
        List<Pickup> pickups = game.getPickups();

        TickPacket tp = new TickPacket(players, pickups);

        Snake snake = tp.getPlayers().get(0).getSnake();
        Location head = snake.getLocations().get(0);
        System.out.println("sent a packet! " + head.x + " " + head.y);

        try {
            //File file = new File("serial.txt");
            //FileOutputStream fos = new FileOutputStream(file);
            //ObjectOutputStream foos = new ObjectOutputStream(fos);
            //foos.writeObject(tp);
            //foos.flush();

            outputStream.reset();
            outputStream.writeObject(tp);
            outputStream.flush();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void run() {
        waitForConnection();

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
