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
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import static nehemie_mutuelle.BatchEpargne.StringToDate;
import static nehemie_mutuelle.BatchEpargne.isValidFormat;

/**
 *
 * @author arnowod
 */
public class BatchTontine extends javax.swing.JFrame {
  private ArrayList<String> epargnelist;
  private Map<String, String> typelist;
  private Map<String, Integer> idlist;
  private Map<String, Boolean> tontineadd;
  private List<Integer> tontinesuppadd;
  private List<Integer> retraitsuppadd;

  private Connection conn;
  String[] values = new String[] {"Ajout", "Retrait"};
    /**
     * Creates new form BatchEpargne
     */
  
float[] columnWidthPercentage = {0.35f, 0.10f, 0.15f, 0.15f, 0.05f, 0.15f};
    public BatchTontine() throws SQLException {
        epargnelist = new ArrayList<String>();  // epargnelist ....mais tontine
        typelist = new HashMap<>();
        idlist = new HashMap<>();
        tontineadd= new HashMap<>();
        tontinesuppadd = new ArrayList<>();
        retraitsuppadd = new ArrayList<>();
       
        filltontinelist();
        initComponents();
        resizeColumns();
        addComponentListener(new ComponentAdapter() {
           @Override
        public void componentResized(ComponentEvent e) {
            resizeColumns();
        }
       });
    
    }
    
    class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {
  public MyComboBoxRenderer(String[] items) {
    super(items);
      setSelectedIndex(0);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
      boolean hasFocus, int row, int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      super.setBackground(table.getSelectionBackground());
    } else {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }
    setSelectedItem(value);
    return this;
  }
}

