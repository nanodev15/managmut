/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import static nehemie_mutuelle.RetraitTontine.conn;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author elommarcarnold
 */
public class ModRetraitTontine extends javax.swing.JFrame {
    private int idretraitTontine;
    private Double montant=(double) 0;
    private Date dateRet = new Date();
    static Connection conn = null;
    Connection connect = null;
    static PreparedStatement pre = null;
    private Boolean dateChanged = false; 

    /** Creates new form ModRetraitTontine */
    
     public void intializedata(){
         conn = Connect.ConnectDb();
         ResultSet rs = null;
        try {
            pre = conn.prepareStatement("SELECT DateRet, Montant FROM retraits_tontine WHERE idretraits_tontine='"+this.idretraitTontine+"'; ");
                
            rs = pre.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            while (rs.next()) {
                dateRet=rs.getDate(1);
                montant=rs.getDouble(2);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
     
    public ModRetraitTontine(int idretraitTontine) {
        this.idretraitTontine=idretraitTontine;
        intializedata();
        initComponents();
    }
    
   
    
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        demoDateField1 = new com.jp.samples.comp.calendarnew.DemoDateField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/mod_retraits_tontine.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel1.setText("Date retrait:");

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel2.setText("Ancien montant:");

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel3.setText("Nouveau montant:");

        DocumentFilter numericFilter = new DocumentFilter(){

            @Override
            public void insertString(FilterBypass fb, int offset,
                String string, AttributeSet attr)
            throws BadLocationException {
                fb.insertString(offset, string.replaceAll("[^\\d]", ""), attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length,
                String text, AttributeSet attrs)
            throws BadLocationException {

                fb.replace(offset, length, text.replaceAll("[^\\d]", ""), attrs);
            }
        };
        ((AbstractDocument) jTextField1.getDocument()).setDocumentFilter(numericFilter);
        jTextField1.setText(BigDecimal.valueOf(montant).toBigInteger().toString());
        jTextField1.setEnabled(false);

        DocumentFilter numericFilter2 = new DocumentFilter(){

            @Override
            public void insertString(FilterBypass fb, int offset,
                String string, AttributeSet attr)
            throws BadLocationException {
                fb.insertString(offset, string.replaceAll("[^\\d]", ""), attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length,
                String text, AttributeSet attrs)
            throws BadLocationException {

                fb.replace(offset, length, text.replaceAll("[^\\d]", ""), attrs);
            }
        };
        ((AbstractDocument) jTextField2.getDocument()).setDocumentFilter(numericFilter2);

        jButton1.setText("Valider");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        demoDateField1.setToolTipText("");
        demoDateField1.setYearDigitsAmount(4);
        demoDateField1.setDate(dateRet);
        demoDateField1.addPropertyChangeListener(
            new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent e) {
                    if ("date".equals(e.getPropertyName())) {
                        // System.out.println(e.getPropertyName()
                            //     + ": " + (Date) e.getNewValue());
                        // System.out.println(demoDateField1.getDate());
                        dateChanged = true;
                    }
                }
            });

            org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jLabel6)
                .add(layout.createSequentialGroup()
                    .add(24, 24, 24)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jLabel2)
                        .add(jLabel3)
                        .add(jLabel1))
                    .add(73, 73, 73)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(jTextField1)
                        .add(jTextField2)
                        .add(demoDateField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)))
                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                    .add(0, 195, Short.MAX_VALUE)
                    .add(jButton1)
                    .addContainerGap(210, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(49, 49, 49)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jLabel1)
                        .add(demoDateField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(9, 9, 9)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel2)
                        .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(22, 22, 22)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel3)
                        .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 34, Short.MAX_VALUE)
                    .add(jButton1)
                    .add(29, 29, 29))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Boolean success=true;
         System.out.println("here A");
         connect=Connect.ConnectDb();
        if (dateChanged) {
              System.out.println("here 0");
            String sql="update retraits_tontine set DateRet='" + new java.sql.Date(demoDateField1.getDate().getTime())
                                    + "' where idretraits_tontine='" +this.idretraitTontine+"';";
            
             try {
                 pre=connect.prepareStatement(sql);
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(ModRetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
             }
             try {
                 pre.execute();
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(ModRetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
             }
             } 
            
             if (!jTextField2.getText().isEmpty()){
                 System.out.println("here 1");
                 if (Double.parseDouble(jTextField2.getText()) == Double.parseDouble(jTextField1.getText())) {
                     JOptionPane.showMessageDialog(this, "Veuillez renseigner un nouveau montant");
                     success=false;
                 } else {
                 Double.parseDouble(jTextField1.getText());
                 String sql="UPDATE retraits_tontine set Montant='" + Double.parseDouble(jTextField2.getText())
                                    + "' WHERE idretraits_tontine='" +this.idretraitTontine+"';";
                 System.out.println("here 2");
             try {
                 pre=connect.prepareStatement(sql);
                  System.out.println("executed");
                } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(ModRetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
             }
             try {
                 pre.execute();
                 System.out.println("here "
                         + "3");
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(ModRetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
             }
             }
             } 
             
             if (DateUtils.isSameDay(new Date(), demoDateField1.getDate())) {
                 System.out.println("here 10");
                  String sql="update retraits_tontine set DateRet='" + new java.sql.Timestamp(new Date().getTime())
                                    + "' where idretraits_tontine='" +this.idretraitTontine+"';";
            
             try {
                 pre=connect.prepareStatement(sql);
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(ModRetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
             }
             try {
                 pre.execute();
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(ModRetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
             }
             
                 
             }
        
        
        if (success){
            JOptionPane.showMessageDialog(null, "Modification effectué avec succès");
            this.dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ModRetraitTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModRetraitTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModRetraitTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModRetraitTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ModRetraitTontine(0).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

}