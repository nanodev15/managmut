/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author arnowod
 */
public class StatisticsInterface extends javax.swing.JFrame {

    /**
     * Creates new form statisticsmutuelle
     */
    public StatisticsInterface() {
        initComponents();
        setTitle("Statistiques de la mutuelle Néhémie");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Bilan Epargne", "Bilan Tontine", "Proportions Type Epargnant", "10 plus grands revenus Tontine", "10 plus grands revenus Epargne", "10 plus grands revenus" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("Valider");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Cantarell", 0, 24)); // NOI18N
        jLabel1.setText("Statistisque de la mutuelle");

        jLabel2.setText("Choix de la statistique");

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

        ((AbstractDocument) jTextField2.getDocument()).setDocumentFilter(numericFilter);

        jLabel3.setText("Année début");

        jLabel4.setText("Année fin");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(jLabel1)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         Statistics stats;
       
        if(jComboBox1.getSelectedIndex()==0) {
            if (jTextField1.getText().isEmpty() ||  jTextField2.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez indiquer les années");
            } else {
                
           
             try {
                 stats= new Statistics(0, Integer.valueOf(jTextField1.getText()), Integer.valueOf(jTextField2.getText()));
                 stats.setLocationRelativeTo(null);
                 stats.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                 stats.setVisible(true);
                // this.dispose();
             } catch (SQLException ex) {
                 Logger.getLogger(StatisticsInterface.class.getName()).log(Level.SEVERE, null, ex);
             }} 
        } else if (jComboBox1.getSelectedIndex()==1) {
             if (jTextField1.getText().isEmpty() ||  jTextField2.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez indiquer les années");
            } else {
             try {
                 stats= new Statistics(1, Integer.valueOf(jTextField1.getText()), Integer.valueOf(jTextField2.getText()));
                 stats.setVisible(true);
                 stats.setLocationRelativeTo(null);
                 stats.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                 stats.setVisible(true);
                //  this.dispose();
             } catch (SQLException ex) {
                 Logger.getLogger(StatisticsInterface.class.getName()).log(Level.SEVERE, null, ex);
             }}
        } else if (jComboBox1.getSelectedIndex()==2) {
            
             try {
                 stats= new Statistics(2);
                 stats.setVisible(true);
                 stats.setLocationRelativeTo(null);
                 stats.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                 stats.setVisible(true);
                //  this.dispose();
             } catch (SQLException ex) {
                 Logger.getLogger(StatisticsInterface.class.getName()).log(Level.SEVERE, null, ex);
             } catch (Exception ex) {
                 Logger.getLogger(StatisticsInterface.class.getName()).log(Level.SEVERE, null, ex);
             }
        } else if (jComboBox1.getSelectedIndex()==3) {
             try {
                 stats= new Statistics(3);
                 stats.setVisible(true);
                 stats.setLocationRelativeTo(null);
                 stats.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                 stats.setVisible(true);
               //  this.dispose();
             } catch (SQLException ex) {
                 Logger.getLogger(StatisticsInterface.class.getName()).log(Level.SEVERE, null, ex);
             } catch (Exception ex) {
                 Logger.getLogger(StatisticsInterface.class.getName()).log(Level.SEVERE, null, ex);
             }
        } else if (jComboBox1.getSelectedIndex()==4) {
             try {
                 stats= new Statistics(4);
                 stats.setVisible(true);
                 stats.setLocationRelativeTo(null);
                 stats.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                 stats.setVisible(true);
               //  this.dispose();
             } catch (SQLException ex) {
                 Logger.getLogger(StatisticsInterface.class.getName()).log(Level.SEVERE, null, ex);
             } catch (Exception ex) {
                 Logger.getLogger(StatisticsInterface.class.getName()).log(Level.SEVERE, null, ex);
             }
        } else if (jComboBox1.getSelectedIndex()==5) {
             try {
                 stats= new Statistics(5);
                 stats.setVisible(true);
                 stats.setLocationRelativeTo(null);
                 stats.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                 stats.setVisible(true);
               //  this.dispose();
             } catch (SQLException ex) {
                 Logger.getLogger(StatisticsInterface.class.getName()).log(Level.SEVERE, null, ex);
             } catch (Exception ex) {
                 Logger.getLogger(StatisticsInterface.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
         if(jComboBox1.getSelectedIndex()==0) {
             jTextField1.setEnabled(true);
             jTextField2.setEnabled(true);
         } else if(jComboBox1.getSelectedIndex()==1) {
             jTextField1.setEnabled(true);
             jTextField2.setEnabled(true);
         } else {
             jTextField1.setEnabled(false);
             jTextField2.setEnabled(false);
         }
        
        
    }//GEN-LAST:event_jComboBox1ItemStateChanged

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
            java.util.logging.Logger.getLogger(StatisticsInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StatisticsInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StatisticsInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StatisticsInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StatisticsInterface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}