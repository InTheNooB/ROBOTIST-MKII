/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import wrk.WrkDatabase;

/**
 * REST Web Service
 *
 * @author maye
 */
@Path("methods")
public class RestFulServer {

    @Context
    private UriInfo context;
    private WrkDatabase wrk;

    /**
     * Creates a new instance of DBConnection
     */
    public RestFulServer() {
        this.wrk = new WrkDatabase();

    }

    /**
     * Base method to check if the rest is working
     * @return String 
     */
    @GET
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String base() {
        return "{Working}";
    }

    /**
     * This method is used to retrieve the sequences related to the specified
     * user.
     *
     * @param json the user primary key
     * @return String list of sequences
     */
    @GET
    @Path("getUserSequences")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String getUserSequence(@QueryParam("pk") String json) {
        Gson builder = new Gson();
        ArrayList<String> sequence = wrk.getUserSequences(Integer.parseInt(builder.fromJson(json, String.class)));
        return builder.toJson(sequence);
    }

    /**
     * This method is used to retrieve the logs from the douanier related to the
     * specified user.
     *
     * @param json the user primary key
     * @return String list of logs
     */
    @GET
    @Path("getDouanierLogs")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String getDouanierLog(@QueryParam("pk") String json) {
        Gson builder = new Gson();
        ArrayList<String> sequence = wrk.getLogDouanier(Integer.parseInt(builder.fromJson(json, String.class)));
        return builder.toJson(sequence);
    }

    /**
     * This method is used to retrieve the logs from the etat major related to the
     * specified user.
     *
     * @param json the user primary key
     * @return String list of logs
     */
    @GET
    @Path("getEtatMajorLogs")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String getEtatMajorLog(@QueryParam("pk") String json) {
        Gson builder = new Gson();
        ArrayList<String> sequence = wrk.getLogEtatMajeur(Integer.parseInt(builder.fromJson(json, String.class)));
        return builder.toJson(sequence);
    }

    
    /**
     * This method is used to check login.
     * @param json contains the username and the password
     * @return String the user primary key if login is ok and -1 if not
     */
    @POST
    @Path("login")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String login(String json) {
        Gson builder = new Gson();
        HashMap<String, String> s = builder.fromJson(json, HashMap.class);
        return builder.toJson(wrk.login(s.get("username"), s.get("password")));

    }

        /**
     * This method is used to sign in login.
     * @param json contains the username and the password
     * @return String OK if the user is  added or -1
     */
    @POST
    @Path("signin")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String signin(String json) {
        Gson builder = new Gson();
        HashMap<String, String> s = builder.fromJson(json, HashMap.class);
        return builder.toJson(wrk.signin(s.get("username"), s.get("password")));
    }

    /**
     * This method is used to add douanier logs.
     * @param json contains the log and the primary key of the user
     * @return String true or false 
     */
    @POST
    @Path("addDouanierLog")
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String addDouanierLog(String json) {
        Gson builder = new Gson();
        HashMap<String, String> s = builder.fromJson(json, HashMap.class);
        return builder.toJson(wrk.addDouanierLog(s.get("log"), Integer.parseInt(s.get("pk"))));
    }

    
}
