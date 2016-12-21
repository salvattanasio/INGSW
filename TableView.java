package ingsftw;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TableView {
    private static ResultSet rs;
    protected Statement stmt;
    
    private ClientEntity c;
    private VehicleEntity v;
    private ShipmentEntity s;
    private DriverEntity d;
    
    
    public TableView(){}
    
    
    
    
    /**
     *  Builds the model of the table that will be showed in the frame.
     * @param s
     * @return 
    */
    public DefaultTableModel getModel(String s){
        DefaultTableModel model=null;
        Object [][] rowdata = {};
        
        switch(s){
            case("shipment"):
            {    
                Object [] shipmentT={"ID Ship","ID Client","ID Driver","ID Vehicle","Volume","Weight","Departure","Arrival"};
                model = new DefaultTableModel(rowdata, shipmentT){};  
                break;
            }
            case("vehicle"):
            {
                Object [] vehicleT={"Plate number","Model","Height","Length","Capacity"};
                model = new DefaultTableModel(rowdata, vehicleT){};
                break;
            }
            case("driver"):
            {
                Object [] driverT={"CF","Name","Surname","Address","Tel","CAP","City"};
                model = new DefaultTableModel(rowdata, driverT){};
                break;
            }
            case("client"):
            {
                Object [] clientT={"CF", "Name","Surname","Address","CAP","tel","City"};
                model = new DefaultTableModel(rowdata, clientT){};
                break;
            }
        }
        return(model);
    }
    
    /**
     * Makes a query to the database in order to fill the table.
     * @param sql
     * @param model
    */
    public void fillModel(String sql, DefaultTableModel model){
        try{
            //stmt=DatabaseConfig.defaultConnection.createStatement();	    
            stmt=DatabaseConfig.getDBConnection().createStatement();	    
            rs=stmt.executeQuery(sql);
            
            ResultSetMetaData rsmd = rs.getMetaData();
            int col = rsmd.getColumnCount();
            while(rs.next()){   
                if(!rs.getString(1).trim().equals("")){
                    String[] addme=new String[col];
                    for(int i=1;i<=col;i++)
                        addme[i-1]=rs.getString(i);
                    model.addRow(addme);
                }
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Errore: "+ e);
        }
    }
        
    /**
     * Makes a query to the database in order to show on the table the requested values.
     * @param s
     * @param search
     * @param nome
    */    
    public void doSearch(String s, String search, javax.swing.JTable nome){
        DefaultTableModel model=getModel(s);    //Makes a call to the method that build the model of the table.
	nome.setModel(model);
        String sql=null;
        
        switch(s){
            case("vehicle"):{
                sql="SELECT * FROM "+s+" where plate_number like '%"+search+"%'or model like '%"+search+"%'";
                try{
                    double f=Double.valueOf(search);
                    sql+="or height ="+ f+"or length ="+f+"or capacity ="+f;
                    sql+=" order by plate_number";
                }
                catch (NumberFormatException e){}
                break;
            }
            case("client"):{
                sql="SELECT * FROM "+s+" where cf like '%"+search+"%'or name like '%"+search;
                sql+="%'or surname like '%"+search+"%'or address like '%"+search;
                sql+="%'or cap like '%"+search+"%'or telephone like '%"+search;  
                sql+="%'or city like '%"+search+"%'"; 
                sql+=" order by cf";
                break;
            }
            case("driver"):{
                sql="SELECT * FROM "+s+" where cf like '%"+search+"%'or name like '%"+search;
                sql+="%'or surname like '%"+search+"%'";
                sql+=" order by cf";
                break;
            }
            case("shipment"):{
                sql="SELECT * FROM "+s+" where idclient like '%"+search+"%'or iddriver like '%"+search;
                sql+="%'or idvehicle like '%"+search+"%'";
                try{
                    //search.trim();
                    sql+="or to_char(departuretime, 'YYYY-MM-DD HH:MI:SSXFF3') like '" + search + "%'";
                    double f=Double.valueOf(search);
                    sql+="or weight ="+ f + "or volume =" + f;
                }
                catch (NumberFormatException e){}
                sql+=" order by idshipment";
                break;
            }
        }        
        fillModel(sql,model);   //Makes a call to the method that fills the table with data obtained from the database.
        nome.requestFocus();
    }
        
    /**
     * Makes a query on the database to show all the data on the table.
     * @param s
     * @param nome
    */
    public void tableFields(String s, javax.swing.JTable nome){
        String sql;
        DefaultTableModel model=getModel(s);    //Uses the model of the table which is to be filled.
	nome.setModel(model);
        if(s.equals("shipment")){   //Shipment
           
            sql="SELECT * FROM shipment WHERE ";
            sql+="(extract(year from departuretime) = extract(year from sysdate) and ";
            sql+="extract(month from departuretime) = extract(month from sysdate) and ";
            sql+="extract(day from departuretime) >= extract(day from sysdate)) order by idshipment";           
        }
        else{   //client, driver, vehicle.
            sql="SELECT * FROM " + s;
            sql+=" order by "+nome.getColumnName(0).replace(" ","_");
        }
        fillModel(sql, model);      //Fills the table with the values returned from a query on the database.
        nome.requestFocus();
    }    
    
    public void setFromTable(String x, javax.swing.JTable nome){
        switch(x){
            case("vehicle"): 
                v=VehicleEntity.getVehicleEntity();
                setFromTable(v, nome);
            break;
            case("shipment"): 
                s=ShipmentEntity.getShipmentEntity();
                setFromTable(s, nome);
            break;
            case("client"):
                c=ClientEntity.getClientEntity();
                setFromTable(c, nome);
            break;
            case("driver"): 
                d=DriverEntity.getDriverEntity();
                setFromTable(d, nome);
            break;
        }
    
    }

    /**
     * This method gets an instance from the table "client" and sets the attributes with the values returned from it.
     * @param c
     * @param nome
     */
    public void setFromTable(ClientEntity c, javax.swing.JTable nome){
            //c=ClientEntity.getClientEntity();
            
            c.setCf(nome.getValueAt(nome.getSelectedRow(), 0).toString());
            c.setName(nome.getValueAt(nome.getSelectedRow(), 1).toString());
            c.setSurname(nome.getValueAt(nome.getSelectedRow(), 2).toString());
            c.setAddress(nome.getValueAt(nome.getSelectedRow(), 3).toString());
            c.setCap(nome.getValueAt(nome.getSelectedRow(), 4).toString());
            c.setTelephone(nome.getValueAt(nome.getSelectedRow(), 5).toString());
            c.setCity(nome.getValueAt(nome.getSelectedRow(), 6).toString());            
    }
    
    public void setFromTable(DriverEntity d, javax.swing.JTable nome){
            //d=DriverEntity.getDriverEntity();
            
            d.setCf(nome.getValueAt(nome.getSelectedRow(), 0).toString());
            d.setName(nome.getValueAt(nome.getSelectedRow(), 1).toString());
            d.setSurname(nome.getValueAt(nome.getSelectedRow(), 2).toString());
            d.setAddress(nome.getValueAt(nome.getSelectedRow(), 3).toString());
            d.setCap(nome.getValueAt(nome.getSelectedRow(), 4).toString());
            d.setTelephone(nome.getValueAt(nome.getSelectedRow(), 5).toString());
            d.setCity(nome.getValueAt(nome.getSelectedRow(), 6).toString());            
    }
    
    public void setFromTable(VehicleEntity v, javax.swing.JTable nome){
            //v=VehicleEntity.getVehicleEntity();
            
            v.setPlateNumber(nome.getValueAt(nome.getSelectedRow(), 0).toString());
            v.setModel(nome.getValueAt(nome.getSelectedRow(), 1).toString());
            v.setHeight(Double.valueOf(nome.getValueAt(nome.getSelectedRow(), 2).toString()));
            v.setLength(Double.valueOf(nome.getValueAt(nome.getSelectedRow(), 3).toString()));
            v.setCapacity(Double.valueOf(nome.getValueAt(nome.getSelectedRow(), 4).toString()));
    }
    
    /**
     *   This method gets an instance from the table "shipment" and sets the fields with the values rsturned from it.
     * @param s
     * @param nome
    **/
    public void setFromTable(ShipmentEntity s, javax.swing.JTable nome){
            //s=ShipmentEntity.getShipmentEntity();
            
            s.setIdShip(nome.getValueAt(nome.getSelectedRow(), 0).toString());
            s.setIdClient(nome.getValueAt(nome.getSelectedRow(), 1).toString());
            s.setIdDriver(nome.getValueAt(nome.getSelectedRow(), 2).toString());
            s.setIdVehicle(nome.getValueAt(nome.getSelectedRow(), 3).toString());
            s.setVolume(Double.valueOf(nome.getValueAt(nome.getSelectedRow(), 4).toString()));
            s.setWeight(Double.valueOf(nome.getValueAt(nome.getSelectedRow(), 5).toString()));
            try{
                s.setDepartureTime(Timestamp.valueOf((nome.getValueAt(nome.getSelectedRow(), 6).toString())).toLocalDateTime());
                s.setArrivalTime(Timestamp.valueOf((nome.getValueAt(nome.getSelectedRow(), 7).toString())).toLocalDateTime());
            }
            catch(IllegalArgumentException e){}
    }
    
    public void openFrame(String stato){
        RegisteredUserList l=new RegisteredUserList(stato);
        l.setVisible(true);             
    }
    
    
    /**
     * This method is used to fill the waypoint table with the waypoints.
     * @param A
     * @param nome
     */
    public void fillWaypointTable(ArrayList<ArrayList<String>> A, javax.swing.JTable nome){
        c=ClientEntity.getClientEntity();
        
        String col[]={"Alternative Path"};
        DefaultTableModel model=new DefaultTableModel(col,0);
        nome.setModel(model);
        
        for(int i=0; i<A.size(); i++){
            String path="MSA -> ";      //indirizzo del sorgente.
            for(int j=0; j<A.get(i).size(); j++){
                path+=A.get(i).get(j)+" -> ";
                path=path.replace("+"," ");
            }
            path+=c.getAddress();   //indirizzo del destinatario.
            Object[] obj={path};
            model.addRow(obj);
        }
        nome.requestFocus();
        
        /** setto la prima riga della tabella "nome" selezionata. */
        nome.changeSelection(0, 0, false, false);
    }
    
    
    
     
    public void confirmButton(String stato){
        s=ShipmentEntity.getShipmentEntity();
        
        switch(stato){
            case("client"):{
                c=ClientEntity.getClientEntity();
                Assignments.fillForm(c);
                s.setIdClient(c.getCf());        
                break;
            }
            case("driver"):{
                d=DriverEntity.getDriverEntity();
                Assignments.driverField.setText(d.getCf());
                s.setIdDriver(d.getCf());
                break;
            }
            case("vehicle"):{
                v=VehicleEntity.getVehicleEntity();
                Assignments.vehicleField.setText(v.getPlateNumber());
                s.setIdVehicle(s.getIdVehicle());
                break;
            }            
        }
    }
}
