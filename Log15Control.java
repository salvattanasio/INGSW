package ingsftw;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Log15Control {
    private VehicleEntity v;
    private ShipmentEntity s;
    private ClientEntity c;
    private DriverEntity d;
    
    private static ResultSet rs;
    protected Statement stmt;

    
    public Log15Control(){}

    /**
     * This method opens the frame according to the pressed button.
     * @param stato
     * @param press
     */
    public void openFrame(String stato, String press){
        switch(stato){
            case "menu":
            {
                Menus menu=new Menus();             
                menu.setVisible(true);              
                break;
            }
            case "shipment":
            {
                s=ShipmentEntity.getShipmentEntity();
                Assignments assignment;
                
                if(press.equals("modify")){
                    c=ClientEntity.getClientEntity();
                    searchClient(s.getIdClient());                    
                    String currAdd=c.concatAddress(c.getAddress(), c.getCap(), c.getCity());
                    assignment=new Assignments(currAdd);      
                    
                    assignment.fillForm(s);
                    Assignments.fillForm(c);
                    assignment.createMap(currAdd);
                }
                else
                    assignment=new Assignments();
                
                
                assignment.setVisible(true);            //Sets the Assignment frame as visible.
                break;
            }
            case "vehicle":
            {
                Vehicle vehicle=new Vehicle(press);             //Creates the Vehicle frame.
                if(press.equals("modify")){
                    v=VehicleEntity.getVehicleEntity();
                    vehicle.fillForm(v);
                }
                vehicle.setVisible(true);               //Sets the Vehicle frame as visible.
                break;
            }
            /**  
              * Client and Driver frame do not exist, however the 
              * following lines will be left here in case of future implementation.
            case "client":
            {break;}
            case "driver":
            {break;}
            */
        }
    }
    
    
    
    /**
     *
     * @param vehicle
     * @param press
     */
    public void operation(Vehicle vehicle, String press){
        v=VehicleEntity.getVehicleEntity();
        
        v.setPlateNumber(vehicle.plateField.getText());
        v.setModel(vehicle.modelField.getText());
        v.setHeight(Double.valueOf(vehicle.heightField.getText()));
        v.setLength(Double.valueOf(vehicle.lengthField.getText()));
        v.setCapacity(Double.valueOf(vehicle.capacityField.getText()));
            
        switch(press){
            case("insert"):{
                insert(v);
                vehicle.refreshFields();
                v=VehicleEntity.getEmptyVehicle();
            }
            break;
            case("modify"):{
                modify(v, vehicle.oldPlateField.getText());
                openFrame("menu", "");
                vehicle.dispose();/**
                                   * dispose is only used here in order to give the user the 
                                   * possibility to insert more vehicles at a time without having to reopen this frame. 
                                   */ 
                v=VehicleEntity.getEmptyVehicle();
            }
            break;
            case("delete"):
                delete();
                v=VehicleEntity.getEmptyVehicle();
            break;        
        }
    }
   
    public void operation(Assignments assignment, String press){
        s=ShipmentEntity.getShipmentEntity();
        
        s.setWeight(Double.valueOf(Assignments.weightField.getText()));
        s.setVolume(Double.valueOf(Assignments.volField.getText()));
        s.setDepartureTime(assignment.getDT());
        s.setArrivalTime(assignment.getAT());
            
        switch(press){
            case("insert"):{
                insert(s);
                s=ShipmentEntity.getEmptyShipment();
            }
            break;
            case("modify"):{
                modify(s);
                s=ShipmentEntity.getEmptyShipment();
            }
            break;             
        }
    }
    
    
    
    public void assignNotBusy(String stato, LocalDateTime dt){
        s=ShipmentEntity.getShipmentEntity();
        
        if(stato.equals("vehicle")){
            v=VehicleEntity.getVehicleEntity();
            assignNotBusy(v, dt);
            Assignments.vehicleField.setText(v.getPlateNumber());
            s.setIdVehicle(v.getPlateNumber());
        }
        else{   //driver
            d=DriverEntity.getDriverEntity();
            assignNotBusy(d, dt);
            Assignments.driverField.setText(d.getCf());
            s.setIdDriver(d.getCf());
        }       
    }
    
    /**
     * This method is used to select a driver that is not currently busy with a delivery and assign him to a newly entered delivery.
     * @param d
     * @param data
     */
    public void assignNotBusy(DriverEntity d, LocalDateTime data){
        try{
            stmt = DatabaseConfig.getDBConnection().createStatement();
           
            String sql="SELECT CF FROM DRIVER WHERE cf not in (select iddriver from shipment ";
                   sql+="where ( extract(year from departuretime) = "+data.getYear()+" and ";
                   sql+="extract(month from departuretime) = "+data.getMonthValue()+" and ";                
                   sql+="extract(day from departuretime) = "+data.getDayOfMonth()+")) ";   
                    
            rs=stmt.executeQuery(sql);
            if(rs.next()==true){
                d.setCf(rs.getString(1));                    
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Errore: " + ex);
        }
    }
    
    /**
     * This method is used to select a vehicle that is not currently busy with a delivery and assign it to a newly entered delivery.
     * @param v
     * @param data
     */
    public void assignNotBusy(VehicleEntity v, LocalDateTime data){
        
        try{
            stmt = DatabaseConfig.getDBConnection().createStatement();
           
            String sql="select plate_number from vehicle where plate_number not in (select idvehicle from shipment ";
                   sql+="where ( extract(year from departuretime) = "+data.getYear()+" and ";
                   sql+="extract(month from departuretime) = "+data.getMonthValue()+" and ";                
                   sql+="extract(day from departuretime) = "+data.getDayOfMonth()+")) ";   
                    
            rs=stmt.executeQuery(sql);
            if(rs.next() ==true){
                v.setPlateNumber(rs.getString(1));
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Errore: " + ex);
        } 
    }
    
    public ArrayList<String> findNotBusyShip(LocalDateTime dt, double weight){
        
        String  sql="select c.address, c.cap, c.city ";
                sql+="from CLIENT c join SHIPMENT s on c.cf=s.idclient join VEHICLE v ON s.IDVEHICLE=v.PLATE_NUMBER ";
                sql+="where ( extract(year from s.departuretime) = "+dt.getYear()+" and ";
                sql+="extract(month from s.departuretime) = "+dt.getMonthValue()+" and ";                
                sql+="extract(day from s.departuretime) = "+dt.getDayOfMonth()+") ";     
                
                sql+="and ((v.CAPACITY*100)>(select SUM(weight) +" + weight;
                sql+=" FROM SHIPMENT GROUP BY s.IDSHIPMENT)) ";
                        
        
        ArrayList<String> clientAddress=new ArrayList<>();
        
        try{
            stmt = DatabaseConfig.getDBConnection().createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int col = rsmd.getColumnCount();
            while(rs.next()){                               //Iterates on ResultSet's columns.
                if(!rs.getString(1).trim().equals("")){     //Checks if the first column of resultset is empty.
                    String[] addme=new String[col];         //Addme is an array made of strings which has the same length as col.
                    for(int i=1;i<=col;i++){                //Iterates on ResultSet's columns.
                        addme[i-1]=rs.getString(i);
                    }                        
                    String dbAdd=addme[0]+",+"+addme[1]+",+"+addme[2];  
                    dbAdd=dbAdd.replace(" ","+");
                    clientAddress.add(dbAdd);
                }
            }
        }
        catch (SQLException ex){
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return(clientAddress);       
    }
    
    /**
      * Looking in the database the driver and vehicle that have been assigned to the requested delivery.
     * @param st
     * @param dt
      */
    public void assignVehicleDriverFromShipment(String [] st, LocalDateTime dt){
        s=ShipmentEntity.getShipmentEntity();
        String address=st[0].replace("'", "''");
        String cap=st[1];
        String city=st[2];
         
        String sql="select iddriver, idvehicle from shipment where idclient=(";
                sql+=" select cf from client where address='"+address+"' ";
                sql+= "and cap='"+cap+"'";
                sql+= "and city='"+city+"')";                
                sql+="and extract(year from departuretime) = "+dt.getYear()+" and ";
                sql+="extract(month from departuretime) = "+dt.getMonthValue()+" and ";                
                sql+="extract(day from departuretime) = "+dt.getDayOfMonth();     
                
        try {
            stmt = DatabaseConfig.getDBConnection().createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int col = rsmd.getColumnCount();
            while(rs.next()){                               //This while iterates on the ResultSet columns,
                if(!rs.getString(1).trim().equals("")){     //checks if the first column of ResultSet is empty.
                    String[] addme=new String[col];         //Addme is an array made of strings having the length of "col".
                    for(int i=1;i<=col;i++){                //Iterating on ResultSet columns.
                        addme[i-1]=rs.getString(i);
                    }
                    s.setIdDriver(addme[0]);
                    s.setIdVehicle(addme[1]);
                    Assignments.driverField.setText(addme[0]);
                    Assignments.vehicleField.setText(addme[1]);
                }                    
            }
        } catch (SQLException ex) {
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public void logOut(){
        Login log = new Login();
        log.setVisible(true);
        DatabaseConfig.closeConnection();
    }
    
    
    /**
     * cerca il cliente con cod fiscale cf dalla tabella.
     * @param cf
     */
    public void searchClient(String cf){
        c=ClientEntity.getClientEntity();
        try{
            stmt=DatabaseConfig.getDBConnection().createStatement();
            
            String sql="select * from client where cf='"+cf+"'" ;
            rs=stmt.executeQuery(sql);
            if(rs.next()== true){
                c.setCf(rs.getString(1));
                c.setName(rs.getString(2));
                c.setSurname(rs.getString(3));
                c.setAddress(rs.getString(4));
                c.setCap(rs.getString(5));
                c.setTelephone(rs.getString(6));
                c.setCity(rs.getString(7));
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Errore: "+ e);
        }            
    }
    
    /**
     * This method is used to insert a shipment in the database.
     * @param s
     **/
    public void insert(ShipmentEntity s){
        try{
            stmt= DatabaseConfig.getDBConnection().createStatement();
            java.sql.Timestamp departure =Timestamp.valueOf(s.getDepartureTime());
            java.sql.Timestamp arrive =Timestamp.valueOf(s.getArrivalTime());
           
            String sql="INSERT INTO shipment VALUES (";
                sql += s.getIdShip()+ ", '";
                sql += s.getIdClient() + "', '";
                sql += s.getIdDriver() + "', '";
                sql += s.getIdVehicle() +"', ";
                sql += s.getVolume() +", ";                       
                sql += s.getWeight() +", ?, ?)";
               
            PreparedStatement stat = DatabaseConfig.getDBConnection().prepareStatement(sql);
            stat.setTimestamp(1, departure);
            stat.setTimestamp(2, arrive);
            stat.execute();
            JOptionPane.showMessageDialog(null, "Operation Successful");
        } 
        catch (SQLException ex) {
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Errore: " + ex);
        }
    } 
    
    /**
     * This method is used to modify an already existing shipment in the database.
     * @param s
     */    
    public void modify(ShipmentEntity s){
        try{
            String sql ="select distinct departuretime, arrivaltime from shipment where idshipment = '";
                       sql+=s.getIdShip()+"'";
                       sql+=" and extract(year from departuretime) <= extract(year from sysdate) ";
                       sql+=" and extract(month from departuretime) <= extract(month from sysdate) ";
                       sql+=" and extract(day from departuretime) <= extract(day from sysdate) ";
                       sql+=" and extract(year from arrivaltime) >= extract(year from sysdate) ";
                       sql+=" and extract(month from arrivaltime) >= extract(month from sysdate) ";
                       sql+=" and extract(day from arrivaltime) >= extract(day from sysdate) ";
               
            stmt=DatabaseConfig.getDBConnection().createStatement();	    
            rs=stmt.executeQuery(sql);
                
            if (rs.next()){
                JOptionPane.showMessageDialog(null,"Shipment already in progress therefore not editable"); 
            }
            else{
                stmt = DatabaseConfig.getDBConnection().createStatement();
       
                sql ="UPDATE shipment SET ";
                sql+="idclient = '"+s.getIdClient()+"', ";
                sql+="iddriver = '"+s.getIdDriver()+"', "; 
                sql+="idvehicle ='" +s.getIdVehicle()+"', ";
                sql+="weight = "+s.getWeight()+", ";
                sql+="volume = " +s.getVolume();
                sql+="WHERE idshipment = "+s.getIdShip();
                    
                stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Operation Successful");
            }
        } 
        catch(SQLException ex){
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Errore: " + ex);
        } 
    }

    
    
    public void insert(VehicleEntity v){
        try{
            stmt= DatabaseConfig.getDBConnection().createStatement();
            
            String  sql="INSERT INTO VEHICLE VALUES ('";
                    sql+=v.getPlateNumber() + "','";
                    sql+=v.getModel() + "', "; 
                    sql+=v.getHeight() + ", ";
                    sql+=v.getLength() + ", ";
                    sql+=v.getCapacity() + ")";
        
            stmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Operation Successful");
        } 
        catch(SQLException ex){
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Errore: " + ex);
        }   
    }
    
    /**
     * Methode used to delete a vehicle from the database whose plate number is the same as the one given in input.
     * The vehicle can only be deleted if it is not currently busy in a delivery, in case it is the client will be informed by a message. 
     */
    public void delete(){
        v=VehicleEntity.getVehicleEntity();
        try{
            String sql ="select distinct departuretime from shipment where idvehicle = '"+v.getPlateNumber()+"'";
                   sql+=" and extract(year from departuretime) = extract(year from sysdate) ";
                   sql+=" and extract(month from departuretime) = extract(month from sysdate) ";
                   sql+=" and extract(day from departuretime) = extract(day from sysdate) ";
                   
            stmt=DatabaseConfig.getDBConnection().createStatement();	    
            rs=stmt.executeQuery(sql);
            if(rs.next()){
                JOptionPane.showMessageDialog(null,"Vehicle currently busy therefore not deleteble");
            }
            else{
                stmt = DatabaseConfig.getDBConnection().createStatement();
                sql ="DELETE VEHICLE WHERE plate_number ='"+v.getPlateNumber()+"'";
                stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Operation Successful");
            }
        }
        catch(SQLException ex){
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Errore: " + ex);
        }
    }
    
    public void modify(VehicleEntity v, String oldplate){
        try{
            stmt = DatabaseConfig.getDBConnection().createStatement();
       
            String  sql ="UPDATE VEHICLE SET plate_number = '"+v.getPlateNumber();
                    sql+="', model = '"+v.getModel(); 
                    sql+="' , height =" +v.getHeight();
                    sql+=", length ="+v.getLength();
                    sql+=", capacity ="+v.getCapacity();
                    sql+=" WHERE plate_number ='"+oldplate+"'";
        
            stmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Operation Successful");
        } 
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Errore: " + ex);
        } 
    }
    
    
    
    


}
