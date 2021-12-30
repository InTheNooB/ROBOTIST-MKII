package servlet;

import ctrl.Ctrl;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author dingl01
 */
public class Servlet extends HttpServlet {

    private final Ctrl ctrl = new Ctrl();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        HttpSession session = request.getSession();

        String jsonResponse = "{";
        if (request.getParameter("action") != null) {
            switch (request.getParameter("action")) {
                case "getUserSequences":
                    if (isLoggedIn(session)) {
                        ArrayList<String> sequences = ctrl.getUserSequences((Integer) session.getAttribute("userPk"));
                        String jsonSequences = "";
                        for (String sequence : sequences) {
                            jsonSequences += ",\"" + sequence + "\"";
                        }
                        if (jsonSequences.length() > 0) {
                            jsonSequences = jsonSequences.substring(1);
                        }
                        jsonResponse += "\"sequences\" : [" + jsonSequences + "],";
                    } else {
                        jsonResponse = appendJSONAccessRefused(jsonResponse);
                    }
                    break;
                case "getDouanierLogs":
                    ArrayList<String> logs;
                    if (isLoggedIn(session)) {
                        logs = ctrl.getDouanierLogs((Integer) session.getAttribute("userPk"));
                    } else {
                        logs = ctrl.getDouanierLogs(25);
                    }
                    String jsonLogs = "";
                    for (String log : logs) {
                        jsonLogs += ",\"" + log + "\"";
                    }
                    if (jsonLogs.length() > 0) {
                        jsonLogs = jsonLogs.substring(1);
                    }
                    jsonResponse += "\"douanierLogs\" : [" + jsonLogs + "],";
                    break;
                case "getEtatMajorLogs":
                    if (isLoggedIn(session)) {
                        logs = ctrl.getEtatMajorLogs((Integer) session.getAttribute("userPk"));
                        jsonLogs = "";
                        for (String log : logs) {
                            jsonLogs += ",\"" + log + "\"";
                        }
                        if (jsonLogs.length() > 0) {
                            jsonLogs = jsonLogs.substring(1);
                        }
                        jsonLogs = jsonLogs.substring(1);
                        jsonResponse += "\"etatMajorLogs\" : [" + jsonLogs + "],";
                    } else {
                        jsonResponse = appendJSONAccessRefused(jsonResponse);
                    }
                default:
                    jsonResponse = appendJSONUnknownAction(jsonResponse);
                    break;
            }
        } else {
            jsonResponse = appendJSONUnknownAction(jsonResponse);
        }

        response.setContentType("text/plain;charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        if (jsonResponse.length() != 1) {
            jsonResponse = jsonResponse.substring(0, jsonResponse.length() - 1);
        }

        jsonResponse += "}";
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        HttpSession session = request.getSession();

