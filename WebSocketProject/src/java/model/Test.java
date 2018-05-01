/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.MySQLClient.SensorTypes;

/**
 *
 * @author mejla
 */
public class Test {
    static MySQLClient client = new MySQLClient(); 
    
    public static void main(String[] args) throws SQLException{
        List<SensorTypes> typeList = client.getSensortypes();
        List<Sensor> sensorList = client.getAllData();
        
         for(SensorTypes type : typeList) {
            System.out.println("TypeList------------------------------------------------------");
            System.out.println(type.getId());
            System.out.println(type.getSensorType());
            System.out.println("\n-------------------------------------------------------------");
        }
         
         for(Sensor sensor : sensorList){
            System.out.println("SensorList------------------------------------------------------");
            System.out.println(sensor.getId());
            System.out.println(sensor.getData());
             System.out.println(sensor.getTime());
             System.out.println(sensor.getType());
            System.out.println("\n-------------------------------------------------------------");
             
         }
        
         
    }

   
   
}
