package wrk;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import rest.RESTfulClient;

/**
 *
 * @author dingl01
 */
public class Wrk implements ItfWrkCtrl, ItfWrkTCPClient {

    private final String OUVRIER_URL = "WSTEMFA45-15";
    private final RESTfulClient restfulClient;

    private final TCPClient tcpClient;
    private final Gson gson;

    private String receivedMessageFromRobot;

    public Wrk() {
        tcpClient = new TCPClient(this);
        restfulClient = new RESTfulClient();
        gson = new Gson();
        receivedMessageFromRobot = null;
    }

    /**
     * Asks the Etat Major if the credentials are corrects.
     *
     * @param username The login of the user
     * @param password The password of the user
     * @return An integer corresponding to the primary key of the user if any
     * was found. -1 otherwise
     */
    @Override
    public int login(String username, String password) {
        HashMap<String, String> h = new HashMap();
        h.put("username", username);
        h.put("password", password);
        try {
            return gson.fromJson(restfulClient.login(gson.toJson(h)), Integer.class);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Sends a "register new user" message to the EtatMajor.
     *
     * @param username The username of the user to create
     * @param password The password of the user to create
     * @return True if the user was successfully created
     */
    @Override
    public boolean register(String username, String password) {
        HashMap<String, String> h = new HashMap();
        h.put("username", username);
        h.put("password", password);
        return "OK".equals(gson.fromJson(restfulClient.signin(gson.toJson(h)), String.class));
    }

    /**
     * Get every sequences of a specified user by asking the EtatMajor
     *
     * @param userPk The primary key of the user
     * @return A JSON String containing every sequences of the user
     */
    @Override
    public ArrayList<String> getUserSequences(int userPk) {
        return gson.fromJson(restfulClient.getUserSequences(gson.toJson(userPk)), ArrayList.class);
    }

    /**
     * Get every logs related to the etat major that concerns the specified user
     * by asking the EtatMajor
     *
     * @param userPk The primary key of the user
     * @return A JSON String containing every logs of the user
     */
    @Override
    public ArrayList<String> getEtatMajorLogs(int userPk) {
        return gson.fromJson(restfulClient.getEtatMajorLogs(gson.toJson(userPk)), ArrayList.class);
    }

    /**
     * Get every logs related to the douanier that concerns the specified user
     * by asking the EtatMajor
     *
     * @param userPk The primary key of the user
     * @return A JSON String containing every logs of the user
     */
    @Override
    public ArrayList<String> getDouanierLogs(int userPk) {
        return gson.fromJson(restfulClient.getDouanierLogs(gson.toJson(userPk)), ArrayList.class);
    }

    /**
     * Connects to the "ouvrier" if not already connected, and sends him a
     * message.
     *
     * @param s The message to send
     * @return A boolean, true if the robot moved successfully, false otherwise.
     */
    private boolean sendRobotMessage(String s) {
        if (tcpClient.isConnected() || tcpClient.connectToServer(OUVRIER_URL)) {
            tcpClient.writeMessage(s);
            return true;
        }
        return false;
    }

    /**
     * Sends a "move" message to the robot specifying the speed of each track
     *
     * @param leftTrackSpeed The speed of the left track (-999&999)
     * @param rightTrackSpeed The speed of the right track (-999&999)
     * @return True if the robot moved successfully, false otherwise
     */
    @Override
    public boolean moveRobot(int leftTrackSpeed, int rightTrackSpeed) {
        String message = PacketTypes.MOVE_ROBOT_BODY + "" + leftTrackSpeed + ";" + rightTrackSpeed;
        return sendRobotMessage(message);
    }

    /**
     * Sends a "play sequence" message to the robot specifying the primary key
     * of the sequence. Then waits (1000ms) for the robot to send a confirmation
     * message.
     *
     * @param sequence The primary key of the sequence to play
     * @return True if the sequence is being played, false otherwise
     */
    @Override
    public boolean playSequence(String sequence) {
        String message = PacketTypes.READ_SEQUENCE + sequence;
        sendRobotMessage(message);
        try {
            synchronized (this) {
                this.wait(1000);
            }
            if (receivedMessageFromRobot != null && receivedMessageFromRobot.startsWith(PacketTypes.READ_SEQUENCE.toString())) {
                boolean sequenceStarted = "true".equals(receivedMessageFromRobot.substring(PacketTypes.READ_SEQUENCE.toString().length()));
                receivedMessageFromRobot = null;
                return sequenceStarted;
            }
        } catch (InterruptedException ex) {
        }
        return false;
    }

    /**
     * Sets the aatribut "receivedMessageFromRobot" to the value of the received
     * message. The wakes up the waiting thread.
     *
     * @param msg The received message.
     */
    @Override
    public void receivedMessage(String msg) {
        receivedMessageFromRobot = msg;
        synchronized (this) {
            this.notifyAll();
        }
    }

    /**
     * Asks the etat major to add a douanier log.
     *
     * @param pkUser The pk of the user related to the log
     * @param log The log itself
     */
    @Override
    public void addDouanierLog(int pkUser, String log) {
        HashMap<String, String> h = new HashMap();
        h.put("pk", pkUser + "");
        h.put("log", log);
        restfulClient.addDouanierLog(gson.toJson(h));
    }

}
