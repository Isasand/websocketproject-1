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
    
    public MySQLClient(){
        connectToDB();
    }
    
    //connect to database
    public void connectToDB(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://groupproject.cvkvpqimwsoj.eu-central-1.rds.amazonaws.com:3306/sensordata", "masteruser", "Jason2009");
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
    
}
