package wrk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPClient extends Thread {

    public static final int RECEIVE_RATE = 10;
    private volatile Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private volatile boolean running;
    private final ItfWrkTCPClient refWrk;
    private volatile boolean robotDisconnected;
    private final Object objectToWaitToIfRobotDisconnected;

    public TCPClient(ItfWrkTCPClient wrk) {
        setName("TCP Client Thread");
        this.refWrk = wrk;
        robotDisconnected = true;
        objectToWaitToIfRobotDisconnected = new Object();
        this.start();
    }

    /**
     * Checks if a TCP packet is received every RECEIVE_RATE milliseconds. The
     * packet is then added to the ActionList
     */
    @Override
    public void run() {
        running = true;
        while (running) {
            if (socket != null && in != null) {
                if (socket.isConnected() && !(socket.isClosed())) {
                    receiveMessage();
                }
            } else if (robotDisconnected) {
                synchronized (objectToWaitToIfRobotDisconnected) {
                    try {
                        objectToWaitToIfRobotDisconnected.wait();
                    } catch (InterruptedException ex) {
                    }
                }
            }

            sleeep(RECEIVE_RATE);
        }
    }

    /**
     * Connect to server with ip address.
     *
     * @param IP the ip address of the server
     * @return true if connected and false if not
     */
    public boolean connectToServer(String IP) {
        boolean result;

        try {
            SocketAddress sockaddr = new InetSocketAddress(InetAddress.getByName(IP), 2121);
            // Create your socket
            socket = new Socket();
            socket.connect(sockaddr, 1000);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            result = true;
            synchronized (objectToWaitToIfRobotDisconnected) {
                objectToWaitToIfRobotDisconnected.notifyAll();
            }
        } catch (IOException exc) {
            result = false;
        }
        return result;
    }

    /**
     * Disconnect client from the server.
     */
    public void disconnect() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException ex) {
            }
        }
        if (out != null) {
            out.close();
        }
        in = null;
        out = null;
        socket = null;
        robotDisconnected = true;
    }

    /**
     * Receive message incoming from the server. The message is then added to
     * the action list to be processed later
     */
    public void receiveMessage() {
        String msg = null;
        try {
            msg = in.readLine();
        } catch (IOException ex) {
        }
        if (msg == null) {
            disconnect();
        } else {
            refWrk.receivedMessage(msg);
        }
    }

    /**
     * Send message to the server.
     *
     * @param message the message to send
     */
    public void writeMessage(String message) {
        if (out != null) {
            out.println(message);
            out.flush();
        }
    }

    /**
     * This method is used to sleep
     *
     * @param ms number of milliseconds to sleep
     */
    public void sleeep(long ms) {
        try {
            sleep(ms);
        } catch (InterruptedException ex) {
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

}
