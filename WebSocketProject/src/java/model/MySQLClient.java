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
import java.math.BigInteger;
import java.sql.PreparedStatement;
import javafx.util.Pair;

//klass för att koppla upp oss mot mySQL 
public class MySQLClient {

    Connection con;
    Statement stmt;
    int DBLimit = 1000;
    String AllSensorId = "idSensors";

    public MySQLClient() {
        connectToDB();
    }

    //connect to database
    public void connectToDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://groupproject.cvkvpqimwsoj.eu-central-1.rds.amazonaws.com:3306/GrupparbeteDB", "masteruser", "Jason2009");
            stmt = con.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public class SensorTypes {

        private int id;
        private String sensorType;

        public SensorTypes(int id, String sensorType) {
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

    public List<SensorTypes> getSensortypes() throws SQLException {
        List<SensorTypes> typeList = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SensorTypes");
        while (rs.next()) {
            int id = rs.getInt("idtypes");
            String type = rs.getString("sensorType");
            typeList.add(new SensorTypes(id, type));
        }
        return typeList;
    }

    public List<Sensor> getAllHistoricalData() throws SQLException {

        List<Sensor> sensorList = new ArrayList<>();
        List<SensorTypes> typeList = getSensortypes();
        for (SensorTypes type : typeList) {
            sensorList.addAll(getData(DBLimit, AllSensorId));
        }

        return sensorList;
    }
    

    public List<Pair<String, String>> getAllHistoricalDataBySensor(String sensorId) throws SQLException {

        List<Pair<String, String>> historicValues = new ArrayList<>();
        List<Sensor> sensorList = getData(5,sensorId);
        int i= 0;
       
        for (Sensor sensor : sensorList) {
            historicValues.add(new Pair <String,String> (sensor.getTime(), sensor.getCurrentData()));           
        }

        return historicValues;
    }

    public List<Sensor> getAllHistoricalDataLimit(int limit) throws SQLException {

        List<Sensor> sensorList = new ArrayList<>();
        List<SensorTypes> typeList = getSensortypes();
        for (SensorTypes type : typeList) {
            sensorList.addAll(getData(limit, AllSensorId));
        }

        return sensorList;
    }

    public List<Sensor> getAllHistoricalDataLimitAndSensor(int limit,String sensorId) throws SQLException {

        List<Sensor> sensorList = new ArrayList<>();
        List<SensorTypes> typeList = getSensortypes();
        for (SensorTypes type : typeList) {
            sensorList.addAll(getData(limit, sensorId));
        }

        return sensorList;
    }

    public List<Sensor> getAllData() throws SQLException {
        List<Sensor> sensorList = getData(DBLimit, AllSensorId);
        return sensorList;
    }

    public List<Sensor> getData(int amount, String sensorId) throws SQLException {
        List<Sensor> sensorList = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT idSensorS,sensorData,sensorTimestamp,sensorType FROM Sensor,SensorTypes,Sensors\n"
                + "where idSensor = idSensors AND sensorTypeNr = idtypes and idsensor = " + sensorId + "\n"
                + "order by idSensorS,sensorTimestamp desc limit " + amount);

        while (rs.next()) {
            String id = rs.getString("idSensors");
            String data = rs.getString("sensorData");
            String time = rs.getString("sensorTimestamp");
            String type = rs.getString("sensorType");
            sensorList.add(new Sensor(data, id, type, time));
        }
        return sensorList;
    }
    

    public Sensor getTempData(String sensorId) throws SQLException  {
        Sensor tempSensor = new Sensor();
        List<Sensor> sensorList= getAllHistoricalDataLimitAndSensor(1,sensorId);
        for(Sensor sensor : sensorList){
            tempSensor.setCurrentData(sensor.getCurrentData());
            tempSensor.setHistoricValues(getAllHistoricalDataBySensor(sensorId));
            tempSensor.setId(sensor.getId());
            tempSensor.setType(sensor.getType());
            tempSensor.setTime(sensor.getTime());
        }
      return tempSensor;

    }
    
    public void addSensorDataToDB (String data,String id,String type,String time) throws SQLException{
        int inNr = Integer.parseInt(id);    
        String sql = "INSERT INTO Sensor (sensorData,sensorTimestamp,idSensorS)" +
                    "VALUES (?,?,?)";
                    
        Statement stmt = con.createStatement();
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, data);
        preparedStatement.setString(2, time);
        preparedStatement.setString(3, id);
        preparedStatement.executeUpdate(); 
//        stmt.executeQuery("INSERT INTO `Sensor` (`sensorData`, `sensorTimestamp`, `idSensorS`) VALUES ('33','2018-04-27 12:12:33', '1');");
        
    }
    public void disconnect() throws SQLException{
        con.close(); 
    }
    
}
