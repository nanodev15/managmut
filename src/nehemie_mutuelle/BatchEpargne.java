/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author arnowod
 */
public class BatchEpargne extends javax.swing.JFrame {
  private ArrayList<String> epargnelist;
  private Map<String, String> typelist;
  private Map<String, Integer> idlist;
  private Connection conn;
    /**
     * Creates new form BatchEpargne
     */
  
float[] columnWidthPercentage = {0.35f, 0.15f, 0.15f, 0.35f};
    public BatchEpargne() throws SQLException {
        epargnelist = new ArrayList<String>();
        typelist = new HashMap<>();
        idlist = new HashMap<>();
       
         fillepargnelist();
        initComponents();
         resizeColumns();
       addComponentListener(new ComponentAdapter() {
           @Override
        public void componentResized(ComponentEvent e) {
            resizeColumns();
        }
       });
    
    }
    
    
    
    private void fillepargnelist() throws SQLException {
        conn = Connect.ConnectDb();
        String sql ="SELECT Nom, Prenoms, carnet, Date_adhesion_ep, id, Typo FROM (SELECT a.idProfil_enfant as id, LEFT(a.Num_carnet,4) as carnet, a.Nom as Nom, a.Prenoms as Prenoms, a.Date_adhesion_ep as Date_adhesion_ep, 'Enfant' as Typo From Profil_enfant a WHERE lower(a.Type_adhesion) LIKE '%epargne%' AND Active IS TRUE UNION SELECT b.idProfil_adulte as id, LEFT(b.Num_carnet,4) as carnet, b.Noms as Nom, b.Prenoms as Prenoms, b.Date_adhesion_ep as Date_adhesion_ep, 'Adulte' as Typo From Profil_adulte b WHERE lower(b.Type_adhesion) LIKE '%epargne%' AND Active is TRUE  UNION SELECT c.idProfil_persmorale as id, LEFT(c.Num_carnet,4) as carnet, c.Raison_sociale as Nom, c.Raison_sociale as Prenoms, c.Date_adhesion_ep as Date_adhesion_ep, 'Pers Morale' as Typo From Profil_persmorale c WHERE lower(c.Type_adhesion) LIKE '%epargne%' AND Active IS TRUE) Test ORDER BY Test.carnet";
        PreparedStatement pre= conn.prepareStatement(sql);
        ResultSet rst = pre.executeQuery(sql);
     
      
        while (rst.next()){
            System.out.println("epargnelsit"+epargnelist);
            epargnelist.add(rst.getString(3)+" "+rst.getString(1)+" "+rst.getString(2));
            typelist.put(rst.getString(3), rst.getString(6)); 
            idlist.put(rst.getString(3), rst.getInt(5)); 
        }
     if (rst !=null) rst.close();
     if (pre !=null) pre.close();
     if (conn !=null) conn.close();
        
    }
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/delete_symbol.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/iconval.png"))); // NOI18N
        jButton2.setText("Valider");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Cantarell", 0, 25)); // NOI18N
        jLabel1.setText("Enregistrement Epargne");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Numero carnet", "Date", "Montant", "Libelle"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Double.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        TableColumn nx = jTable1.getColumnModel().getColumn(0);
        nx.setCellEditor(new TextfieldEditor());
        jTable1.setRowHeight(jTable1.getRowHeight() + 8);