class MyComboBoxEditor extends DefaultCellEditor {
  public MyComboBoxEditor(String[] items) {
         
    super(new JComboBox(items));
  }
  
  
  
}
    
    
    
    private void filltontinelist() throws SQLException {
        conn = Connect.ConnectDb();
        String sql ="SELECT Nom, Prenoms, carnet, Date_adhesion_ep, id, Typo FROM (SELECT a.idProfil_enfant as id, LEFT(a.Num_carnet,4) as carnet, a.Nom as Nom, a.Prenoms as Prenoms, a.Date_adhesion_ep as Date_adhesion_ep, 'Enfant' as Typo From Profil_enfant a WHERE lower(a.Type_adhesion) LIKE '%tontine%' AND Active IS TRUE UNION SELECT b.idProfil_adulte as id, LEFT(b.Num_carnet,4) as carnet, b.Noms as Nom, b.Prenoms as Prenoms, b.Date_adhesion_ep as Date_adhesion_ep, 'Adulte' as Typo From Profil_adulte b WHERE lower(b.Type_adhesion) LIKE '%tontine%' AND Active is TRUE  UNION SELECT c.idProfil_persmorale as id, LEFT(c.Num_carnet,4) as carnet, c.Raison_sociale as Nom, c.Raison_sociale as Prenoms, c.Date_adhesion_ep as Date_adhesion_ep, 'Pers Morale' as Typo From Profil_persmorale c WHERE lower(c.Type_adhesion) LIKE '%tontine%' AND Active IS TRUE) Test ORDER BY Test.carnet";
        String sql2 ="SELECT idtontinesupp, numcarnettont, iduser, typeuser, Nom, Prenoms FROM tontinesupp JOIN  (SELECT Nom, Prenoms, carnet, Date_adhesion_ep, id, Typo FROM (SELECT a.idProfil_enfant as id, LEFT(a.Num_carnet,4) as carnet, a.Nom as Nom, a.Prenoms as Prenoms, a.Date_adhesion_ep as Date_adhesion_ep, 'Enfant' as Typo From Profil_enfant a WHERE lower(a.Type_adhesion) LIKE '%tontine%' AND Active IS TRUE UNION SELECT b.idProfil_adulte as id, LEFT(b.Num_carnet,4) as carnet, b.Noms as Nom, b.Prenoms as Prenoms, b.Date_adhesion_ep as Date_adhesion_ep, 'Adulte' as Typo From Profil_adulte b WHERE lower(b.Type_adhesion) LIKE '%tontine%' AND Active is TRUE  UNION SELECT c.idProfil_persmorale as id, LEFT(c.Num_carnet,4) as carnet, c.Raison_sociale as Nom, c.Raison_sociale as Prenoms, c.Date_adhesion_ep as Date_adhesion_ep, 'Pers Morale' as Typo From Profil_persmorale c WHERE lower(c.Type_adhesion) LIKE '%tontine%' AND Active IS TRUE) Test0) Test ON tontinesupp.iduser= Test.id AND tontinesupp.typeuser = Test.Typo" ;
 
        PreparedStatement pre= conn.prepareStatement(sql);
        PreparedStatement pre2= conn.prepareStatement(sql2);
   
        ResultSet rst = pre.executeQuery(sql);
        
        System.out.println("sql2"+sql2);
        ResultSet rst2 = pre2.executeQuery(sql2);

        
      
     
      
        while (rst.next()) {
            System.out.println("epargnelsit"+epargnelist);
            epargnelist.add(rst.getString(3)+" "+rst.getString(1)+" "+rst.getString(2));
            typelist.put(rst.getString(3), rst.getString(6)); 
            idlist.put(rst.getString(3), rst.getInt(5)); 
            tontineadd.put(rst.getString(3), TRUE);
        }
        
        
          while (rst2.next()) {
            System.out.println("epargnelsit"+epargnelist);
            epargnelist.add(rst2.getString(2)+" "+rst2.getString(5)+" "+rst2.getString(6));   // 5 et 6 
            typelist.put(rst2.getString(2), rst2.getString(4)); 
            idlist.put(rst2.getString(2), rst2.getInt(3));
            tontineadd.put(rst2.getString(2), FALSE);
        }
          
          
        
        
        
        
     if (rst !=null) rst.close();
     if (pre !=null) pre.close();
     if (conn !=null) conn.close();
       System.out.println("epargnelist"+epargnelist);
        
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
        jLabel1.setText("Enregistrement Tontine");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Numero carnet", "Type", "Date", "Mise/Montant retrait", "Nbre de cot", "Libelle"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Double.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int row, int column) {
                if (getValueAt(row, 1) !=null && ((String)getValueAt(row, 1)).equalsIgnoreCase("ajout") && column ==5)
                {
                    return false;
                } else if (getValueAt(row, 1) !=null && ((String)getValueAt(row, 1)).equalsIgnoreCase("retrait") && column ==4) {
                    return false;
                } else {
                    return true;
                }
                //  return super.isCellEditable(row, column); // or maybe simply "true"
            }
        });
        jScrollPane1.setViewportView(jTable1);
        TableColumn nx = jTable1.getColumnModel().getColumn(0);
        nx.setCellEditor(new TextfieldEditor());
        jTable1.setRowHeight(jTable1.getRowHeight() + 8);

        TableColumn col = jTable1.getColumnModel().getColumn(1);

        col.setCellEditor(new MyComboBoxEditor(values));
        jTable1.getColumnModel().getColumn(2).setCellEditor(new JDateChooserEditor(new JCheckBox()));

        //jTable1.getModel().addTableModelListener(
            //new TableModelListener()
            //{
                //    public void tableChanged(TableModelEvent evt)
                //    {

                    //        int row = evt.getFirstRow();
                    //        int column = evt.getColumn();
                    //        DefaultTableModel model = (DefaultTableModel) evt.getSource();
                    //        if (column == 1) {
                        //         String data = (String) model.getValueAt(row, column);
                        //         if (data.equalsIgnoreCase("ajout")){

                            //         }

                        //        }
                    // here goes your code "on cell update"
                    //    }
                //});
        //col.setCellRenderer(new MyComboBoxRenderer(values));

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
                        .addGap(5, 5, 5)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(28, 28, 28)
                        .addComponent(jButton2))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 899, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
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
     
     
   public String converttosetstring(int i) {
       String result ="";
       int k;
       if (i <=0) return result;
         result = "('";
       for(k = 1; k <= i; k++) {        
           result = result +String.valueOf(k);
           if (k!=i) result +=",";
                   
       }
       result = result +"')";
       
       return result;
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
            
            boolean atleastins = false;
            boolean atleastret = false;
            int countadd = 0;
            int countretr = 0;
            String libelleret = "";

            // enregistrer dans la table 
         //   String sql002 = "INSERT INTO Epargne VALUES ("+null+", '"+ currentTime + "', '"+"Dépot initial"+"', '"+jTextField13.getText()+"', '"+generatdkey+"', '"+"Enfant"+"');";
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int counter;
            String time;
            String carnet;
            String sql0="INSERT INTO Tontine (idTontine, DateTontine, JoursTontine, Mise, IdEpargnant, TypeEpargnant) \n VALUES  \n";    // Pour ajout
            String sql1 = "INSERT INTO retraits_tontine (idretraits_tontine, DateRet, IdEpargnant, TypeEpargnant, Montant, Libelle) \n  VALUES \n  ";    // Pour retrait 
            for (counter=0; counter < jTable1.getRowCount(); counter++) {
                carnet = (String) jTable1.getValueAt(counter, 0);
                if (((String) jTable1.getValueAt(counter, 1)).equalsIgnoreCase("ajout")) {
                    countadd++;
                    if (atleastins == false)  atleastins =true;
                    try {
                    // Si c'est un ajout
                    time = sdf.format(StringToDate((String) jTable1.getValueAt(counter, 2)));
                    
                     sql0 = sql0 + "("+ null+ ", '"+time+"', " + converttosetstring((int) jTable1.getValueAt(counter, 4)) +", '"+ jTable1.getValueAt(counter, 3)+"', \n '";    // Corriger les indices 
//                     if (counter == jTable1.getRowCount()-1)
//                       sql0= sql0+ (int) idlist.get(carnet).intValue()+"', '"+typelist.get(carnet)+"'); ";
//                       else 
                     sql0= sql0+ (int) idlist.get(carnet).intValue()+"', '"+typelist.get(carnet)+"'), ";   
                     } catch (ParseException ex) {
                        Logger.getLogger(BatchTontine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    System.out.println("carnet"+carnet);
                    System.out.println(tontineadd);
                    System.out.println("get carnet"+tontineadd.get(carnet));
                    
                    if (tontineadd.get(carnet) == true) tontinesuppadd.add(countadd);
                }
                
                
                 if (((String) jTable1.getValueAt(counter, 1)).equalsIgnoreCase("retrait")) {  
                            countretr++;
                            if (atleastret == false)  atleastret =true;
                     
                   try {
                     // Si c'est un retrait
                     time = sdf.format(StringToDate((String) jTable1.getValueAt(counter, 2)));
                  
                     sql1 = sql1 + "("+ null+ ", '"+time+"', '";
//                     if (counter == jTable1.getRowCount()-1)
//                       sql0= sql0+ (int) idlist.get(carnet).intValue()+"', '"+typelist.get(carnet)+"'); ";
//                       else 
                     sql1= sql1+ (int) idlist.get(carnet).intValue()+"', '"+typelist.get(carnet)+"', ";
                     if (jTable1.getValueAt(counter, 5) == null) libelleret ="";
                     else libelleret = (String) jTable1.getValueAt(counter, 5);
                     sql1 = sql1+ (Double) jTable1.getValueAt(counter, 3) +", '"+libelleret+"' ),";    // Corriger les indices 
                    } catch (ParseException ex) {
                        Logger.getLogger(BatchTontine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (tontineadd.get(carnet) == true) retraitsuppadd.add(countretr);

                }
                 
                

                
                    
                    
                    
//                     "VALUES ("+null+", '"+new java.sql.Timestamp(DateRet.getTime())+"', '"+this.IdEpargnant+"', '"+this.TypeEpargnant+"', '"+montant+"', '"+jTextField3.getText().trim()+"');";
//             System.out.println(sql0);
//             
                    
}
        
//        conn = Connect.ConnectDb();
//        Boolean success = false;
//        PreparedStatement pst;
//            try {              
//                  pst=  conn.prepareStatement(sql);                  
//                  pst.execute();
//            } catch (SQLException ex) {
//                Logger.getLogger(BatchTontine.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            
//        if (success) {
//            JOptionPane.showMessageDialog(this, " Enregistré effectué avec succès");
//
//        } else {
//            JOptionPane.showMessageDialog(this, " L'enregistrement n'a pas pu être effectué");
//
//        }
//        
//       
//        }
            
               sql0 = sql0.trim();   
                   sql0 = sql0.substring(0,sql0.length() - 1);
                   sql0 = sql0 + ";";
                   
                   sql1 = sql1.trim();   
                   sql1 = sql1.substring(0,sql1.length() - 1);
                   sql1 = sql1 + ";";
              
        System.out.println("sql0"+ sql0);
        System.out.println("sql1"+ sql1);
        
        
        // Insert in database
        conn = Connect.ConnectDb2();
        
         Statement stmt;
         Boolean success = true;
         if (atleastins) {
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(sql0, Statement.RETURN_GENERATED_KEYS);
                  ResultSet keys = stmt.getGeneratedKeys();
                   int lastKey = 1;
                   while (keys.next()) {
                       System.out.println("key"+keys.getInt(1));
                       lastKey = keys.getInt(1);
                  
                    }
   
            } catch (SQLException ex) {
                success = false;
                Logger.getLogger(BatchTontine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
         
         
          if (atleastret) {
            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(sql1, Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException ex) {
                success = false;
                Logger.getLogger(BatchTontine.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
          
          
        if (success)   {
            JOptionPane.showMessageDialog(this, "Enregistrement effectué avec succès");
            DefaultTableModel dm = (DefaultTableModel) jTable1.getModel();
            int rowCount = dm.getRowCount();
            //Remove rows one by one from the end of the table
            for (int i = rowCount - 1; i >= 0; i--) {
                dm.removeRow(i);
            }
            
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
                JOptionPane.showMessageDialog(this, "Numéro carnet invalide à la ligne "+ (i+1));
                jTable1.setRowSelectionInterval(i, i);
                return false;
            } else if (jTable1.getValueAt(i, 1) == null || !((String) jTable1.getValueAt(i, 1)).equalsIgnoreCase("ajout") &&  !((String) jTable1.getValueAt(i, 1)).equalsIgnoreCase("retrait")) {
                JOptionPane.showMessageDialog(this, "Veuillez choisir le type d'enregistrement à la ligne "+ (i+1));
                jTable1.setRowSelectionInterval(i, i);
                return false;
            } else if(jTable1.getValueAt(i, 2) == null || !isValidFormat("dd/MM/yyyy", (String) jTable1.getValueAt(i, 2))) {
                JOptionPane.showMessageDialog(this, " Date invalide à la ligne "+(i+1));
                jTable1.setRowSelectionInterval(i, i);
                return false; 
            } else if (((String) jTable1.getValueAt(i, 1)).equalsIgnoreCase("ajout") &&  ((Double) jTable1.getValueAt(i, 3) == null || (Integer) jTable1.getValueAt(i, 4) == null)) {
                JOptionPane.showMessageDialog(this, " Mise ou nombre de cotisations invalide à la ligne "+ (i+1));
                jTable1.setRowSelectionInterval(i, i);
                return false;
            }  else if (((String) jTable1.getValueAt(i, 1)).equalsIgnoreCase("ajout") &&  ((Integer) jTable1.getValueAt(i, 4) == 0)) {            
                JOptionPane.showMessageDialog(this, " Nombre de cotisations doit etre au moins 1 à la ligne "+ (i+1));
                jTable1.setRowSelectionInterval(i, i);
                return false;
            }  else if (((String) jTable1.getValueAt(i, 1)).equalsIgnoreCase("retrait") &&  ((Double) jTable1.getValueAt(i, 3) == null)) {
                JOptionPane.showMessageDialog(this, " Montant du retrait invalide à la ligne "+ (i+1));
                jTable1.setRowSelectionInterval(i, i);
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
            java.util.logging.Logger.getLogger(BatchTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BatchTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BatchTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BatchTontine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new BatchTontine().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(BatchTontine.class.getName()).log(Level.SEVERE, null, ex);
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
