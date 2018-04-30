/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Sensor;

//klass för att koppla upp oss mot mySQL 
public class MySQLClient {
    Connection con; 
    Statement stmt;
    
    public  MySQLClient(){
        connectToDB();
    }
    
    //connect to database
    public void connectToDB(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://groupproject.cvkvpqimwsoj.eu-central-1.rds.amazonaws.com:3306/GrupparbeteDB", "masteruser", "Jason2009");
            stmt = con.createStatement();
        }
        catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    
    public Sensor getTempData() throws SQLException{
        List<Sensor> sensorList = new ArrayList<>();
        Statement stmt = con.createStatement(); 
        ResultSet rs = stmt.executeQuery("SELECT * FROM data ORDER BY time DESC");
        Sensor s = new Sensor("hej", null, null, null); 
        
        while(rs.next()){
            String time = rs.getString("time");
            String type = rs.getString("type");
            String data = rs.getString("data");
            String id = rs.getString("id"); 
            s = new Sensor(data, id, time, type);
        } 
        return s;
    }
    
    public class SensorTypes {
        private int id;
        private String sensorType;

        public SensorTypes(int id,String sensorType) {         
            this.id = id;
            this.sensorType = sensorType;
        }

        /**
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * @return the sensorType
         */
        public String getSensorType() {
            return sensorType;
        }

        /**
         * @param sensorType the sensorType to set
         */
        public void setSensorType(String sensorType) {
            this.sensorType = sensorType;
        }

    }
    public List<SensorTypes> getSensortypes() throws SQLException{
        List<SensorTypes> typeList = new ArrayList<>();
        
        Statement stmt = con.createStatement(); 
        ResultSet rs = stmt.executeQuery("SELECT * FROM SensorTypes");
        
        while(rs.next()){
            int id = rs.getInt("idtypes");            
            String type = rs.getString("type");
            
            typeList.add(new SensorTypes(id,type));
        } 
        return typeList;
    }
    
    
    
    
    public List<Sensor> getAllData() throws SQLException{
//        private String id;
//        private String type;
//        private String data;
//        private String time;
        List<Sensor> sensorList = new ArrayList<>();
        
        Statement stmt = con.createStatement(); 
        ResultSet rs = stmt.executeQuery("SELECT sensorIndex,sensorData,type, sensorTimestamp FROM Sensor,SensorTypes,Sensors\n" +
"where idSensor = idSensors AND sensorType = idtypes;;");
        
        while(rs.next()){
            String id = rs.getString("sensorIndex");
            String data = rs.getString("sensorData");
            String time = rs.getString("sensorTimestamp");
            
            String type = rs.getString("type");
            
            
             
            sensorList.add(new Sensor(data, id, time, type));
        } 
        return sensorList;
    }
    
    public List<Sensor> getHistoricalData(int amount) throws SQLException{
        List<Sensor> sensorList = new ArrayList<>();
        Statement stmt = con.createStatement(); 
        ResultSet rs = stmt.executeQuery("SELECT * FROM data");
        
        while(rs.next()){
            String time = rs.getString("time");
            
            String type = rs.getString("type");
            String data = rs.getString("data");
            
            String id = rs.getString("id"); 
            sensorList.add(new Sensor(data, id, time, type));
        } 
        return sensorList;
    }
    
}
