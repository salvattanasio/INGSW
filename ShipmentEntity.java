package ingsftw;

import java.time.LocalDateTime;

public class ShipmentEntity {
    private static ShipmentEntity instance=null;
    private String idClient;
    private String idDriver;
    private String idVehicle;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double weight;
    private double volume;
    private String idShip;

    
        
    private void ShipmentEntity(){};
    private void ShipmentEntity(String i, String c, String d, String v, LocalDateTime dt, LocalDateTime at, double w, double vo){
        idShip=i;
        idClient=c;
        idDriver=d;
        idVehicle=v;
        departureTime=dt;
        arrivalTime=at;
        weight=w;
        volume=vo;        
    };
    
    public void setDepartureTime(LocalDateTime d){
        departureTime=d;
    }
    public LocalDateTime getDepartureTime(){
        return departureTime;
    }
    public void setArrivalTime(LocalDateTime da){
        arrivalTime=da;
    }
    public LocalDateTime getArrivalTime(){
        return arrivalTime;
    }
    public void setIdShip(String id){
        idShip=id;
    }
    public String getIdShip(){
        return idShip;
    }
    
    public void setIdClient(String c){
        idClient=c;
    }
    public void setIdDriver(String d){
        idDriver=d;
    }
    public void setIdVehicle(String v){
        idVehicle=v;
    }
    public void setWeight(double w){
        weight=w;
    }
    public void setVolume(double l){
        volume=l;
    }
    public double getWeight(){
        return weight;
    }
    public double getVolume(){
        return volume;
    }
    public String getIdClient(){
        return idClient;
    }
    public String getIdDriver(){
        return idDriver;
    }
    public String getIdVehicle(){
        return idVehicle;
    }
    
    public static ShipmentEntity getShipmentEntity(){
        if(instance==null) 
            instance=new ShipmentEntity();
        
        return instance;
    } 
    
    public static ShipmentEntity getEmptyShipment(){
        instance=new ShipmentEntity();
        return instance;
    }
    
    
    
}
