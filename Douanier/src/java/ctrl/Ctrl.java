package ctrl;

import java.util.ArrayList;
import wrk.ItfWrkCtrl;
import wrk.Wrk;

public class Ctrl {

    private final ItfWrkCtrl wrk;

    
    
    public Ctrl() {
        wrk = new Wrk();
    }

    /**
     * Asks the wrk to get the list of sequence of a specified user and returns
     * it.
     *
     * @param userPk The primary key of the user
     * @return The list of sequence
     */
    public ArrayList<String> getUserSequences(int userPk) {
        addDouanierLog(userPk, "User asked to get his sequences");
        return wrk.getUserSequences(userPk);
    }

    /**
     * Asks the wrk to get the list of logs (related to the douanier) of a
     * specified user and returns it.
     *
     * @param userPk The primary key of the user
     * @return The list of sequence
     */
    public ArrayList<String> getDouanierLogs(int userPk) {
        addDouanierLog(userPk, "User asked to get the Douanier Logs");
        return wrk.getDouanierLogs(userPk);
    }

    /**
     * Asks the wrk to get the list of logs (related to the douanier) of a
     * specified user and returns it.
     *
     * @param userPk The primary key of the user
     * @return The list of sequence
     */
    public ArrayList<String> getEtatMajorLogs(int userPk) {
        addDouanierLog(userPk, "User asked to get the Etat Major Logs");
        return wrk.getEtatMajorLogs(userPk);
    }

    /**
     * Asks the wrk to play a specified sequence.
     *
     * @param userPk The logged user's pk
     * @param sequence The sequence itself
     * @return True if the sequence is being played, fales otherwise
     */
    public boolean playSequence(int userPk, String sequence) {
        addDouanierLog(userPk, "User asked to play the sequence : (" + sequence + ")");
        return wrk.playSequence(sequence);
    }

    /**
     * Asks the wrk to move to robot depending on the speed of each track
     *
     * @param leftTrackSpeed The speed of the left track (-999->+999)
     * @param rightTrackSpeed The speed of the left track (-999->+999)
     * @return True if the robot moved successfully, false otherwise
     */
    public boolean moveRobot(int leftTrackSpeed, int rightTrackSpeed) {
        return wrk.moveRobot(leftTrackSpeed, rightTrackSpeed);
    }

    /**
     * Asks the wrk to register a new user.
     *
     * @param username The username of the user to create
     * @param password The password of the user to create
     * @return True if the user was successfully created, false otherwise
     */
    public boolean register(String username, String password) {
        addDouanierLog(-1, "Unknown visitor registered a new user : {" + username + ":" + password + "}");
        return wrk.register(username, password);
    }

    /**
     * Asks the wrk to check the credentials of a user.
     *
     * @param username The username of the potential user
     * @param password The password of the potential user
     * @return An integer corresponding to the primary key of the user if any
     * was found. -1 otherwise
     */
    public int login(String username, String password) {
        addDouanierLog(-1, "Unknown visitor tried to login using : {" + username + ":" + password + "}");
        int userPk = wrk.login(username, password);
        if (userPk != -1) {
            addDouanierLog(userPk, "User successfully logged in");
        }
        return userPk;
    }

    /**
     * Asks the wrk to add a Douanier log
     *
     * @param pkUser The pk of the user related to the log
     * @param log The log itself
     */
    public void addDouanierLog(int pkUser, String log) {
        wrk.addDouanierLog(pkUser, log);
    }

}
