package ingsftw;


import com.google.maps.model.LatLng;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;



public class Assignments extends javax.swing.JFrame {
    private String currAdd;
    private LocalDateTime dt;
    private LocalDateTime at;
    private final String data;
    private int k;
    
    private ArrayList<ArrayList<LatLng>> parts;     /** Array that contains the journey's path. */
    private ArrayList<ArrayList<String>> allPathList;
    private String press;
    private final long delay;
    
    private static TableView tableview;
    private static Log15Control logctrl;  
    
    
    
    public Assignments(){
        this.delay = 30;
        this.k = 0;
        this.data = "dd/M/y  H:m:s";
        logctrl=new Log15Control();
        tableview=new TableView();
        
        currAdd="";
        press="insert";
        
        parts = new ArrayList<>();
        allPathList=new ArrayList<>();        
        
        initComponents();
        setVisibleButton(press);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("truck1.jpg")));  
        setTitle("Log'15_SoftEngUniNa");
    
    }
    
    public Assignments(String add){
        this.delay = 30;
        this.k = 0;
        this.data = "dd/M/y  H:m:s";
        initComponents();
        
        currAdd=add;
        press="modify";
        setVisibleButton(press);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("truck1.jpg")));  
        setTitle("Log'15_SoftEngUniNa");
        
        logctrl=new Log15Control();
        tableview=new TableView();
        parts = new ArrayList<>();
        allPathList=new ArrayList<>();        
    }
    
    
    public LocalDateTime getDT(){
        return(dt);
    }
    
    public LocalDateTime getAT(){
        return(at);
    }
    
    /**
     * @param p
     */
    private void setVisibleButton(String p){
        backButton.setVisible(false);
        forwardButton.setVisible(false);
        slideLabel.setVisible(false);
        
        if(p.equals("insert"))
        {          
            /**
             * Each shipment will have 6:00am of the next day as departure time.
             */
            dt=LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(6,0));    
            departureTimeField.setText(dt.format(DateTimeFormatter.ofPattern(data)));
            
            mapsLabel1.setVisible(false);
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
            confirmButton.setVisible(false);
            shipPanel.setVisible(false);
        }
        else    //modify or defineShipment
        {
            waypointTable.setVisible(true);
            registeredClientButton.setVisible(false);
            mapsLabel.setVisible(true);
            mapsLabel1.setVisible(true);
            defineShipmentButton.setVisible(false);
            ArrivalTimeField.setVisible(true);
            ArrivalTimeLabel.setVisible(true);
            driverField.setVisible(true);
            driverLabel.setVisible(true);
            driverModifyButton.setVisible(true);
            vehicleField.setVisible(true);
            vehicleLabel.setVisible(true);
            vehicleModifyButton.setVisible(true);
            confirmButton.setVisible(true);
            shipPanel.setVisible(true);
        }
    } 
    
    /**
     *  This method is used to fill the Client information's fields
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
     *  This method is used to fill the shipment information's fields
     * @param s
     */
    public void fillForm(ShipmentEntity s){            
        departureTimeField.setText(s.getDepartureTime().format(DateTimeFormatter.ofPattern(data)));    
        ArrivalTimeField.setText(s.getArrivalTime().format(DateTimeFormatter.ofPattern(data)));        
        driverField.setText(s.getIdDriver());
        vehicleField.setText(s.getIdVehicle());
        weightField.setText(Double.toString(s.getWeight()));
        volField.setText(Double.toString(s.getVolume()));
        
        dt=s.getDepartureTime();        
    }
    
    
    /**
     * This method is used to show the map in mapLabel.
     * @param url
     * @return 
     * @throws java.io.IOException
     */
    public BufferedImage ImageTool(URL url) throws IOException {
        BufferedImage image = ImageIO.read(url);
        mapsLabel.setIcon(new ImageIcon(image));
         
        return image;
    }
    
    /**
     * This method shows in mapLavel the map with the rest of the path.
     */
    private void changeVisiblePath(){    //Called in backButton and forwardButton
        URL url;
        
        try {
            Maps maps=new Maps();
            if(k!=parts.size()-1){
                if(k==0)
                    url = new URL(maps.buildUrl(parts.get(k),Maps.source));
                else
                    url = new URL(maps.buildUrl(parts.get(k)));
            }
            else{
                //Showing in the map the marker that symbolises the end of the journey.
                url = new URL(maps.buildUrl(parts.get(k),currAdd));
            }
            BufferedImage img = ImageTool(url);
        } 
        catch (MalformedURLException ex) {
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) {
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /** This method is used to request to Google Maps the path containing the surce,
     *  the waypoints and the destination. It gives Google Maps the source, the destination and the waypoints
     *  and gets back a static map containing the journey's path.
     * 
     * @param WP
     * @param address
     */
    public void showMapInLabel(ArrayList WP, String address){ 
        try{
            Maps maps=new Maps();
            StringBuilder response=maps.GetRequest(Maps.source,address,WP);    
         
            /** Sets the arrival time as departure time plus the duration of the journey also added to a certain delay time **/
            at=dt.plusMinutes(maps.getArrivalTime(response)).plusMinutes(delay);   
            ArrivalTimeField.setText(at.format(DateTimeFormatter.ofPattern(data)));
                 
            
            /** Divides the journey in substrings so that a http request is possible. */
            parts = maps.getPartsPart(maps.getPath(response));      
            
            slideLabel.setText((k+1)+" of "+parts.size());
            backButton.setVisible(false);
            slideLabel.setVisible(true);
            
            if(parts.size() > k+1){
                forwardButton.setVisible(true);
            }
            URL url;
            if(parts.size()>1){
                url=new URL(maps.buildUrl(parts.get(0),Maps.source));
            }
            else{
                url=new URL(maps.buildUrl(parts.get(0),Maps.source,address));
            }
            BufferedImage img = ImageTool(url);   
        }
        catch (Exception ex) {
            Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
 
    public void createMap(String currAdd){
            //lista degli indirizzi dei clienti le cui spedizioni possono trasportare il pacco della nuova spedizione
            ArrayList<String> l=logctrl.findNotBusyShip(dt, Double.valueOf(weightField.getText()));  
            Maps maps=new Maps();
            //cerco tutti i percorsi minimi tra "HOME" e "CURRADDRESS" considerando i possibili waypoints "l".
            allPathList=maps.findWaypoint(currAdd, l); 
            //visualizzo tutti i percorsi minimi nella tabella.
            tableview.fillWaypointTable(allPathList, waypointTable);
            //mostro il percorso più veloce nella mappa.
            showMapInLabel(allPathList.get(0), currAdd);            
    }
    
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DataPanel = new javax.swing.JPanel();
        DepartureTimeLabel = new javax.swing.JLabel();
        departureTimeField = new javax.swing.JFormattedTextField();
        ArrivalTimeLabel = new javax.swing.JLabel();
        ArrivalTimeField = new javax.swing.JTextField();
        confirmButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        AddresseePanel = new javax.swing.JPanel();
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
        registeredClientButton = new javax.swing.JButton();
        PackagePanel = new javax.swing.JPanel();
        weightLabel = new javax.swing.JLabel();
        volLabel = new javax.swing.JLabel();
        weightField = new javax.swing.JTextField();
        volField = new javax.swing.JTextField();
        shipPanel = new javax.swing.JPanel();
        driverField = new javax.swing.JTextField();
        driverLabel = new java.awt.Label();
        driverModifyButton = new javax.swing.JButton();
        vehicleLabel = new java.awt.Label();
        vehicleField = new javax.swing.JTextField();
        vehicleModifyButton = new javax.swing.JButton();
        MandatoryFieldsLabel = new javax.swing.JLabel();
        defineShipmentButton = new javax.swing.JToggleButton();
        mapsLabel1 = new javax.swing.JPanel();
        mapsLabel = new javax.swing.JLabel();
        forwardButton = new javax.swing.JButton();
        slideLabel = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        waypointTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        Home = new javax.swing.JButton();
        logOut = new javax.swing.JButton();
        titleLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        DataPanel.setBackground(new java.awt.Color(255, 255, 255));

        DepartureTimeLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        DepartureTimeLabel.setText("Departure Time");

        departureTimeField.setEditable(false);
        departureTimeField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        departureTimeField.setEnabled(false);
        departureTimeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departureTimeFieldActionPerformed(evt);
            }
        });

        ArrivalTimeLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ArrivalTimeLabel.setText("Arrival Time");

        ArrivalTimeField.setEditable(false);
        ArrivalTimeField.setEnabled(false);
        ArrivalTimeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArrivalTimeFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DataPanelLayout = new javax.swing.GroupLayout(DataPanel);
        DataPanel.setLayout(DataPanelLayout);
        DataPanelLayout.setHorizontalGroup(
            DataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DataPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(DepartureTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(departureTimeField, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(ArrivalTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ArrivalTimeField, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        DataPanelLayout.setVerticalGroup(
            DataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(DataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DepartureTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(departureTimeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ArrivalTimeLabel)
                    .addComponent(ArrivalTimeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        confirmButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        confirmButton.setText("Confirm");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        AddresseePanel.setBackground(new java.awt.Color(255, 255, 255));
        AddresseePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255), 2), "Addressee", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 255))); // NOI18N

        nameLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        nameLabel.setText("Name");

        surnameLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        surnameLabel.setText("Surname");

        addressLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        addressLabel.setText("Address");

        capLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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

        cfLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cfLabel.setText("CF");

        cfField.setEditable(false);
        cfField.setBackground(new java.awt.Color(153, 153, 153));
        cfField.setEnabled(false);
        cfField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cfFieldActionPerformed(evt);
            }
        });

        cityLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cityLabel.setText("City");

        capField.setEditable(false);
        capField.setBackground(new java.awt.Color(153, 153, 153));
        capField.setEnabled(false);
        capField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                capFieldActionPerformed(evt);
            }
        });

        telephoneLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        telephoneLabel.setText("Telephone");

        telField.setEditable(false);
        telField.setBackground(new java.awt.Color(153, 153, 153));
        telField.setEnabled(false);
        telField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                telFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout AddresseePanelLayout = new javax.swing.GroupLayout(AddresseePanel);
        AddresseePanel.setLayout(AddresseePanelLayout);
        AddresseePanelLayout.setHorizontalGroup(
            AddresseePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddresseePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AddresseePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(surnameLabel)
                    .addComponent(addressLabel)
                    .addComponent(cityLabel)
                    .addComponent(capLabel)
                    .addComponent(telephoneLabel)
                    .addComponent(cfLabel)
                    .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AddresseePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AddresseePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cfField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(surnameField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(capField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(telField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        AddresseePanelLayout.setVerticalGroup(
            AddresseePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddresseePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AddresseePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cfField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cfLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AddresseePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AddresseePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(surnameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(surnameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AddresseePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressLabel)
                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AddresseePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(capField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(capLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AddresseePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cityLabel)
                    .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AddresseePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(telField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(telephoneLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        registeredClientButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        registeredClientButton.setForeground(new java.awt.Color(153, 0, 0));
        registeredClientButton.setText("Registered Client");
        registeredClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registeredClientButtonActionPerformed(evt);
            }
        });

        PackagePanel.setBackground(new java.awt.Color(255, 255, 255));
        PackagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255), 2), "Package", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 255))); // NOI18N

        weightLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        weightLabel.setForeground(new java.awt.Color(153, 0, 0));
        weightLabel.setText("Weight (Kg)");

        volLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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

        javax.swing.GroupLayout PackagePanelLayout = new javax.swing.GroupLayout(PackagePanel);
        PackagePanel.setLayout(PackagePanelLayout);
        PackagePanelLayout.setHorizontalGroup(
            PackagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PackagePanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(PackagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(volLabel)
                    .addComponent(weightLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PackagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(weightField, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(volField, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        PackagePanelLayout.setVerticalGroup(
            PackagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PackagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PackagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(weightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weightLabel))
                .addGap(18, 18, 18)
                .addGroup(PackagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(volField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(volLabel))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        shipPanel.setBackground(new java.awt.Color(255, 255, 255));
        shipPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255), 2), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 255))); // NOI18N

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

        driverLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        driverLabel.setText("Driver");

        driverModifyButton.setText("Modify");
        driverModifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                driverModifyButtonActionPerformed(evt);
            }
        });

        vehicleLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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
                .addContainerGap()
                .addGroup(shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(shipPanelLayout.createSequentialGroup()
                        .addComponent(vehicleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addGroup(shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(driverField, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                            .addComponent(vehicleField)))
                    .addComponent(driverLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(driverModifyButton)
                    .addComponent(vehicleModifyButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        shipPanelLayout.setVerticalGroup(
            shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(shipPanelLayout.createSequentialGroup()
                .addGroup(shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(shipPanelLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(driverField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(driverModifyButton)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, shipPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(driverLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, shipPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(vehicleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(vehicleModifyButton))
                    .addComponent(vehicleLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        MandatoryFieldsLabel.setFont(new java.awt.Font("Arial Narrow", 1, 12)); // NOI18N
        MandatoryFieldsLabel.setForeground(new java.awt.Color(204, 0, 0));
        MandatoryFieldsLabel.setText("*Mandatory fields are written in red");

        defineShipmentButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        defineShipmentButton.setText("Define Shipment");
        defineShipmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defineShipmentButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MandatoryFieldsLabel)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(161, 161, 161)
                                .addComponent(registeredClientButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(defineShipmentButton)
                        .addGap(31, 31, 31))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(AddresseePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(98, 98, 98)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(shipPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PackagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(34, 34, 34))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(PackagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(shipPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(AddresseePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(registeredClientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(MandatoryFieldsLabel)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(defineShipmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        mapsLabel1.setBackground(new java.awt.Color(255, 255, 255));
        mapsLabel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255), 2), "Path", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 255))); // NOI18N

        forwardButton.setText(">>");
        forwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forwardButtonActionPerformed(evt);
            }
        });

        slideLabel.setText("1/1");

        backButton.setText("<<");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mapsLabel1Layout = new javax.swing.GroupLayout(mapsLabel1);
        mapsLabel1.setLayout(mapsLabel1Layout);
        mapsLabel1Layout.setHorizontalGroup(
            mapsLabel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mapsLabel1Layout.createSequentialGroup()
                .addGroup(mapsLabel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mapsLabel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(mapsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mapsLabel1Layout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addComponent(backButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(slideLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(forwardButton)))
                .addContainerGap())
        );
        mapsLabel1Layout.setVerticalGroup(
            mapsLabel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mapsLabel1Layout.createSequentialGroup()
                .addComponent(mapsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mapsLabel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton)
                    .addComponent(forwardButton)
                    .addComponent(slideLabel)))
        );

        jScrollPane1.setBorder(null);

        waypointTable.setAutoCreateRowSorter(true);
        waypointTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        waypointTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        waypointTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                waypointTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                waypointTableMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(waypointTable);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        Home.setBackground(new java.awt.Color(255, 255, 255));
        Home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/home.png"))); // NOI18N
        Home.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HomeActionPerformed(evt);
            }
        });

        logOut.setBackground(new java.awt.Color(255, 255, 255));
        logOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.png"))); // NOI18N
        logOut.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        logOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutActionPerformed(evt);
            }
        });

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(0, 153, 255));
        titleLabel.setText("SHIPMENT MANAGEMENT");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logOut, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Home, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(logOut, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Home, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(DataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(mapsLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1034, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(mapsLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void HomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HomeActionPerformed
        logctrl.openFrame("menu", "");
        this.dispose();        
    }//GEN-LAST:event_HomeActionPerformed

    private void logOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutActionPerformed
        logctrl.logOut();
        this.dispose();
    }//GEN-LAST:event_logOutActionPerformed

    private void cityFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cityFieldActionPerformed
    
    }//GEN-LAST:event_cityFieldActionPerformed

    private void telFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_telFieldActionPerformed
       
    }//GEN-LAST:event_telFieldActionPerformed

    private void addressFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressFieldActionPerformed
    
    }//GEN-LAST:event_addressFieldActionPerformed

    private void surnameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_surnameFieldActionPerformed
       
    }//GEN-LAST:event_surnameFieldActionPerformed

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameFieldActionPerformed
      
    }//GEN-LAST:event_nameFieldActionPerformed

    private void defineShipmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defineShipmentButtonActionPerformed
        try{
            if(cfField.getText().equals("") || weightField.getText().equals("") || volField.getText().equals(""))
               throw new NumberFormatException(); 
             
            setVisibleButton("confirmShipmentButton");
            
            currAdd=addressField.getText()+",+"+capField.getText()+",+"+cityField.getText();
            currAdd=currAdd.replace(" ","+");
            
            createMap(currAdd);
          
            logctrl.assignNotBusy("vehicle", dt);
            logctrl.assignNotBusy("driver", dt);            
        }
        catch(NumberFormatException e){
           JOptionPane.showMessageDialog(null, "Error: fill all the mandatory fields");
           
        }        
    }//GEN-LAST:event_defineShipmentButtonActionPerformed
                
    private void weightFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weightFieldActionPerformed
        
    }//GEN-LAST:event_weightFieldActionPerformed

    private void registeredClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registeredClientButtonActionPerformed
        tableview.openFrame("client");        
    }//GEN-LAST:event_registeredClientButtonActionPerformed

    private void cfFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cfFieldActionPerformed
     
    }//GEN-LAST:event_cfFieldActionPerformed

    private void driverModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_driverModifyButtonActionPerformed
        tableview.openFrame("driver");
    }//GEN-LAST:event_driverModifyButtonActionPerformed

    private void vehicleModifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicleModifyButtonActionPerformed
        tableview.openFrame("vehicle");
    }//GEN-LAST:event_vehicleModifyButtonActionPerformed

    private void driverFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_driverFieldActionPerformed
        
    }//GEN-LAST:event_driverFieldActionPerformed

    private void departureTimeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departureTimeFieldActionPerformed
        
    }//GEN-LAST:event_departureTimeFieldActionPerformed

    private void volFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_volFieldActionPerformed
       
    }//GEN-LAST:event_volFieldActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        try{            
            logctrl.operation(this, press);
            logctrl.openFrame("menu", "");
            this.dispose();
        }
        catch(NumberFormatException e){
           JOptionPane.showMessageDialog(null, "Error: fill all the mandatory fields");
        }
    }//GEN-LAST:event_confirmButtonActionPerformed

    private void capFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_capFieldActionPerformed
       
    }//GEN-LAST:event_capFieldActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        k--;
        if(k!=parts.size()-1){
            forwardButton.setVisible(true);
        }else{
            forwardButton.setVisible(false);
        }
        if(k!=0){
            backButton.setVisible(true);
        }else{
            backButton.setVisible(false);
        }
        slideLabel.setText((k+1)+" di "+parts.size());
        
        changeVisiblePath();
    }//GEN-LAST:event_backButtonActionPerformed

    private void forwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forwardButtonActionPerformed
        k++;
        if(k!=parts.size()-1){
            forwardButton.setVisible(true);
        }else{
            forwardButton.setVisible(false);
        }
        if(k!=0){
            backButton.setVisible(true);
        }else{
            backButton.setVisible(false);
        }
        slideLabel.setText((k+1)+" di "+parts.size());
        
        changeVisiblePath();
    }//GEN-LAST:event_forwardButtonActionPerformed

    private void ArrivalTimeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ArrivalTimeFieldActionPerformed
     
    }//GEN-LAST:event_ArrivalTimeFieldActionPerformed

    private void waypointTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_waypointTableMousePressed

    }//GEN-LAST:event_waypointTableMousePressed

    private void waypointTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_waypointTableMouseClicked
        k=0;
        int nRow=waypointTable.getSelectedRow();
        showMapInLabel(allPathList.get(nRow), currAdd);
        
        if(allPathList.get(nRow).isEmpty()){
            logctrl.assignNotBusy("vehicle", dt);
            logctrl.assignNotBusy("driver", dt);
        }
        else{
            /**
             * Looking in the database the driver and vehicle that have been assigned to the requested delivery.
             */
            String [] st=allPathList.get(nRow).get(allPathList.get(nRow).size()-1).replace("+"," ").split(", ");
            logctrl.assignVehicleDriverFromShipment(st, dt);            
        }
    }//GEN-LAST:event_waypointTableMouseClicked
    
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
    private javax.swing.JPanel AddresseePanel;
    public static javax.swing.JTextField ArrivalTimeField;
    private javax.swing.JLabel ArrivalTimeLabel;
    private javax.swing.JPanel DataPanel;
    private javax.swing.JLabel DepartureTimeLabel;
    private javax.swing.JButton Home;
    private javax.swing.JLabel MandatoryFieldsLabel;
    private javax.swing.JPanel PackagePanel;
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
    private javax.swing.JButton forwardButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton logOut;
    private javax.swing.JLabel mapsLabel;
    private javax.swing.JPanel mapsLabel1;
    protected static javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton registeredClientButton;
    private javax.swing.JPanel shipPanel;
    private javax.swing.JLabel slideLabel;
    protected static javax.swing.JTextField surnameField;
    private javax.swing.JLabel surnameLabel;
    protected static javax.swing.JTextField telField;
    private javax.swing.JLabel telephoneLabel;
    private javax.swing.JLabel titleLabel;
    protected static javax.swing.JTextField vehicleField;
    private java.awt.Label vehicleLabel;
    private javax.swing.JButton vehicleModifyButton;
    protected static javax.swing.JTextField volField;
    private javax.swing.JLabel volLabel;
    protected static javax.swing.JTable waypointTable;
    protected static javax.swing.JTextField weightField;
    private javax.swing.JLabel weightLabel;
    // End of variables declaration//GEN-END:variables
}
