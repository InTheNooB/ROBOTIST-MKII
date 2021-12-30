package wrk;

import java.util.ArrayList;

public interface ItfWrkCtrl {

    public ArrayList<String> getUserSequences(int userPk);

    public int login(String username, String password);

    public boolean register(String username, String password);

    public boolean moveRobot(int leftTrackSpeed, int rightTrackSpeed);

    public boolean playSequence(String sequence);

    public ArrayList<String> getEtatMajorLogs(int userPk);

    public ArrayList<String> getDouanierLogs(int userPk);

    public void addDouanierLog(int pkUser, String log);

}
