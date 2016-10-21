package ingsftw;
public class ClientEntity {
    private static ClientEntity instance=null;
    private String cf;
    private String name;
    private String surname;
    private String address;
    private String city;
    private String cap;         //String di 5 char, la 6 si usa solo per stati esteri.     
    private String telephone;   //meglio tenerlo String perchè: int pochi numeri per rappr il n_tel,
                                //double e long rischiano il data loss.
    
    private void ClientEntity(){};
    private void ClientEntity(String c, String n, String s, String a, String ca, String t, String ci){
        cf=c;
        name=n;
        surname=s;
        address=a;
        cap=ca;
        telephone=t;
        city=ci;
    };
    
    public void setCf(String c){
        cf=c;
    }
    public void setName(String n){
        name=n;
    }
    public void setSurname(String s){
        surname=s;
    }
    public void setAddress(String a){
        address=a;
    }
    public void setCap(String c){
        cap=c;
    }
    public void setTelephone(String t){
        telephone=t;
    }
    public void setCity(String t){
        city=t;
    }
    public String getCity(){
        return(city);
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
    
    
    public static ClientEntity getClientEntity(){
        if(instance==null) 
            instance=new ClientEntity();
        
        return (instance);
    }

    public static ClientEntity getEmptyClient(){
        instance=new ClientEntity();
        return (instance);
    }
}
