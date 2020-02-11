package nehemie_mutuelle;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import org.apache.commons.lang.time.DateUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author elommarcarnold
 */
public class RetraitTontine extends javax.swing.JFrame {
    private int IdEpargnant;
    private String TypeEpargnant;
    private Date DateRet;
    static Connection conn = null;
    Connection connect = null;
    static PreparedStatement pre = null;
    private TontineUser tontuser;
    private String carnet;
    private Boolean othertontine = false;

    /** Creates new form RetraitTontine */
    public RetraitTontine(int IdEpargnant, String TypeEpargnant ) {
        this.IdEpargnant=IdEpargnant;
        this.TypeEpargnant=TypeEpargnant; 
        initComponents();
    }
    
    public RetraitTontine(int IdEpargnant, String TypeEpargnant, TontineUser tontuser) {
        this.IdEpargnant=IdEpargnant;
        this.TypeEpargnant=TypeEpargnant; 
        this.tontuser =tontuser;
        initComponents();
    }
    
     public RetraitTontine(int IdEpargnant, String TypeEpargnant, TontineUser tontuser, String carnet) {
        this.IdEpargnant=IdEpargnant;
        this.TypeEpargnant=TypeEpargnant; 
        this.tontuser =tontuser;
        this.carnet = carnet;
        this.othertontine =true;
        initComponents();
    }
    
    public Double getTotalTontine() {
        conn = Connect.ConnectDb();
         ResultSet rs = null;
        try {
            if(!othertontine) pre = conn.prepareStatement("SELECT SUM((bit_count(JoursTontine)-1)*Mise) FROM Tontine WHERE IdEpargnant='"+this.IdEpargnant+"' AND TypeEpargnant='"+this.TypeEpargnant+"' AND bit_count(JoursTontine) >=1;");
            else pre = conn.prepareStatement("SELECT SUM((bit_count(JoursTontine)-1)*Mise) FROM Tontine WHERE IdEpargnant='"+this.IdEpargnant+"' AND TypeEpargnant='"+this.TypeEpargnant+"' AND bit_count(JoursTontine) >=1 AND idTontine IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet =  '"+carnet+"')");
            rs = pre.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            while (rs.next()) {
                return rs.getDouble(1);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return (double) 0;
    }
    
    public Double getTotalRetraits() {
        conn = Connect.ConnectDb();
         ResultSet rs = null;
        try {
            
            if(!othertontine)  pre = conn.prepareStatement("SELECT SUM(Montant) FROM retraits_tontine WHERE IdEpargnant='"+this.IdEpargnant+"' AND TypeEpargnant='"+this.TypeEpargnant+" ';");
            else pre = conn.prepareStatement("SELECT SUM(Montant) FROM retraits_tontine WHERE IdEpargnant='"+this.IdEpargnant+"' AND TypeEpargnant='"+this.TypeEpargnant+" ' AND idretraits_tontine IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet = '"+this.carnet+"' and type ='retrait');");

            rs = pre.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            while (rs.next()) {
                return rs.getDouble(1);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return (double) 0;
    }
     
    
     private void performSummation(java.awt.event.ActionEvent evt) {
        double total = getTotalTontine()-getTotalRetraits();
        if(jTextField1.getText().trim().length() > 0){
            try{
                total -= Double.parseDouble(jTextField1.getText());
            }catch(NumberFormatException nbx){
            }
        }
       
       jLabel5.setText(BigDecimal.valueOf(total).toBigInteger().toString());
        
        
        

       

        

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        demoDateField1 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel1.setText("Solde Tontine avant ret:");

        jLabel2.setText(BigDecimal.valueOf(this.getTotalTontine()-this.getTotalRetraits()).toBigInteger().toString());

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel3.setText("Montant retrait:");

        DocumentListener documentListener = new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                performSummation(null);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                performSummation(null);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };

        jTextField1.getDocument().addDocumentListener(documentListener);

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

        jButton1.setText("Valider");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel4.setText("Solde Tontine après ret:");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/retrait_tontine.png"))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel7.setText("Date de retrait:");

        demoDateField1.setYearDigitsAmount(4);
        demoDateField1.setDate(new Date());

        jLabel8.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel8.setText("Libellé");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(180, 180, 180))
            .add(layout.createSequentialGroup()
                .add(32, 32, 32)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel8)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel3)
                            .add(jLabel7))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(67, 67, 67)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel5)
                                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 138, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(jTextField3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                        .add(demoDateField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(jTextField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)))
                                .add(28, 28, 28))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(41, 41, 41)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jLabel5))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel7)
                        .add(20, 20, 20))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(demoDateField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel3)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(23, 23, 23)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 25, Short.MAX_VALUE)
                .add(jButton1)
                .add(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        
        DateRet=demoDateField1.getDate();
        
        
        
        if (jTextField1.getText().isEmpty()){
             JOptionPane.showMessageDialog(this, "Veuillez entrer un montant");
        } else if (demoDateField1.getDate()==null) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer une date valide");
        } else {
            double montant = Double.parseDouble(jTextField1.getText());
            if(DateUtils.isSameDay(DateRet, new Date())){
                DateRet=new Date();
                 System.out.println(true);
            }
            System.out.println(DateRet);
        // Vérifications 
        
        if(this.getTotalTontine()-this.getTotalRetraits()-montant < 0){
             JOptionPane.showMessageDialog(this, "Retrait impossible car le montant est supérieur au solde !");
//        }else if (montant==0) {
//             JOptionPane.showMessageDialog(this, "Le montant à retirer ne peut être nul");

         // insertion dans la base de données 
        } else {
             conn = Connect.ConnectDb();
             PreparedStatement pst=null;
             Boolean success= true;
             String sql0 = "INSERT INTO retraits_tontine " + 
                     "VALUES ("+null+", '"+new java.sql.Timestamp(DateRet.getTime())+"', '"+this.IdEpargnant+"', '"+this.TypeEpargnant+"', '"+montant+"', '"+jTextField3.getText().trim()+"');";
             System.out.println(sql0);
             
             


            int newid =0;
             try {  
                pst=conn.prepareStatement(sql0,Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException ex) {
                success=false;
                Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                
                pst.execute();
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    newid = rs.getInt(1);
                    
                }
            } catch (SQLException ex) {
                Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Erreur d'enregistrement");
                success=false;
            
            }
            
            // SQL 03
            if (othertontine) { 
            String sql03 = "INSERT INTO enrtontinesupp VALUES ("+ null + ", '" +newid+"', '"+carnet+"', 'retrait')";
            try {
               pst=conn.prepareStatement(sql03); 
               pst.execute();
               
            } catch (SQLException ex) {
                Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Erreur d'enregistrement");
                success=false;
            
            } 
            } 
            
            

            
            if (success){
                
                JOptionPane.showMessageDialog(null, "Retrait effectué avec succès");
                jTextField1.setText("");
                jTextField3.setText("");
               // this.dispose();
               
                
            }
        }
       
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
            java.util.logging.Logger.getLogger(RetraitTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RetraitTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RetraitTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RetraitTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RetraitTontine(0,"dfd").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables

}