        jTable1.getColumnModel().getColumn(1).setCellEditor(new JDateChooserEditor(new JCheckBox()));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/add-icon.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 22, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(215, 215, 215)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(28, 28, 28)
                        .addComponent(jButton2))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 899, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1)
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton1))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
   
     public static Date StringToDate(String dob) throws ParseException {
      //Instantiating the SimpleDateFormat class
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      //Parsing the given String to Date object
      Date date = formatter.parse(dob);
      System.out.println("Date object value: "+date);
      return date;
   }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        ((DefaultTableModel)jTable1.getModel()).addRow(new Object[]{null,null,null,null});
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ((DefaultTableModel)jTable1.getModel()).removeRow(jTable1.getSelectedRow());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        // validate the table 
        if(validateTable()) {
            // enregistrer dans la table 
         //   String sql002 = "INSERT INTO Epargne VALUES ("+null+", '"+ currentTime + "', '"+"Dépot initial"+"', '"+jTextField13.getText()+"', '"+generatdkey+"', '"+"Enfant"+"');";
             java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               
         String sql = "INSERT INTO Epargne VALUES ";
         for (int i=0; i< jTable1.getRowCount(); i++) {
             String carnet = (String) jTable1.getValueAt(i, 0);
             String time;
                 try {
                     time = sdf.format(StringToDate((String) jTable1.getValueAt(i, 1)));
                       sql = sql + "("+null+", '"+ time + "', '"+(String) jTable1.getValueAt(i, 3)+"', '"+(Double) jTable1.getValueAt(i, 2)+"', '"; 
                       if (i == jTable1.getRowCount()-1)
                       sql= sql+ (int) idlist.get(carnet).intValue()+"', '"+typelist.get(carnet)+"'); ";
                       else 
                       sql= sql+ (int) idlist.get(carnet).intValue()+"', '"+typelist.get(carnet)+"'), ";   
                 } catch (ParseException ex) {
                     Logger.getLogger(BatchEpargne.class.getName()).log(Level.SEVERE, null, ex);
                 }
           
         }
        
        conn = Connect.ConnectDb();
        Boolean success = false;
        PreparedStatement pst;
            try {              
                  pst=  conn.prepareStatement(sql);                  
                  pst.execute();
            } catch (SQLException ex) {
                Logger.getLogger(BatchEpargne.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        if (success) {
            JOptionPane.showMessageDialog(this, " Enregistré effectué avec succès");

        } else {
            JOptionPane.showMessageDialog(this, " L'enregistrement n'a pas pu être effectué");

        }
        
       
        }
        
        
        
    }//GEN-LAST:event_jButton2ActionPerformed
    
    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }
    
    public boolean validateTable()   {
        
        for (int i =0; i< jTable1.getRowCount(); i++) {
            if (jTable1.getValueAt(i, 0)== null || ((String) jTable1.getValueAt(i, 0)).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Numéro carnet invalide à la ligne "+i+1);
                return false;
            } else if (jTable1.getValueAt(i, 1) == null || !isValidFormat("dd/MM/yyyy", (String) jTable1.getValueAt(i, 1))) {
                JOptionPane.showMessageDialog(this, " Date invalide à la ligne"+i+1);
                return false;
            }  else if ((Double) jTable1.getValueAt(i, 2) == null) {
                JOptionPane.showMessageDialog(this, " Montant invalide à la ligne"+i+1);
                return false;
            }
            
            
        }
        
        return true;
         
    }
    
    
    public class TextfieldEditor extends AbstractCellEditor implements TableCellEditor {
     JComponent component ;
   // JComponent component = new JTextField();
    private ArrayList<String> listSomeString = new ArrayList<String>();
   
   
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int rowIndex, int vColIndex) {
        
        
        if (value ==null ) {
        
       
      Java2sAutoTextField someTextField = new Java2sAutoTextField(epargnelist);
       component = someTextField;

        
        someTextField.setFont(new Font("Serif", Font.BOLD, 16));
        someTextField.setForeground(Color.black);
        someTextField.setBackground(Color.orange);
        someTextField.setName("someTextField");
        someTextField.setDataList(epargnelist);
         } else  {
                ((Java2sAutoTextField ) component).setText((String) value);
        }
//
     

      
        	
       

       

        // Return the configured component
        return component;
    }

	@Override
	public Object getCellEditorValue() {
		 return ((JTextField)component).getText().split(" ", 2)[0];
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BatchEpargne.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BatchEpargne.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BatchEpargne.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BatchEpargne.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new BatchEpargne().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(BatchEpargne.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
   
    private void resizeColumns() {
    // Use TableColumnModel.getTotalColumnWidth() if your table is included in a JScrollPane
    int tW = jTable1.getWidth();
    TableColumn column;
    TableColumnModel jTableColumnModel = jTable1.getColumnModel();
    int cantCols = jTableColumnModel.getColumnCount();
    for (int i = 0; i < cantCols; i++) {
        column = jTableColumnModel.getColumn(i);
        int pWidth = Math.round(columnWidthPercentage[i] * tW);
        column.setPreferredWidth(pWidth);
    }
    }
    
    
    class JDateChooserEditor extends DefaultCellEditor
{
  public JDateChooserEditor(JCheckBox checkBox)
  {
    super(checkBox);

  }

  JDateChooser date;
  public Component getTableCellEditorComponent(JTable table, Object value,
      boolean isSelected, int row, int column) 
  {

         date = new JDateChooser();
         date.setDateFormatString("dd/MM/yyyy");
         date.setDate(new Date());
         return date;
  }

  public Object getCellEditorValue() 
  {
    return new String(((JTextField)date.getDateEditor().getUiComponent()).getText());
  }

  public boolean stopCellEditing()
  {
    return super.stopCellEditing();
  }

  protected void fireEditingStopped() {
    super.fireEditingStopped();
  }
}

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
