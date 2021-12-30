/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wrk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MayencourtE
 */
public class WrkDatabase {

    private Connection jdbcConnection;

    public WrkDatabase() {
        jdbcConnection = null;
    }

    //--------------------------------------------------------------------------
    //--------------------------------DATABASE----------------------------------
    //--------------------------------------------------------------------------
    /**
     * This method is used to connect to the database
     * @return true if connected successfully or 
     */
    private boolean dbConnect() {
        boolean ok = false;
        try {
            if (jdbcConnection == null) {
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/mayencourte_robotist_mkII?zeroDateTimeBehavior=convertToNull&serverTimezone=UTC";
                jdbcConnection = DriverManager.getConnection(url, "mayencourte_robotist_mkII_admin", "KQN5]VUO;d2[");
                ok = true;
            }
        } catch (SQLException b) {
            Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, b);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ok;
    }

    /**
     * This method is used to disconnect from the database
     * @return true if disconnected or false if not
     */
    private boolean dbDisconnect() {
        boolean ok = false;
        if (jdbcConnection != null) {
            try {
                jdbcConnection.close();
                jdbcConnection = null;
                ok = true;
            } catch (SQLException ex) {
                Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ok;
    }

    //--------------------------------------------------------------------------
    //--------------------------------SELECT------------------------------------
    //--------------------------------------------------------------------------
    
    /**
     * This method is used to check login.
     * @param username the username of the user
     * @param password the password of the user
     * @return String the primary key of the user if the credentials are correct or -1
     */
    public String login(String username, String password) {
        ResultSet rs = null;
        String result = "-1";
        PreparedStatement prestmt = null;
        if (dbConnect()) {
            try {
                String sql = "SELECT PK_User,password FROM T_User where username = ?";
                if ((prestmt = jdbcConnection.prepareStatement(sql)) != null) {
                    prestmt.setString(1, username);
                    if ((rs = prestmt.executeQuery()) != null) {
                        while (rs.next()) {
                            result = rs.getString("password").equals(password) ? rs.getString("PK_User") : "-1";
                        }
                    }
                    prestmt.close();
                    prestmt = null;

                    rs.close();
                    rs = null;

                }
            } catch (SQLException ex) {
                Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                dbDisconnect();
                try {
                    if (rs != null) {
                        rs.close();
                        rs = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (prestmt != null) {
                        prestmt.close();
                        prestmt = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    
    /**
     * This method is used to get all the user sequences
     * @param pk the primary key of the user
     * @return ArrayList of Strings the list of sequences
     */
    public ArrayList<String> getUserSequences(int pk) {
        ArrayList<String> resultat = new ArrayList<>();
        PreparedStatement prestmt = null;
        ResultSet rs = null;
        if (dbConnect()) {
            try {
                String sql = "SELECT * FROM T_Sequence where FK_User = ?";
                if ((prestmt = jdbcConnection.prepareStatement(sql)) != null) {
                    prestmt.setInt(1, pk);
                    if ((rs = prestmt.executeQuery()) != null) {
                        while (rs.next()) {
                            String s = rs.getString("nom");
                            resultat.add(s);
                        }
                    }
                    prestmt.close();
                    prestmt = null;

                    rs.close();
                    rs = null;

                }
            } catch (SQLException ex) {
                Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                dbDisconnect();
                try {
                    if (rs != null) {
                        rs.close();
                        rs = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (prestmt != null) {
                        prestmt.close();
                        prestmt = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return resultat;
    }
    /**
     * This method is used to get the douanier logs related to the user
     * @param pk the primary key of the user
     * @return ArrayList of Strings the list of logs
     */
    public ArrayList<String> getLogDouanier(int pk) {
        PreparedStatement prestmt = null;
        ResultSet rs = null;
        ArrayList<String> result = new ArrayList<String>();
        if (dbConnect()) {
            try {
                String sql = "SELECT douanier_log FROM T_Douanier_Log where FK_User = ?";

                if ((prestmt = jdbcConnection.prepareStatement(sql)) != null) {
                    prestmt.setInt(1, pk);
                    if ((rs = prestmt.executeQuery()) != null) {
                        while (rs.next()) {
                            result.add(rs.getString("douanier_log"));
                        }
                    }
                    prestmt.close();
                    prestmt = null;

                    rs.close();
                    rs = null;

                }
            } catch (SQLException ex) {
                Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                dbDisconnect();
                try {
                    if (rs != null) {
                        rs.close();
                        rs = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (prestmt != null) {
                        prestmt.close();
                        prestmt = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }
    /**
     * This method is used to get the etat majeur logs related to the user
     * @param pk the primary key of the user
     * @return ArrayList of Strings the list of logs
     */
    public ArrayList<String> getLogEtatMajeur(int pk) {
        PreparedStatement prestmt = null;
        ResultSet rs = null;
        ArrayList<String> result = new ArrayList<String>();
        if (dbConnect()) {
            try {
                String sql = "SELECT etat_majeur_log FROM T_Etat_Majeur_Log where FK_User = ?";

                  if ((prestmt = jdbcConnection.prepareStatement(sql)) != null) {
                    prestmt.setInt(1, pk);
                    
                    if ((rs = prestmt.executeQuery()) != null) {
                        while (rs.next()) {
                              result.add(rs.getString("etat_majeur_log"));
                        }
                    }
                    prestmt.close();
                    prestmt = null;

                    rs.close();
                    rs = null;

                }
            } catch (SQLException ex) {
                Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                dbDisconnect();
                try {
                    if (rs != null) {
                        rs.close();
                        rs = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (prestmt != null) {
                        prestmt.close();
                        prestmt = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    //--------------------------------------------------------------------------
    //--------------------------------INSERT------------------------------------
    //--------------------------------------------------------------------------
    
    
    
    /**
     * This method is used to create a new user
     * @param username the username of the new user
     * @param password the password of the new user
     * @return String OK if user has been added or -1
     */
    public String signin(String username, String password) {
        String result = "-1";
        PreparedStatement prestmt = null;
        ResultSet rs = null;
        if (dbConnect()) {
            try {
                String sql = "INSERT INTO T_User (username,password) values (?,?)";
                if ((prestmt = jdbcConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
                        != null) {
                    prestmt.setString(1, username);
                    prestmt.setString(2, password);
                    prestmt.executeUpdate();
                    if ((rs = prestmt.getGeneratedKeys()) != null) {
                        if (rs.next()) {
                            result = "OK";
                        }
                    }
                }
                prestmt.close();
                prestmt = null;
                rs.close();
                rs = null;
            } catch (SQLException ex) {
                Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                dbDisconnect();
                try {
                    if (rs != null) {
                        rs.close();
                        rs = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (prestmt != null) {
                        prestmt.close();
                        prestmt = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    /**
     * This method is used to add douanier logs
     * @param log the log to add
     * @param pk the primary key of the user
     * @return boolean true if the log has been added or false
     */
    public boolean addDouanierLog(String log, int pk) {
        boolean ok = false;
        PreparedStatement prestmt = null;
        ResultSet rs = null;
        if (dbConnect()) {
            try {
                String sql = "INSERT INTO T_Douanier_Log (douanier_log,FK_User) values (?,?)";
                //On essaie notre requête préparée si elle est valide et fonctionnelle.
                if ((prestmt = jdbcConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
                        != null) {
                    prestmt.setString(1, log);
                    prestmt.setInt(2, pk);
                    prestmt.executeUpdate();
                    if ((rs = prestmt.getGeneratedKeys()) != null) {
                        if (rs.next()) {
                            ok = true;
                        }
                    }
                }
                prestmt.close();
                prestmt = null;
                rs.close();
                rs = null;
            } catch (SQLException ex) {
                Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                dbDisconnect();
                try {
                    if (rs != null) {
                        rs.close();
                        rs = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (prestmt != null) {
                        prestmt.close();
                        prestmt = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return ok;
    }
    /**
     * This method is used to add etat major logs
     * @param log the log to add
     * @param pk the primary key of the user
     * @return boolean true if the log has been added or false
     */
    public boolean addEtatMajeurLog(String log, int pk) {
        boolean ok = false;
        PreparedStatement prestmt = null;
        ResultSet rs = null;
        if (dbConnect()) {
            try {
                String sql = "INSERT INTO T_Etat_Majeur_Log (etat_majeur_log,FK_User) values (?,?)";
                if ((prestmt = jdbcConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
                        != null) {
                    prestmt.setString(1, log);
                    prestmt.setInt(2, pk);
                    prestmt.executeUpdate();
                    if ((rs = prestmt.getGeneratedKeys()) != null) {
                        if (rs.next()) {
                            ok = true;
                        }
                    }
                }
                prestmt.close();
                prestmt = null;
                rs.close();
                rs = null;
            } catch (SQLException ex) {
                Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                dbDisconnect();
                try {
                    if (rs != null) {
                        rs.close();
                        rs = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (prestmt != null) {
                        prestmt.close();
                        prestmt = null;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(WrkDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return ok;
    }

}
