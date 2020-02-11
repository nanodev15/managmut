/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author elommarcarnold
 */
public class ModifyMouvement extends javax.swing.JFrame {
    private String memo;
    private int IdEpargne;
    private Date dateEpargne;
    private String montant;
    private Connection connect=null;
    private boolean pos;
    private Connect conn=new Connect();

    /** Creates new form ModifyMouvement */
    public ModifyMouvement() {
        initComponents();
    }
    
    public ModifyMouvement (int IdEpargne, Date dateEpargne, String montant , String memo, boolean pos) {
        this.IdEpargne=IdEpargne; 
        this.dateEpargne=dateEpargne;
        this.montant=montant;
        this.memo=memo;
        this.pos=pos;
        initComponents();
    }
    
    private int getId (String nomEpargnant, String prenomEpargnant, String typeEpargnant){
         connect = Connect.ConnectDb();
         if(typeEpargnant.equalsIgnoreCase("adulte")){
             String sql01="SELECT idProfil_adulte FROM Profil_adulte WHERE Noms= '" + nomEpargnant + "' AND lower(Prenoms)= '"+prenomEpargnant.toLowerCase(Locale.FRENCH)+ "'";
             Statement stmt = null;
            connect=Connect.ConnectDb();
        
            ResultSet rs1=null;
            int result=0;
            
             try {
                   stmt= connect.createStatement();
                   rs1=stmt.executeQuery(sql01);
                   //result= rs1.getInt(1);
              } catch (SQLException ex) {
                  Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
              }
               try {
                 if (rs1.next() ){

                  result= rs1.getInt(1);
                 }
             } catch (SQLException ex) {
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
              return result;
         } else if(typeEpargnant.equalsIgnoreCase("enfant")){
             String sql01="SELECT idProfil_enfant FROM Profil_enfant WHERE Nom= '" + nomEpargnant + "' AND lower(Prenoms)= '"+prenomEpargnant.toLowerCase(Locale.FRENCH)+ "'";
             Statement stmt = null;
             
            connect=Connect.ConnectDb();
        
            ResultSet rs1=null;
            int result=0;
            
             try {
                   stmt= connect.createStatement();
                 
                   rs1=stmt.executeQuery(sql01);
                   
               //    System.out.println("valeur"+rs1.getInt("idProfil_enfant"));
                //  result= rs1.getInt("idProfil_enfant");
              } catch (SQLException ex) {
                  Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
              }
             
             try {
                 if (rs1.next() ){

                  result= rs1.getInt(1);
                 }
             } catch (SQLException ex) {
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
              return result;
         } else if(typeEpargnant.equalsIgnoreCase("pers morale")){
              String sql01="SELECT idProfil_persmorale FROM Profil_persmorale WHERE Raison_sociale= '" + nomEpargnant + "'";
             Statement stmt = null;
            connect=Connect.ConnectDb();
        
            ResultSet rs1=null;
            int result=0;
            
             try {
                   stmt= connect.createStatement();
                   rs1=stmt.executeQuery(sql01);
                 //  result= rs1.getInt(1);
              } catch (SQLException ex) {
                  Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
              }
             
              try {
                 if (rs1.next() ){

                  result= rs1.getInt(1);
                 }
             } catch (SQLException ex) {
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
              
              return result;
             
         }
        
        
        return 0;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        demoDateField1 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel1.setText("Mutuelle Néhémie");

        jLabel2.setText("Modification");

        jRadioButton1.setText("Débit");

        jRadioButton2.setText("Crédit");
        if (!pos) jRadioButton1.setSelected(true);
        else jRadioButton2.setSelected(true);

        ButtonGroup bg = new ButtonGroup();
        bg.add(jRadioButton1);
        bg.add(jRadioButton2);

        jLabel3.setText("Date");

        demoDateField1.setDate(this.dateEpargne);
        demoDateField1.setYearDigitsAmount(4);

        jLabel4.setText("Montant");

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
        jTextField1.setText(this.montant);

        jLabel5.setText("Libellé");

        jTextField2.setText(this.memo);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(jPanel1Layout.createSequentialGroup()
                            .add(jLabel1)
                            .add(26, 26, 26)
                            .add(jLabel2))
                        .add(jPanel1Layout.createSequentialGroup()
                            .add(jRadioButton1)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jRadioButton2)))
                    .add(jLabel4)
                    .add(jLabel5)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField2)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField1)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, demoDateField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)))
                .addContainerGap(158, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jRadioButton1)
                    .add(jRadioButton2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(demoDateField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel5)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Annuler");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Calculatrice");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    jButton3CallCalc(evt);
                }catch (IOException ex) {

                }
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jButton3)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jButton1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButton3)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
         if (demoDateField1.getDate()==null) {
              JOptionPane.showMessageDialog(this, "Veuillez renseigner une date valide");
         } else if (jTextField1.getText().isEmpty()) {
              JOptionPane.showMessageDialog(this, "Veuillez renseigner un montant");
         } else {
                      
            //  insertion dans la base de données 
             connect = Connect.ConnectDb();
             boolean success=true;
             PreparedStatement pst=null;
             String currentTime;
              if (! DateUtils.isSameDay(demoDateField1.getDate(), this.dateEpargne)){     //demoDateField1.getDate()!= new Date()
                   if ( DateUtils.isSameDay(demoDateField1.getDate(), new Date())){
                       java.util.Date dt = new java.util.Date();
                       java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        currentTime = sdf.format(dt);
                   } else {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        currentTime = sdf.format(demoDateField1.getDate());
                   }
                    String sql="update Epargne set DateEpargne='" + currentTime
                                    + "' where idEpargne='" +this.IdEpargne+"'";
                    
             try {
                 pst=connect.prepareStatement(sql);
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
             try {
                 pst.execute();
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
                    
                   
                 
             } else if (! jTextField1.getText().equals(this.montant))  {
                  
                  String sql;
                  if (!jRadioButton1.isSelected()) {
                  sql="update Epargne set MontantEpargne='" + jTextField1.getText()
                                    + "' where idEpargne='" +this.IdEpargne+"'";
                  } else {
 /*code à vérifier */   sql="update Epargne set MontantEpargne='-" + jTextField1.getText()
                                   + "' where idEpargne='" +this.IdEpargne+"'"; 
                  }  
             try {
                 pst=connect.prepareStatement(sql);
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
             try {
                 pst.execute();
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
                 
             } else if (! jTextField2.getText().equals(this.memo)){
                 
                 String sql="update Epargne set MotifEpargne='" + jTextField2.getText()
                                    + "' where idEpargne='" +this.IdEpargne+"'";
                    
             try {
                 pst=connect.prepareStatement(sql);
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
             try {
                 pst.execute();
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
                 
             } else if (pos==true && jRadioButton1.isSelected()){
                 String sql;
                 
                  
 /*code à vérifier */   sql="update Epargne set MontantEpargne='-" + jTextField1.getText()
                                   + "' where idEpargne='" +this.IdEpargne+"'"; 
                 
             try {
                 pst=connect.prepareStatement(sql);
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
             try {
                 pst.execute();
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
                 
             } else if (pos==false && jRadioButton2.isSelected()){
                  String sql;
                 
                  sql="update Epargne set MontantEpargne='" + jTextField1.getText()
                                    + "' where idEpargne='" +this.IdEpargne+"'";
             try {
                 pst=connect.prepareStatement(sql);
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
             try {
                 pst.execute();
             } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
             }
                 
             }
              
              
//          
//             System.out.println("Id="+getId("ATALAKOU", "koffi", "enfant"));
               if (success) {
               JOptionPane.showMessageDialog(null, "Toutes les modifications ont été prises en comptes avec succès");
               this.dispose();
               }
         }
    }//GEN-LAST:event_jButton1ActionPerformed
    
     private void jButton3CallCalc(java.awt.event.ActionEvent evt) throws IOException {
              
               Runtime.getRuntime().exec("calc");
              
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ModifyMouvement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModifyMouvement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModifyMouvement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModifyMouvement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ModifyMouvement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

}
