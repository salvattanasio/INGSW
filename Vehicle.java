package ingsftw;

import java.awt.Toolkit;
import javax.swing.JOptionPane;


public class Vehicle extends javax.swing.JFrame {
    private static Log15Control logctrl;
    private String press;
    
    
    
    public Vehicle() {
        logctrl=new Log15Control();
        press="insert";
         
        initComponents();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("truck1.jpg")));
        setTitle("Log'15_SoftEngUniNa");
    }
     
    public Vehicle(String p) {
        logctrl=new Log15Control();
        press=p;
        
        initComponents();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("truck1.jpg")));
        setTitle("Log'15_SoftEngUniNa");
    }
    
    /* 
     * Fills all the Vehicle's forms with the information taken from the table in Menus.
    */
    public void fillForm(VehicleEntity v){
        oldPlateLabel.setVisible(true);
        oldPlateField.setVisible(true);
        oldPlateNumberFrame.setVisible(true);
        
        oldPlateField.setText(v.getPlateNumber());
        plateField.setText(v.getPlateNumber());
        lengthField.setText(((Double)v.getLength()).toString());
        heightField.setText(((Double)v.getHeight()).toString());
        modelField.setText(v.getModel()); 
        capacityField.setText(((Double)v.getCapacity()).toString());
    }
    
    public void refreshFields(){
        plateField.setText(null);
        modelField.setText(null);
        heightField.setText(null);
        lengthField.setText(null);     
        capacityField.setText(null);   
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        Home = new javax.swing.JButton();
        logOut = new javax.swing.JButton();
        titleLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        vehiclePanel2 = new javax.swing.JPanel();
        capacityLabel = new javax.swing.JLabel();
        capacityField = new javax.swing.JTextField();
        oldPlateNumberFrame = new javax.swing.JPanel();
        oldPlateLabel = new javax.swing.JLabel();
        oldPlateField = new javax.swing.JTextField();
        vehiclePanel = new javax.swing.JPanel();
        plateField = new javax.swing.JTextField();
        plateLabel = new javax.swing.JLabel();
        modelField = new javax.swing.JTextField();
        modelLabel = new javax.swing.JLabel();
        vehiclePanel1 = new javax.swing.JPanel();
        lengthLabel = new javax.swing.JLabel();
        heightField = new javax.swing.JTextField();
        lengthField = new javax.swing.JTextField();
        heigthLabel = new javax.swing.JLabel();
        confirm = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        Home.setBackground(new java.awt.Color(255, 255, 255));
        Home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/home.png"))); // NOI18N
        Home.setBorder(null);
        Home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HomeActionPerformed(evt);
            }
        });

        logOut.setBackground(new java.awt.Color(255, 255, 255));
        logOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/logout.png"))); // NOI18N
        logOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutActionPerformed(evt);
            }
        });

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(0, 153, 255));
        titleLabel.setText("VEHICLE MANAGEMENT");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logOut, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(Home, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(logOut, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(Home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        vehiclePanel2.setBackground(new java.awt.Color(255, 255, 255));
        vehiclePanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255), 2), "Cargo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 255))); // NOI18N

        capacityLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        capacityLabel.setForeground(new java.awt.Color(204, 0, 0));
        capacityLabel.setText("Capacity (tons)");

        capacityField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                capacityFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout vehiclePanel2Layout = new javax.swing.GroupLayout(vehiclePanel2);
        vehiclePanel2.setLayout(vehiclePanel2Layout);
        vehiclePanel2Layout.setHorizontalGroup(
            vehiclePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vehiclePanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(capacityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(capacityField, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 34, Short.MAX_VALUE))
        );
        vehiclePanel2Layout.setVerticalGroup(
            vehiclePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vehiclePanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(vehiclePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(capacityLabel)
                    .addComponent(capacityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 20, Short.MAX_VALUE))
        );

        oldPlateNumberFrame.setVisible(false);
        oldPlateNumberFrame.setBackground(new java.awt.Color(255, 255, 255));
        oldPlateNumberFrame.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255), 2));

        oldPlateLabel.setVisible(false);
        oldPlateLabel.setBackground(new java.awt.Color(255, 255, 255));
        oldPlateLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        oldPlateLabel.setText("Old Plate Number");

        oldPlateField.setVisible(false);
        oldPlateField.setEnabled(false);
        oldPlateField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oldPlateFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout oldPlateNumberFrameLayout = new javax.swing.GroupLayout(oldPlateNumberFrame);
        oldPlateNumberFrame.setLayout(oldPlateNumberFrameLayout);
        oldPlateNumberFrameLayout.setHorizontalGroup(
            oldPlateNumberFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(oldPlateNumberFrameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(oldPlateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(oldPlateField, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        oldPlateNumberFrameLayout.setVerticalGroup(
            oldPlateNumberFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(oldPlateNumberFrameLayout.createSequentialGroup()
                .addGap(0, 9, Short.MAX_VALUE)
                .addGroup(oldPlateNumberFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oldPlateLabel)
                    .addComponent(oldPlateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        vehiclePanel.setBackground(new java.awt.Color(255, 255, 255));
        vehiclePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255), 2), "Registry", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 255))); // NOI18N

        plateField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plateFieldActionPerformed(evt);
            }
        });

        plateLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        plateLabel.setForeground(new java.awt.Color(204, 0, 0));
        plateLabel.setText("Plate Number");

        modelField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modelFieldActionPerformed(evt);
            }
        });

        modelLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        modelLabel.setForeground(new java.awt.Color(204, 0, 0));
        modelLabel.setText("Model ");

        javax.swing.GroupLayout vehiclePanelLayout = new javax.swing.GroupLayout(vehiclePanel);
        vehiclePanel.setLayout(vehiclePanelLayout);
        vehiclePanelLayout.setHorizontalGroup(
            vehiclePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vehiclePanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(vehiclePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, vehiclePanelLayout.createSequentialGroup()
                        .addComponent(plateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(vehiclePanelLayout.createSequentialGroup()
                        .addComponent(modelLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)))
                .addGroup(vehiclePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(modelField, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE)
                    .addComponent(plateField))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        vehiclePanelLayout.setVerticalGroup(
            vehiclePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vehiclePanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(vehiclePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(plateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(plateLabel))
                .addGap(18, 18, 18)
                .addGroup(vehiclePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(modelLabel)
                    .addComponent(modelField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        vehiclePanel1.setBackground(new java.awt.Color(255, 255, 255));
        vehiclePanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255), 2), "Dimension", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(0, 153, 255))); // NOI18N

        lengthLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lengthLabel.setForeground(new java.awt.Color(204, 0, 0));
        lengthLabel.setText("Length (mt)");

        heightField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                heightFieldActionPerformed(evt);
            }
        });

        lengthField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lengthFieldActionPerformed(evt);
            }
        });

        heigthLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        heigthLabel.setForeground(new java.awt.Color(204, 0, 0));
        heigthLabel.setText("Height (mt)");

        javax.swing.GroupLayout vehiclePanel1Layout = new javax.swing.GroupLayout(vehiclePanel1);
        vehiclePanel1.setLayout(vehiclePanel1Layout);
        vehiclePanel1Layout.setHorizontalGroup(
            vehiclePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vehiclePanel1Layout.createSequentialGroup()
                .addGap(0, 20, Short.MAX_VALUE)
                .addGroup(vehiclePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lengthLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(heigthLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(vehiclePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lengthField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(heightField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );
        vehiclePanel1Layout.setVerticalGroup(
            vehiclePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(vehiclePanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(vehiclePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lengthLabel)
                    .addComponent(lengthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(vehiclePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(heigthLabel)
                    .addComponent(heightField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        confirm.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        confirm.setText("Confirm");
        confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oldPlateNumberFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(vehiclePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(vehiclePanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(122, 122, 122)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(vehiclePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(139, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(oldPlateNumberFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vehiclePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vehiclePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(vehiclePanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(82, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void confirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmActionPerformed
   
        try{
            logctrl.operation(this, press);
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Error: fill all the mandatory fields");
        }
    }//GEN-LAST:event_confirmActionPerformed

    private void HomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HomeActionPerformed
        logctrl.openFrame("menu", "");        
        this.dispose();
    }//GEN-LAST:event_HomeActionPerformed

    private void logOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutActionPerformed
        logctrl.logOut();
        this.dispose();
    }//GEN-LAST:event_logOutActionPerformed

    private void heightFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_heightFieldActionPerformed

    }//GEN-LAST:event_heightFieldActionPerformed

    private void lengthFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lengthFieldActionPerformed

    }//GEN-LAST:event_lengthFieldActionPerformed

    private void modelFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modelFieldActionPerformed
 
    }//GEN-LAST:event_modelFieldActionPerformed

    private void plateFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plateFieldActionPerformed

    }//GEN-LAST:event_plateFieldActionPerformed

    private void capacityFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_capacityFieldActionPerformed

    }//GEN-LAST:event_capacityFieldActionPerformed

    private void oldPlateFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oldPlateFieldActionPerformed

    }//GEN-LAST:event_oldPlateFieldActionPerformed

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
            java.util.logging.Logger.getLogger(Vehicle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Vehicle().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Home;
    protected javax.swing.JTextField capacityField;
    private javax.swing.JLabel capacityLabel;
    private javax.swing.JButton confirm;
    protected javax.swing.JTextField heightField;
    private javax.swing.JLabel heigthLabel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    protected javax.swing.JTextField lengthField;
    private javax.swing.JLabel lengthLabel;
    private javax.swing.JButton logOut;
    protected javax.swing.JTextField modelField;
    private javax.swing.JLabel modelLabel;
    protected javax.swing.JTextField oldPlateField;
    public static javax.swing.JLabel oldPlateLabel;
    private javax.swing.JPanel oldPlateNumberFrame;
    protected javax.swing.JTextField plateField;
    private javax.swing.JLabel plateLabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel vehiclePanel;
    private javax.swing.JPanel vehiclePanel1;
    private javax.swing.JPanel vehiclePanel2;
    // End of variables declaration//GEN-END:variables
}
