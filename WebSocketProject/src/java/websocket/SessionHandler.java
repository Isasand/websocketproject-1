/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket;

/**
 *
 * @author Isa
 */
import java.io.IOException;
import java.sql.SQLException;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.Session;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import model.MySQLClient;
import model.Sensor; 


@ApplicationScoped
public class SessionHandler {
    static MySQLClient client = new MySQLClient(); 
    
    private final Set<Session> sessions = new HashSet<>();
    //a heashset for storing the active sessions in the application
        
    public void addSession(Session session) {
        sessions.add(session);
    }
    //methods for adding and removing sessions to the server
    public void removeSession(Session session) {
        sessions.remove(session);
    }
    
    public void sendData() throws SQLException{
        Sensor sens = client.getTempData(); 
        JsonObject jsonMsg = createJsonMessage(sens); 
        sendToAllConnectedSessions(jsonMsg); 
    }
    
    private void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }
    
    private JsonObject createJsonMessage(Sensor s) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "tempdata")
                .add("id", s.getId())
                .add("type", s.getType())
                .add("time", s.getTime())
                .add("data", s.getData())
                .build();
        return msg;
    }
    
    
    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(SessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
