/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import static nehemie_mutuelle.TontineUser.conn;
import org.jdatepicker.DateModel;
import org.jdatepicker.constraints.DateSelectionConstraint;

/**
 *
 * @author elommarcarnold
 */
public class QuickTontine extends javax.swing.JFrame {
    

     private List nbcot = new ArrayList<Integer>();
     private List misesList = new ArrayList<Integer>();
     private List dateList = new ArrayList<Date>();
     private int  idEpargnant;
     private String typeEpargnant;
     private Connection conn;
     private Date adhdate;
     private Boolean exist=false;
     private int nbcotvalue;
     private int mise;

    /** Creates new form QuickTontine */
    public QuickTontine() {
        initComponents();
    }
    
    public QuickTontine(int idEpargnant, String typeEpargnant) throws SQLException {
        this.idEpargnant=idEpargnant;
        this.typeEpargnant= typeEpargnant;
        initComponents();
        filladhDate();
        filltontinevalues();
        customDatePicker3.addDateSelectionConstraint(new RangeConstraint(adhdate, new Date()));
       
      //System.out.println("custom selection "+ customDatePanel2.getDateSelectionConstraints());
       System.out.println("DateList"+dateList+"Dateadh"+adhdate);
        
      
    }
    
   
    
    private void filladhDate () throws SQLException {
        String AdhTable=""; 
        String idname ="";
        if(typeEpargnant.equalsIgnoreCase("enfant")) {AdhTable="Profil_enfant"; idname="idProfil_enfant";}
        else if (typeEpargnant.equalsIgnoreCase("adulte")){ AdhTable="Profil_adulte"; idname= "idProfil_adulte";}
        else {AdhTable="Profil_persmorale"; idname= "idProfil_persmorale";}
        String sql="SELECT Date_adhesion_to FROM "+AdhTable+" WHERE "+idname+"='"+this.idEpargnant+"' ";
        System.out.println("sql"+sql);
        conn = Connect.ConnectDb2();
        PreparedStatement pre = conn.prepareStatement(sql);
        ResultSet rs= pre.executeQuery();
        
        int countrow = 0;  
        while (rs.next()) {
            adhdate =new Date(rs.getDate(1).getTime());
        }
      
        
        
        
        if (conn !=null) conn.close();
        if (pre != null) pre.close();
        if (rs != null) rs.close();
                
        
    }
    
