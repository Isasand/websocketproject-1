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

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import model.DateHandler;
import model.Sensor;

@ApplicationScoped
@ServerEndpoint("/actions") //define wesocket server endpoint

public class WebSocketServer {
    
    static String msg = ""; 
    Sensor currentSensor = new Sensor(); 
    
    @Inject
    private SessionHandler sessionHandler;
    //sessionhandler object for processing lifecycle events in each session
    
    //define lifecycle annotations
     @OnOpen
        public void open(Session session) throws SQLException {
            sessionHandler.addSession(session);
            //sessionHandler.sendData(); 
    }

    @OnClose
        public void close(Session session) throws SQLException {
            sessionHandler.removeSession(session);
    }

    @OnError
        public void onError(Throwable error) {
            Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
        public void handleMessage(String message, Session session) throws SQLException {
            msg = message; 
            try (JsonReader reader = Json.createReader(new StringReader(message))) {
                JsonObject jsonMessage = reader.readObject();
               
                if("updatedata".equals(jsonMessage.getString("action"))){
                    
                    String type = jsonMessage.getString("type"); 
                    String id = jsonMessage.getString("id");
                    String data = jsonMessage.getString("data"); 
                    String time = DateHandler.getDateString(new Date());
                    sessionHandler.sendRealTimeData(jsonMessage); 
                    sessionHandler.client.addSensorDataToDB(data, id, type, time);
                   
                    //sessionHandler.sendHistoricalJsonMsg(sessionHandler.client.getAllHistoricalDataBySensor("1"));
                }
                
                if ("updatehistorical".equals(jsonMessage.getString("action"))){
                    sessionHandler.sendHistoricalJsonMsg(sessionHandler.client.getAllHistoricalDataBySensor("1"));
               
                }
                
                
        }
}
}
        

