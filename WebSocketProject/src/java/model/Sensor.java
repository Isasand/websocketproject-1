/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javafx.util.Pair;
/**
 *
 * @author Isa
 */

//not sure if we are going to use this class
public class Sensor {

    private String id;
    private String type;
    private String currentData;
    private String time;
    private List<Pair<String, String>> historicValues = new ArrayList<>(); 
    

    public Sensor(){}; 
        
    public Sensor(String data, String id, String type, String time) {
        this.currentData = data; 
        this.time = time; 
        this.id = id; 
        this.type = type;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the data
     */
    public String getCurrentData() {
        return currentData;
    }

    /**
     * @param data the data to set
     */
    public void setCurrentData(String data) {
        this.currentData = data;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }
    
    
    public void setHistoricValues(List<Pair<String, String>> values_times){
        this.historicValues = values_times; 
    }
    
    public List<Pair<String,String>> getHistoricValues(){
        return this.historicValues; 
    }
    
    public void updateHistoricValues(Pair<String,String> value_time){
        this.historicValues.remove(0);
        this.historicValues.add(value_time); 
    }
    
}