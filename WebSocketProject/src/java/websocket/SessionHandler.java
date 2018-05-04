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
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.websocket.Session;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import model.MySQLClient;
import model.Sensor; 


@ApplicationScoped
public class SessionHandler {
    MySQLClient client = new MySQLClient(); 
    
    private final Set<Session> sessions = new HashSet<>();
    //a heashset for storing the active sessions in the application
    private final Set<Sensor> sensors = new HashSet<>(); 
    //hash set for sensors if we want to load them for the clients from db
    
    public void addSession(Session session) {
        sessions.add(session);
    }
    
    //methods for adding and removing sessions to the server
    public void removeSession(Session session) throws SQLException {
        sessions.remove(session);
        client.disconnect(); 
    }
    
    public void sendData() throws SQLException{
        Sensor sens = client.getTempData("1"); 
        JsonObject jsonMsg = createJsonMessage(sens); 
        sendToAllConnectedSessions(jsonMsg); 
    }
    
     public void sendTestData() throws SQLException{
        Sensor sens = client.getTempData("1"); 
        JsonObject jsonMsg = createJsonMessage(sens); 
        sendToAllConnectedSessions(jsonMsg); 
    }
    
    public void sendRealTimeData(JsonObject jsonMsg){
        sendToAllConnectedSessions(jsonMsg);
    }
    
    private void sendToAllConnectedSessions(JsonObject message) {
        for (Session session : sessions) {
            sendToSession(session, message);
        }
    }
    
    public void sendHistoricalJsonMsg(List<Pair<String,String>> historicList){
        JsonObject msg = createHistoricalJsonMsg(historicList); 
        sendToAllConnectedSessions(msg); 
    }
    
    private JsonObject createHistoricalJsonMsg(List<Pair<String,String>> historicList){
        JsonProvider provider = JsonProvider.provider(); 
        JsonObject msg = null; 
        int count = 0; 
        
        msg = provider.createObjectBuilder()
                .add("action", "historical")
                .add("time00", historicList.get(0).getKey())                              
                .add("data01", historicList.get(0).getValue())
                .add("time10", historicList.get(1).getKey())                              
                .add("data11", historicList.get(1).getValue())
                .add("time20", historicList.get(2).getKey())                              
                .add("data21", historicList.get(2).getValue())
                .add("time30", historicList.get(3).getKey())                              
                .add("data31", historicList.get(3).getValue())
                .add("time40", historicList.get(4).getKey())                              
                .add("data41", historicList.get(4).getValue())
                .build();
                
        
        return msg; 
    }
    
    private JsonObject createJsonMessage(Sensor s) {
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "test")
                .add("id", s.getId())
                .add("type", s.getType())
                .add("time", s.getTime())
                .add("data", s.getCurrentData())
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
    
    
    
    private JsonObject createDataMsg() throws SQLException{
        MySQLClient newClient = new MySQLClient(); 
        Sensor sens = newClient.getTempData("1"); 
        
        JsonProvider provider = JsonProvider.provider();
        JsonObject msg = provider.createObjectBuilder()
                .add("action", "data")
                .add("data",  sens.getCurrentData())
                .build(); 
        return msg;
    }
    
    
    public void sendDataMsg() throws SQLException{
        
        JsonObject jsonMsg = createDataMsg();  
        sendToAllConnectedSessions(jsonMsg); 
    }
    
}
