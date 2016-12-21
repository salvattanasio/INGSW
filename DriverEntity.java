package ingsftw;


public class DriverEntity {
    private static DriverEntity instance=null;
    private String cf;
    private String name;
    private String surname;
    private String address;
    private String telephone;
    private String cap;
    private String city;
    
    
    
    
    private void DriverEntity(){}
    
    public void setCf(String c){
        cf=c;
    }
    public void setName(String c){
        name=c;
    }
    public void setSurname(String c){
        surname=c;
    }
    public void setAddress(String c){
        address=c;
    }
    public void setTelephone(String c){
        telephone=c;
    }
    public void setCap(String c){
        cap=c;
    }
    public void setCity(String c){
        city=c;
    }
    
    public String getCf(){
        return(cf);
    }
    public String getName(){
        return(name);
    }
    public String getSurname(){
        return(surname);
    }
    public String getAddress(){
        return(address);
    }
    public String getCap(){
        return(cap);
    }
    public String getTelephone(){
        return(telephone);
    }
    public String getCity(){
        return(city);
    }
    
    
    
    public static DriverEntity getDriverEntity(){
        if(instance==null) 
            instance=new DriverEntity();
        
        return instance;
    } 
    
    public static DriverEntity getEmptyDriver(){
        instance=new DriverEntity();
        return instance;
    }
    
    
    
    
    
}
