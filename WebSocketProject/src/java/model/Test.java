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
        List<Sensor> AllsensorListHistorical = client.getAllHistoricalData();
        Sensor s = client.getTempData("1");
        
        System.out.println("TypeList----------------------");
         for(SensorTypes type : typeList) {
            System.out.println(type.getId());
            System.out.println(type.getSensorType());           
        }
          System.out.println("\n---------------------------");
         
          System.out.println("SensorList--------------------");
         for(Sensor sensor : sensorList){
            
            System.out.println(sensor.getId());
            System.out.println(sensor.getCurrentData());
             System.out.println(sensor.getTime());
             System.out.println(sensor.getType());         
             
         }
         System.out.println("\n-----------------------------");
         
         System.out.println("SensorListHistorical-----------");
          for(Sensor sensor : AllsensorListHistorical){            
            System.out.println(sensor.getId());
            System.out.println(sensor.getCurrentData());
             System.out.println(sensor.getTime());
             System.out.println(sensor.getType());      
          }
         System.out.println("\n-----------------------------");
         
         System.out.println("Sensor-------------------------");
                    
            System.out.println(s.getId());
            System.out.println(s.getType());
             System.out.println(s.getTime());
             System.out.println(s.getCurrentData());      
            System.out.println("\n------------------------------");
         
         client.con.close();
    }

   
   
}

  
