package ingsftw;

public class VehicleEntity {
    private static VehicleEntity instance=null;
    private String plateNumber;
    private String model;
    private double length;  
    private double height;
    private double capacity;
    
    private void VehicleEntity(){};        
    private void VehicleEntity(String p, String m, double l, double h, double c){        
        plateNumber=p;
        model=m;
        length=l;
        height=h;
        capacity=c;
    };
    
    public void setPlateNumber(String p){
        plateNumber=p;
    }     
    public void setModel(String m){
        model=m;
    }     
    public void setLength(double l){
        length=l;
    }     
    public void setHeight(double h){
        height=h;
    }     
    public void setCapacity(double h){
        capacity=h;
    }     
    public double getCapacity(){
        return (capacity);
    }    
    public String getPlateNumber(){
        return (plateNumber);
    }     
    public String getModel(){
        return(model);
    }     
    public double getLength(){
        return(length);
    }     
    public double getHeight(){
        return(height);
    } 

    /*
     * PATTERN SINGLETON, fà in modo che ci sia sempre una sola istanza di Vehicle.
    */
    public static VehicleEntity getVehicleEntity(){
        if(instance==null) 
            instance=new VehicleEntity();
        
        return (instance);
    } 
    
    public static VehicleEntity getEmptyVehicle(){
        instance=new VehicleEntity();
        VehicleEntity.getEmptyVehicle();
        
        return (instance);
    }
}
