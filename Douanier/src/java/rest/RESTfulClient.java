/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:DBConnection [methods]<br>
 * USAGE:
 * <pre>
 *        RESTfulClient client = new RESTfulClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author dingl01
 */
public class RESTfulClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://mayencourte.emf-informatique.ch/java-etatMajor/webresources";

    public RESTfulClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("methods");
    }

    public String getDouanierLogs(String userPk) throws ClientErrorException {
        return webTarget.queryParam("pk", userPk).path("getDouanierLogs").request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String signin(Object requestEntity) throws ClientErrorException {
        return webTarget.path("signin").request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
    }

    public String getEtatMajorLogs(String userPk) throws ClientErrorException {
        return webTarget.queryParam("pk", userPk).path("getEtatMajorLogs").request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String login(String requestEntity) throws ClientErrorException {
        return webTarget.path("login").request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
    }

    public String base() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String getUserSequences(String userPk) throws ClientErrorException {
        return webTarget.queryParam("pk", userPk).path("getUserSequences").request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String addDouanierLog(Object requestEntity) throws ClientErrorException {
        return webTarget.path("addDouanierLog").request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
    }

    public void close() {
        client.close();
    }

}
