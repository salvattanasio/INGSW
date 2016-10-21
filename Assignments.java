package ingsftw;


import com.google.maps.model.LatLng;
import static ingsftw.Maps.encode;
import static ingsftw.Menus.stato;  ///=> si traduce in dipendenza
import static ingsftw.Menus.press;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Assignments extends javax.swing.JFrame {
    private ClientEntity c;
    private ShipmentEntity s;
    private VehicleEntity v;
    private ArrayList<ArrayList<LatLng>> parts;
    
    private static String data="dd/M/y  H:m:s";
    private static int id;
    protected Statement stmt;
    private static ResultSet rs;
    
    private int k = 0;
    private int tot = 1;
    private BufferedImage img;
    private int lastx, lasty;
    private int posx, posy;
    
    public Assignments(){
        initComponents();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("truck1.jpg")));  
        
        c=ClientEntity.getClientEntity();
        s=ShipmentEntity.getShipmentEntity();
        parts = new ArrayList<ArrayList<LatLng>>();
        
        backButton.setVisible(false);
        forwardButton.setVisible(false);
        slideLabel.setVisible(false);
        setVisibleButton(press);
    }
    
    public void setVisibleButton(String p){
        if(p.equals("insert"))
        {
            LocalDateTime dt=LocalDateTime.now();
            departureTimeField.setText(dt.format(DateTimeFormatter.ofPattern(data)));
            s.setDepartureTime(dt);
            //System.out.println(dt);
            
            mapsLabel.setVisible(false);
            defineShipmentButton.setVisible(true);
            ArrivalTimeField.setVisible(false);
            ArrivalTimeLabel.setVisible(false);
            driverField.setVisible(false);
            driverLabel.setVisible(false);
            driverModifyButton.setVisible(false);
            vehicleField.setVisible(false);
            vehicleLabel.setVisible(false);
            vehicleModifyButton.setVisible(false);
            durationField.setVisible(false);
            durationLabel.setVisible(false);
            confirmButton.setVisible(false);
            shipPanel.setVisible(false);
        }
        else
        {
            mapsLabel.setVisible(true);
            defineShipmentButton.setVisible(false);
            ArrivalTimeField.setVisible(true);
            ArrivalTimeLabel.setVisible(true);
            driverField.setVisible(true);
            driverLabel.setVisible(true);
            driverModifyButton.setVisible(true);
            vehicleField.setVisible(true);
            vehicleLabel.setVisible(true);
            vehicleModifyButton.setVisible(true);
            durationField.setVisible(true);
            durationLabel.setVisible(true);
            confirmButton.setVisible(true);
            shipPanel.setVisible(true);
        }
    } 
    /**
     *  RIEMPIE I FIELD RELATIVI AL CLIENTE.
     * @param c
     **/
    public static void fillForm(ClientEntity c){
        cfField.setText(c.getCf());
        nameField.setText(c.getName());
        surnameField.setText(c.getSurname());
        addressField.setText(c.getAddress());
        telField.setText(c.getTelephone());
        cityField.setText(c.getCity());
        capField.setText(c.getCap());
   }
    /**
     *  RIEMPIE I FIELD RELATIVI AD UNO SHIPMENT.
     * @param s
     */
    public static void fillForm(ShipmentEntity s){            
        departureTimeField.setText(s.getDepartureTime().format(DateTimeFormatter.ofPattern(data)));    
        ArrivalTimeField.setText(s.getArrivalTime().format(DateTimeFormatter.ofPattern(data)));        
        driverField.setText(s.getIdDriver());
        vehicleField.setText(s.getIdVehicle());
        weightField.setText(Double.toString(s.getWeight()));
        volField.setText(Double.toString(s.getVolume()));
    }
    /**
     * EFFETTUA UNA QUERY AL DB PER INSERIRE UNO SHIPMENT.
     * @param s
     **/
    public void insertShipment(ShipmentEntity s){
        try{
            stmt = DatabaseConfig.defaultConnection.createStatement();
           
            String sql="INSERT INTO shipment VALUES (null, '";
                sql += s.getIdClient() + "', '";
                sql += s.getIdDriver() + "', '";
                sql += s.getIdVehicle() +"', ";
                sql += s.getWeight()+", ";
                sql += s.getVolume()+", ";                       
                sql +="'"+s.getDepartureTime()+"', ";
                sql +="'"+s.getArrivalTime()+"') ";
                //sql += "(select sysdate from dual), ";
                //sql += " null)";
                
            stmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Operation Successful");
        } 
        catch (SQLException ex) {
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Errore: " + ex);
        } 
    }   
    /**
     * EFFETTUA UNA QUERY AL DB PER MODIFICARE UNO SHIPMENT.
     * @param s
     * @param idship
     */    
    public void modifyShipment(ShipmentEntity s, int idship){
        try{
            stmt = DatabaseConfig.defaultConnection.createStatement();
       
            String  sql ="UPDATE shipment SET ";
                    sql+="idclient = '"+s.getIdClient();
                    sql+="', iddriver = '"+s.getIdDriver(); 
                    sql+="' , idvehicle  ='" +s.getIdVehicle();
                   // sql+=", currdate ="+s.getDate();      //PENSO CHE LA DATA NON SI DEBBA MODIFICARE.
                    sql+="', weight ="+s.getWeight();
                    sql+=", volume ="+s.getVolume();
                    sql+=" WHERE idshipment ='"+idship+"'";
                    
            stmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, "Operation Successful");
        } 
        catch(SQLException ex){
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Errore: " + ex);
        } 
    }
    
    //per testing, MODIFICARE LA QUERY aggiungendo la data
    public void assignDriver(){
        s=ShipmentEntity.getShipmentEntity();
        
        try{
            stmt = DatabaseConfig.defaultConnection.createStatement();
           
            String sql="SELECT CF FROM DRIVER WHERE cf not in (select iddriver from shipment)";
        
            rs=stmt.executeQuery(sql);
            if(rs.next()== true){
                s.setIdDriver(rs.getString(1));
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Errore: " + ex);
        }
    }
    //per testing, MODIFICARE LA QUERY aggiungendo la data.
    public void assignVehicle(){
        s=ShipmentEntity.getShipmentEntity();
        
        try{
            stmt = DatabaseConfig.defaultConnection.createStatement();
           
            String sql="select plate_number from vehicle where plate_number not in (select idvehicle from shipment)";
        
            rs=stmt.executeQuery(sql);
            if(rs.next()== true){
                s.setIdVehicle(rs.getString(1));
            }
        } 
        catch (SQLException ex) {
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Errore: " + ex);
        } 
    }
    /**
    *   Richiama/Crea l'istanza di shipment e setta gli attributi con i dati presi da table.
     * @param table
    **/
    public void setShipmentFromTable(javax.swing.JTable table){
            s=ShipmentEntity.getShipmentEntity();
            
            id=Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
            s.setIdClient(table.getValueAt(table.getSelectedRow(), 1).toString());
            s.setIdDriver(table.getValueAt(table.getSelectedRow(), 2).toString());
            s.setIdVehicle(table.getValueAt(table.getSelectedRow(), 3).toString());
            s.setVolume(Double.valueOf(table.getValueAt(table.getSelectedRow(), 4).toString()));
            s.setWeight(Double.valueOf(table.getValueAt(table.getSelectedRow(), 5).toString()));
            try{
                s.setDepartureTime(LocalDateTime.parse(table.getValueAt(table.getSelectedRow(), 6).toString()));
                s.setArrivalTime(LocalDateTime.parse(table.getValueAt(table.getSelectedRow(), 7).toString()));
            }
            catch(IllegalArgumentException e){}
            fillForm(s);
    }
    
    public void setClientFromTable(){
            c=ClientEntity.getClientEntity();
            s=ShipmentEntity.getShipmentEntity();
            try{
                stmt=DatabaseConfig.defaultConnection.createStatement();
            
                String sql="select * from client where cf='"+s.getIdClient()+"'" ;
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
            fillForm(c);
    }
    
    public BufferedImage ImageTool(URL url) throws IOException {
        /*//FUNZIONA!
        BufferedImage image = ImageIO.read(url);
        JLabel label = new JLabel(new ImageIcon(image));
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(label);
        f.pack();
        f.setLocation(200,200);
        f.setVisible(true);
        */
        BufferedImage image = ImageIO.read(url);
        mapsLabel.setIcon(new ImageIcon(image));
         
        return image;
    }
    
    
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Addressee = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        surnameLabel = new javax.swing.JLabel();
        addressLabel = new javax.swing.JLabel();
        capLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        surnameField = new javax.swing.JTextField();
        cityField = new javax.swing.JTextField();
        addressField = new javax.swing.JTextField();
        cfLabel = new javax.swing.JLabel();
        cfField = new javax.swing.JTextField();
        cityLabel = new javax.swing.JLabel();
        capField = new javax.swing.JTextField();
        telephoneLabel = new javax.swing.JLabel();
        telField = new javax.swing.JTextField();
        defineShipmentButton = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        DepartureTimeLabel = new javax.swing.JLabel();
        departureTimeField = new javax.swing.JFormattedTextField();
        ArrivalTimeLabel = new javax.swing.JLabel();
        ArrivalTimeField = new javax.swing.JTextField();
        packagePanel = new javax.swing.JPanel();
        weightLabel = new javax.swing.JLabel();
        volLabel = new javax.swing.JLabel();
        weightField = new javax.swing.JTextField();
        volField = new javax.swing.JTextField();
        plusButton = new javax.swing.JButton();
        registeredClientButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        confirmButton = new javax.swing.JButton();
        shipPanel = new javax.swing.JPanel();
        driverField = new javax.swing.JTextField();
        driverLabel = new java.awt.Label();
        driverModifyButton = new javax.swing.JButton();
        vehicleLabel = new java.awt.Label();
        vehicleField = new javax.swing.JTextField();
        vehicleModifyButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        logOut = new javax.swing.JButton();
        Home = new javax.swing.JButton();
        durationLabel = new javax.swing.JLabel();
        durationField = new javax.swing.JTextField();
        mapsLabel = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        forwardButton = new javax.swing.JButton();
        slideLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        Addressee.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Addressee"));

        nameLabel.setForeground(new java.awt.Color(153, 0, 0));
        nameLabel.setText("Name");

        surnameLabel.setForeground(new java.awt.Color(153, 0, 0));
        surnameLabel.setText("Surname");

        addressLabel.setForeground(new java.awt.Color(153, 0, 0));
        addressLabel.setText("Address");

        capLabel.setForeground(new java.awt.Color(153, 0, 0));
        capLabel.setText("CAP");

        nameField.setEditable(false);
        nameField.setBackground(new java.awt.Color(153, 153, 153));
        nameField.setEnabled(false);
        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });

        surnameField.setEditable(false);
        surnameField.setBackground(new java.awt.Color(153, 153, 153));
        surnameField.setEnabled(false);
        surnameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                surnameFieldActionPerformed(evt);
            }
        });

        cityField.setEditable(false);
        cityField.setBackground(new java.awt.Color(153, 153, 153));
        cityField.setEnabled(false);
        cityField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cityFieldActionPerformed(evt);
            }
        });

        addressField.setEditable(false);
        addressField.setBackground(new java.awt.Color(153, 153, 153));
        addressField.setEnabled(false);
        addressField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressFieldActionPerformed(evt);
            }
        });

        cfLabel.setForeground(new java.awt.Color(153, 0, 0));
        cfLabel.setText("CF");

        cfField.setEditable(false);
        cfField.setBackground(new java.awt.Color(153, 153, 153));
        cfField.setEnabled(false);
        cfField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cfFieldActionPerformed(evt);
            }
        });

        cityLabel.setForeground(new java.awt.Color(153, 0, 0));
        cityLabel.setText("City");

        capField.setEditable(false);
        capField.setBackground(new java.awt.Color(153, 153, 153));
        capField.setEnabled(false);
        capField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                capFieldActionPerformed(evt);
            }
        });

        telephoneLabel.setForeground(new java.awt.Color(153, 0, 0));
        telephoneLabel.setText("Telephone");

        telField.setEditable(false);
        telField.setBackground(new java.awt.Color(153, 153, 153));
        telField.setEnabled(false);
        telField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                telFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AddresseeLayout = new javax.swing.GroupLayout(Addressee);
        Addressee.setLayout(AddresseeLayout);
        AddresseeLayout.setHorizontalGroup(
            AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddresseeLayout.createSequentialGroup()
                .addGroup(AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, AddresseeLayout.createSequentialGroup()
                        .addGroup(AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameLabel)
                            .addGroup(AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cfLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(surnameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cfField)
                            .addComponent(nameField)
                            .addComponent(surnameField)
                            .addComponent(addressField)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, AddresseeLayout.createSequentialGroup()
                        .addGroup(AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(AddresseeLayout.createSequentialGroup()
                                .addComponent(cityLabel)
                                .addGap(47, 47, 47))
                            .addComponent(capLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(4, 4, 4)
                        .addGroup(AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(capField, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                            .addComponent(cityField)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, AddresseeLayout.createSequentialGroup()
                        .addComponent(telephoneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(telField)))
                .addGap(0, 0, 0))
        );
        AddresseeLayout.setVerticalGroup(
            AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddresseeLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cfLabel)
                    .addComponent(cfField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(surnameLabel)
                    .addComponent(surnameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressLabel)
                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(capLabel)
                    .addComponent(capField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cityLabel)
                    .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AddresseeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(telephoneLabel)
                    .addComponent(telField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        defineShipmentButton.setText("Define Shipment");
        defineShipmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defineShipmentButtonActionPerformed(evt);
            }
        });

        DepartureTimeLabel.setText("Departure Time");

        departureTimeField.setEditable(false);
        departureTimeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        departureTimeField.setEnabled(false);
        departureTimeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departureTimeFieldActionPerformed(evt);
            }
        });

        ArrivalTimeLabel.setText("Arrival Time");

        ArrivalTimeField.setEditable(false);
        ArrivalTimeField.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(DepartureTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(departureTimeField, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(ArrivalTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ArrivalTimeField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(DepartureTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(departureTimeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ArrivalTimeLabel)
                .addComponent(ArrivalTimeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        packagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Package"));

        weightLabel.setForeground(new java.awt.Color(153, 0, 0));
        weightLabel.setText("Weight (Kg)");

        volLabel.setForeground(new java.awt.Color(153, 0, 0));
        volLabel.setText("Volume (mt*3)");

        weightField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weightFieldActionPerformed(evt);
            }
        });

        volField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                volFieldActionPerformed(evt);
            }
        });

        plusButton.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        plusButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/plus.jpg"))); // NOI18N
        plusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plusButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout packagePanelLayout = new javax.swing.GroupLayout(packagePanel);
        packagePanel.setLayout(packagePanelLayout);
        packagePanelLayout.setHorizontalGroup(
            packagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, packagePanelLayout.createSequentialGroup()
                .addGroup(packagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(weightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(volLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(packagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(volField, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                    .addComponent(weightField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(plusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        packagePanelLayout.setVerticalGroup(
            packagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(packagePanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(packagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, packagePanelLayout.createSequentialGroup()
                        .addGroup(packagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(weightLabel)
                            .addComponent(weightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(packagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(volLabel)
                            .addComponent(volField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(plusButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        registeredClientButton.setText("Registered Client");
        registeredClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registeredClientButtonActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial Narrow", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 0, 0));
        jLabel3.setText("*Mandatory fields are written in red");

        confirmButton.setText("Confirm");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });

        shipPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));

        driverField.setEditable(false);
        driverField.setBackground(new java.awt.Color(153, 153, 153));
        driverField.setEnabled(false);
        driverField.setMaximumSize(new java.awt.Dimension(16, 16));
        driverField.setMinimumSize(new java.awt.Dimension(16, 16));
        driverField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                driverFieldActionPerformed(evt);
            }
        });

        driverLabel.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        driverLabel.setText("Driver");

        driverModifyButton.setText("Modify");
        driverModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                driverModifyButtonActionPerformed(evt);
            }
        });

        vehicleLabel.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        vehicleLabel.setName("vehicleLabel"); // NOI18N
        vehicleLabel.setText("Vehicle");

        vehicleField.setBackground(new java.awt.Color(153, 153, 153));
        vehicleField.setEnabled(false);

        vehicleModifyButton.setText("Modify");
        vehicleModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehicleModifyButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout shipPanelLayout = new javax.swing.GroupLayout(shipPanel);
        shipPanel.setLayout(shipPanelLayout);
        shipPanelLayout.setHorizontalGroup(
            shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(shipPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(shipPanelLayout.createSequentialGroup()
                        .addComponent(vehicleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addGroup(shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(vehicleField, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(driverField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(driverLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(driverModifyButton)
                    .addComponent(vehicleModifyButton)))
        );
        shipPanelLayout.setVerticalGroup(
            shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(shipPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(driverLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(driverField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(driverModifyButton)))
                .addGap(2, 2, 2)
                .addGroup(shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vehicleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(vehicleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(vehicleModifyButton))))
        );

        logOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.png"))); // NOI18N
        logOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutActionPerformed(evt);
            }
        });

        Home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/home.png"))); // NOI18N
        Home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HomeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(229, Short.MAX_VALUE)
                .addComponent(logOut, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Home, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logOut, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Home, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        durationLabel.setText("Duration");

        durationField.setEditable(false);
        durationField.setEnabled(false);
        durationField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                durationFieldActionPerformed(evt);
            }
        });

        mapsLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        backButton.setText("<<");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        forwardButton.setText(">>");
        forwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forwardButtonActionPerformed(evt);
            }
        });

        slideLabel.setText("1/1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(shipPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(33, 33, 33)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(289, 289, 289)
                                        .addComponent(durationLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(durationField, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(262, 262, 262)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(defineShipmentButton)
                                            .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(16, 16, 16))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(registeredClientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Addressee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(packagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(mapsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(backButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(slideLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(forwardButton)
                        .addGap(162, 162, 162))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Addressee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(registeredClientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(89, 89, 89)
                                .addComponent(shipPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(packagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(durationLabel)
                                    .addComponent(durationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addComponent(defineShipmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jLabel3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(backButton)
                            .addComponent(forwardButton)
                            .addComponent(slideLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mapsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(97, 97, 97)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void HomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HomeActionPerformed
        stato="shipment";
        s=ShipmentEntity.getShipmentEntity();
        s=ShipmentEntity.getEmptyShipment(); 
        
        Menus menu = new Menus();
        menu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_HomeActionPerformed

    private void logOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutActionPerformed
        Login log = new Login();
        log.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_logOutActionPerformed

    private void cityFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cityFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cityFieldActionPerformed

    private void telFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_telFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_telFieldActionPerformed

    private void addressFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addressFieldActionPerformed

    private void surnameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_surnameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_surnameFieldActionPerformed

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameFieldActionPerformed

    private void defineShipmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defineShipmentButtonActionPerformed
        setVisibleButton("defineButton");
                
        assignDriver();
        assignVehicle();
        driverField.setText(s.getIdDriver());
        vehicleField.setText(s.getIdVehicle());  
        try{
                                          
            c=ClientEntity.getClientEntity();
            String address=c.getAddress()+",+"+c.getCap()+",+"+c.getCity();
            address=address.replace(" ","+");
           // System.out.println(st);
            
            Maps maps=new Maps();
            StringBuilder response=maps.GetRequest(address);            
            
            durationField.setText(maps.getDuration(response));
            
        //data di arrivo= data di partenza + tempo di viaggio.
            LocalDateTime dt=s.getDepartureTime().plusMinutes(maps.getArrivalTime(response)/60);
            ArrivalTimeField.setText(dt.format(DateTimeFormatter.ofPattern(data)));
            s.setArrivalTime(dt);     
            
            //pa contiene l'url della mappa.
            //String pa=maps.getPath(response);
            parts = maps.getPartsPart(maps.getPath(response));
            slideLabel.setText((k+1)+" di "+parts.size());
            backButton.setVisible(false);
            slideLabel.setVisible(true);
            if(parts.size() > k+1){
                forwardButton.setVisible(true);
            }else{
                forwardButton.setVisible(false);
            }
            URL url=new URL(getPato(parts.get(0)));
            img = ImageTool(url);
        }
        catch (Exception ex) {
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_defineShipmentButtonActionPerformed
    private String getPato(ArrayList<LatLng> pati){
     String path = "http://maps.googleapis.com/maps/api/staticmap?";
        //path += "&size=512x512";
        path += "&size=402x309";
        //path += "&zoom=10";
        path += "&maptype=roadmap";
        path += "&key=AIzaSyBYoKm3CXK5_s1SlB4gkIROS93lIti9ksE";
        path += "&format=jpg";
        path += "&path=color:0x0000ff|weight:5";
        path += "|enc:";
        path += new Maps().encode(pati);
        return path;
    }
            
    private void weightFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weightFieldActionPerformed
        
    }//GEN-LAST:event_weightFieldActionPerformed

    private void plusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plusButtonActionPerformed

    }//GEN-LAST:event_plusButtonActionPerformed

    private void registeredClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registeredClientButtonActionPerformed
        stato="client";
        List l=new List();
        //this.setVisible(false);
        l.setVisible(true);        
    }//GEN-LAST:event_registeredClientButtonActionPerformed

    private void cfFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cfFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cfFieldActionPerformed

    private void driverModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_driverModifyButtonActionPerformed
        stato="driver";
        List l=new List();
        //this.setVisible(false);
        l.setVisible(true);    
    }//GEN-LAST:event_driverModifyButtonActionPerformed

    private void vehicleModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicleModifyButtonActionPerformed
        stato="vehicle";
        List l=new List();
       // this.setVisible(false);
        l.setVisible(true);    
    }//GEN-LAST:event_vehicleModifyButtonActionPerformed

    private void driverFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_driverFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_driverFieldActionPerformed

    private void departureTimeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departureTimeFieldActionPerformed
        
    }//GEN-LAST:event_departureTimeFieldActionPerformed

    private void volFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_volFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_volFieldActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        try{
            s=ShipmentEntity.getShipmentEntity();
            s.setWeight(Double.valueOf(weightField.getText()));
            s.setVolume(Double.valueOf(volField.getText()));
            if(press.equals("insert")){
                insertShipment(s);
            }
            else{//press=modify
                modifyShipment(s,id);
            }
            s= ShipmentEntity.getEmptyShipment();    
            stato="shipment";
            Menus menu = new Menus();
            menu.setVisible(true);
            this.dispose();
        }
        catch(NumberFormatException e){
           JOptionPane.showMessageDialog(null, "Error: fill all the mandatory fields");
        }
    }//GEN-LAST:event_confirmButtonActionPerformed

    private void capFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_capFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_capFieldActionPerformed

    private void durationFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_durationFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_durationFieldActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        k--;
        changePath();
    }//GEN-LAST:event_backButtonActionPerformed

    private void forwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forwardButtonActionPerformed
        k++;
        changePath();
    }//GEN-LAST:event_forwardButtonActionPerformed
    
    private void changePath(){
        URL url;
        if(k!=parts.size()-1){
            forwardButton.setVisible(true);
        }
        else{
            forwardButton.setVisible(false);
        }
        if(k!=0){
            backButton.setVisible(true);
        }
        else{
            backButton.setVisible(false);
        }
        
        slideLabel.setText((k+1)+" di "+parts.size());
        try {
            url = new URL(getPato(parts.get(k)));
            img = ImageTool(url);
        } 
        catch (MalformedURLException ex) {
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) {
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Assignments.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Assignments().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Addressee;
    public static javax.swing.JTextField ArrivalTimeField;
    private javax.swing.JLabel ArrivalTimeLabel;
    private javax.swing.JLabel DepartureTimeLabel;
    private javax.swing.JButton Home;
    protected static javax.swing.JTextField addressField;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JButton backButton;
    protected static javax.swing.JTextField capField;
    private javax.swing.JLabel capLabel;
    protected static javax.swing.JTextField cfField;
    private javax.swing.JLabel cfLabel;
    protected static javax.swing.JTextField cityField;
    private javax.swing.JLabel cityLabel;
    private javax.swing.JButton confirmButton;
    private javax.swing.JToggleButton defineShipmentButton;
    private static javax.swing.JFormattedTextField departureTimeField;
    protected static javax.swing.JTextField driverField;
    private java.awt.Label driverLabel;
    private javax.swing.JButton driverModifyButton;
    public static javax.swing.JTextField durationField;
    private javax.swing.JLabel durationLabel;
    private javax.swing.JButton forwardButton;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton logOut;
    private javax.swing.JLabel mapsLabel;
    protected static javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JPanel packagePanel;
    private javax.swing.JButton plusButton;
    private javax.swing.JButton registeredClientButton;
    private javax.swing.JPanel shipPanel;
    private javax.swing.JLabel slideLabel;
    protected static javax.swing.JTextField surnameField;
    private javax.swing.JLabel surnameLabel;
    protected static javax.swing.JTextField telField;
    private javax.swing.JLabel telephoneLabel;
    protected static javax.swing.JTextField vehicleField;
    private java.awt.Label vehicleLabel;
    private javax.swing.JButton vehicleModifyButton;
    protected static javax.swing.JTextField volField;
    private javax.swing.JLabel volLabel;
    protected static javax.swing.JTextField weightField;
    private javax.swing.JLabel weightLabel;
    // End of variables declaration//GEN-END:variables
}
