/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ingsftw;

import java.awt.Color;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Menus extends javax.swing.JFrame {
    private VehicleEntity v;
    private Vehicle vehicle;
    private Assignments assignment;
    private ShipmentEntity ship;
    private ClientEntity c;
    
    private static Statement stmt;    // SQL Statement.
    private static ResultSet rs;      //Save query results.
    
    public static String stato="shipment";
    public static String press=" ";        //var globale che identifica ai frame se ho premuto INSERT, MODIFY.
    
    
    public Menus() {
        initComponents();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("truck1.jpg")));       
        setVisibleButton(stato);
        tableFields(stato,table);
    }
     
    /*
     * Apre il Frame relativo al pulsante premuto.
    */
    public void openFrame(){
        switch(stato){
            case "shipment":
            {
                assignment=new Assignments();       //crea il frame Assignment.
                assignment.setVisible(true);        //rende visibile il frame Assignment
                this.setVisible(false);             //chiude il frame Menu
                break;
            }
            case "vehicle":
            {
                vehicle=new Vehicle();              //crea il frame Vehicle.
                vehicle.setVisible(true);           //rende visibile il frame Vehicle.
                this.setVisible(false);             //chiude il frame Menu.
                break;
            }  
            /*  
            *** CLIENT E DRIVER NON HANNO FRAME. 
            *** LASCIATO PER IPOTETICHE IMPLEMENTAZIONI FUTURE. 
            case "client":
            {break;}
            case "driver":
            {break;}
            */
        }
    }
    
    /*
     *  Costruisce il modello della tabella da visualizzare.
    */
    public DefaultTableModel getModel(String s){
        DefaultTableModel model=null;
        Object [][] rowdata = {};
        
        switch(s){
            case("shipment"):
            {    
                Object [] shipmentT={"ID Ship","ID Addressee","ID Driver","ID Vehicle","Weight","Volume","Departure","Arrival"};
                model = new DefaultTableModel(rowdata, shipmentT){                
                   @Override
                   public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                    }
                };        
                break;
            }
            case("vehicle"):
            {
                Object [] vehicleT={"Plate number","Model","Height","Length","Capacity"};
                model = new DefaultTableModel(rowdata, vehicleT){                
                    @Override
                    public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                    }
                };
                break;
            }
            case("driver"):
            {
                /**
                 * NEL DB CI SONO SOLO I PRIMI 3 CAMPI, PERCHè?
                */
                Object [] driverT={"Name","Surname","FC","Address","Tel","CAP","City"};
                model = new DefaultTableModel(rowdata, driverT){                
                    @Override
                    public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                    }
                };
                break;
            }
            case("client"):
            {
                Object [] clientT={"FC", "Name","Surname","Address","CAP","tel","City"};
                model = new DefaultTableModel(rowdata, clientT){                
                    @Override
                    public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                    }
                };
                break;
            }
        }
        return(model);
    }
    
    /*
     * Esegue la query al DB e riempie la tabella.
    */
    public void fillModel(String sql, DefaultTableModel model){
        try{
            stmt=DatabaseConfig.defaultConnection.createStatement();	    
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
        
    /*
     * Effettua una QUERY al DB per mostrare tutte le occorrenze ricercate
    */    
    public void doSearch(String s, String search, javax.swing.JTable nome){
        DefaultTableModel model=getModel(s);
	nome.setModel(model);
        String sql=null;
        
        switch(s){
                case("vehicle"):{
                    sql="SELECT * FROM "+s+" where plate_number like '%"+search+"%'or model like '%"+search+"%'";
                    try{
                        double f=Double.valueOf(search);
                        sql+="or height ="+ f+"or length ="+f+"or capacity ="+f;
                    }
                    catch (NumberFormatException e){}
                    break;
                }
                case("client"):{
                    sql="SELECT * FROM "+s+" where cf like '%"+search+"%'or name like '%"+search;
                    sql+="%'or surname like '%"+search+"%'or address like '%"+search;
                    sql+="%'or cap like '%"+search+"%'or telephone like '%"+search;  
                    sql+="%'or city like '%"+search+"%'";  
                    break;
                }
                case("driver"):{
                    sql="SELECT * FROM "+s+" where cf like '%"+search+"%'or name like '%"+search;
                    sql+="%'or surname like '%"+search+"%'";                        
                    break;
                }
                case("shipment"):{
                    sql="SELECT * FROM "+s+" where idclient like '%"+search+"%'or iddriver like '%"+search;
                    sql+="%'or idvehicle like '%"+search+"%'";
                    /*

                            **NON FUNZIONA LA QUERY.
                            **fare la ricerca per data
                    
                    
                    */
                    try{
                        sql+="or departuretime like '%"+search+"%' or arrivaltime like '%"+search+"'%"; 
                        double f=Double.valueOf(search);
                        sql+="or weight ="+ f+"or volume ="+f;
                    }
                    catch (NumberFormatException e){}
                    break;
                }
            }        
        fillModel(sql,model);
        nome.requestFocus();
    }
        
    /*
     * Effettua una QUERY al DB per mostrare tutti i dati della tabella.
    */
    public void tableFields(String s, javax.swing.JTable nome){
        String sql;
        DefaultTableModel model=getModel(s);    //prende il modello della tabella da riempire.
	nome.setModel(model);
        
        if(s.equals("shipment")){ 
            /*
            
            
            ///commento l'istruzione sotto per TEST. altrimenti tableField deve mostrare solo le spedizioni giornaliere.
            
            */
            //LocalDateTime dt=LocalDateTime.now();
            //sql="SELECT * FROM shipment WHERE departureTime ='"+dt+"'" ;
            sql="SELECT * FROM " + s;
        }
        else
            sql="SELECT * FROM " + s;
        
        fillModel(sql, model);      //riempie la tabella con i dati presi dalla query
        nome.requestFocus();
    }    
    
    /**
     * Setta la visibilità dei pulsanti in base al frame di provenienza [ASSIGNMENT, VEHICLE, DRIVER, CLIENT].
     * @param s
    */
    public void setVisibleButton(String s){
        switch (s){
            case("shipment"):{
                tableName.setText("Shipment's Table");
                insertButton.setVisible(true);
                modifyButton.setVisible(true);
                deleteButton.setVisible(false);
                trackItButton.setVisible(true);
                searchField.setVisible(true);
                searchLabel.setVisible(true);
                AssignmentButton.setBackground(Color.gray);
                vehicleButton.setBackground(Color.lightGray);
                driverButton.setBackground(Color.lightGray);
                clientButton.setBackground(Color.lightGray);                
                modifyButton.setEnabled(false);
                trackItButton.setEnabled(false);
               /* //setto non cliccabile il pulsante premuto.
                this.vehicleButton.setEnabled(true);
                this.AssignmentButton.setEnabled(false);
                this.driverButton.setEnabled(true);
                this.clientButton.setEnabled(true);
                */
                break;
            }
            case("vehicle"):{
                tableName.setText("Vehicle's Table");
                insertButton.setVisible(true);
                modifyButton.setVisible(true);
                deleteButton.setVisible(true);
                trackItButton.setVisible(false);
                searchField.setVisible(true);
                searchLabel.setVisible(true);
                AssignmentButton.setBackground(Color.lightGray);
                vehicleButton.setBackground(Color.gray);
                driverButton.setBackground(Color.lightGray);
                clientButton.setBackground(Color.lightGray);
                /* //setto non cliccabile il pulsante premuto.
                this.vehicleButton.setEnabled(false);
                this.AssignmentButton.setEnabled(true);
                this.driverButton.setEnabled(true);
                this.clientButton.setEnabled(true);
                */
                break;
            }
            case("driver"):{
                tableName.setText("Driver's Table");
                insertButton.setVisible(false);
                modifyButton.setVisible(false);
                deleteButton.setVisible(false);
                trackItButton.setVisible(false);
                searchField.setVisible(false);
                searchLabel.setVisible(false);
                AssignmentButton.setBackground(Color.lightGray);
                vehicleButton.setBackground(Color.lightGray);
                driverButton.setBackground(Color.gray);
                clientButton.setBackground(Color.lightGray);
                /* //setto non cliccabile il pulsante premuto.
                this.vehicleButton.setEnabled(true);
                this.AssignmentButton.setEnabled(true);
                this.driverButton.setEnabled(false);
                this.clientButton.setEnabled(true);
                */
                break;
            }
            case("client"):{
                tableName.setText("Client's Table");
                insertButton.setVisible(false);
                modifyButton.setVisible(false);
                deleteButton.setVisible(false);
                trackItButton.setVisible(false);
                searchField.setVisible(false);
                searchLabel.setVisible(false);
                AssignmentButton.setBackground(Color.lightGray);
                vehicleButton.setBackground(Color.lightGray);
                driverButton.setBackground(Color.lightGray);
                clientButton.setBackground(Color.gray);
                /* //setto non cliccabile il pulsante premuto.
                this.vehicleButton.setEnabled(true);
                this.AssignmentButton.setEnabled(true);
                this.driverButton.setEnabled(true);
                this.clientButton.setEnabled(false);
                */
                break;
            }            
        }        
    }
          
    
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        menusPanel = new javax.swing.JPanel();
        AssignmentButton = new javax.swing.JButton();
        vehicleButton = new javax.swing.JButton();
        clientButton = new javax.swing.JButton();
        driverButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        tableName = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        logOut = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        searchField = new javax.swing.JTextField();
        deleteButton = new javax.swing.JButton();
        modifyButton = new javax.swing.JButton();
        searchLabel = new javax.swing.JLabel();
        insertButton = new javax.swing.JButton();
        trackItButton = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        AssignmentButton.setText("Assignment Management");
        AssignmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AssignmentButtonActionPerformed(evt);
            }
        });

        vehicleButton.setText("Vehicle Management");
        vehicleButton.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                vehicleButtonStateChanged(evt);
            }
        });
        vehicleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehicleButtonActionPerformed(evt);
            }
        });

        clientButton.setText("Client Management");
        clientButton.setPreferredSize(new java.awt.Dimension(153, 23));
        clientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientButtonActionPerformed(evt);
            }
        });

        driverButton.setText("Driver Management");
        driverButton.setMaximumSize(new java.awt.Dimension(153, 23));
        driverButton.setMinimumSize(new java.awt.Dimension(153, 23));
        driverButton.setPreferredSize(new java.awt.Dimension(153, 23));
        driverButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                driverButtonActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tableMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        tableName.setEditable(false);
        tableName.setText("Shipment's Table");
        tableName.setBorder(null);
        tableName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tableNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menusPanelLayout = new javax.swing.GroupLayout(menusPanel);
        menusPanel.setLayout(menusPanelLayout);
        menusPanelLayout.setHorizontalGroup(
            menusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(menusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menusPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(menusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AssignmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vehicleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(menusPanelLayout.createSequentialGroup()
                        .addComponent(driverButton, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menusPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(clientButton, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(menusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menusPanelLayout.createSequentialGroup()
                        .addGap(445, 445, 445)
                        .addComponent(jLabel2))
                    .addComponent(tableName, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21))
        );
        menusPanelLayout.setVerticalGroup(
            menusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(menusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(menusPanelLayout.createSequentialGroup()
                        .addComponent(AssignmentButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vehicleButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clientButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(driverButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 28, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.png"))); // NOI18N
        logOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutActionPerformed(evt);
            }
        });
        jPanel1.add(logOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 0, 40, 40));

        searchField.setForeground(new java.awt.Color(204, 204, 204));
        searchField.setText("Search");
        searchField.setDisabledTextColor(new java.awt.Color(153, 153, 153));
        searchField.setPreferredSize(new java.awt.Dimension(21, 20));
        searchField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchFieldMouseClicked(evt);
            }
        });
        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete");
        deleteButton.setEnabled(false);
        deleteButton.setVisible(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        modifyButton.setText("Modify");
        modifyButton.setEnabled(false);
        modifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifyButtonActionPerformed(evt);
            }
        });

        searchLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/search.png"))); // NOI18N

        insertButton.setText("Insert");
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });

        trackItButton.setText("Track It");
        trackItButton.setEnabled(false);
        trackItButton.setMaximumSize(new java.awt.Dimension(63, 23));
        trackItButton.setMinimumSize(new java.awt.Dimension(63, 23));
        trackItButton.setPreferredSize(new java.awt.Dimension(63, 23));
        trackItButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trackItButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(79, Short.MAX_VALUE)
                .addComponent(searchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(insertButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(modifyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(trackItButton, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(searchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(insertButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(trackItButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(modifyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(menusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 762, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        String search=searchField.getText();
        doSearch(stato,search,table);
        searchField.setText("Search");
        searchField.setForeground(Color.gray);
    }//GEN-LAST:event_searchFieldActionPerformed
           
    private void driverButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_driverButtonActionPerformed
        stato="driver";
        setVisibleButton(stato);
        tableFields(stato,table);
    }//GEN-LAST:event_driverButtonActionPerformed

    private void clientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientButtonActionPerformed
        stato="client";       
        setVisibleButton(stato);
        tableFields(stato,table);
    }//GEN-LAST:event_clientButtonActionPerformed

    private void vehicleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicleButtonActionPerformed
        stato="vehicle";
        setVisibleButton(stato);
        tableFields(stato,table);
    }//GEN-LAST:event_vehicleButtonActionPerformed

    private void AssignmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AssignmentButtonActionPerformed
        stato="shipment";
        setVisibleButton(stato);
        tableFields(stato,table);
    }//GEN-LAST:event_AssignmentButtonActionPerformed

    private void trackItButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trackItButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trackItButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        switch(stato){
            case("vehicle"):{
                vehicle=new Vehicle(); 
                vehicle.deleteVehicle(table.getValueAt(table.getSelectedRow(), 0).toString());                
                tableFields(stato, table);  //carica la tabella aggiornata.
                break;
            }
            //case("driver"):{break;}
            //case("client"):{break;}
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void modifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifyButtonActionPerformed
        press="modify";
        openFrame();
        if(stato.equals("vehicle")){
            vehicle.setVehicleFromTable(table);            
        }
        else    //stato=shipment.
        {
            assignment.setShipmentFromTable(table);
            assignment.setClientFromTable();            
        }
    }//GEN-LAST:event_modifyButtonActionPerformed

    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertButtonActionPerformed
        press="insert";
        openFrame();       
    }//GEN-LAST:event_insertButtonActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
                            
    }//GEN-LAST:event_tableMouseClicked

    private void tableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMousePressed
        this.modifyButton.setEnabled(true);
        this.deleteButton.setEnabled(true);
        //this.trackItButton.setEnabled(false); //lasciato per implementazioni future.
    }//GEN-LAST:event_tableMousePressed

    private void tableNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tableNameActionPerformed
        
    }//GEN-LAST:event_tableNameActionPerformed

    private void logOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutActionPerformed
        Login log = new Login();
        log.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_logOutActionPerformed

    private void vehicleButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_vehicleButtonStateChanged
        
    }//GEN-LAST:event_vehicleButtonStateChanged

    private void searchFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchFieldMouseClicked
        searchField.setText("");
        searchField.setForeground(Color.black);
    }//GEN-LAST:event_searchFieldMouseClicked

    public static void main(String args[]) {
        
        /* Set the Nimbus look and feel *///<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
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
            java.util.logging.Logger.getLogger(Menus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Menus().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AssignmentButton;
    private javax.swing.JButton clientButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton driverButton;
    private javax.swing.JButton insertButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton logOut;
    private javax.swing.JPanel menusPanel;
    private javax.swing.JButton modifyButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel searchLabel;
    protected static javax.swing.JTable table;
    protected static javax.swing.JTextField tableName;
    private javax.swing.JButton trackItButton;
    private javax.swing.JButton vehicleButton;
    // End of variables declaration//GEN-END:variables

}