    private void filltontinevalues () throws SQLException {
        String sql0="SELECT DateTontine, Mise, bit_count(JoursTontine) FROM Tontine WHERE IdEpargnant='"+this.idEpargnant+"' AND TypeEpargnant='"+this.typeEpargnant+"' ORDER BY DateTontine";
       String sql="SELECT dateTontine, mise, bit_count(jourstontine) FROM Tontine WHERE IdEpargnant='"+this.idEpargnant+"' AND TypeEpargnant='"+this.typeEpargnant+"' ORDER BY dateTontine";

        conn = Connect.ConnectDb2();
        ResultSet rs = null;
        PreparedStatement pre = null;
        try {
            pre = conn.prepareStatement(sql);
            rs = pre.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            while (rs.next()) {
                dateList.add(rs.getDate(1));
                misesList.add(rs.getInt(2));
                nbcot.add(rs.getInt(3));
                System.out.println("rs"+rs.getInt(3));
                System.out.println("datelist"+dateList);
                System.out.println("misesList"+misesList);
                
                
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(RetraitTontine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (pre != null) pre.close();
        if(conn != null) conn.close();
        if(rs != null) rs.close();
    }   
    
    
    
//    public class DateConstraint implements DateSelectionConstraint {
//
//
//        @Override
//        public boolean isValidSelection(DateModel<?> dm) {
//         //   if (dm.isSelected()) {
//            Calendar value = Calendar.getInstance();
//            value.set(dm.getYear(), dm.getMonth(), dm.getDay());
//            value.set(Calendar.DAY_OF_MONTH, 1);
//            value.set(Calendar.HOUR, 0);
//            value.set(Calendar.MINUTE, 0);
//            value.set(Calendar.SECOND, 0);
//            value.set(Calendar.MILLISECOND, 0);
//            
//            // Calendar ...
//            Calendar adhcal = Calendar.getInstance();
//            adhcal.setTime(adhdate);
//            adhcal.set(Calendar.DAY_OF_MONTH, 1);
//            adhcal.set(Calendar.HOUR, 0);
//            adhcal.set(Calendar.MINUTE, 0);
//            adhcal.set(Calendar.SECOND, 0);
//            adhcal.set(Calendar.MILLISECOND, 0);
//            
//            
//            
//            if(value.compareTo(adhcal) < 0) {
//                System.out.println("This is true");
//                return false;
//                
//            }
//            
//            
//                System.out.println("it is not true");
//            
//   // }
//    return true;
//    }
//    }

    
    public class RangeConstraint implements DateSelectionConstraint {

    /**
     * The lower bound of selectable dates.
     */
    private final Calendar after;

    /**
     * The upper bound of selectable dates.
     */
    private final Calendar before;

    /**
     * Create a new constraint for values between (and excluding) the given dates.
     *
     * @param after  Lower bound for values, excluding.
     * @param before Upper bound for values, excluding.
     */
    public RangeConstraint(Calendar after, Calendar before) {
        this.after = after;
        this.before = before;

        // remove hours / minutes / seconds from dates
        cleanTime();
    }

    /**
     * Create a new constraint for values between the given dates.
     *
     * @param after  Lower bound for values, including.
     * @param before Upper bound for values, including.
     */
    public RangeConstraint(Date after, Date before) {
        Calendar _after = Calendar.getInstance();
        Calendar _before = Calendar.getInstance();

        _after.setTime(after);
        _before.setTime(before);

        this.after = _after;
        this.before = _before;

        // remove hours / minutes / seconds from dates
        cleanTime();
    }

    /**
     * Simple helper method to remove the time the date bounds.
     */
    private void cleanTime() {
        if (after != null) {
            after.set(Calendar.HOUR_OF_DAY, 0);
            after.set(Calendar.MINUTE, 0);
            after.set(Calendar.SECOND, 0);
            after.set(Calendar.MILLISECOND, 0);
        }

        if (before != null) {
            before.set(Calendar.HOUR_OF_DAY, 23);
            before.set(Calendar.MINUTE, 59);
            before.set(Calendar.SECOND, 59);
            before.set(Calendar.MILLISECOND, 999);
        }
    }

    public boolean isValidSelection(DateModel<?> model) {
        boolean result = true;

        if (model.isSelected() && after != null) {
            Calendar value = Calendar.getInstance();
            value.set(model.getYear(), model.getMonth(), model.getDay());
            value.set(Calendar.HOUR, 0);
            value.set(Calendar.MINUTE, 0);
            value.set(Calendar.SECOND, 0);
            value.set(Calendar.MILLISECOND, 0);
            result &= value.after(after);
        }
        if (model.isSelected() && before != null) {
            Calendar value = Calendar.getInstance();
            value.set(model.getYear(), model.getMonth(), model.getDay());
            value.set(Calendar.HOUR, 0);
            value.set(Calendar.MINUTE, 0);
            value.set(Calendar.SECOND, 0);
            value.set(Calendar.MILLISECOND, 0);
            result &= value.before(before);
        }

        return result;
    }

    @Override
    // Generated with eclipse depending on: after, before
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((after == null) ? 0 : after.hashCode());
        result = prime * result + ((before == null) ? 0 : before.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        RangeConstraint other = (RangeConstraint) obj;
        if (after == null) {
            if (other.after != null) {
                return false;
            }
        } else if (!after.equals(other.after)) {
            return false;
        }
        if (before == null) {
            if (other.before != null) {
                return false;
            }
        } else if (!before.equals(other.before)) {
            return false;
        }
        return true;
    }

}
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        customDatePicker1 = new nehemie_mutuelle.CustomDatePicker();
        customDatePicker2 = new nehemie_mutuelle.CustomDatePicker();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        customDatePicker3 = new nehemie_mutuelle.CustomDatePicker();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Période:");

        jLabel2.setText("Nbre de cot");

        jSlider1.setMaximum(31);
        jSlider1.setValue(0);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jLabel3.setText("Mise");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "100", "200", "300", "400", "500", "Autre" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jTextField1.setEnabled(false);
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

        jButton1.setText("Enregistrer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/quicktontine.png"))); // NOI18N

        jLabel5.setText("0");

        customDatePicker3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customDatePicker3ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jSeparator1)
            .add(layout.createSequentialGroup()
                .add(50, 50, 50)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3)
                    .add(jLabel1)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel5)
                    .add(jSlider1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 240, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(28, 28, 28)
                        .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(customDatePicker3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButton1)
                .add(176, 176, 176))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(59, 59, 59)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(customDatePicker3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(63, 63, 63)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 89, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jLabel2)
                        .add(82, 82, 82))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jLabel5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jSlider1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(53, 53, 53)))
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jButton1)
                .add(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        // TODO add your handling code here:
      
               // update text field when the slider value changes
               JSlider source = (JSlider) evt.getSource();
               jLabel5.setText("" + source.getValue());
            
        
    }//GEN-LAST:event_jSlider1StateChanged

    private void customDatePicker3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customDatePicker3ActionPerformed
        // TODO add your handling code here:
        jSlider1.setValue(0);
        jComboBox1.setSelectedIndex(0);
        GregorianCalendar cal = (GregorianCalendar) customDatePicker3.getModel().getValue();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date insertedDate= new Date(cal.getTimeInMillis());
        int pos = dateList.indexOf(insertedDate);
        if (pos !=-1) {
        exist = true;
        nbcotvalue = (Integer) nbcot.get(pos);
        mise = (Integer) misesList.get(pos);
        if (mise==100) jComboBox1.setSelectedIndex(0);
        else if (mise==200) jComboBox1.setSelectedIndex(1);
        else if (mise==300) jComboBox1.setSelectedIndex(2);
        else if (mise==400) jComboBox1.setSelectedIndex(3);
        else if (mise==500) jComboBox1.setSelectedIndex(4);
        else jTextField1.setText(String.valueOf(mise));
        jSlider1.setValue(nbcotvalue);
       
        } else {
        exist= false;    
        }
         System.out.println("insertedDate"+insertedDate+"pos"+pos);
    }//GEN-LAST:event_customDatePicker3ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        if (jComboBox1.getSelectedIndex()==jComboBox1.getItemCount()-1) jTextField1.setEnabled(true);
        else jTextField1.setEnabled(false);
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:7
       
        if (customDatePicker3.getModel().getValue() != null) {
        int miseused;  // mise
        int nbCotValueused; // nbcotisations
        PreparedStatement pre = null;
        Connection conn = Connect.ConnectDb2();
        Boolean success = false;
        GregorianCalendar cal = (GregorianCalendar) customDatePicker3.getModel().getValue(); // La date
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date insertedDate= new Date(cal.getTimeInMillis());
        nbCotValueused = jSlider1.getValue();  // nombre de cotisations 
         if (!jTextField1.isEnabled()) {
            miseused = Integer.valueOf((String) jComboBox1.getSelectedItem());
        } else {
            miseused = Integer.valueOf(jTextField1.getText());
        }
         
         
        if (exist) {
            
            System.out.println("It exists");
        // find ID 
            
            
            
            
        
        if (miseused != this.mise || nbCotValueused != this.nbcotvalue) {
            // find ID 
             ResultSet rso = null;
             int idtont=-1;
             String  sql01 = "SELECT idTontine FROM Tontine WHERE IdEpargnant='"+this.idEpargnant+"' AND TypeEpargnant='"+this.typeEpargnant+"' AND DateTontine='"+ new java.sql.Date(insertedDate.getTime())+"';"; 
            try {
                 pre=conn.prepareStatement(sql01);
                 rso = pre.executeQuery();
            } catch (SQLException ex) {
            }
            
            
            
                try {
                    while(rso.next()) {
                        idtont = rso.getInt(1);
                        
                    }   } catch (SQLException ex) {
                    Logger.getLogger(QuickTontine.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            
            
            
            // we have to update
            String  sql = "UPDATE Tontine SET Mise='"+miseused+"', JoursTontine = '";
            int i = 0;
            while (i< nbCotValueused) {
                sql = sql+String.valueOf(i+1)+",";
                i++;
             }
             
            sql=sql.substring(0, sql.length()-1);
            sql=sql+"' WHERE idTontine='"+idtont+"';"; 
            
            System.out.println("sql"+sql);
            try {
                 pre=conn.prepareStatement(sql);
                 pre.execute();
            } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(MiseTontine.class.getName()).log(Level.SEVERE, null, ex);
            }
                
               if (success) {
                     JOptionPane.showMessageDialog(null, "Les cotisations ont été enregistrés avec succès");
               } else  {
                     JOptionPane.showMessageDialog(null, "Erreur d'enregistrement");
               }
               
             
               if (rso != null) try {
                   rso.close();
                    pre.close();
             } catch (SQLException ex) {
                 Logger.getLogger(QuickTontine.class.getName()).log(Level.SEVERE, null, ex);
             }
             
               
               
        }
       } else {   // Il faut now inserer la valeur
            // we have to insert
            String sql0="INSERT INTO Tontine VALUES ("+ null + ", '"+new java.sql.Date(insertedDate.getTime())+ "', ('";
            System.out.println("Nouvelle valeur");
            int k = 0;
            while (k < nbCotValueused) {
                sql0= sql0+String.valueOf(k+1)+",";
                k++;
             }
             
            sql0=sql0.substring(0, sql0.length()-1);
            sql0=sql0+"'), '"+miseused+"', '"+idEpargnant+"', '"+typeEpargnant+"');"; 
            
            System.out.println("sql0 vaut pour tontine"+sql0);
            
             try {
                 pre=conn.prepareStatement(sql0);
                 pre.execute();
                 success =true;
            } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(MiseTontine.class.getName()).log(Level.SEVERE, null, ex);
                }
                
               if (success) {
                     JOptionPane.showMessageDialog(null, "Les cotisations ont été enregistrés avec succès");
               }
               try {
               if (pre != null) pre.close();
               if (conn != null) conn.close();
               
            } catch (SQLException ex) {
                Logger.getLogger(QuickTontine.class.getName()).log(Level.SEVERE, null, ex);
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
            java.util.logging.Logger.getLogger(QuickTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuickTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuickTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuickTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new QuickTontine(40, "Adulte").setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(QuickTontine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private nehemie_mutuelle.CustomDatePicker customDatePicker1;
    private nehemie_mutuelle.CustomDatePicker customDatePicker2;
    private nehemie_mutuelle.CustomDatePicker customDatePicker3;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

}