        String jsonResponse = "{";
        if (request.getParameter("action") != null) {
            String username, password;
            switch (request.getParameter("action")) {
                case "login":
                    if (isLoggedIn(session)) {
                        jsonResponse += "\"result\":\"OK\",";
                        jsonResponse += "\"userPk\":\"" + session.getAttribute("userPk") + "\",";
                        jsonResponse += "\"description\":\"Already connected\",";
                        break;
                    }
                    username = request.getParameter("username");
                    password = request.getParameter("password");
                    if (username != null && password != null) {
                        int userPk = ctrl.login(username, password);
                        if (userPk != -1) {
                            jsonResponse += "\"result\":\"OK\",";
                            jsonResponse += "\"userPk\":\"" + userPk + "\",";
                            jsonResponse += "\"description\":\"Successfully connected\",";
                            session.setAttribute("loggedIn", true);
                            session.setAttribute("userPk", userPk);
                        } else {
                            jsonResponse += "\"result\":\"KO\",";
                            jsonResponse += "\"description\":\"Wrong credentials\",";
                        }
                    } else {
                        jsonResponse = appendJSONError(jsonResponse);
                    }
                    break;
                case "register":
                    username = request.getParameter("username");
                    password = request.getParameter("password");
                    if (username != null && password != null) {
                        boolean created = ctrl.register(username, password);
                        if (created) {
                            jsonResponse += "\"result\":\"OK\",";
                            jsonResponse += "\"description\":\"User created\",";
                        } else {
                            jsonResponse = appendJSONError(jsonResponse);
                        }
                    }
                    break;
                case "logout":
                    if (isLoggedIn(session)) {
                        session.invalidate();
                        jsonResponse += "\"result\":\"OK\",";
                        jsonResponse += "\"description\":\"Session killed\",";
                    } else {
                        jsonResponse += "\"result\":\"KO\",";
                        jsonResponse += "\"description\":\"Not logged in\",";
                    }
                    break;
                case "moveRobot":
                    if (isLoggedIn(session)) {
                        try {
                            int leftTrackSpeed = Integer.parseInt(request.getParameter("leftTrackSpeed"));
                            int rightTrackSpeed = Integer.parseInt(request.getParameter("rightTrackSpeed"));
                            if (!ctrl.moveRobot(leftTrackSpeed, rightTrackSpeed)) {
                                jsonResponse = appendJSONError(jsonResponse);
                            } else {
                                jsonResponse += "\"result\":\"OK\",";
                                jsonResponse += "\"description\":\"Robot Moved\",";
                            }
                        } catch (NumberFormatException e) {
                            jsonResponse = appendJSONError(jsonResponse);
                        }
                    } else {
                        jsonResponse = appendJSONAccessRefused(jsonResponse);
                    }
                    break;
                case "playSequence":
                    if (isLoggedIn(session)) {
                        try {
                            String sequence = request.getParameter("sequence");
                            boolean played = ctrl.playSequence((int) session.getAttribute("userPk"), sequence);
                            if (played) {
                                jsonResponse += "\"result\":\"OK\",";
                                jsonResponse += "\"description\":\"Sequence started\",";
                            } else {
                                jsonResponse = appendJSONError(jsonResponse);
                            }
                        } catch (NumberFormatException e) {
                            jsonResponse = appendJSONError(jsonResponse);
                        }
                    } else {
                        jsonResponse = appendJSONAccessRefused(jsonResponse);
                    }
                    break;
                default:
                    jsonResponse = appendJSONUnknownAction(jsonResponse);
                    break;
            }
        } else {
            jsonResponse = appendJSONUnknownAction(jsonResponse);
        }
        if (jsonResponse.length() != 1) {
            jsonResponse = jsonResponse.substring(0, jsonResponse.length() - 1);
        }
        jsonResponse += "}";
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }

    /**
     * Checks if the specified session contains data confirming that this is a
     * logged user.
     *
     * @param session The session containing the data
     * @return True if the user is logged, false otherwise.
     */
    private boolean isLoggedIn(HttpSession session) {
        boolean logged = false;
        try {
            logged = (boolean) session.getAttribute("loggedIn");
        } catch (Exception e) {
        }
        return logged;
    }

    /**
     * Appends an error "error" to a json response
     *
     * @param jsonResponse The response
     * @return The modified response.
     */
    private String appendJSONError(String jsonResponse) {
        jsonResponse += "\"result\":\"KO\",";
        jsonResponse += "\"description\":\"An error has occured\",";
        return jsonResponse;
    }

    /**
     * Appends an error "access refused" to a json response
     *
     * @param jsonResponse The response
     * @return The modified response.
     */
    private String appendJSONAccessRefused(String jsonResponse) {
        jsonResponse += "\"result\":\"KO\",";
        jsonResponse += "\"description\":\"Access refused\",";
        return jsonResponse;
    }

    /**
     * Appends an error "unknown action" to a json response
     *
     * @param jsonResponse The response
     * @return The modified response.
     */
    private String appendJSONUnknownAction(String jsonResponse) {
        jsonResponse += "\"result\":\"KO\",";
        jsonResponse += "\"description\":\"Unknown action\",";
        return jsonResponse;
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
